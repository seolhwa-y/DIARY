package kr.happyjob.study.controller.login;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
<<<<<<< HEAD
=======

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpSession;
>>>>>>> 4cd4dd1 (dd)


@RestController
@RequestMapping("/api/")
public class LogOutController {
	
	@Autowired
	HttpSession session;

<<<<<<< HEAD
=======
	
>>>>>>> 4cd4dd1 (dd)
	@PostMapping("logout")
	public void logout() {
		session.invalidate(); // 세션 무효화
	}

	@PostMapping("logout/naver")
	public void naverLogout() {
		RestTemplate restTemplate = new RestTemplate();

		URI uri = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
        					            .queryParam("grant_type", "delete")
                    					.queryParam("client_id", "lOlSYvpZSiJZtjXKIs4v")
										.queryParam("client_secret", "KBYDBSTFA_")
										.queryParam("access_token", session.getAttribute("access_token"))
										.queryParam("service_provider", "NAVER")
										.build()
										.toUri();
                    
        Map<?,?> response = restTemplate.getForObject(uri, Map.class);

		System.out.println("로그아웃 결과 : " + response.get("result"));

		if(response.get("result") == "success") session.invalidate();
	}	
}
