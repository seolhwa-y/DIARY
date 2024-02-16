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
		
		List<DiaryVO> list = diaryService.diaryList(paramMap);		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("diary", list); // success 용어 담기 
	    
	    return resultMap;
	}	

}
