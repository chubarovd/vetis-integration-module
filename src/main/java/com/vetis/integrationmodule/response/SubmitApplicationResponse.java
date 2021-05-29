package com.vetis.integrationmodule.response;

import com.vetis.integrationmodule.converter.annotations.VetisRegex;
import com.vetis.integrationmodule.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitApplicationResponse {
    @VetisRegex("applicationId>(.*?)</applicationId")
    private String applicationId;

    @VetisRegex("status>(.*?)</status")
    private ApplicationStatus status;

    @VetisRegex("serviceId>(.*?)</serviceId")
    private String serviceId;

    @VetisRegex("issuerId>(.*?)</issuerId")
    private String issuerId;

    @VetisRegex("issueDate>(.*?)</issueDate")
    private String issueDate;

    @VetisRegex("rcvDate>(.*?)</rcvDate")
    private String rcvDate;
}