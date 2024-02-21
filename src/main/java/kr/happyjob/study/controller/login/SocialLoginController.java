package kr.happyjob.study.controller.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import kr.happyjob.study.vo.login.SocialLogin;

// @controller + @responseBody
@RestController 
public class SocialLoginController {

    @PostMapping("/api/auth/naver")
    public String naverLogin(@RequestBody SocialLogin params) {
    	
    	String code = params.getCode();
    	String state = params.getState();
    	System.out.println("code :  " + code);
    	System.out.println("state :  " + state);
    	
        RestTemplate restTemplate = new RestTemplate();

        // 네이버 토큰 요청 URL 구성
        String tokenRequestUrl = "https://nid.naver.com/oauth2.0/token";
        
        System.out.println("tokenRequestUrl : " + tokenRequestUrl);

        // 요청 파라미터 설정
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("grant_type", "authorization_code");
        requestParams.put("client_id", "lOlSYvpZSiJZtjXKIs4v");
        requestParams.put("client_secret", "KBYDBSTFA_");
        requestParams.put("code", params.getCode());
        requestParams.put("state", params.getState());

        // 토큰 요청 및 응답 받기
        String response = restTemplate.postForObject(tokenRequestUrl, requestParams, String.class);

        // 응답에서 액세스 토큰 추출 (실제 구현에서는 JSON 파싱 필요)
        System.out.println("네이버 토큰 응답: " + response);

        // 여기에서 JSON 파싱 후 액세스 토큰 등을 처리

        return "성공";
    }
	
}
