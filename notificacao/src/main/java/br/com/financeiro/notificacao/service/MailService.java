package br.com.financeiro.notificacao.service;

import br.com.financeiro.notificacao.record.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String templateName, Context context) {

        try {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setTo(to);
                messageHelper.setSubject(subject);

                String content = templateEngine.process(templateName, context);
                messageHelper.setText(content, true);
            };

            javaMailSender.send(messagePreparator);
        } catch (MailException exception) {
            throw new RuntimeException("erro", exception);
        }
    }

    @Async
    public void sendEmailFromTemplate(UserRecord userRecord, String subject, String templateName) {
        if (userRecord.username() == null) {
            throw new RuntimeException("erro");
        }

        String token = userRecord.token();
        String link = "http://localhost:4200/nova-senha?token=" + token;

        Context context = new Context();
        context.setVariable("User", userRecord);
        context.setVariable("resetLink", link);

        sendEmail(userRecord.username(), subject, templateName, context);
    }

    @Async
    public void sendCreationEmail(UserRecord userRecord) {
        sendEmailFromTemplate(userRecord, "Bem-vindo " + userRecord.name(),"createUserEmail");
    }
}
