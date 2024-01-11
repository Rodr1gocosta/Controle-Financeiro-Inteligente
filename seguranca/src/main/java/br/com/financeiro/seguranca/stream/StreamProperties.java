package br.com.financeiro.seguranca.stream;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class StreamProperties {

    @Value(value = "${spring.cloud.stream.bindings.userEventSupplier-out-0.destination}")
    private String userEventSupplier;

    @Value(value = "${spring.cloud.stream.bindings.userNewPasswordEventSupplier-out-0.destination}")
    private String userNewPasswordEventSupplier;

}
