package com.jarubert.threatValidator.model.dto;

import lombok.Data;

@Data
public class ValidationMessage {
    private String client;
    private String url;
    private Integer correlationId;
}
