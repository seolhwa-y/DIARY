package kr.happyjob.study.repository.login;


import org.apache.ibatis.annotations.Mapper;

import kr.happyjob.study.vo.login.LoginVO;
import kr.happyjob.study.vo.login.UserVO;
import kr.happyjob.study.vo.register.UserVo;

@Mapper
public interface LoginMapper {
	
	UserVO login(LoginVO vo);
	
	/**
	 *  유저 조회
	 * @param email
	 * @return
	 */
	int selectUserInfo(String email);

	UserVo phSelected(String email);
}

