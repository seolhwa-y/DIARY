package kr.happyjob.study.service.calendar;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.happyjob.study.vo.calendar.CalendarVO;
import kr.happyjob.study.repository.calendar.CalendarMapper;

@Service
public class CalendarService {

    // Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();
	
	@Autowired
	private CalendarMapper mapper;

	public List<CalendarVO> calendarList(Map<String, Object> paramMap){
		String userId = (String) paramMap.get("userId"); // 파라미터에서 userId 가져오기
        return mapper.calendarList(paramMap);
    }
	
	public int calendarInsert(Map<String, Object> paramMap) throws Exception {

		return mapper.calendarInsert(paramMap);
	}


	public int calendarUpdate(Map<String, Object> paramMap) throws Exception {

		return mapper.calendarUpdate(paramMap);
	}
	
	public CalendarVO calendarRead(Map<String, Object> paramMap) throws Exception {

		return mapper.calendarRead(paramMap);
	}
	
//	public int diaryDelete(Map<String, Object> paramMap) throws Exception {
//
//		return mapper.diaryDelete(paramMap);
//	}
	


}
