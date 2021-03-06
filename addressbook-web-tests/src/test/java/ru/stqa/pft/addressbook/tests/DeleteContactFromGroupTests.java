package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteContactFromGroupTests extends TestBase {

    @BeforeMethod
    private void ensurePreconditions() {

        Contacts contacts = app.db().contacts();
        if ( contacts.stream().noneMatch((c) -> c.getContactGroups().size() > 0) ){

            GroupData group;
            Groups groups = app.db().groups();
            if (groups.size() == 0) {
                group = new GroupData().withName("test1");
                app.goTo().groupPage();
                app.group().create(group);
                groups = app.db().groups();
            }
            group = groups.iterator().next();

            if (contacts.size() > 0) { //add existing contact to group
                app.goTo().homePage();
                app.contact().addToGroup(contacts.iterator().next(), group);
            }
            else{ // create new contact with group
                app.goTo().homePage();
                app.contact().create(new ContactData().withFirstName("fname").withLastName("lname").inGroup(group));
            }
        }
    }

    @Test
    public void testDeletingContactFromGroup() {

        Contacts contacts = app.db().contacts();
        logger.info(String.format("all contacts = %s", contacts));

        ContactData contact = contacts.stream().filter((c) -> c.getContactGroups().size() > 0 ).findFirst()
                .orElseThrow(() -> new IllegalStateException("No contact with group exists"));
        logger.info(String.format("contact to delete from group = %s", contact));

        Groups contactGroupsBefore = contact.getContactGroups();
        logger.info(String.format("contactGroupsBefore = %s", contactGroupsBefore));

        GroupData group = contactGroupsBefore.iterator().next();
        logger.info(String.format("Deleting %s from %s", contact, group));

        app.goTo().homePage();
        app.contact().deleteFromGroup(contact, group);

        Groups contactGroupsAfter = app.db().contacts().stream()
                .filter((c) -> c.getId() == contact.getId()).findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Contact with group_id = %s not exists", contact.getId())))
                .getContactGroups();
        logger.info(String.format("contactGroupsAfter = %s", contactGroupsAfter));

        assertThat(contactGroupsAfter, equalTo(contactGroupsBefore.without(group)));
    }
}
