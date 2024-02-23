package kr.happyjob.study.service.login;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.happyjob.study.repository.login.LoginMapper;
import kr.happyjob.study.vo.login.LoginVO;
import kr.happyjob.study.vo.login.UserVO;
import kr.happyjob.study.vo.register.UserVo;

@Service
public class LoginService {
	
	@Autowired
	LoginMapper loginMapper;
	
	// Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	private final String className = this.getClass().toString();
	   
	
	@Autowired
	private LoginMapper mapper;
	
	public UserVO login(LoginVO vo) {
		return mapper.login(vo);
	}
	
	public int selectUserInfo(String email) {
		// TODO Auto-generated method stub
		return loginMapper.selectUserInfo(email);
	}

	public UserVo phSelected(String email) {
		// TODO Auto-generated method stub
		return loginMapper.phSelected(email);
	}
	
}
