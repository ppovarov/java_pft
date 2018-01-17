package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

    @DataProvider
    public Iterator<Object[]> validContacts() throws IOException {
        try (Reader reader = new FileReader(new File(app.property("contacts.json.file"))
        )) {
            Gson gson = new Gson();
            List<ContactData> contacts = gson.fromJson(reader, new TypeToken<List<ContactData>>() {
            }.getType());
            return contacts.stream().map((c) -> new Object[]{c}).collect(Collectors.toList()).iterator();
        }
    }

    @BeforeMethod
    private void ensurePreconditions() {
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test1"));
        }
    }

    @Test(dataProvider = "validContacts")
    public void testContactCreation(ContactData contact) {
        Groups groups = app.db().groups();
        File photo = new File(app.property("photo.file"));
        contact.withPhoto(photo).inGroup(groups.iterator().next());

        app.goTo().homePage();
        Contacts before = app.db().contacts();
        app.contact().create(contact);

        assertThat(app.contact().count(), equalTo(before.size() + 1));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.withAdded(contact.withID(after.stream().mapToInt((c) -> (c.getId())).max().getAsInt()))));
    }
}
