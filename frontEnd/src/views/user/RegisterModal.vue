<template>
  <div class="modal" v-if="isVisible">
    <div class="modal-content">
      <span class="close" @click="hideModal">&times;</span>
      <h1>회원가입</h1>
      <!-- 회원가입 양식 -->
      <form @submit.prevent="registerUser">
        <div class="form-group">
          <label for="loginID">사용자ID</label>
          <input
            type="text"
            id="loginID"
            name="loginID"
            v-model="userForm.loginID"
            required
          />
          <!-- 사용자ID 중복 확인 메시지 -->
          <p
            v-if="userIdDuplicateCheckMessage"
            :class="{
              'error-message': isUserIdDuplicate,
              'success-message': !isUserIdDuplicate,
            }"
          >
            {{ userIdDuplicateCheckMessage }}
          </p>
        </div>
        <div class="form-group">
          <label for="name">이름</label>
          <input
            type="text"
            id="name"
            name="name"
            v-model="userForm.name"
            required
          />
          <div class="form-group">
            <label for="email">이메일</label>
            <input
              type="email"
              id="email"
              name="email"
              v-model="userForm.email"
              required
            />
            <!-- 이메일 중복 검사 메시지 표시 -->
            <p
              v-if="emailDuplicateCheckMessage"
              :class="{
                'error-message': isEmailDuplicate,
                'success-message': !isEmailDuplicate,
              }"
            >
              {{ emailDuplicateCheckMessage }}
            </p>
          </div>
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            type="password"
            id="password"
            name="password"
            v-model="userForm.password"
            required
          />
        </div>

        <!-- 연락처 입력 필드 -->
        <div class="form-group">
          <label for="hp">연락처</label>
          <input
            type="text"
            id="hp"
            name="hp"
            v-model="userForm.hp"
            placeholder="'-' 생략하고 입력"
            required
            maxlength="11"
          />
          <!-- 에러 메시지가 있을 경우 표시 -->
          <!-- 인증 코드를 요청하는 버튼 -->
          <button @click.prevent="requestVerificationCode">
            {{ verificationRequested ? "다시보내기" : "인증 코드 보내기" }}
          </button>
          <p v-if="hpError" class="error-message">{{ hpError }}</p>
        </div>

        <!-- 소셜로그인 구분 -->
        <div class="form-group" v-show="false">
          <label for="naver">네이버</label>
          <input v-model="userForm.naver" />
        </div>
        <div class="form-group" v-show="false">
          <label for="google">구글</label>
          <input v-model="userForm.google" />
        </div>
        <div class="form-group" v-show="false">
          <label for="kakao">카카오</label>
          <input v-model="userForm.kakao" />
        </div>

        <!-- 인증 코드 입력 필드, verificationRequested가 true일 때만 표시됩니다. -->
        <div class="form-group" v-if="verificationRequested">
          <label for="verificationCode">인증 코드</label>
          <input
            type="text"
            id="verificationCode"
            name="verificationCode"
            v-model="verificationCode"
            required
            @input="verifyCodeInput"
            maxlength="6"
          />
          <p v-if="codeVerificationMessage" :class="messageClass">
            {{ codeVerificationMessage }}
          </p>
        </div>

        <button type="submit">회원가입</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      isVisible: false,
      verificationRequested: false, // 인증 코드 요청 상태를 관리하는 플래그
      verificationCode: "", // 사용자가 입력한 인증 코드
      correctVerificationCode: "", // 서버로부터 받은 인증 코드를 저장할 변수
      hpError: null, // 에러 메시지 초기화
      codeVerificationMessage: "", // 인증 코드 검증 결과 메시지
      messageClass: "",
      verificationCodeTimeout: null, // 인증번호 만료 타이머 ID
      isUserIdDuplicate: false, //중복검사 상태
      userIdDuplicateCheckMessage: "", //중복검사 메세지
      emailDuplicateCheckMessage: "", // 이메일 중복 검사 결과 메시지
      isEmailDuplicate: false, // 이메일 중복 여부
      emailFormatValid: true, // 이메일 형식 유효성 상태
      // 폼 데이터를 위한 데이터 모델 추가
      userForm: {
        loginID: "",
        name: "",
        email: "",
        password: "",
        hp: "",
        naver: "N",
        google: "N",
        kakao: "N",
      },
      props: {
        name: String,
        ph: String,
        naver: String,
        google: String,
        kakao: String,
      },
    };
  },
  // 아이디 중복 확인 로직
  watch: {
    "userForm.loginID": function (newValue) {
      this.checkUserIdDuplicate(newValue);
    },
    "userForm.email"(newValue) {
      this.checkEmailDuplicate(newValue);
    },
  },
  methods: {
    showModal() {
      this.isVisible = true;
    },
    hideModal() {
      this.isVisible = false;

      // 모든 사용자 입력 데이터 초기화
      this.userForm = {
        loginID: "",
        name: "",
        password: "",
        hp: "",
        email: "",
        naver: "N",
        google: "N",
        kakao: "N",
      };

      // 모든 상태 초기화
      this.verificationRequested = false;
      this.verificationCode = "";
      this.correctVerificationCode = "";
      this.hpError = null;
      this.codeVerificationMessage = "";
      this.messageClass = "";

      // 타이머 관련 상태 초기화
      clearTimeout(this.verificationCodeTimeout); // 기존 타이머 취소
      this.verificationCodeTimeout = null;
      this.timer = null;
    },
    //아이디 중복 검사
    checkUserIdDuplicate(loginID) {
      console.log("입력한 값 : ", loginID);
      if (!loginID.trim()) {
        this.userIdDuplicateCheckMessage = "";
        return;
      }
      // 여기에 사용자 ID 중복 확인 API 요청 로직 구현
      axios
        .post("/api/checkID", { loginID })

        .then((response) => {
          console.log("받은 값 : ", response.data);
          if (response.data === 1) {
            this.isUserIdDuplicate = true;
            this.userIdDuplicateCheckMessage = "이미 사용 중인 아이디입니다.";
          } else {
            this.isUserIdDuplicate = false;
            this.userIdDuplicateCheckMessage = "사용 가능한 아이디입니다.";
          }
        })
        .catch((error) => {
          console.error("아이디 중복 확인 중 오류 발생:", error);
        });
    },
    // 이메일 형식 검증
    validateEmailFormat(email) {
      const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.emailFormatValid = pattern.test(email);
      return this.emailFormatValid; // 이메일 형식이 유효한지 여부 반환
    },
    // 이메일 중복 검사
    checkEmailDuplicate(email) {
      // 입력값이 비었을 경우 메시지를 초기화하고 함수를 종료한다.
      if (!email.trim()) {
        this.isEmailDuplicate = false; // 중복 상태 초기화
        this.emailDuplicateCheckMessage = ""; // 메시지 초기화
        return; // 함수 종료
      }

      // 이메일 형식 검증
      if (!this.validateEmailFormat(email)) {
        this.isEmailDuplicate = true;
        this.emailDuplicateCheckMessage = "유효하지 않은 이메일 형식입니다.";
        return; // 이메일 형식이 유효하지 않으면 중복 검사를 수행하지 않는다.
      }

      // 이메일 형식이 유효하면 중복 검사를 수행한다.
      axios
        .post("/api/checkEmail", { email: email })
        .then((response) => {
          // 중복 검사 결과 처리
          if (response.data) {
            this.isEmailDuplicate = true;
            this.emailDuplicateCheckMessage = "이미 사용 중인 이메일입니다.";
          } else {
            this.isEmailDuplicate = false;
            this.emailDuplicateCheckMessage = "사용 가능한 이메일입니다.";
          }
        })
        .catch((error) => {
          console.error("이메일 중복 확인 중 오류 발생:", error);
          this.emailDuplicateCheckMessage = "중복 검사 중 오류가 발생했습니다.";
        });
    },

    // 휴대폰 인증
    requestVerificationCode() {
      // 연락처 입력값이 11자리인지 확인
      if (this.userForm.hp.length !== 11) {
        this.hpError = "*연락처는 11자리여야 합니다.*";
        return; // 여기서 함수 종료
      }

      // 휴대폰 번호 중복 검사 로직 구현
      axios
        .post("/api/checkHp", { hp: this.userForm.hp })
        .then((response) => {
          // 중복 검사 결과에 따라 처리
          if (response.data >= 1) {
            // 중복된 경우 에러 메시지 설정 및 인증번호 요청 중단
            this.hpError = "이미 사용 중인 휴대폰 번호입니다.";
          } else {
            // 중복되지 않은 경우 인증번호 요청 로직 수행
            this.sendVerificationCode();
          }
        })
        .catch((error) => {
          console.error("휴대폰 번호 중복 확인 중 오류 발생:", error);
        });
    },

    // 인증번호 요청 로직을 별도의 메서드로 분리
    sendVerificationCode() {
      // 기존 인증번호와 관련된 상태 초기화
      this.hpError = null; // 에러 메시지 초기화
      this.verificationRequested = true; // 버튼 텍스트 변경
      this.verificationCode = ""; // 사용자 입력 인증번호 초기화
      this.codeVerificationMessage = ""; // 인증번호 검증 메시지 초기화
      this.messageClass = ""; // 메시지 클래스 초기화

      // 기존 타이머가 있다면 취소
      clearTimeout(this.verificationCodeTimeout);
      // 인증 코드 요청 로직
      axios
        .post("/api/sendSMS", this.userForm)
        .then((response) => {
          console.log(response.data);
          this.correctVerificationCode = response.data; // 서버로부터 받은 인증 코드 저장
          this.setVerificationCodeTimeout(); // 인증번호 만료 타이머 설정
        })
        .catch((error) => {
          console.error("인증번호 요청 중 오류 발생:", error);
        });
    },

    // 인증번호 만료 타이머 설정 메서드
    setVerificationCodeTimeout() {
      this.verificationCodeTimeout = setTimeout(() => {
        alert("인증번호가 만료되었습니다.");
        this.correctVerificationCode = ""; // 인증번호 만료
        if (this.verificationCode) {
          // 기존에 인증번호가 입력되어 있으면
          this.codeVerificationMessage =
            "인증번호가 만료되었습니다. 다시 요청해주세요.";
          this.messageClass = "error-message";
        }
      }, 10000); // 10초 후 만료
    },
    verifyCodeInput() {
      if (!this.correctVerificationCode) {
        // 만료된 경우
        this.codeVerificationMessage =
          "인증번호가 만료되었습니다. 다시 요청해주세요.";
        this.messageClass = "error-message";
        return; // 처리 종료
      }
      // 사용자 입력과 정확한 인증 코드가 모두 비어있지 않은 경우에만 검증 로직을 수행
      if (this.verificationCode && this.correctVerificationCode) {
        console.log("내가 입력한 코드 : ", this.verificationCode);
        console.log("인증 코드 : ", this.correctVerificationCode);

        const inputCode = String(this.verificationCode).trim();
        const correctCode = String(this.correctVerificationCode).trim();

        if (inputCode.length !== 6) {
          // 인증 코드 길이가 6자리가 아니면 에러 메시지를 표시
          this.codeVerificationMessage = "인증 코드는 6자리여야 합니다.";
          this.messageClass = "error-message";
        } else if (inputCode !== correctCode) {
          // 인증 코드가 일치하지 않으면 에러 메시지를 표시
          this.codeVerificationMessage = "인증 코드가 일치하지 않습니다.";
          this.messageClass = "error-message";
        } else {
          // 인증 코드가 일치하면 성공 메시지를 표시
          this.codeVerificationMessage = "인증 코드가 일치합니다.";
          this.messageClass = "success-message";
        }
      } else {
        // 인증 코드 입력이 시작되지 않았거나 입력이 비어 있는 경우 메시지와 클래스를 초기화
        this.codeVerificationMessage = "";
        this.messageClass = "";
      }
    },

    registerUser() {
      // 아이디 중복 검사
      if (this.isUserIdDuplicate) {
        alert("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
        return; // 중복 아이디가 있으므로 회원가입 로직 중단
      }
      if (this.codeVerificationMessage != "인증 코드가 일치합니다.") {
        alert("휴대폰 인증을 해주세요.");
        return; // 회원가입 로직 중단
      }

      // 회원가입 로직
      axios
        .post("/api/register", this.userForm)
        .then((response) => {
          console.log(response.data);
          // 회원 가입 성공 시 대시보드 홈으로 리다이렉트
          this.$router.push("/");
          this.hideModal();
        })
        .catch((error) => {
          console.error("회원가입 에러:", error.response);
        });
    },
  },
};
</script>

