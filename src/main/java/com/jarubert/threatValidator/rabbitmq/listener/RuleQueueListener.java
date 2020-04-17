package com.jarubert.threatValidator.rabbitmq.listener;

import com.jarubert.threatValidator.model.dto.RuleMessage;
import com.jarubert.threatValidator.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Component
public class RuleQueueListener {

    private final Logger logger = LoggerFactory.getLogger(RuleQueueListener.class);

    @Autowired
    private RuleService ruleService;

    @RabbitListener(queues="#{insertionQueue.name}", returnExceptions = "true")
    public void receiveRuleMessage(RuleMessage newRule) {
        logger.info("Received message for insertion: " + newRule.toString());
        if (ruleService.isValidRule(newRule)) {
            ruleService.createRuleFromMessageIfNotPresent(newRule);
        }
    }


}
