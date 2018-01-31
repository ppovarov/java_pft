package ru.stqa.pft.mantis.appmanager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import ru.stqa.pft.mantis.model.Issue;
import ru.stqa.pft.mantis.model.Project;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

public class BugifyRestHelper {

    private ApplicationManager app;

    public BugifyRestHelper(ApplicationManager app) {
        this.app = app;
    }

    private Executor getExecutor() {
        return Executor.newInstance().auth(app.getProperty("bugify.apiKey"), "");
    }

    public Issue getIssue(int issueId) throws IOException {

        String json = getExecutor().execute(Request.Get(String.format("http://demo.bugify.com/api/issues/%s.json", issueId)))
                .returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        JsonObject issue = parsed.getAsJsonObject()
                .get("issues").getAsJsonArray()
                .get(0).getAsJsonObject();

        return new GsonBuilder().registerTypeAdapter(Issue.class, deserializer).create().fromJson(issue, Issue.class);
    }

    JsonDeserializer<Issue> deserializer = new JsonDeserializer<Issue>() {
        @Override
        public Issue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            Project project = new Project().withId(jsonObject.get("project_id").getAsInt());

            return new Issue()
                    .withId(jsonObject.get("id").getAsInt())
                    .withSummary(jsonObject.get("subject").getAsString())
                    .withDescription(jsonObject.get("description").getAsString())
                    .withProject(project)
                    .withStatus(jsonObject.get("state_name").getAsString());

        }
    };

}

