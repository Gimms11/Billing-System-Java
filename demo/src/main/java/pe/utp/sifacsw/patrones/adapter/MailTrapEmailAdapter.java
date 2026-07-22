package pe.utp.sifacsw.patrones.adapter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * Adapter Pattern - Implementa EmailSender usando MailTrap
 * Adapta la interfaz JavaMail a nuestro contrato EmailSender
 */
public class MailTrapEmailAdapter implements EmailSender {

    // Credenciales MailTrap
    private static final String HOST = "sandbox.smtp.mailtrap.io";
    private static final int PORT = 2525;
    private static final String USERNAME = "b8e9a799c432a0";
    private static final String PASSWORD = "6646b682525616";
    private static final String FROM_EMAIL = "noreply@mitrufely.com";

    @Override
    public void sendEmail(String to, String subject, String body, File xmlFile) throws Exception {

        // Configurar propiedades de SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", String.valueOf(PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "false");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        // Crear sesión autenticada
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Crear mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Crear contenido con múltiples partes (texto + adjunto)
            MimeMultipart multipart = new MimeMultipart();

            // Parte 1: Cuerpo del texto
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body, "utf-8");
            multipart.addBodyPart(textPart);

            // Parte 2: Archivo XML adjunto
            if (xmlFile != null && xmlFile.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(xmlFile);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            // Enviar correo
            Transport.send(message);


        } catch (MessagingException e) {
            System.err.println("✗ Error al enviar correo: " + e.getMessage());
            throw new Exception("Error al enviar correo a través de MailTrap: " + e.getMessage(), e);
        }
    }
}

