package com.vetis.integrationmodule.converter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class VetisRequestBodyConverter<T> implements Converter<T, RequestBody> {
    static final VetisRequestBodyConverter<Object> INSTANCE = new VetisRequestBodyConverter<>();
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8");

    private VetisRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(T value) {
        return RequestBody.create(MEDIA_TYPE, value.toString());
    }
}
