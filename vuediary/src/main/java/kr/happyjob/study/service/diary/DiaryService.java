package kr.happyjob.study.service.diary;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.happyjob.study.vo.diary.DiaryVO;
import kr.happyjob.study.repository.diary.DiaryMapper;

@Service
public class DiaryService {

    // Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();
	
	@Autowired
	private DiaryMapper mapper;

	public List<DiaryVO> diaryeList(Map<String, Object> paramMap){
        return mapper.diaryList(paramMap);
    }
	
}
