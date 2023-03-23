package kz.kaznu.smart.services;

public interface MailSender {
    boolean send(String subject, String text, String recipient);
}
