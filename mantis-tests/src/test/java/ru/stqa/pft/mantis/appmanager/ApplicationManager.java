package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private WebDriver wd;
    private final Properties properties;
    private String browser;
    private RegistrationHelper registratiobHelper;

    public ApplicationManager(String browser) {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        String target = System.getProperty("target", "local");
        properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
    }

    public void stop() {
        if (wd != null){
            wd.quit();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public HttpSession newSession(){
        return new HttpSession(this);
    }

    public RegistrationHelper registration() {
        if (registratiobHelper == null) {
            registratiobHelper = new RegistrationHelper(this);
        }
        return registratiobHelper;
    }

    public WebDriver getDriver() {
        if (wd == null){
            switch (browser) {
                case BrowserType.FIREFOX:
                    wd = new FirefoxDriver(new FirefoxOptions().setLegacy(true));
                    break;
                case BrowserType.CHROME:
                    wd = new ChromeDriver();
                    break;
                case BrowserType.IE:
                    wd = new InternetExplorerDriver();
                    break;
            }
            wd.manage().timeouts().implicitlyWait(Integer.parseInt(getProperty("webdriver.wait")), TimeUnit.SECONDS);
            wd.get(properties.getProperty("web.baseURL"));
        }
        return wd;
    }
}
