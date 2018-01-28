package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import ru.stqa.pft.mantis.model.UserData;

public class AccountHelper extends HelperBase {

    public AccountHelper(ApplicationManager app) {
        super(app);
    }

    public void register(String username, String email) {
        wd.get(app.getProperty("web.baseURL") + "/signup_page.php");
        type(By.name("username"), username);
        type(By.name("email"), email);
        click(By.cssSelector("input[value='Signup']"));
    }

    public void verify(String confirmationLink, String password) {
        wd.get(confirmationLink);
        type(By.name("password"), password);
        type(By.name("password_confirm"), password);
        click(By.cssSelector("button[type='submit']"));
    }

    public void changePassword(UserData user) {
        //login by admin
        wd.get(app.getProperty("web.baseURL") + "/login_page.php");
        type(By.name("username"), app.getProperty("web.adminLogin"));
        click(By.cssSelector("input[value='Login']"));
        type(By.name("password"), app.getProperty("web.adminPassword"));
        click(By.cssSelector("input[value='Login']"));

        //open Edit user page for <user>
        wd.get(String.format("%s/manage_user_edit_page.php?user_id=%s", app.getProperty("web.baseURL"), user.getId()));

        //click Reset password
        click(By.cssSelector("input[value='Reset Password']"));
    }

}
