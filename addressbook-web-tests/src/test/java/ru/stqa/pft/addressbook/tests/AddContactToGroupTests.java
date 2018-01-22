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

public class AddContactToGroupTests extends TestBase {

    @BeforeMethod
    private void ensurePreconditions() {

        Groups groups = app.db().groups();
        if (groups.size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test1"));
            groups = app.db().groups();
        }

        Contacts contacts = app.db().contacts();
        int groupsCount = groups.size();
        if ( contacts.stream().noneMatch((c) -> c.getContactGroups().size() < groupsCount) ){
            app.goTo().homePage();
            app.contact().create(new ContactData().withFirstName("fname").withLastName("lname"));
        }
    }

    @Test
    public void testAddingContactToGroup() {

        Groups groups = app.db().groups();
        Contacts contacts = app.db().contacts();
        logger.info(String.format("all contacts = %s", contacts));

        ContactData contact = contacts.stream().filter((c) -> c.getContactGroups().size() < groups.size()).findFirst()
                .orElseThrow(() -> new IllegalStateException("No contact for adding to group exists"));
        logger.info(String.format("contact to add to group = %s", contact));

        Groups contactGroupsBefore = contact.getContactGroups();
        logger.info(String.format("contactGroupsBefore = %s", contactGroupsBefore));

        logger.info(String.format("all groups = %s", groups));
        for (GroupData contactGroup : contactGroupsBefore) {
            groups.remove(contactGroup);
        }
        logger.info(String.format("groups contact is NOT added to = %s", groups));
        GroupData group = groups.iterator().next();

        logger.info(String.format("Adding %s to %s", contact, group));
        app.goTo().homePage();
        app.contact().addToGroup(contact, group);

        Groups contactGroupsAfter = app.db().contacts().stream()
                .filter((c) -> c.getId() == contact.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Contact with group_id = %s not exists", contact.getId())))
                .getContactGroups();
        logger.info(String.format("contactGroupsAfter = %s", contactGroupsAfter));

        assertThat(contactGroupsAfter, equalTo(contactGroupsBefore.withAdded(group)));


    }
}
