package kr.happyjob.study.repository.notice;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.happyjob.study.vo.notice.NoticeVO;

@Mapper
public interface NoticeMapper {
    // 공지사항 리스트 조회
	public List<NoticeVO> noticeList(Map<String, Object> paramMap);

	// 공지사항 목록 카운트 조회
	public int noticeCnt(Map<String, Object> paramMap) ;

	// 공지사항 단건 조회
	public NoticeVO noticeDetail(Map<String, Object> paramMap) ;

	// 공지사항 저장
	public int insertNotice(Map<String, Object> paramMap);

	// 공지사항 수정
	public int updateNotice(Map<String, Object> paramMap);

	 // 공지사항 삭제
	public int deleteNotice(Map<String, Object> paramMap);
}