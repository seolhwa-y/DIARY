package kr.happyjob.study.controller.login;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import kr.happyjob.study.service.login.LoginService;
import kr.happyjob.study.vo.register.UserVo;

// @controller + @responseBody
@RestController
public class SocialLoginController {

	@Autowired
	LoginService loginService;

	@GetMapping("/api/auth/naver")
	public ResponseEntity<?> naverLogin(@RequestParam String code, @RequestParam String state) {
		System.out.println("Code: " + code);
		System.out.println("State: " + state);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "lOlSYvpZSiJZtjXKIs4v");
		params.add("client_secret", "KBYDBSTFA_");
		params.add("redirect_uri", "http://localhost:8080");
		params.add("code", code);
		params.add("state", state);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

		String tokenRequestUrl = "https://nid.naver.com/oauth2.0/token";
		ResponseEntity<String> response = restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, requestEntity,
				String.class);

		JSONObject tokenResponse = new JSONObject(response.getBody());
		String accessToken = tokenResponse.getString("access_token");
		String tokenType = tokenResponse.getString("token_type");

		// 사용자 정보 요청
		HttpHeaders userInfoHeaders = new HttpHeaders();
		userInfoHeaders.setBearerAuth(accessToken);
		HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
		String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
		ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest,
				String.class);

		JSONObject userResponse = new JSONObject(userInfoResponse.getBody());
		JSONObject userInfo = userResponse.getJSONObject("response");

		System.out.println("유저 정보 : " + userInfo);
		// 사용자 정보 출력 (예: ID, 이메일)
//	        System.out.println("User ID: " + userInfo.getString("id"));
//	        System.out.println("User Email: " + userInfo.getString("email"));
		// 전화번호 값
//		String mobile = userInfo.getString("mobile");
		// 유저 닉네임

//	        하이픈 제거
//		String ph = mobile.replace("-", "");
//		System.out.println("ph : " + ph);
		// 이메일
		String email = userInfo.getString("email");
		// 회원 조회
		int result = loginService.selectUserInfo(email);

		System.out.println("result : " + result);

		String naver = "Y";
		
		// 조회 결과를 userInfo JSON 객체에 추가
		userInfo.put("login_result", result);
		userInfo.put("naver", naver);

		// 사용자 정보를 포함한 응답 반환
		return ResponseEntity.ok(userInfo.toString());
	}
	
	// 카카오 이메일로 사용자 조회
	@GetMapping("/api/auth/kakao")
	public int selectKakaEmail(@RequestParam String email) {
		
		System.out.println("email : " + email);
		
		int result = loginService.selectUserInfo(email);
		
		System.out.println("카카오 가입 유무" + result);
		
		return result;
	}
	
	
	//
	@PostMapping("/api/login")
	public UserVo phSelected(@RequestBody UserVo userVo) {
		System.out.println("userVo : " + userVo);

		String email = userVo.getEmail();
		
		System.out.println("hp : " + email);
		UserVo userInfo = loginService.phSelected(email);
		
		System.out.println("userInfo : " + userInfo);
		
		return userInfo;
	}

}
