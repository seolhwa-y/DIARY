package kr.happyjob.study.repository.mypage;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPageMapper {
    Map<String, Object> getInfo(Map<String, Object> paramMap);

    int updInfo(Map<String, Object> paramMap);

    int resign(Map<String, Object> paramMap);
}
