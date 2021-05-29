package com.vetis.integrationmodule;

import com.vetis.integrationmodule.response.VetDocument;
import com.vetis.integrationmodule.response.VetisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VetisHelper {
    @Autowired
    private VetisApplicationManagementService appManagementService;

    @Autowired
    private VetisConfig vetisConfig;

    /**
     * Получение и подстановка наименований по guid в VetDocument из VetisResponse.vetDocumentList
     *
     * @return
     */
    public VetisResponse postProcessVetDocumentList(VetisResponse response) throws VetisHelperException {
        for(VetDocument vetDocument : response.getVetDocumentList()) {
            vetDocument.setCountryName(this.getCountryNameByGuid(vetDocument.getCountryName()));
            vetDocument.setProducerName(this.getProducerNameByGuid(vetDocument.getProducerName()));
            vetDocument.setUnits(this.getUnitsNameByGuid(vetDocument.getUnits()));
        }
        return response;
    }

    /**
     * Получение названия страны по guid
     *
     * @param countryGuid
     * @return countryName
     * @throws VetisHelperException
     */
    private String getCountryNameByGuid(String countryGuid) throws VetisHelperException {
        // формируем запрос
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ws=\"http://api.vetrf.ru/schema/cdm/ikar/ws-definitions\" " +
                        "xmlns:base=\"http://api.vetrf.ru/schema/cdm/base\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "       <ws:getCountryByGuidRequest>\n" +
                        "           <base:guid>{{guid}}</base:guid>\n" +
                        "       </ws:getCountryByGuidRequest>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        xml = xml.replaceAll("\\{\\{guid\\}\\}", countryGuid);

        // получаем данные о стране
        String response = appManagementService.helperRequest(vetisConfig.getIkarUri(), xml);

        // вытаскиваем из ответа наименование страны
        Pattern p = Pattern.compile("name>(.*?)</");
        Matcher m = p.matcher(response);
        if(m.find()) {
            // возвращаем его, если нашлось
            return m.group(1);
        } else {
            // кидаем ошибку если вдруг не нашлось
            throw new VetisHelperException(
                    "Couldn't get country name");
        }
    }

    /**
     * Получение названия предприятия по guid
     *
     * @param enterpriseGuid
     * @return enterpriseName
     * @throws VetisHelperException
     */
    private String getProducerNameByGuid(String enterpriseGuid) throws VetisHelperException {
        // формируем запрос
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ws=\"http://api.vetrf.ru/schema/cdm/registry/ws-definitions/v2\" " +
                        "xmlns:bs=\"http://api.vetrf.ru/schema/cdm/base\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "       <ws:getEnterpriseByGuidRequest>\n" +
                        "           <bs:guid>{{guid}}</bs:guid>\n" +
                        "       </ws:getEnterpriseByGuidRequest>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        xml = xml.replaceAll("\\{\\{guid\\}\\}", enterpriseGuid);

        // получаем данные о предприятии
        String response = appManagementService.helperRequest(vetisConfig.getCerberUri(), xml);

        // вытаскиваем из ответа наименование предприятия
        Pattern p = Pattern.compile("enterprise.*?name>(.*?)</");
        Matcher m = p.matcher(response);
        if(m.find()) {
            // возвращаем его, если нашлось
            return m.group(1);
        } else {
            // кидаем ошибку если вдруг не нашлось
            throw new VetisHelperException(
                    "Couldn't get producer name");
        }
    }

    /**
     *  Получение названия единиц измерения по guid
     *
     * @param unitsGuid
     * @return unitsName
     * @throws VetisHelperException
     */
    private String getUnitsNameByGuid(String unitsGuid) throws VetisHelperException {
        // формируем запрос
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ws=\"http://api.vetrf.ru/schema/cdm/registry/ws-definitions/v2\" " +
                        "xmlns:bs=\"http://api.vetrf.ru/schema/cdm/base\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "       <ws:getUnitByGuidRequest>\n" +
                        "           <bs:guid>{{guid}}</bs:guid>\n" +
                        "       </ws:getUnitByGuidRequest>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        xml = xml.replaceAll("\\{\\{guid\\}\\}", unitsGuid);

        // получаем данные о единицах измерения
        String response = appManagementService.helperRequest(vetisConfig.getDictionaryUri(), xml);

        // вытаскиваем из ответа их наименование
        Pattern p = Pattern.compile("fullName>(.*?)</");
        Matcher m = p.matcher(response);
        if(m.find()) {
            // возвращаем его, если нашлось
            return m.group(1);
        } else {
            // кидаем ошибку если вдруг не нашлось
            throw new VetisHelperException(
                    "Couldn't get units name");
        }
    }
}
