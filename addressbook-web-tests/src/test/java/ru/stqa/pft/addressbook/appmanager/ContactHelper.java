package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.List;

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

    public void clickEditContact(int index) {
        getContactsTable().get(index).findElement(By.xpath("td[8]/a")).click();
    }

    public void returnToHomePage() {
        click(By.linkText("home page"));
    }

    public void submitContactUpdate() {
        click(By.name("update"));
    }

    public void selectContact(int index) {
        WebElement checkbox = getContactsTable().get(index).findElement(By.tagName("input"));
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

    public void createContact(ContactData contactData) {
        gotoCreateContactPage();
        fillCreateContactForm(contactData, true);
        submitContactCreation();
        returnToHomePage();

    }

    public boolean isThereAContact() {
        return isElementPresent(By.xpath("//table[@id='maintable']//tr[@name='entry']"));
    }

    public List<ContactData> getContactList() {
        List<ContactData> contacts = new ArrayList<ContactData>();
        List<WebElement> elements = getContactsTable();
        for (WebElement element : elements) {
            int id = Integer.parseInt(element.findElement(By.xpath("td[1]/input")).getAttribute("id"));
            String lastName = element.findElement(By.xpath("td[2]")).getText();
            String firstName = element.findElement(By.xpath("td[3]")).getText();
            contacts.add(new ContactData(id, firstName, lastName));
        }
        return contacts;
    }

    private List<WebElement> getContactsTable() {
        return wd.findElements(By.xpath("//table[@id='maintable']//tr[@name='entry']"));

    }
}

