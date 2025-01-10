package com.example.domainservice.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ProtobufMessageConverter;

import java.util.List;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("orderExchange");
    }

    @Bean
    public Queue createOrderQueue() {
        return new Queue("createOrderQueue");
    }

    @Bean
    public Queue updateOrderQueue() {
        return new Queue("updateOrderQueue");
    }

    @Bean
    public Queue deleteOrderQueue() {
        return new Queue("deleteOrderQueue");
    }

    @Bean
    public Binding bindingCreateOrder() {
        return BindingBuilder.bind(createOrderQueue())
                .to(exchange())
                .with("createOrderRoutingKey");
    }

    @Bean
    public Binding bindingUpdateOrder() {
        return BindingBuilder.bind(updateOrderQueue())
                .to(exchange())
                .with("updateOrderRoutingKey");
    }

    @Bean
    public Binding bindingDeleteOrder() {
        return BindingBuilder.bind(deleteOrderQueue())
                .to(exchange())
                .with("deleteOrderRoutingKey");
    }
}