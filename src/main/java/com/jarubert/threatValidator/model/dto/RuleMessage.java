package com.jarubert.threatValidator.model.dto;

import lombok.Data;

@Data
public class RuleMessage {
    private String client;
    private String regex;

}
