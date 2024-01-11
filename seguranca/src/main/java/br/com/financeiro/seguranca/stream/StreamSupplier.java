package br.com.financeiro.seguranca.stream;

import br.com.financeiro.seguranca.stream.record.UserEventRecord;
import br.com.financeiro.seguranca.stream.record.UserNewPasswordEventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StreamSupplier {

    private final StreamBridge streamBridge;
    private final StreamProperties streamProperties;

    public void publishUserEvent(UserEventRecord userEventRecord) {
        log.info("Novo usuario " + userEventRecord.id());
        streamBridge.send(streamProperties.getUserEventSupplier(), userEventRecord);
    }

    public void publishUserNewPasswordEvent(UserNewPasswordEventRecord userNewPasswordEventRecord) {
        log.info("Enviar token para criar nova senha");
        streamBridge.send(streamProperties.getUserNewPasswordEventSupplier(), userNewPasswordEventRecord);
    }
}
