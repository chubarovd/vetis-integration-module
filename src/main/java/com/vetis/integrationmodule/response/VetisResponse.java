package com.vetis.integrationmodule.response;

import com.vetis.integrationmodule.converter.annotations.VetisListRegex;
import com.vetis.integrationmodule.converter.annotations.VetisRegex;
import com.vetis.integrationmodule.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VetisResponse {
    @VetisRegex("application.*?applicationId>(.*?)</applicationId")
    private String applicationId;

    @VetisRegex("application.*?status>(.*?)</status")
    private ApplicationStatus status;

    @VetisRegex("application.*?serviceId>(.*?)</serviceId")
    private String serviceId;

    @VetisRegex("application.*?issuerId>(.*?)</issuerId")
    private String issuerId;

    @VetisRegex("application.*?issueDate>(.*?)</issueDate")
    private String issueDate;

    @VetisRegex("application.*?rcvDate>(.*?)</rcvDate")
    private String rcvDate;

    @VetisRegex("application.*?prdcRsltDate>(.*?)</prdcRsltDate")
    private String prdcRsltDate;

    @VetisListRegex(
            wrapperRegex = "vd:vetDocumentList>(.*?)</vd:vetDocumentList",
            value = "(vd:vetDocument>.*?</vd:vetDocument)")
    private List<VetDocument> vetDocumentList;

    private String errorInfo = "";
}
