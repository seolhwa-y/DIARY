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

	public List<DiaryVO> diaryList(Map<String, Object> paramMap){
		String userId = (String) paramMap.get("userId"); // 파라미터에서 userId 가져오기
        return mapper.diaryList(paramMap);
    }
	
	public DiaryVO diaryDetail(Map<String, Object> paramMap) throws Exception {

		return mapper.diaryDetail(paramMap);
	}
	
	public int diaryDelete(Map<String, Object> paramMap) throws Exception {

		return mapper.diaryDelete(paramMap);
	}
	
	public int diaryInsert(Map<String, Object> paramMap) throws Exception {

		return mapper.diaryInsert(paramMap);
	}

	public int diaryUpdate(Map<String, Object> paramMap) throws Exception {

		return mapper.diaryUpdate(paramMap);
	}
	
	public boolean isDateDuplicate(Map<String, Object> paramMap) {
	    // Mapper를 통해 중복된 날짜가 있는지 확인하는 메소드 호출
	    int count = mapper.isDateDuplicate(paramMap);
	    // count가 0보다 크면 중복된 날짜가 있음을 의미
	    return count > 0;
	}

}
