package br.com.financeiro.notificacao.stream.consumer;

import br.com.financeiro.notificacao.record.UserRecord;
import br.com.financeiro.notificacao.service.MailService;
import br.com.financeiro.notificacao.stream.record.UserNewPasswordEventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserNewPasswordConsumer implements Consumer<UserNewPasswordEventRecord> {

    private final MailService mailService;;

    @Override
    public void accept(UserNewPasswordEventRecord userNewPasswordEventRecord) {
        log.info("Consumer new UserEvent");

        UserRecord userRecord = new UserRecord(userNewPasswordEventRecord.name(), userNewPasswordEventRecord.email(), userNewPasswordEventRecord.token());
        mailService.sendCreationEmail(userRecord);
    }
}
