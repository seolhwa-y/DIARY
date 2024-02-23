package kr.happyjob.study.repository.calendar;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.happyjob.study.vo.calendar.CalendarVO;
import kr.happyjob.study.vo.diary.DiaryVO;

@Mapper
public interface CalendarMapper {
	
	public List<CalendarVO> calendarList(Map<String, Object> paramMap);
	
//	public DiaryVO diaryDetail(Map<String, Object> paramMap);
	
//	public int diaryDelete(Map<String, Object> paramMap);
	
	public int calendarInsert(Map<String, Object> paramMap);
			
	public int calendarUpdate(Map<String, Object> paramMap);

}