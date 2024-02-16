package kr.happyjob.study.repository.register;

import org.apache.ibatis.annotations.Mapper;

import kr.happyjob.study.vo.register.UserVo;

@Mapper
public interface RegisterMapper {
	
	
	 /**
     * 사용자 ID 중복 검사
     * @param loginID 검사할 사용자 ID
     * @return 중복이면 1, 아니면 0
     */
	int checkID(String loginID);
	
	 /**
     * 휴대폰 번호 중복 검사
     * @param hp 검사할 휴대폰 번호 
     * @return 중복이면 1, 아니면 0
     */
	int checkHp(String hp);
	
	
	
	/**
	 * 회원가입
	 * @param userVo 회원정보
	 * @return 1 또는 0
	 */
	int insertUser(UserVo userVo);
	
	/**
	 * 아이디 비밀번호 찾기
	 * @param hp 휴대폰 번호로 조히
	 * @return 
	 */
	UserVo findAccount(String hp);
	
	/**
	 * 임시 비밀번호 업데이트
	 * @param hp
	 * @param password
	 */
	void updateUserPassword(String hp, String password);
	
	
	
	

}
