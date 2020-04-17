package com.jarubert.threatValidator.rabbitmq.config;

import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Value("${threatvalidator.rabbitmq.response_exchange}")
    private String exchangeName;

    @Value("${threatvalidator.rabbitmq.insertion_queue}")
	private String insertionQueueName;

	@Value("${threatvalidator.rabbitmq.validations_queue}")
	private String validationQueueName;

    @Value("${threatvalidator.rabbitmq.response_routing_key}")
    private String routingKey;

    @Bean
    public Queue insertionQueue() {
        return new Queue(insertionQueueName, true);
    }

    @Bean
    public Queue validationQueue() {
        return new Queue(validationQueueName, true);
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Declarables createExchange() {
        DirectExchange exchange = new DirectExchange(exchangeName);

        return new Declarables(exchange);
    }

}
