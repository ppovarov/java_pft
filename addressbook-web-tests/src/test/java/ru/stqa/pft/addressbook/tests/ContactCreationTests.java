package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.Comparator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {

        Contacts before = app.contact().all();
        ContactData contact = new ContactData().withFirstName("fname").withLastName("lname").withAddress("address").withHomePhone("1111111111")
                .withMobilePhone("2222222222").withWorkPhone("3333333333").withEmail("username@domain.com").withGroup("test1");

        app.contact().create(contact);

        Contacts after = app.contact().all();
        assertThat(after.size(), equalTo(before.size() + 1));
        assertThat(after, equalTo(before.withAdded(contact.withID(after.stream().mapToInt((c) -> (c.getId())).max().getAsInt()))));
    }
}
