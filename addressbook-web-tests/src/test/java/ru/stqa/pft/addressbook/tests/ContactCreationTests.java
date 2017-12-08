package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {
        app.getContactHelper().createContact(new ContactData("fname", "lname", "address", "1111111111",
                "2222222222", "3333333333", "username@domain.com", "test1"));
    }
}
