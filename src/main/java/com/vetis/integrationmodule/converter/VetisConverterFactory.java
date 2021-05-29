package com.vetis.integrationmodule.converter;

import lombok.NoArgsConstructor;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@NoArgsConstructor
public final class VetisConverterFactory extends Converter.Factory {

    public static VetisConverterFactory create() {
        return new VetisConverterFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return VetisRequestBodyConverter.INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        try {
            return VetisResponseBodyConverter.withTargetType(type);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
