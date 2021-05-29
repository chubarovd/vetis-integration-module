package com.vetis.integrationmodule;

import com.jaredsburrows.retrofit2.adapter.synchronous.SynchronousCallAdapterFactory;
import com.vetis.integrationmodule.converter.VetisConverterFactory;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class VetisApplicationManagementConfig {
    /**
     * Базовый адрес
     */
    @Getter
    @Value("${vetis.ams.baseUrl}")
    private String baseUrl;

    /**
     * Таймаут для Retrofit (в секундах)
     */
    @Getter
    @Value("${vetis.ams.timeout}")
    private Integer timeout;

    /**
     * Получаем конвертер
     *
     * @return конвертер
     */
    private static retrofit2.Converter.Factory defaultConverter() {
        return VetisConverterFactory.create();
    }

    /**
     * Получаем адаптер
     *
     * @return адаптер
     */
    private static retrofit2.CallAdapter.Factory defaultCallAdapter() {
        return SynchronousCallAdapterFactory.create();
    }

    /**
     * Создаем экземпляр Retrofit
     *
     * @param client клиент
     * @return экземпляр Retrofit
     */
    private static Retrofit buildRetrofit(String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(defaultConverter())
                .validateEagerly(false)
                .addCallAdapterFactory(defaultCallAdapter())
                .client(client)
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public VetisApplicationManagementService gitEmsHubService() {
        HttpLoggingInterceptor loggerInterceptor = new HttpLoggingInterceptor();
        loggerInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Настройки подключения
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggerInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();

        //Создаем ретрофит
        Retrofit retrofit = buildRetrofit(baseUrl, okHttpClient);

        //Возвращаем экземпляр
        return retrofit.create(VetisApplicationManagementService.class);
    }
}
