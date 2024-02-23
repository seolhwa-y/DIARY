package kr.happyjob.study.vo.register;

public class UserVo {
    
    private String loginID;
    private String name;
    private String password;
    private String hp;
    private String user_type; // 사용자 유형 필드 추가
    private String kakao;
    private String naver;
    private String google;
    private String email;

    
   
    
    public UserVo() {
		// TODO Auto-generated constructor stub
	}




	public String getLoginID() {
		return loginID;
	}




	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getHp() {
		return hp;
	}




	public void setHp(String hp) {
		this.hp = hp;
	}




	public String getUser_type() {
		return user_type;
	}




	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}




	public String getKakao() {
		return kakao;
	}




	public void setKakao(String kakao) {
		this.kakao = kakao;
	}




	public String getNaver() {
		return naver;
	}




	public void setNaver(String naver) {
		this.naver = naver;
	}




	public String getGoogle() {
		return google;
	}




	public void setGoogle(String google) {
		this.google = google;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	@Override
	public String toString() {
		return "UserVo [loginID=" + loginID + ", name=" + name + ", password=" + password + ", hp=" + hp
				+ ", user_type=" + user_type + ", kakao=" + kakao + ", naver=" + naver + ", google=" + google
				+ ", email=" + email + "]";
	}
    
    

    
}
