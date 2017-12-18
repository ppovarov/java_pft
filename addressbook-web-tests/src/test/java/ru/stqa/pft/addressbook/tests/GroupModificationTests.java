package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.HashSet;
import java.util.List;

public class GroupModificationTests extends TestBase {

    @Test
    public void testGroupModification() {
        app.getNavigationHelper().gotoGroupPage();
        if (!app.getGroupHelper().isThereAGroup()) {
            app.getGroupHelper().createGroup(new GroupData("test1", null, null));
        }
        List<GroupData> before = app.getGroupHelper().getGroupList();
        int selectedGroupIndex = before.size() - 1;
        app.getGroupHelper().selectGroup(selectedGroupIndex);
        app.getGroupHelper().editSelectedGroup();
        GroupData group = new GroupData(before.get(selectedGroupIndex).getId(), "test1", "test2update", "test3update");
        app.getGroupHelper().fillGroupForm(group);
        app.getGroupHelper().submitGroupUpdate();
        app.getGroupHelper().returnToGroupPage();
        List<GroupData> after = app.getGroupHelper().getGroupList();
        Assert.assertEquals(after.size(), before.size());

        before.remove(selectedGroupIndex);
        before.add(group);
        Assert.assertEquals(new HashSet<Object>(before), new HashSet<Object>(after));

    }

}
