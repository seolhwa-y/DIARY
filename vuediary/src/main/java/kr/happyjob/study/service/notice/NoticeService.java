package kr.happyjob.study.service.notice;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.happyjob.study.vo.notice.NoticeVO;
import kr.happyjob.study.repository.notice.NoticeMapper;

@Service
public class NoticeService {

    	// Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();
	
	@Autowired
	private NoticeMapper mapper;

        // 공지사항 리스트 조회
	public List<NoticeVO> noticeList(Map<String, Object> paramMap){
        return mapper.noticeList(paramMap);
    }
	
	// 공지사항 목록 카운트 조회
	public int noticeCnt(Map<String, Object> paramMap){
        return mapper.noticeCnt(paramMap);
    }
    
	
	// 공지사항 단건 조회
	public NoticeVO noticeDetail(Map<String, Object> paramMap){
        return mapper.noticeDetail(paramMap);
    }
	
	// 공지사항 저장
	public int insertNotice(Map<String, Object> paramMap){
        return mapper.insertNotice(paramMap);
    }
	
	// 공지사항 수정
	public int updateNotice(Map<String, Object> paramMap){
        return mapper.updateNotice(paramMap);
    }
	
	// 공지사항 삭제
	public int deleteNotice(Map<String, Object> paramMap){
        return mapper.deleteNotice(paramMap);
    }
	
}
