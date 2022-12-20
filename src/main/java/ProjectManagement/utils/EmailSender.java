package ProjectManagement.utils;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class EmailSender {
    private final String ourAddress="ProjectManagement@cc19f344-a8a3-43b3-b47d-f72472321dfa.azurecomm.net";
    private EmailClient emailClient;
    public EmailSender(@Value("${azure.connectionString}") String connectionString) {
        emailClient = new EmailClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
    public void sendEmail(List<String> recipientsAddress, String subject, String body) {
        EmailContent content = new EmailContent(subject).setPlainText(body);
        EmailRecipients recipients =new EmailRecipients(recipientsAddress.stream().map(EmailAddress::new).collect(Collectors.toList()));
        EmailMessage message = new EmailMessage(ourAddress,content).setRecipients(recipients);
        emailClient.send(message);
    }
}
