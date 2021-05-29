package com.vetis.integrationmodule;


import com.vetis.integrationmodule.enums.ApplicationStatus;
import com.vetis.integrationmodule.request.ReceiveApplicationResultRequest;
import com.vetis.integrationmodule.request.VetisRequest;
import com.vetis.integrationmodule.response.SubmitApplicationResponse;
import com.vetis.integrationmodule.response.VetisResponse;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VetisService {
    @Autowired
    VetisConfig vetisConfig;

    @Autowired
    VetisApplicationManagementService appManagementService;

    @Autowired
    private VetisHelper vetisHelper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(
                    name = "amq.rabbitmq.reply-to",
                    type = ExchangeTypes.DIRECT
            )
    ))
    public VetisResponse vetisRequest(VetisRequest request) {
        System.out.println(request);
        VetisResponse response = new VetisResponse();
        request.setServiceId(vetisConfig.getMercuryId());

        // запрос на обработку заявки
        SubmitApplicationResponse applicationResponse =
                appManagementService.getSubmit(
                        vetisConfig.getAmsUri(),
                        request
                );

        if(applicationResponse == null) {
            response.setErrorInfo("Internal server error.");
            return response;
        }

        //  проверяем, принята ли заявка
        if(applicationResponse.getStatus() != ApplicationStatus.ACCEPTED) {
            response.setErrorInfo("Application is rejected by Vetis.");
            return response; //  если заявка отклонена
        }

        // запрос на получение результата по заявке c повтором попытки через 100 мс
        for(int i = 0; i < vetisConfig.getTimeout(); i++) {
            VetisResponse tempResponse =
                    appManagementService.getVetisResponse(
                            vetisConfig.getAmsUri(),
                            new ReceiveApplicationResultRequest(
                                    request.getApiKey(),
                                    request.getIssuerId(),
                                    applicationResponse.getApplicationId()
                            )
                    );

            if(tempResponse == null) {
                response.setErrorInfo("Internal server error.");
                return response;
            }

            // проверяем статус заявки
            if(response.getStatus() == ApplicationStatus.REJECTED) {
                response.setErrorInfo("Application is rejected by Vetis.");
                return response; //  если заявка отклонена
            }
            if(response.getStatus() == ApplicationStatus.COMPLETED) {
                break; //  если завершена - выходим из цикла
            }
            if(response.getStatus() == ApplicationStatus.IN_PROCESS) {
                try {
                    Thread.sleep(100); //  если в процессе - повторяем
                } catch(InterruptedException e) {
                    continue;
                }
            }
        }

        // обработка наименований каждого vetDocument
        try {
            response = vetisHelper.postProcessVetDocumentList(response);
        } catch(VetisHelperException e) {
            response.setErrorInfo(e.getMessage());
        }

        // возвращаем ответ в Rabbit
        return response;
    }
}
