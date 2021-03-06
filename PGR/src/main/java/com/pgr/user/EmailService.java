package com.pgr.user;

import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pgr.Const;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	// static으로 메모리에 올려서 controller에서 사용
	public static String ePw = createKey();

	private MimeMessage createMessage(String to) throws Exception {
		MimeMessage message = emailSender.createMimeMessage();
		
		message.addRecipients(RecipientType.TO, to);// 보내는 대상
		message.setSubject("PGR 인증번호가 도착했습니다.");// 제목

		String msgg = "";
		msgg += "<div style='margin:100px;'>";
		msgg += "<h1> 안녕하세요  PGR입니다!!! </h1>";
		msgg += "<br>";
		msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
		msgg += "<br>";
		msgg += "<p>감사합니다!<p>";
		msgg += "<br>";
		msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
		msgg += "<h3 style='color:blue;'>회원가입 코드입니다.</h3>";
		msgg += "<div style='font-size:130%'>";
		msgg += "CODE : <strong>";
		msgg += ePw + "</strong><div><br/> ";
		msgg += "</div>";
		message.setText(msgg, "utf-8", "html");// 내용
		message.setFrom(new InternetAddress(Const.FROM_ADDRESS));
		System.out.println("인증번호 : " + ePw);
		return message;
	}

//	인증코드 만들기
	public static String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 8; i++) { // 인증코드 6자리
			key.append((rnd.nextInt(10)));
				// 0~9
		}

		return key.toString();
	}

	public void sendSimpleMessage(String to) throws Exception {
		MimeMessage message = createMessage(to);
		try {// 예외처리
			emailSender.send(message);
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}

	}


}
