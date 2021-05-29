package com.vetis.integrationmodule.response;


import com.vetis.integrationmodule.converter.annotations.VetisRegex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VetDocument {
    @VetisRegex("vetDocument.*?uuid>(.*?)</")
    private String uuid;

    @VetisRegex("vetDocument.*?issueDate>(.*?)</")
    private String issueDate;

    @VetisRegex("productItem.*?name>(.*?)</")
    private String productName;

    @VetisRegex("volume>(.*?)</")
    private String volume;

    @VetisRegex("unit.*?guid>(.*?)</")
    private String units;

    @VetisRegex("origin.*?producer>.*?guid>(.*?)</")
    private String producerName;

    @VetisRegex("country.*?guid>(.*?)</")
    private String countryName;
}
