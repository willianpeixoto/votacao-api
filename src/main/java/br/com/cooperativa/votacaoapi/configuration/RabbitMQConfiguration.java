package br.com.cooperativa.votacaoapi.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE_NAME = "fanout-exchange";
    public static final String QUEUE_1 = "queue-1";
    public static final String QUEUE_2 = "queue-2";
    public static final String QUEUE_3 = "queue-3";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_1, false);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_2, false);
    }

    @Bean
    public Queue queue3() {
        return new Queue(QUEUE_3, false);
    }

    @Bean
    public Binding binding1(FanoutExchange fanoutExchange, Queue queue1) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }

    @Bean
    public Binding binding2(FanoutExchange fanoutExchange, Queue queue2) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    @Bean
    public Binding binding3(FanoutExchange fanoutExchange, Queue queue3) {
        return BindingBuilder.bind(queue3).to(fanoutExchange);
    }
}