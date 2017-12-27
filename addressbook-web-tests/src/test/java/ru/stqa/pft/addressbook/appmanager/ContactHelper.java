package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.List;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void gotoCreateContactPage() {
        click(By.linkText("add new"));
    }

    public void returnToHomePage() {
        click(By.linkText("home page"));
    }

    private void gotoToHomePage() {
        click(By.linkText("home"));
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

    public void clickEditContactById(int id) {
        wd.findElement(By.xpath("//a[@href='edit.php?id=" + id + "']")).click();
    }

    public void submitContactUpdate() {
        click(By.name("update"));
    }

    private void selectContactById(int id) {
        WebElement checkbox = wd.findElement(By.cssSelector("input[value='" + id + "']"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickDeleteContact() {
        click(By.xpath("//div[@id='content']/form[2]/div[2]/input"));
    }

    public void confirmContactDeletion() {
        wd.switchTo().alert().accept();
    }

    public void create(ContactData contactData) {
        gotoCreateContactPage();
        fillCreateContactForm(contactData, true);
        submitContactCreation();
        contactCache = null;
        returnToHomePage();

    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        clickDeleteContact();
        confirmContactDeletion();
        contactCache = null;
        gotoToHomePage();
    }

    public void modify(ContactData contact) {
        clickEditContactById(contact.getId());
        fillCreateContactForm(contact, false);
        submitContactUpdate();
        contactCache = null;
        returnToHomePage();
    }

    public boolean isThereAContact() {
        return isElementPresent(By.xpath("//table[@id='maintable']//tr[@name='entry']"));
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null){
            return new Contacts(contactCache);
        }

        contactCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.xpath("//table[@id='maintable']//tr[@name='entry']"));
        for (WebElement element : elements) {
            int id = Integer.parseInt(element.findElement(By.xpath("td[1]/input")).getAttribute("id"));
            String lastName = element.findElement(By.xpath("td[2]")).getText();
            String firstName = element.findElement(By.xpath("td[3]")).getText();
            contactCache.add(new ContactData().withID(id).withFirstName(firstName).withLastName(lastName));
        }
        return new Contacts(contactCache);
    }
}

