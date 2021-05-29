package com.vetis.integrationmodule.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vetis.integrationmodule.VetisConfig;
import com.vetis.integrationmodule.enums.VetDocumentStatus;
import com.vetis.integrationmodule.enums.VetDocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VetisRequest {
    private String apiKey;
    @JsonIgnore
    private String serviceId;
    private String issuerId;
    private String issueDate;

    private String localTransactionId;
    private String login;

    private Integer count;
    private Integer offset;
    private VetDocumentType vetDocumentType;
    private VetDocumentStatus vetDocumentStatus;
    private String enterpriseGuid;

    private static String template =
            "<soapenv:Envelope " + VetisConfig.XMLNS + ">\n" +
                    "<soapenv:Body>\n" +
                    "<apldef:submitApplicationRequest>\n" +
                    "<apldef:apiKey>{{apiKey}}</apldef:apiKey>\n" +
                    "<apl:application>\n" +
                    "<apl:serviceId>{{serviceId}}</apl:serviceId>\n" +
                    "<apl:issuerId>{{issuerId}}</apl:issuerId>\n" +
                    "<apl:issueDate>{{issueDate}}</apl:issueDate>\n" +
                    "<apl:data>\n" +
                    "<merc:getVetDocumentListRequest>\n" +
                    "<merc:localTransactionId>{{localTransactionId}}</merc:localTransactionId>\n" +
                    "<merc:initiator>\n" +
                    "<vd:login>{{login}}</vd:login>\n" +
                    "</merc:initiator>\n" +
                    "{{listOptions}}" +
                    "{{vetDocumentType}}" +
                    "{{vetDocumentStatus}}" +
                    "<dt:enterpriseGuid>{{enterpriseGuid}}</dt:enterpriseGuid>\n" +
                    "</merc:getVetDocumentListRequest>\n" +
                    "</apl:data>\n" +
                    "</apl:application>\n" +
                    "</apldef:submitApplicationRequest>\n" +
                    "</soapenv:Body>\n" +
                    "</soapenv:Envelope>";

    @Override
    public String toString() {
        String xml = template
                .replaceAll("\\{\\{apiKey\\}\\}", apiKey)
                .replaceAll("\\{\\{serviceId\\}\\}", serviceId)
                .replaceAll("\\{\\{issuerId\\}\\}", issuerId)
                .replaceAll("\\{\\{issueDate\\}\\}", issueDate)
                .replaceAll("\\{\\{localTransactionId\\}\\}", localTransactionId)
                .replaceAll("\\{\\{login\\}\\}", login)
                .replaceAll("\\{\\{enterpriseGuid\\}\\}", enterpriseGuid)
                .replaceAll("\\{\\{vetDocumentType\\}\\}",
                        vetDocumentType != null ? ("<vd:vetDocumentType>" + vetDocumentType + "</vd:vetDocumentType>\n") : "")
                .replaceAll("\\{\\{vetDocumentStatus\\}\\}",
                        vetDocumentStatus != null ? ("<vd:vetDocumentStatus>" + vetDocumentStatus + "</vd:vetDocumentStatus>\n") : "");

        if(count != null || offset != null) {
            xml = xml
                    .replaceAll("\\{\\{listOptions\\}\\}", "<bs:listOptions>\n{{count}}{{offset}}</bs:listOptions>\n")
                    .replaceAll("\\{\\{count\\}\\}", count != null ? ("<bs:count>" + count + "</bs:count>\n") : "")
                    .replaceAll("\\{\\{offset\\}\\}", offset != null ? ("<bs:offset>" + offset + "</bs:offset>\n") : "");
        } else {
            xml = xml
                    .replaceAll("\\{\\{listOptions\\}\\}", "");
        }

        return xml;
    }
}