<style scoped>
.success-message {
  color: green; /* 성공 메시지 색상 */
}
.error-message {
  color: red; /* 에러 메시지 색상 */
}
h1 {
  font-size: 2.5rem; /* 크기 */
  color: #333; /* 색상 */
  text-align: center; /* 중앙 정렬 */
  text-transform: uppercase; /* 대문자 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2); /* 텍스트 그림자 */
  margin-top: 0; /* 상단 여백 */
  margin-bottom: 1rem; /* 하단 여백 */
  padding: 0.5rem; /* 패딩 */
  background-color: #f8f9fa; /* 배경 색상 */
  border-radius: 10px; /* 모서리 둥글게 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 박스 그림자 */
  display: inline-block; /* 내용의 너비만큼 크기 조정 */
  width: auto; /* 자동 너비 */
  max-width: 100%; /* 최대 너비 설정 */
}

.modal {
  position: fixed; /* 모달을 화면에 고정 */
  left: 0;
  top: 0;
  width: 100%; /* 전체 너비 */
  height: 100%; /* 전체 높이 */
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  align-items: center; /* 중앙 정렬 */
  z-index: 1000; /* 다른 요소 위에 표시 */
}

.modal-content {
  background-color: #fff; /* 배경색 */
  padding: 20px; /* 패딩 */
  border-radius: 10px; /* 모서리 둥글게 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  width: 500px; /* 너비 */
  max-width: 80%; /* 최대 너비 */
  animation: modalOpen 0.5s ease; /* 애니메이션 효과 */
}

