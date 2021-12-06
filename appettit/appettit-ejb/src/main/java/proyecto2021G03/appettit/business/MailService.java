package proyecto2021G03.appettit.business;


import com.fasterxml.jackson.databind.annotation.JsonAppend;
import proyecto2021G03.appettit.exception.AppettitException;

import proyecto2021G03.appettit.util.Constantes;
import javax.ejb.EJB;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.ejb.Stateless;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Stateless
public class MailService implements IMailService {
	@EJB
	IUsuarioService iUsuarioService;

	public void sendMail(String mail, String link) {

		//DATA DEL EMISOR
		String from = "Appetit";
//		final String username = "info.appetit.g3@gmail.com";
//		final String password = "appetit123";


		//DATA DEL SMTP GMAIL
		Properties props = Constantes.MAIL_PROPS();
//		String host = "smtp.gmail.com";
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", host);
//		props.put("mail.smtp.port", "587");

		//CREAMOS OBJETO SESSION CON EL USER Y PASS
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(Constantes.USER_NAME, Constantes.USER_PASS);
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
			String clientName = iUsuarioService.buscarPorCorreoCliente(mail).getNombre();
			//CREO HYPERLINK CON EL LINK RECIBIDO
			String hypLink = "<a href= " + link + ">link</a>";
			//CREO EL BODY DEL CORREO
			String body = "<p style="+"font-size:14px"+">Hola " + clientName +"!"
					+ "<br>"
					+ " Este es el "+ hypLink +" para resetear su contraseña.</p>"
//					+ "<br>"
					+ "<p>Saluda atentamente,<br> "
					+ "el staff de Appetit.</p>"
					+ "<br>"
					+ "<p style="+"font-size:12px"+"><i> El enlace expirará en una hora."
					+ "<br>"
					+ "Si usted no solicitó el cambio de contraseña, ignore este mensaje.</i></p>";


			//SETEO EL BODY EN EL MENSAJE
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body,"UTF-8","html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			//ENVIO EL MENSAJE
			Transport.send(message);
			//CONTROL
			System.out.println("El mail se envio correctamente!");
		} catch (MessagingException | AppettitException e) {
			throw new RuntimeException(e);
		}
	}

}




