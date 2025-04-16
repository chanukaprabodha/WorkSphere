package lk.ijse.worksphere.service;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlContent);
}