@keyframes modalOpen {
  from {
    opacity: 0; /* 처음에는 투명 */
    transform: translateY(-50px); /* 위에서 아래로 약간 이동 */
  }
  to {
    opacity: 1; /* 최종적으로 불투명 */
    transform: translateY(0); /* 원래 위치로 */
  }
}

.close {
  float: right; /* 오른쪽 상단에 위치 */
  font-size: 25px; /* 크기 */
  font-weight: bold;
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
  margin-left: 90%;
}

.close:hover {
  color: red; /* 호버 시 색상 변경 */
}

/* 입력 필드 스타일 */
input[type="email"],
input[type="text"],
input[type="password"] {
  width: calc(100% - 20px); /* 전체 너비에서 패딩 제외 */
  padding: 10px;
  margin-bottom: 15px; /* 마진 */
  border: 1px solid #ccc; /* 테두리 */
  border-radius: 5px; /* 모서리 둥글게 */
}

/* 버튼 스타일 */
button {
  width: 100%; /* 전체 너비 */
  padding: 10px 20px;
  background-color: #007bff; /* 배경색 */
  color: #fff; /* 글자색 */
  border: none;
  border-radius: 5px; /* 모서리 둥글게 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}

button:hover {
  background-color: #0056b3; /* 호버 시 배경색 변경 */
}
</style>
