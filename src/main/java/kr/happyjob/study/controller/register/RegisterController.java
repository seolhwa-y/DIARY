package kr.happyjob.study.controller.register;

import java.security.SecureRandom;

import org.hibernate.boot.model.source.spi.JoinedSubclassEntitySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.happyjob.study.service.register.RegisterService;
import kr.happyjob.study.vo.register.UserVo;

@RestController
@RequestMapping("/api/")
public class RegisterController {

	@Autowired
	RegisterService registerService;

	// 유저ID 중복 검사
	@PostMapping("checkID")
	public int checkID(@RequestBody UserVo userVo) {

		System.out.println("userVo : " + userVo);

		String loginID = userVo.getLoginID();

		System.out.println("loginID : " + loginID);

		int result = registerService.checkID(loginID);
		System.out.println("result : " + result);
		return result;
	}
	
	// 이메일 중복 검사
		@PostMapping("checkEmail")
		public int checkEmail(@RequestBody UserVo userVo) {


			String loginEmail = userVo.getEmail();

			System.out.println("이메일 : " + loginEmail);

			int result = registerService.checkEmail(loginEmail);
			System.out.println("result : " + result);
			return result;
		}

	// 휴대폰 번호 중복 검사 / 가입유무
	@PostMapping("checkHp")
	public int checkHp(@RequestBody UserVo userVo) {

		System.out.println("userVo : " + userVo);

		String hp = userVo.getHp();

		System.out.println("loginID : " + hp);

		int result = registerService.checkHp(hp);
		System.out.println("result : " + result);
		return result;

	}

	// 휴대폰 인증
	@PostMapping("sendSMS")
	public String sendSMS(@RequestBody UserVo userVo) {

		String userPhone = userVo.getHp();

		System.out.println("핸드폰 번호 : " + userPhone);

		// 보안을 강화하기 위해 SecureRandom 사용
		SecureRandom rand = new SecureRandom();
		StringBuilder numStr = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int ran = rand.nextInt(10);
			numStr.append(ran);
		}

		// 인증 코드를 SMS로 전송하는 로직을 서비스 레이어에서 처리
		registerService.certifiedPhoneNumber(userPhone, numStr.toString());

		// 인증 코드를 클라이언트(프론트엔드)로 반환
		// 보안상의 이유로 실제 배포 환경에서는 인증 코드를 직접 반환하지 않습니다.
		return numStr.toString();
	}

	// 회원가입
	@PostMapping("register")
	public int register(@RequestBody UserVo userVo) {
		System.out.println("받은 값 : " + userVo);
		int result = registerService.register(userVo);

		System.out.println("result : " + result);

		return result;
	}

	// 아이디 비밀번호 찾기
	@PostMapping("findAccount")
	public UserVo findAccount(@RequestBody UserVo userVo) {
		System.out.println("userVo : " + userVo);

		String hp = userVo.getHp();
		System.out.println("hp : " + hp);

		UserVo userInfo = registerService.findAccount(hp);
		System.out.println("userInfo : " + userInfo);
		if (userInfo != null) {
			// 비밀번호 랜덤 생성
			String newPassword = generateRandomPassword(6);
			// 비밀번호 업데이트
			userInfo.setPassword(newPassword);
			
			registerService.updateUserPassword(userInfo);

			System.out.println("변경된 비밀번호 : " + userInfo.getPassword());
			// 업데이트된 비밀번호로 사용자 정보 반환
			System.out.println("변경 된 userInfo : " + userInfo);

			return userInfo;
		}

		return null; // 사용자 정보가 없을 경우 null 반환
	}

	// 랜덤 비밀번호
	private String generateRandomPassword(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			// 0-9 사이의 숫자를 생성
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}