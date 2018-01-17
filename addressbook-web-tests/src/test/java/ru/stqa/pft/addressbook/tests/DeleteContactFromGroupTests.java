package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteContactFromGroupTests extends TestBase {

    @BeforeMethod
    private void ensurePreconditions() {
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test1"));
        }
    }

    @Test
    public void testDeletingContactFromGroup() {

        Supplier<ContactData> contactSupplier = () -> {
            Groups groups = app.db().groups();
            ContactData newContact = new ContactData()
                    .withFirstName("fname")
                    .withLastName("lname")
                    .inGroup(groups.iterator().next());
            app.goTo().homePage();
            app.contact().create(newContact);
            Contacts after = app.db().contacts();
            return newContact.withID(after.stream().mapToInt((c) -> (c.getId())).max().getAsInt());
        };

        Groups groups = app.db().groups();
        Contacts contacts = app.db().contacts();
        logger.info(String.format("all groups = %s", groups));
        logger.info(String.format("all contacts = %s", contacts));

        ContactData contact = contacts.stream().filter((c) -> c.getContactGroups().size() > 0 ).findFirst().orElseGet(contactSupplier);
        logger.info(String.format("contact to delete from group = %s", contact));

        Groups contactGroupsBefore = contact.getContactGroups();
        logger.info(String.format("contactGroupsBefore = %s", contactGroupsBefore));

        GroupData group = contactGroupsBefore.iterator().next();
        logger.info(String.format("Deleting %s from %s", contact, group));
        app.goTo().homePage();
        app.contact().deleteFromGroup(contact, group);

        Groups contactGroupsAfter = app.db().contacts().stream()
                .filter((c) -> c.getId() == contact.getId())
                .findFirst().orElse(null)
                .getContactGroups();
        logger.info(String.format("contactGroupsAfter = %s", contactGroupsAfter));

        assertThat(contactGroupsAfter, equalTo(contactGroupsBefore.without(group)));

    }
}