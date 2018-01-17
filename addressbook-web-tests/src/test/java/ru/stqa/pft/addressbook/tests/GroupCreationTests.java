package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

    @DataProvider
    public Iterator<Object[]> validGroupsFromXML() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(app.property("groups.xml.file"))))) {
            StringBuilder xmlBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                xmlBuilder.append(line);
                line = reader.readLine();
            }
            XStream xstream = new XStream();
            xstream.processAnnotations(GroupData.class);
            List<GroupData> groups = (List<GroupData>) xstream.fromXML(xmlBuilder.toString());
            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    @DataProvider
    public Iterator<Object[]> validGroupsFromJSON() throws IOException {
        try (Reader reader = new FileReader(new File(app.property("groups.json.file")))) {
            Gson gson = new Gson();
            List<GroupData> groups = gson.fromJson(reader, new TypeToken<List<GroupData>>() {
            }.getType());
            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    @Test(dataProvider = "validGroupsFromJSON")
    public void testGroupCreation(GroupData group) {

        app.goTo().groupPage();
        Groups before = app.db().groups();

        app.group().create(group);
        logger.info(String.format("Group %s is created", group));

        assertThat(app.group().count(), equalTo(before.size() + 1));
        Groups after = app.db().groups();
        assertThat(after, equalTo(before.withAdded(group.withId( after.stream().mapToInt(GroupData::getId).max().getAsInt() ))));
        verifyGroupListInUI();
    }

    @Test
    public void testBadGroupCreation() {

        app.goTo().groupPage();
        Groups before = app.db().groups();
        GroupData group = new GroupData().withName("test1'");

        app.group().create(group);

        assertThat(app.group().count(), equalTo(before.size()));
        Groups after = app.db().groups();
        assertThat(after, equalTo(before));
    }

}
