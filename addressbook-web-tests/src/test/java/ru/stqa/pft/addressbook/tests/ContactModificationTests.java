package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactCreation() {
        app.getNavigationHelper().gotoHomePage();
        app.getContactHelper().clickEditContact();
        app.getContactHelper().fillCreateContactForm(new ContactData("fnameUpdate", "lnameUpdate", "address", "1111111111", "2222222222", "3333333333", "username@domain.com"));
        app.getContactHelper().submitContactUpdate();
        app.getContactHelper().returnToHomePage();
    }
}