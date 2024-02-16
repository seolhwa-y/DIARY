package kr.happyjob.study.service.register;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.happyjob.study.repository.register.RegisterMapper;
import kr.happyjob.study.vo.register.UserVo;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class RegisterService {
	@Autowired
	RegisterMapper registerMapper;

	// 아이디 중복검사
	public int checkID(String loginID) {

		return registerMapper.checkID(loginID);
	}

	// 휴대폰 중복검사
	public int checkHp(String hp) {
		// TODO Auto-generated method stub
		return registerMapper.checkHp(hp);

	}

	// 휴대폰 인증
	public void certifiedPhoneNumber(String userPhon, String cerNum) {

		String api_key = "NCS5A6B7M9WDC6RT";
		String api_secret = "YW943QDUWRVTNLZMUQ5VV1HSQW1M7ZYK";
		Message coolsms = new Message(api_key, api_secret);

		// 4 params(to, from, type, text) are mandatory. must be filled
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", userPhon); // 수신전화번호
		params.put("from", userPhon); // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
		params.put("type", "SMS");
		params.put("text", "해피잡 휴대폰인증 테스트 메시지 : 인증번호는" + "[" + cerNum + "]" + "입니다.");
		params.put("app_version", "test app 1.2"); // application name and version

		try {
			JSONObject obj = (JSONObject) coolsms.send(params);
			System.out.println(obj.toString());
		} catch (CoolsmsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCode());
		}

	}

	// 회원가입
	public int register(UserVo userVo) {
		return registerMapper.insertUser(userVo);
	}

	// 아이디 비밀번호 찾기
	public UserVo findAccount(String hp) {
		// TODO Auto-generated method stub
		return registerMapper.findAccount(hp);
	}
	
	// 임시 비밀번호 업데이트
	public void updateUserPassword(UserVo userVo) {
		registerMapper.updateUserPassword(userVo.getHp(), userVo.getPassword());
	}

}
