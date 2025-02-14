package br.com.cooperativa.votacaoapi.service.pubsub;

import br.com.cooperativa.votacaoapi.configuration.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PautaConsumer {

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_1)
    public void consumeMessageQueue1(String message) {
        System.out.println("[Queue 1] Mensagem recebida: " + message);
    }

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_2)
    public void consumeMessageQueue2(String message) {
        System.out.println("[Queue 2] Mensagem recebida: " + message);
    }

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_3)
    public void consumeMessageQueue3(String message) {
        System.out.println("[Queue 3] Mensagem recebida: " + message);
    }
}