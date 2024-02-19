package kr.happyjob.study.repository.diary;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.happyjob.study.vo.diary.DiaryVO;

@Mapper
public interface DiaryMapper {
	
	public List<DiaryVO> diaryList(Map<String, Object> paramMap);
	
	public DiaryVO diaryDetail(Map<String, Object> paramMap);

}