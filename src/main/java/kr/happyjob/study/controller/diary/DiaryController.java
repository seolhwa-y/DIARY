package kr.happyjob.study.controller.diary;

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

import kr.happyjob.study.service.diary.DiaryService;
import kr.happyjob.study.vo.diary.DiaryVO;
import kr.happyjob.study.vo.notice.NoticeModel;


@Controller
@RequestMapping("/diary/")
public class DiaryController {
    // Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();

    @Autowired
	DiaryService diaryService;

	// 리스트 출력
	@RequestMapping("list.do")
	@ResponseBody
	public Map<String, Object> list(Model model, @RequestParam Map<String, Object> paramMap, 
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		logger.info("   - paramMap : " + paramMap);
		String title = (String) paramMap.get("title");
		String userId = (String) paramMap.get("userId"); // 세션에서 로그인 아이디 가져오기
	    
	    paramMap.put("userId", userId); // paramMap에 로그인 아이디 추가
		
		List<DiaryVO> list = diaryService.diaryList(paramMap);		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("diary", list); // success 용어 담기 
	    
	    return resultMap;
	}	
	
	// 상세조회
	@RequestMapping("detail.do")
	@ResponseBody
	public Map<String,Object> detailDiary(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		logger.info("+ Start " + className + ".detailDiary");
		logger.info("   - paramMap : " + paramMap);
		  
		String result="";
		
		// 선택된 게시판 1건 조회 
		DiaryVO detailDiary = diaryService.diaryDetail(paramMap);
		
		if(detailDiary != null) {
			result = "SUCCESS";  // 성공시 찍습니다. 
		}else {
			result = "FAIL / 불러오기에 실패했습니다.";  // null이면 실패입니다.
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", result); // success 용어 담기 
		resultMap.put("result", detailDiary); // 리턴 값 해쉬에 담기 
		System.out.println("detailDiary" + detailDiary);
		
		logger.info("+ End " + className + ".detailDiary");
	    
	    return resultMap;
	}
	
	// 삭제
	@RequestMapping("delete.do")
	@ResponseBody
	public Map<String, Object> diaryDelete(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		
		logger.info("+ Start " + className + ".diaryDelete");
		logger.info("   - paramMap : " + paramMap);

		String result = "SUCCESS";
		String resultMsg = "삭제 되었습니다.";
		
		// 그룹코드 삭제
		diaryService.diaryDelete(paramMap);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		resultMap.put("resultMsg", resultMsg);
		
		logger.info("+ End " + className + ".noticeDelete");
		
		return resultMap;
	}

}
