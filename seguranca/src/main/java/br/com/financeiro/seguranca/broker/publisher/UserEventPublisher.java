package br.com.financeiro.seguranca.broker.publisher;

import br.com.financeiro.seguranca.broker.record.UserEventRecord;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.financeiro.name}")
    private String routingKey;

    public void publishUserEvent(UserEventRecord userEventRecord) {
        rabbitTemplate.convertAndSend("", routingKey, userEventRecord);
    }
}
