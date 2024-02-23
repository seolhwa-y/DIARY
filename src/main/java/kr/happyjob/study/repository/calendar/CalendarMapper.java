package kr.happyjob.study.repository.calendar;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.happyjob.study.vo.calendar.CalendarVO;

@Mapper
public interface CalendarMapper {
	
	public List<CalendarVO> calendarList(Map<String, Object> paramMap);
	
	public CalendarVO calendarRead(Map<String, Object> paramMap);
	
	public int calendarDelete(Map<String, Object> paramMap);
	
	public int calendarInsert(Map<String, Object> paramMap);
			
	public int calendarUpdate(Map<String, Object> paramMap);

}