package com.jarubert.threatValidator.rabbitmq.listener;

import com.jarubert.threatValidator.model.dto.ResponseMessage;
import com.jarubert.threatValidator.model.dto.ValidationMessage;
import com.jarubert.threatValidator.model.entity.Rule;
import com.jarubert.threatValidator.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidationQueueListener {

    private final Logger logger = LoggerFactory.getLogger(ValidationQueueListener.class);

    @Value("${threatvalidator.rabbitmq.response_exchange}")
    private String exchangeName;

    @Value("${threatvalidator.rabbitmq.response_routing_key}")
    private String routingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RuleService ruleService;

    @RabbitListener(queues="#{validationQueue.name}",
            returnExceptions = "true",
            concurrency = "${threatvalidator.rabbitmq.number_validation_consumers}")
    public void validate(ValidationMessage validationMessage) {
        logger.info("Received message for validation: " + validationMessage.toString());
        ResponseMessage responseMessage = ruleService.isWhiteListed(validationMessage);
        logger.info("Sending response: " + responseMessage.toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, responseMessage);
    }
}
