package br.com.financeiro.seguranca.stream;

import br.com.financeiro.seguranca.stream.record.UserEventRecord;
import br.com.financeiro.seguranca.stream.record.UserNewPasswordEventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class StreamSupplier {

    private final StreamBridge streamBridge;

    private final StreamProperties streamProperties;

    public void publishUserEvent(UserEventRecord userEventRecord) {
        log.info("Publish new userEvent : {}",userEventRecord.id());
        streamBridge.send(streamProperties.getUserEventSupplier(), userEventRecord);
    }

    public void publishUserNewPasswordEvent(UserNewPasswordEventRecord userNewPasswordEventRecord) {
        log.info("Publish send token for create new password");
        streamBridge.send(streamProperties.getUserNewPasswordEventSupplier(), userNewPasswordEventRecord);
    }
}
