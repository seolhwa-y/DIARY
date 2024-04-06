<template>
  <div class="modal" v-if="isVisible">
    <div class="modal-content">
      <span class="close" @click="hideModal">&times;</span>
      <h2>아이디/비밀번호 찾기</h2>
      <div class="modal-body" v-if="!isUserInfoVisible">
        <p>
          아이디 또는 비밀번호를 잊으셨나요? 등록하신 휴대폰 번호를
          입력해주세요.
        </p>
        <!-- '휴대폰 번호' 입력 필드 -->
        <input
          type="text"
          v-model="phoneNumber"
          placeholder="휴대폰 번호 ('-' 제외)"
          class="modal-input"
          required
          maxlength="11"
        />
        <button @click.prevent="requestVerificationCode">
          {{ verificationRequested ? "다시보내기" : "인증 코드 보내기" }}
        </button>
        <p v-if="hpError" class="error-message">{{ hpError }}</p>
      </div>

      <div
        class="form-group"
        v-if="verificationRequested && !isUserInfoVisible"
      >
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

      <div class="user-info" v-if="isUserInfoVisible">
        <h3>사용자 정보</h3>
        <p>아이디: {{ userInfo.loginID }}</p>
        <p>임시 비밀번호: {{ userInfo.password }}</p>
        <p>*마이페이지에서 비밀번호 변경을 권장합니다.*</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      isVisible: false, // 모달의 표시 상태
      phoneNumber: "", // 사용자가 입력한 휴대폰 번호
      verificationRequested: false, // 인증 코드 요청 여부
      verificationCode: "", // 사용자가 입력한 인증 코드
      codeVerificationMessage: "", // 인증 코드 검증 결과 메시지
      messageClass: "", // 메시지 CSS 클래스 (성공/실패)
      hpError: "", // 휴대폰 번호 입력 에러 메시지
      correctVerificationCode: "", //서버로 부터 받은 인증 코드 번호
      isUserInfoVisible: false, // 사용자 정보 표시 상태
      userInfo: {}, // 사용자 정보 저장
    };
  },
  methods: {
    showModal() {
      this.isVisible = true;
    },
    hideModal() {
      this.phoneNumber = "";
      this.verificationRequested = false;
      this.verificationCode = "";
      this.codeVerificationMessage = "";
      this.messageClass = "";
      this.hpError = "";
      this.correctVerificationCode = "";
      this.isUserInfoVisible = false;
      this.userInfo = {};
      this.isVisible = false;
      // 상태 초기화
      this.phoneNumber = "";
      this.verificationRequested = false;
      this.verificationCode = "";
      this.codeVerificationMessage = "";
      this.messageClass = "";
      this.hpError = "";
      this.correctVerificationCode = "";
    },

    requestVerificationCode() {
      // 휴대폰 번호 유효성 검사
      if (!this.phoneNumber || this.phoneNumber.length !== 11) {
        this.hpError = "올바른 휴대폰 번호를 입력해주세요.";
        return;
      }

      // 백엔드 API 호출하여 연락처 존재 여부 및 인증 코드 전송
      axios
        .post("/api/checkHp", { hp: this.phoneNumber })
        .then((response) => {
          // 연락처가 존재하는 경우 인증 코드 전송 로직
          if (response.data >= 1) {
            // 인증 코드 전송 로직 (기존 로직 유지 또는 수정)
            this.sendVerificationCode();
          } else {
            // 연락처가 존재하지 않는 경우 에러 메시지 표시
            this.hpError = "회원가입이 안된 아이디입니다.";
            this.verificationRequested = false; // 인증 요청 상태 초기화
          }
        })
        .catch((error) => {
          console.error("연락처 확인 중 오류 발생: ", error);
          this.hpError = "연락처 확인 중 문제가 발생했습니다.";
        });
    },
    // 인증번호
    sendVerificationCode() {
      axios
        .post("/api/sendSMS", { hp: this.phoneNumber })
        .then((response) => {
          this.verificationRequested = true;
          console.log("인증 코드가 전송되었습니다: ", response.data);
          this.correctVerificationCode = response.data;
          this.hpError = "";
        })
        .catch((error) => {
          console.error("인증 코드 전송 중 오류 발생: ", error);
          this.hpError = "인증 코드 전송 중 문제가 발생했습니다.";
        });
    },
    verifyCodeInput() {
      console.log("서버에서 받은 코드", this.correctVerificationCode);
      console.log("내가 입력한 코드", this.verificationCode);

      const inputCode = String(this.verificationCode).trim();
      const correctCode = String(this.correctVerificationCode).trim();
      // 인증 코드 검증 로직 (여기서는 예시로 직접 비교)
      if (inputCode === correctCode) {
        // 인증 코드가 일치하는 경우, 사용자 정보 조회 및 표시 로직
        this.fetchUserInfo();
      } else {
        this.codeVerificationMessage = "잘못된 인증 코드입니다.";
        this.messageClass = "error-message";
      }
    },

    fetchUserInfo() {
      // 백엔드 API 호출하여 사용자 정보 조회
      axios
        .post("/api/findAccount", { hp: this.phoneNumber })
        .then((response) => {
          console.log("response", response);
          console.log("response.data.loginID", response.data.loginID);
          // 사용자 정보 표시 로직
          // 예: response.data에 사용자 아이디와 임시 비밀번호 또는 비밀번호 재설정 링크 포함
          this.displayUserInfo(response.data);
        })
        .catch((error) => {
          console.error("사용자 정보 조회 중 오류 발생: ", error);
          this.codeVerificationMessage =
            "사용자 정보 조회 중 문제가 발생했습니다.";
          this.messageClass = "error-message";
        });
    },

    // 기존 메서드들
    displayUserInfo(userInfo) {
      this.userInfo = userInfo; // 사용자 정보 저장
      this.isUserInfoVisible = true; // 사용자 정보 표시
      this.verificationRequested = false; // 인증 요청 상태 초기화
    },
  },
};
</script>

<style scoped>
.user-info {
  background-color: #f0f4f8; /* 연한 회색 배경 */
  border: 1px solid #d1dce5; /* 테두리 색상 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 20px; /* 내부 여백 */
  margin-top: 20px; /* 상단 여백 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}

.user-info h3 {
  color: #333; /* 제목 색상 */
  margin-bottom: 15px; /* 제목 아래 여백 */
  text-align: center; /* 중앙 정렬 */
}

.user-info p {
  color: #555; /* 문단 색상 */
  font-size: 16px; /* 글자 크기 */
  line-height: 1.5; /* 줄 간격 */
  text-align: center; /* 중앙 정렬 */
}

.user-info p:last-child {
  font-style: italic; /* 이탤릭체 */
  margin-top: 10px; /* 마지막 문단 상단 여백 */
}

.success-message {
  color: green; /* 성공 메시지 색상 */
}
.error-message {
  color: red; /* 에러 메시지 색상 */
}
h2 {
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
input[type="text"] {
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
  display: block; /* 블록 레벨 요소로 변경 */
  margin: 10px auto; /* 자동 마진으로 중앙 정렬 */
}

button:hover {
  background-color: #0056b3; /* 호버 시 배경색 변경 */
}
</style>
