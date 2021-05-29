package com.vetis.integrationmodule.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveApplicationResultRequest {
    private String apiKey;
    private String issuerId;
    private String applicationId;

    private static final String template =
            "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <Body>\n" +
                    "    <receiveApplicationResultRequest xmlns=\"http://api.vetrf.ru/schema/cdm/application/ws-definitions\">\n" +
                    "      <apiKey>{{apiKey}}</apiKey>\n" +
                    "      <issuerId>{{issuerId}}</issuerId>\n" +
                    "      <applicationId>{{applicationId}}</applicationId>\n" +
                    "    </receiveApplicationResultRequest>\n" +
                    "  </Body>\n" +
                    "</Envelope>";

    @Override
    public String toString() {
        String xml = template;
        return xml
                .replaceAll("\\{\\{apiKey\\}\\}", apiKey)
                .replaceAll("\\{\\{issuerId\\}\\}", issuerId)
                .replaceAll("\\{\\{applicationId\\}\\}", applicationId);
    }
}