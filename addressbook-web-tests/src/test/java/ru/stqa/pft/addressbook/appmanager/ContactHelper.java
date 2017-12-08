package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void gotoCreateContactPage() {
        click(By.linkText("add new"));
    }

    public void fillCreateContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstName());
        type(By.name("lastname"), contactData.getLastName());
        type(By.name("address"), contactData.getAddress());
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("email"), contactData.getEmailAddress());

        if (creation) {
            if (isElementPresent(By.xpath("//select[@name='new_group']"))) {
                Select groupDropdown = new Select(wd.findElement(By.xpath("//select[@name='new_group']")));
                if (isElementPresent(By.xpath("//select[@name='new_group']/option[text()='" + contactData.getGroup() + "']"))) {
                    groupDropdown.selectByVisibleText(contactData.getGroup());
                } else {
                    groupDropdown.selectByVisibleText("[none]");
                }

            } else {
                Assert.fail("Group dropdown is not displayed on Group Creation form");
            }
        } else {
            Assert.assertFalse(isElementPresent(By.xpath("//select[@name='new_group']")), "Group dropdown is displayed on Group Update form");
        }
    }

    public void submitContactCreation() {
        click(By.xpath("//div[@id='content']/form/input[21]"));
    }

    public void clickEditContact() {
        click(By.xpath("//table[@id='maintable']/tbody/tr[2]/td[8]/a/img"));
    }

    public void returnToHomePage() {
        click(By.linkText("home page"));
    }

    public void submitContactUpdate() {
        click(By.name("update"));
    }

    public void selectContact() {
        if (!wd.findElement(By.name("selected[]")).isSelected()) {
            click(By.name("selected[]"));
        }

    }

    public void clickDeleteContact() {
        click(By.xpath("//div[@id='content']/form[2]/div[2]/input"));
    }

    public void confirmContactDeletion() {
        wd.switchTo().alert().accept();
    }

    public void createContact(ContactData contactData) {
        gotoCreateContactPage();
        fillCreateContactForm(contactData, true);
        submitContactCreation();
        returnToHomePage();

    }

    public boolean isThereAContact() {
        if (isElementPresent(By.xpath("//table[@id='maintable']//tr[@name='entry']"))) {
            return true;
        } else {
            return false;
        }

    }
}
