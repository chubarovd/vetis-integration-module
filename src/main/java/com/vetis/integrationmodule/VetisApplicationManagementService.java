package com.vetis.integrationmodule;

import com.vetis.integrationmodule.request.ReceiveApplicationResultRequest;
import com.vetis.integrationmodule.request.VetisRequest;
import com.vetis.integrationmodule.response.SubmitApplicationResponse;
import com.vetis.integrationmodule.response.VetisResponse;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface VetisApplicationManagementService {
    @POST
    @Headers({"Content-Type: text/xml",
            "Authorization: Basic cXVpY2tyZXN0by0xODA0MjU6Mk10MnRTOW4=",
            "WWW-Authenticate: Basic realm=\"weblogic\""})
    SubmitApplicationResponse getSubmit(@Url String url, @Body VetisRequest request);

    @POST
    @Headers({"Content-Type: text/xml",
            "Authorization: Basic cXVpY2tyZXN0by0xODA0MjU6Mk10MnRTOW4=",
            "WWW-Authenticate: Basic realm=\"weblogic\""})
    VetisResponse getVetisResponse(@Url String url, @Body ReceiveApplicationResultRequest request);

    @POST
    @Headers({"Content-Type: text/xml",
            "Authorization: Basic cXVpY2tyZXN0by0xODA0MjU6Mk10MnRTOW4=",
            "WWW-Authenticate: Basic realm=\"weblogic\""})
    String helperRequest(@Url String url, @Body String request);
}
