package com.pgr.user;

import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pgr.Const;
import com.pgr.SecurityUtils;
import com.pgr.model.UserDomain;
import com.pgr.model.UserEntity;

@Service
public class UserService {

	@Autowired
	private UserMapper mapper;

	@Autowired
	private SecurityUtils sUtils;

	@Autowired
	private JavaMailSender emailSender;

	// join 로직
	public int join(UserEntity p) throws Exception {
		UserEntity check = mapper.selUser(p);

		if (p.getUserEmail().equals("")) { // 아이디(이메일)칸이 비어 있으면 0을 리턴
			return 0;
		}

		// 비밀번호를 암호화
		String salt = sUtils.getSalt();
		String hashPw = sUtils.getHashPw(p.getUserPw(), salt);

		if (check != null) { // 이미 있는 아이디(이메일)이면 1을 리턴
			return 1;
		}
		if (p.getUserPw().equals("")) { // 비밀번호 칸이 비어 있으면 2를 리턴
			return 2;
		}
		if (p.getUserPwRe().equals("")) { // 비밀번호 확인 칸이 비어 있으면 3을 리턴
			return 3;
		}
		if (p.getNickname().equals("")) { // 닉네임 칸이 비어 있으면 4를 리턴
			return 4;
		}
		if (!p.getUserPw().equals(p.getUserPwRe())) { // 비밀번호와 비밀번호 확인 칸의 값이 다르면 5를 리턴
			return 5;
		}
		p.setUserPw(hashPw);

		mapper.insUser(p); // if문에서 하나도 안걸리면 정보를 입력

		return 6; // 회원가입이 성공하면 6를 리턴
	}

	// login 로직
	public int login(UserEntity p, HttpSession hs) throws Exception {
		UserEntity check = mapper.selUser(p); // DB에 저장되어 있는 데이터 가져옴

		if (check == null) { // check == null 이면 2 리턴
			// (select를 userEmail로 하기때문에 입력한 email이 DB에 저장되어있는 userEmail값과 일치하지않으면 NULL값이
			// 넘어옴)
			return 2;
		}

		if (!BCrypt.checkpw(p.getUserPw(), check.getUserPw())) { // 입력한 비밀번호와 DB에 저장되어있는 비밀번호값이 일치하지않으면 3 리턴
			return 3;
		}

		// 세션에 담을때 사용하지않을 정보를 담아두면 메모리 낭비가 되기 때문에 NULL처리
		check.setUserPw(null);
		hs.setAttribute(Const.KEY_LOGINUSER, check);

		return 1;
	}

	public MimeMessage createMessage(UserEntity p) throws Exception {

		MimeMessage message = emailSender.createMimeMessage();

		message.addRecipients(RecipientType.TO, p.getUserEmail());// 보내는 대상
		message.setSubject("PGR 임시 비밀번호가 도착했습니다.");// 제목

		String msg = "";
		msg += "PGR 임시 비밀번호 입니다.";
		msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
		msg += "<h3 style='color: blue;'>";
		msg += p.getNickname() + "님의 임시 비밀번호 입니다. 비밀번호를 변경하여 사용하세요.</h3>";
		msg += "<p>임시 비밀번호 : ";
		msg += p.getUserPw() + "</p></div>";

		message.setText(msg, "utf-8", "html");// 내용
		message.setFrom(new InternetAddress(Const.FROM_ADDRESS));
		return message;

	}

	// findPw 로직
	public int findPw(UserEntity p) throws Exception {
		UserEntity check = mapper.selUser(p); // DB에 저장되어 있는 데이터 가져옴
		// 이메일이 없으면
		if (check == null) {
			return 1;
		}
		// 가입된 닉네임이 아니면
		if (!p.getNickname().equals(check.getNickname())) {
			return 2;
		} else {
			// 임시 비밀번호 생성
			String pw = "";
			for (int i = 0; i < 12; i++) {
				pw += (char) ((Math.random() * 26) + 97);
			}

			// 비밀번호 변경
			p.setUserPw(pw);
			mapper.updateUserPassword(p);

			// 비밀번호 변경 메일 발송
			sendSimpleMessage(p);

			// 비밀번호 암호화 DB저장
			String salt = sUtils.getSalt();
			String hashPw = sUtils.getHashPw(pw, salt);

			p.setUserPw(hashPw);
			mapper.updateUserPassword(p);

			return 3;
		}

	}

	public void sendSimpleMessage(UserEntity p) throws Exception {
		MimeMessage message = createMessage(p);
		try {// 예외처리
			emailSender.send(message);
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}

	}

	// 회원정보 수정 로직
	public int profileChange(UserDomain p, HttpSession hs) throws Exception {
		UserEntity check = mapper.selUser(p); // DB에 저장되어 있는 데이터 가져옴

		if (p.getNickname().equals("")) { // 닉네임이 비어있으면 0 리턴
			return 0;
		}
		if (!BCrypt.checkpw(p.getUserPw(), check.getUserPw())) { // DB에 저장되어있는 비밀번호와 입력한 비밀번호의 정보가 다르면 1 리턴
			return 1;
		}
		if (p.getUserNewPw().equals("")) { // 새 비밀번호 칸이 비어있으면 2 리턴
			return 2;
		}
		if (!p.getUserNewPw().equals(p.getUserPwRe())) { // 새 비밀번호 칸과 새 비밀번호 확인 칸이 다르면 3 리턴
			return 3;
		}

		// 비밀번호 암호화
		String salt = sUtils.getSalt();
		String hashPw = sUtils.getHashPw(p.getUserNewPw(), salt);

		p.setUserPw(hashPw);

		// 세션에 값을 담기위해 set
		check.setNickname(p.getNickname());
		check.setUserPw(p.getUserPw());

		// 세션에 새로 로그인정보를 담아줌
		hs.setAttribute(Const.KEY_LOGINUSER, check);

		mapper.profileChange(p);
		return 4;
	}

	public List<UserEntity> selTopUser() {
		return mapper.selTopUser();
	}
}
