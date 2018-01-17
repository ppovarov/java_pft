package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

    @BeforeMethod
    private void ensurePreconditions() {
        app.goTo().homePage();
        if (app.db().contacts().size() == 0) {
            app.contact().create(new ContactData().withFirstName("fname").withLastName("lname"));
        }
    }

    @Test
    public void testContactModification() {

        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        ContactData contact = new ContactData()
                    .withID(modifiedContact.getId())
                    .withFirstName(modifiedContact.getFirstName())
                    .withLastName(modifiedContact.getLastName())
                    .withAddress(modifiedContact.getAddress())
                    .withHomePhone(modifiedContact.getHomePhone())
                    .withMobilePhone(modifiedContact.getMobilePhone())
                    .withWorkPhone(modifiedContact.getWorkPhone())
                    .withEmail(modifiedContact.getEmail())
                    // now modifying group
                    .withFirstName("fnameUpdate")
                    .withLastName("lnameUpdate");

        app.contact().modify(contact);
        logger.info(String.format("Contact %s is modified with %s", modifiedContact, contact));

        assertThat(app.contact().count(), equalTo(before.size()));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
    }
}
