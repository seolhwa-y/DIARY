package kr.happyjob.study.controller.calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.happyjob.study.service.calendar.CalendarService;

import kr.happyjob.study.vo.calendar.CalendarVO;


@Controller
@RequestMapping("/home/")
public class CalendarController {
    // Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();

    @Autowired
	CalendarService calendarService;

	// 리스트 출력
	@RequestMapping("list.do")
	@ResponseBody
	public Map<String, Object> list(Model model, @RequestParam Map<String, Object> paramMap, 
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		logger.info("   - paramMap : " + paramMap);
		String title = (String) paramMap.get("title");
		String userId = (String) paramMap.get("userId"); // 세션에서 로그인 아이디 가져오기
	    
	    paramMap.put("userId", userId); // paramMap에 로그인 아이디 추가
		
		List<CalendarVO> list = calendarService.calendarList(paramMap);		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("diary", list); // success 용어 담기 
	    
	    return resultMap;
	}	
	
	@RequestMapping("Save.do")
	@ResponseBody
	public Map<String, Object> Save(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		logger.info("+ Start " + className + ".calSave");
		logger.info("   - paramMap : " + paramMap);
		
		String action = (String)paramMap.get("action");
		String resultMsg = "";
		
		// 사용자 정보 설정
		paramMap.put("loginId", session.getAttribute("loginId"));
		
		logger.info("loginId : " + paramMap.get("loginId"));
		logger.info("action : " + paramMap.get("action"));
		
		if ("I".equals(action)) {
			// 신규 저장
			calendarService.calendarInsert(paramMap);
			resultMsg = "SUCCESS";
		} else if("U".equals(action)) {
			// 수정
			calendarService.calendarUpdate(paramMap);
			resultMsg = "UPDATED";
			System.out.println(paramMap);
		} else {
			resultMsg = "FALSE : 등록에 실패하였습니다.";
		}
		
		//결과 값 전송
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", resultMsg);
	    
	    return resultMap;
	}
	
	// 상세조회
		@RequestMapping("read.do")
		@ResponseBody
		public Map<String,Object> calendarRead(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
			
			logger.info("+ Start " + className + ".calendarRead");
			logger.info("   - paramMap : " + paramMap);
			  
			String result="";
			paramMap.put("loginId", session.getAttribute("loginId"));
			
			// 선택된 게시판 1건 조회 
			CalendarVO calendarRead = calendarService.calendarRead(paramMap);
			
			if(calendarRead != null) {
				result = "SUCCESS";  // 성공시 찍습니다. 
			}else {
				result = "FAIL / 불러오기에 실패했습니다.";  // null이면 실패입니다.
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultMsg", result); // success 용어 담기 
			resultMap.put("result", calendarRead); // 리턴 값 해쉬에 담기 
			System.out.println("calendarRead" + calendarRead);
			
			logger.info("+ End " + className + ".calendarRead");
		    
		    return resultMap;
		}
		
		// 삭제
		@RequestMapping("delete.do")
		@ResponseBody
		public Map<String, Object> calendarDelete(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
				HttpServletResponse response, HttpSession session) throws Exception {
			
			logger.info("+ Start " + className + ".calendarDelete");
			logger.info("   - paramMap : " + paramMap);

			String result = "SUCCESS";
			String resultMsg = "삭제 되었습니다.";
			
			calendarService.calendarDelete(paramMap);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("result", result);
			resultMap.put("resultMsg", resultMsg);
			
			logger.info("+ End " + className + ".calendarDelete");
			
			return resultMap;
		}

}
	

	

