package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactModification() {
        app.getNavigationHelper().gotoHomePage();
        if (!app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("fname", "lname", "address", "1111111111",
                    "2222222222", "3333333333", "username@domain.com", "test1"));
        }

        List<ContactData> before = app.getContactHelper().getContactList();

        int editContactIndex = before.size() - 1;
        app.getContactHelper().clickEditContact(editContactIndex);
        ContactData contact = new ContactData("fnameUpdate", "lnameUpdate", "address","1111111111", "2222222222", "3333333333",
                "username@domain.com", null);
        app.getContactHelper().fillCreateContactForm(contact, false);
        app.getContactHelper().submitContactUpdate();
        app.getContactHelper().returnToHomePage();

        List<ContactData> after = app.getContactHelper().getContactList();

        before.remove(editContactIndex);
        before.add(contact);
        Comparator<? super ContactData> byId = Comparator.comparingInt(ContactData::getId);
        before.sort(byId);
        after.sort(byId);

        Assert.assertEquals(before, after);



    }
}
