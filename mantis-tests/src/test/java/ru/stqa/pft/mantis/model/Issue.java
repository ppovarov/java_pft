package ru.stqa.pft.mantis.model;

import com.google.gson.annotations.SerializedName;

public class Issue {

    private int id;

    @SerializedName(value="summary", alternate="subject")
    private String summary;

    private String description;
    private Project project;

    @SerializedName(value="status", alternate="state_name")
    private String status;


    public int getId() {
        return id;
    }

    public Issue withId(int id) {
        this.id = id;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Issue withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Issue withDescription(String description) {
        this.description = description;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public Issue withProject(Project project) {
        this.project = project;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Issue withStatus(String status) {
        this.status = status;
        return this;
    }

}