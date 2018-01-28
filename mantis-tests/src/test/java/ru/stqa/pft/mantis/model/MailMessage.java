package ru.stqa.pft.mantis.model;

public class MailMessage {

    public String to;
    public String subject;
    public String text;

    public MailMessage(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}