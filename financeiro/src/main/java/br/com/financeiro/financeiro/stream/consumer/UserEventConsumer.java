package br.com.financeiro.financeiro.stream.consumer;

import br.com.financeiro.financeiro.stream.record.UserEventRecord;
import br.com.financeiro.financeiro.domain.enums.ActionType;
import br.com.financeiro.financeiro.record.UserRecord;
import br.com.financeiro.financeiro.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer implements Consumer<UserEventRecord> {

    private final UserService userService;

    @Override
    public void accept(UserEventRecord userEventRecord) {
        log.info("Novo usuario recebido " + userEventRecord.id());

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
