package org.jeecg.modules.job.wztaizhangjob.ztyjjob;

import lombok.Getter;

@Getter
public class MCTechOpenApiRequestException extends MCTechException {
    private final ApiGatewayError error;

    public MCTechOpenApiRequestException(String message, ApiGatewayError error) {
        super(message);

        this.error = error;
    }
}
