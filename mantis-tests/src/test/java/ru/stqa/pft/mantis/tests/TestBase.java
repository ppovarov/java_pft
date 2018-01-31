package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.remote.BrowserType;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.mantis.appmanager.ApplicationManager;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;

public class TestBase {

    protected static final ApplicationManager app = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

    @BeforeSuite
    public void setUp() throws Exception {
        app.init();
        app.ftp().upload(new File("src/test/resources/config_inc.php"), "config_inc.php", "config_inc.php.bak");
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws IOException {
        app.ftp().restore("config_inc.php.bak", "config_inc.php");
        app.stop();
    }

    public boolean isIssueOpen(int issueId) throws IOException, ServiceException {
        //String status = app.soap().getIssue(issueId).getStatus();
        String status = app.bugify().getIssue(issueId).getStatus();
        return !(status.equalsIgnoreCase("resolved") || status.equalsIgnoreCase("closed"));
    }

    public void skipIfNotFixed(int issueId) {
        try {
            if (isIssueOpen(issueId)) {
                throw new SkipException("Ignored because of issue " + issueId);
            }
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
        }
    }
}
