package kr.happyjob.study.controller.mypage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.happyjob.study.service.mypage.MyPageService;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/mypage/")
public class MyPageController {
	
	// Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();

	@Autowired
	MyPageService mps;
	

	@RequestMapping(value = "myPage.do", method = RequestMethod.GET)
	public Map<String, Object> myPage(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		paramMap.put("loginId", session.getAttribute("loginId"));

		resultMap.put("tell", mps.getTell(paramMap));
		
		return resultMap;
	}

	
	@RequestMapping(value = "resign.do", method = RequestMethod.POST)
	public Map<String, Object> resign(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		paramMap.put("loginId", session.getAttribute("loginId"));

		logger.info("[ "+ className + " ]    paramMap :: " + paramMap.toString());
		
		
		if(mps.resign(paramMap) > 0)resultMap.put("MESSAGE", "SUCCESS");
		else resultMap.put("MESSAGE", "FAIL");

		return resultMap;
	}


	@RequestMapping(value = "updInfo.do", method = RequestMethod.POST)
	public Map<String, Object> updInfo(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.info("[ "+ className + " ]    paramMap :: " + paramMap.toString());

		paramMap.put("loginId", session.getAttribute("loginId"));
		
		if(mps.updInfo(paramMap) > 0)resultMap.put("MESSAGE", "SUCCESS");
		else resultMap.put("MESSAGE", "FAIL");

		return resultMap;
	}

}