package br.com.financeiro.notificacao.stream.consumer;

import br.com.financeiro.notificacao.stream.record.UserNewPasswordEventRecord;
import br.com.financeiro.notificacao.record.UserRecord;
import br.com.financeiro.notificacao.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserNewPasswordConsumer implements Consumer<UserNewPasswordEventRecord> {

    private final MailService mailService;;

    @Override
    public void accept(UserNewPasswordEventRecord userNewPasswordEventRecord) {
        UserRecord userRecord = new UserRecord(userNewPasswordEventRecord.name(), userNewPasswordEventRecord.email(), userNewPasswordEventRecord.token());
        mailService.sendCreationEmail(userRecord);
    }
}
