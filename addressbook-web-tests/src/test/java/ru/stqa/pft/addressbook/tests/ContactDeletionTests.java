package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase {

    @Test
    public void testContactDeletion() {
        app.getNavigationHelper().gotoHomePage();
        if (!app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("fname", "lname", "address", "1111111111",
                    "2222222222", "3333333333", "username@domain.com", "test1"));
        }

        List<ContactData> before = app.getContactHelper().getContactList();

        int selectedContactIndex = before.size() - 1;
        app.getContactHelper().selectContact(selectedContactIndex);
        app.getContactHelper().clickDeleteContact();
        app.getContactHelper().confirmContactDeletion();
        app.getNavigationHelper().gotoHomePage();

        List<ContactData> after = app.getContactHelper().getContactList();

        before.remove(selectedContactIndex);

        Assert.assertEquals(before, after);
    }
}
