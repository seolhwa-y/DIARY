package kr.happyjob.study.service.mypage;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.happyjob.study.repository.mypage.MyPageMapper;


@Service
public class MyPageService {

	@Autowired
	private MyPageMapper mapper;
	
	public String getTell(Map<String, Object> paramMap) {
		return mapper.getTell(paramMap);
	}

	public int updInfo(Map<String, Object> paramMap) {
		return mapper.updInfo(paramMap);
	}

	public int resign(Map<String, Object> paramMap) {
		return mapper.resign(paramMap);
	}
	
}
