package com.vetis.integrationmodule;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class VetisConfig {
    /**
     * Токен
     */
    @Getter
    @Value("${vetis.apiKey}")
    private String apiKey;

    /**
     * ID инициатора
     */
    @Getter
    @Value("${vetis.issuerId}")
    private String issuerId;

    /**
     * Логин
     */
    @Getter
    @Value("${vetis.login}")
    private String login;

    /**
     * ID сервиса Меркурий
     */
    @Getter
    @Value("${vetis.mercury}")
    private String mercuryId;

    /**
     * Предельное количество попыток получения результата по заявке
     */
    @Getter
    @Value("${vetis.timeout}")
    private Integer timeout;

    /**
     * ApplicationManagerService URI
     */
    @Getter
    @Value("${vetis.uri.ams}")
    private String amsUri;

    /**
     * Ikar URI
     */
    @Getter
    @Value("${vetis.uri.ikar}")
    private String ikarUri;

    /**
     * Cerberus URI
     */
    @Getter
    @Value("${vetis.uri.cerberus}")
    private String cerberUri;

    /**
     * Dictionary URI
     */
    @Getter
    @Value("${vetis.uri.dictionary}")
    private String dictionaryUri;

    public static final String XMLNS =
            "xmlns:dt=\"http://api.vetrf.ru/schema/cdm/dictionary/v2\" " +
                    "xmlns:bs=\"http://api.vetrf.ru/schema/cdm/base\" " +
                    "xmlns:merc=\"http://api.vetrf.ru/schema/cdm/mercury/g2b/applications/v2\" " +
                    "xmlns:apldef=\"http://api.vetrf.ru/schema/cdm/application/ws-definitions\" " +
                    "xmlns:apl=\"http://api.vetrf.ru/schema/cdm/application\" " +
                    "xmlns:vd=\"http://api.vetrf.ru/schema/cdm/mercury/vet-document/v2\" " +
                    "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"";
}
