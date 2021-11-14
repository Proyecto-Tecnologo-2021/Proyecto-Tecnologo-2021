package proyecto2021G03.appettit.business;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ejb.Stateless;
import java.util.Properties;

@Stateless
public class MailService implements IMailService {

	public void sendMail(String mail, String link) {

		//DATA DEL EMISOR
		String from = "Appetit";
		final String username = "info.appetit.g3@gmail.com";
		final String password = "appetit123";

		//DATA DEL SMTP GMAIL
		String host = "smtp.gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		//CREAMOS OBJETO SESSION CON EL USER Y PASS
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		try {
			//CREA MIME MESSAGE
			Message message = new MimeMessage(session);
			//SETEO EL "FROM"
			message.setFrom(new InternetAddress(from));
			//SETEO EL "HEADER"
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mail));
			//SETEO EL "SUBJECT"
			message.setSubject("Solicitud para cambio de contraseña");
			//CREO Y SETEO EL "BODY"
			String body = "Hello there! Este es el link para resetear su contraseña: "
					+ link + "\n" + "El link expirara en 1 hora." + " \n"
					+ "Si ud no solicitó cambio de password, ignore este mensaje.";
			message.setText(body);
			//ENVIO EL MENSAJE
			Transport.send(message);
			//CONTROL
			System.out.println("El mail se envio correctamente!");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}




