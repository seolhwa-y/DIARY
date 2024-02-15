package kr.happyjob.study.vo.register;

public class UserVo {
    
    private String loginID;
    private String name;
    private String password;
    private String hp;
    private String userType; // 사용자 유형 필드 추가

    // 기본 생성자
    public UserVo() {
        // 기본 생성자의 내용이 필요하지 않을 경우 비워둘 수 있습니다.
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

    public String getUserType() { // user_type 필드의 getter 메소드
        return userType;
    }

    public void setUserType(String userType) { // user_type 필드의 setter 메소드
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserVo [loginID=" + loginID + ", name=" + name + ", password=" + password + ", hp=" + hp + ", user_type=" + userType + "]";
    }

    // 모든 필드를 포함하는 생성자
    public UserVo(String loginID, String name, String password, String hp, String userType) {
        super();
        this.loginID = loginID;
        this.name = name;
        this.password = password;
        this.hp = hp;
        this.userType = userType; // 생성자에 user_type 필드 추가
    }
}
