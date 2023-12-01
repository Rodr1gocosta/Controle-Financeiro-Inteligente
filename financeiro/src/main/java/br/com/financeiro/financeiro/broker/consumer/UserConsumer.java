package br.com.financeiro.financeiro.broker.consumer;

import br.com.financeiro.financeiro.broker.record.UserEventRecord;
import br.com.financeiro.financeiro.domain.enums.ActionType;
import br.com.financeiro.financeiro.record.UserRecord;
import br.com.financeiro.financeiro.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    UserService userService;

    @RabbitListener(queues = "${broker.queue.financeiro.name}")
    public void listenUserEvent(@Payload UserEventRecord userEventRecord) {
        UserRecord userRecord =  new UserRecord(userEventRecord.id(), userEventRecord.name(), userEventRecord.status(), userEventRecord.cpf(), userEventRecord.phoneNumber());

        switch (ActionType.valueOf(userEventRecord.actionType())) {
            case CREATE :
            case UPDATE :
                userService.save(userRecord);
                break;
            case DELETE :
                userService.delete(userEventRecord.id());
                break;
        }
    }
}
