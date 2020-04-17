package com.jarubert.threatValidator.service;

import com.jarubert.threatValidator.model.dto.ResponseMessage;
import com.jarubert.threatValidator.model.dto.RuleMessage;
import com.jarubert.threatValidator.model.dto.ValidationMessage;
import com.jarubert.threatValidator.model.entity.Client;
import com.jarubert.threatValidator.model.entity.Rule;
import com.jarubert.threatValidator.repository.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
public class RuleService {

    private final Logger logger = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ClientService clientService;

    /**
     * Create a new rule based on the received RuleMessage if there isnt already a rule with the same client + regex
     *
     * @param newRule
     * @return
     */
    public Rule createRuleFromMessageIfNotPresent(RuleMessage newRule) {
        Client client = newRule.getClient() != null ? clientService.getOrCreateClient(newRule.getClient()) : null;
        Rule rule = ruleRepository.findByExpressionAndClient(newRule.getRegex(), client);
        if (rule == null) {
            rule = ruleRepository.save(new Rule(client, newRule.getRegex()));
            logger.info("Added new rule: " + rule.toString());
        }
        return rule;
    }

    /**
     * Return the first rule that matches with the requested url, otherwise return null
     *
     * @param validationMessage
     * @return
     */
    public ResponseMessage isWhiteListed(ValidationMessage validationMessage) {
        Rule rule = ruleRepository.findMatch(validationMessage.getClient(), validationMessage.getUrl());
        ResponseMessage responseMessage = new ResponseMessage(validationMessage.getCorrelationId());
        if (rule != null) {
            logger.info("Found whitelist rule: " + rule.toString() + " for correlationId: " + validationMessage.getCorrelationId());
            responseMessage.setRegex(rule.getExpression());
            responseMessage.setMatch(true);
        } else {
            logger.info("No whitelist rule found for correcationId: " + validationMessage.getCorrelationId());
            responseMessage.setMatch(false);
        }

        return responseMessage;
    }

    /**
     *
     * validates if the rule regex is valid
     *
     * @param newRule
     * @return
     */
    public boolean isValidRule(RuleMessage newRule) {
        try {
            Pattern.compile(newRule.getRegex());
        } catch (PatternSyntaxException e) {
            logger.info("Received message for insertion has Invalid regex: " + newRule.getRegex());
            return false;
        }

        return true;
    }

}