package com.jarubert.threatValidator.model.dto;

import lombok.Data;

@Data
public class ResponseMessage {
    private Boolean match;
    private String regex;
    private Integer correlationId;

    public ResponseMessage() {
    }

    public ResponseMessage(Integer correlationId) {
        this.correlationId = correlationId;
    }

}
