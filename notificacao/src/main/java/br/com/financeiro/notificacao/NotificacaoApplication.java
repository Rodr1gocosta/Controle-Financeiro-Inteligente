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

		UserRecord userRecord = new UserRecord("Rodrigo Costa", "rodrigomcosta21@gmail.com", "5637060a-a3f1-485b-87ca-87a320949559_2023-12-29T19:18:56.866707341_2023-12-29T20:18:56.866707341");
		mailService.sendCreationEmail(userRecord);
	}
}
