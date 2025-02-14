package br.com.cooperativa.votacaoapi.service.pubsub;

import br.com.cooperativa.votacaoapi.configuration.RabbitMQConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PautaPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicaMensagem(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, "", message);
        System.out.println(message);
    }
}