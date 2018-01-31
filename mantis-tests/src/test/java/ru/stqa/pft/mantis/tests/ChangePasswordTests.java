package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChangePasswordTests extends  TestBase {

    @Test
    public void testChangePassword() throws IOException, MessagingException {

        skipIfNotFixed(2);
        skipIfNotFixed(3);

        String mantisUsername = "test";
        String newMantisPassword = "Password456";

        User user = app.db().users().stream().filter((u)-> mantisUsername.equals(u.getUsername())).findFirst().get();

        //String emailUsername = "test";
        String emailUsername = user.getEmail().split("@")[0];
        String emailPassword = "Password123";

        app.james().drainEmail(emailUsername, emailPassword);
        app.account().changePassword(user);

        List<MailMessage> mailMessages = app.james().waitForMail(emailUsername, emailPassword, 60000);
        MailMessage passwordResetMessage = mailMessages.stream().filter((m) -> m.subject.equals("[MantisBT] Password Reset")).findFirst().get();
        String confirmationLink = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build().getText(passwordResetMessage.text);

        app.account().verify(confirmationLink, newMantisPassword);

        assertTrue(app.newSession().login(user.getUsername(), newMantisPassword));

    }

}
