package com.vetis.integrationmodule.converter;

import com.vetis.integrationmodule.converter.annotations.VetisListRegex;
import com.vetis.integrationmodule.converter.annotations.VetisRegex;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VetisResponseBodyConverter implements Converter<ResponseBody, Object> {
    private Class<?> targetClass;

    public static VetisResponseBodyConverter withTargetType(Type targetType) throws ClassNotFoundException {
        VetisResponseBodyConverter converter = new VetisResponseBodyConverter();
        converter.targetClass = Class.forName(targetType.getTypeName());
        return converter;
    }

    private VetisResponseBodyConverter() {
    }

    @Override
    public Object convert(ResponseBody value) {
        try {
            return convertNestedObject(targetClass, value.string());
        } catch(Exception e) {
            return null;
        }
    }

    private Object convertNestedObject(Class<?> targetClass_arg, String source) throws Exception {
        Object result = targetClass_arg.getConstructor().newInstance();

        Matcher m;
        Pattern p;
        Annotation a;

        Field[] fields = targetClass_arg.getDeclaredFields();
        for(Field field : fields) {
            // пытаемся получить у поля аннотацию @VetisListRegex
            a = field.getAnnotation(VetisListRegex.class);

            if(a != null) {
                p = Pattern.compile(((VetisListRegex) a).value(), Pattern.DOTALL);
                m = p.matcher(source);
                ArrayList<Object> temp = new ArrayList<>();

                while(m.find()) {
                    // получаем тип аргумента листа
                    ParameterizedType par = (ParameterizedType) field.getGenericType();
                    Type[] args = par.getActualTypeArguments();

                    // добавляем значение рекурсивного вызова в лист
                    try {
                        temp.add(convertNestedObject(Class.forName(args[0].getTypeName()), m.group(1)));
                    } catch(ClassNotFoundException e) {
                        continue;
                    }
                }

                field.setAccessible(true);
                try {
                    field.set(
                            result,
                            temp
                    );
                } catch(IllegalAccessException e) {
                    continue;
                }
                field.setAccessible(false);
            } else {
                a = field.getAnnotation(VetisRegex.class);
                p = Pattern.compile(((VetisRegex) a).value(), Pattern.DOTALL);
                m = p.matcher(source);

                if(m.find() && m.groupCount() == 1) {
                    try {
                        field.setAccessible(true);
                        try {
                            field.set(
                                    result,
                                    field.getType().getMethod("valueOf", String.class).invoke(field.getType(), m.group(1))
                            );
                        } catch(NoSuchMethodException e) {
                            try {
                                field.set(
                                        result,
                                        field.getType().getMethod("valueOf", Object.class).invoke(field.getType(), m.group(1))
                                );
                            } catch(NoSuchMethodException e1) {
                                continue;
                            }
                        }
                    } catch(IllegalAccessException | InvocationTargetException e) {
                        continue;
                    }
                    field.setAccessible(false);
                } else {
                    continue;
                }
            }
        }
        return result;
    }
}
