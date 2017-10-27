package controller.member;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.member.MemberDAO;

public class MailSender {
	private static final String HOST = "localhost";
	private static final String MAIL_ID = "opilior86@gmail.com";
	private static final String MAIL_PW = "bo2560815";

	private static MailSender ms = new MailSender();

	private MailSender() {
	}

	public static MailSender getInstance() {
		return ms;
	}

	public static void sendTemporaryPassword(String userId, String userEmail) {
		Session session = null;
		MimeMessage msg = null;
		String tempPassword = null;

		try {
			if (MemberDAO.getInstance().isExist(userId)) {
				Properties props = new Properties();
				// SSL 사용하는 경우
				props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
				props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
				props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
				props.put("mail.smtp.port", "465"); // SMTP Port

				// TLS 사용하는 경우
				props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
				props.put("mail.smtp.port", "587"); // TLS Port
				props.put("mail.smtp.auth", "true"); // enable authentication
				props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

				// 인증
				System.out.println("[MailSender] Starting Password Auth...");
				Authenticator auth = new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(MAIL_ID, MAIL_PW);
					}
				};

				// 메일 세션
				System.out.println("[MailSender] Starting Mail Session...");
				session = Session.getInstance(props, auth);
				msg = new MimeMessage(session);
				
				msg.setFrom(new InternetAddress(MAIL_ID));
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail, false));

				// tempPassword Logic
				tempPassword = "" + (int) (Math.random() * 100000 + 1);

				// set tempPassword
				String body = "Your Temporary Password for " + userId + " : " + tempPassword;

				MemberDAO.getInstance().updateFindPass(userId, tempPassword);

				msg.setSubject("[ Walk & Talk ] : " + userId + ", Temporary Password !", "UTF-8");
				msg.setText(body, "UTF-8");
				msg.setSentDate(new Date());

				System.out.println("Message is ready");

				Transport transport = session.getTransport("smtps");
				transport.connect("smtp.gmail.com", MAIL_ID, MAIL_PW);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();

				System.out.println("[MailSender] : Email Sent Successfully!!");
			} else {
				System.out.println("[MailSender] : No Such User Found !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}