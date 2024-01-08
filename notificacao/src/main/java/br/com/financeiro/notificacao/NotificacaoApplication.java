package br.com.financeiro.notificacao;

import br.com.financeiro.notificacao.record.UserRecord;
import br.com.financeiro.notificacao.service.MailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.context.Context;

@SpringBootApplication
public class NotificacaoApplication implements CommandLineRunner {

	private final MailService mailService;

	public NotificacaoApplication(MailService mailService) {
		this.mailService = mailService;
	}

	public static void main(String[] args) {
		SpringApplication.run(NotificacaoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		UserRecord userRecord = new UserRecord("Rodrigo Costa", "rodrigomcosta20@icloud.com", "a73c868b-b738-41e2-87d9-c227b87efef4_2024-01-06T10:35:25.069234108_2024-01-06T11:35:25.069234108");
		mailService.sendCreationEmail(userRecord);
	}
}
