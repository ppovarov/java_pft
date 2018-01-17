package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactHomePageTests extends TestBase {

    @BeforeMethod
    private void ensurePreconditions() {
        app.goTo().homePage();
        if (app.db().contacts().size() == 0) {
            app.contact().create(new ContactData().withFirstName("fname").withLastName("lname"));
        }
    }

    @Test
    public void testContactHomePage() {

        app.goTo().homePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

        assertThat(contact.getAddress(), equalTo(cleanedAddress(contactInfoFromEditForm)));
        assertThat(contact.getallEmails(), equalTo(mergeEmails(contactInfoFromEditForm)));
        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
    }

    private String cleanedAddress(ContactData contact) {
        return Arrays.stream(contact.getAddress().split("\n"))
                .map((s) -> s.replaceAll("[ ]{2,}", " "))
                .map(String::trim)
                .collect(Collectors.joining("\n"));
    }

    private String mergeEmails(ContactData contact) {
        return Stream.of(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .filter((s) -> !s.equals(""))
                .map(String::trim)
                .collect(Collectors.joining("\n"));
    }

    private String mergePhones(ContactData contact) {
        return Stream.of(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
                .filter((s) -> !s.equals(""))
                .map(ContactHomePageTests::cleanedPhone)
                .collect(Collectors.joining("\n"));
    }

    private static String cleanedPhone(String phone) {
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }


}
