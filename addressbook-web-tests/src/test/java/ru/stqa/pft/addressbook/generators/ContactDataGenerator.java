package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

    @Parameter(names = "-c", description = "Contact count")
    public int count;

    @Parameter(names = "-f", description = "Target file", required = true)
    public String file;

    @Parameter(names = "-d", description = "Data format")
    public String format = "json";

    public static void main(String[] args) throws IOException {
        ContactDataGenerator generator = new ContactDataGenerator();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(generator)
                .build();
        try {
            jCommander.parse(args);
        } catch (ParameterException ex) {
            jCommander.usage();
            return;
        }
        generator.run();
    }

    private void run() throws IOException {
        List<ContactData> contacts = generateContacts(count);
        if (format.equals("json")) {
            saveAsJSON(contacts, new File(file));
        } else {
            System.out.println(String.format("Unrecognized format %s", format));
        }
    }

    private void saveAsJSON(List<ContactData> contacts, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(contacts);
        try (Writer writer = new FileWriter(file)) {
            writer.write(json);
        }
    }

    private List<ContactData> generateContacts(int count) {
        List<ContactData> contacts = new ArrayList<ContactData>();
        for (int i = 0; i < count; i++) {
            contacts.add(new ContactData()
                    .withFirstName(String.format("firstname %s", i))
                    .withLastName(String.format("lastname %s", i))
                    .withAddress(String.format("%s Broadway", i))
                    .withHomePhone("(111)111-11-11 ")
                    .withMobilePhone("  222 222 22 22")
                    .withWorkPhone("+7(333)333-3333")
                    .withEmail(String.format("username%s@domain.com", i))
                    .withGroup(String.format("test_%s", i)));
        }
        return contacts;
    }
}
