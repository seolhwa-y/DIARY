<template>
  <div>
    <el-calendar v-model="value" @click="handleDateClick"> </el-calendar>
    <el-dialog v-model="modalVisible" :title="title">
      <el-form label-width="120px">
        <el-form-item label="날짜">
          <span>{{ formattedDate }}</span>
        </el-form-item>
        <el-form-item label="내용">
          <el-input
            type="textarea"
            v-model="eventContent"
            placeholder="일정 내용을 입력하세요"
          />
        </el-form-item>
        <el-form-item>
          <el-button name="Write" type="primary" @click="addEvent">{{
            btnT
          }}</el-button>
          <el-button v-if="action === 'U'" @click="deleteEvent" type="danger"
            >삭제</el-button
          >
          <el-button @click="cancelEvent">취소</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CalendarComponent",
  data() {
    return {
      value: "",
      calendar: new Date(),
      modalVisible: false,
      eventContent: "",
      formattedDate: "",
      clickedDate: "",
      title: "",
      btnT: "",
      action: "",
    };
  },
  methods: {
    handleDateClick(event) {
      const clickedElement = event.target;
      // 클릭된 요소가 일자를 나타내는지 확인
      if (clickedElement.classList.contains("el-calendar-day")) {
        // 클릭된 날짜 가져오기
        const clickedDate = clickedElement.querySelector("span").innerText;

        // 달력의 연도와 월 가져오기
        const calendarTitleElement = document.querySelector(
          ".el-calendar__title"
        );
        let calendarTitleText = "";
        if (calendarTitleElement) {
          calendarTitleText = calendarTitleElement.innerText.trim();
        }

        // 클릭된 날짜와 달력의 제목을 합치기
        const formattedDate = this.formatDate(clickedDate, calendarTitleText);
        console.log("Formatted Date:", formattedDate);
        // this.modalVisible = true;
        // this.formattedDate = formattedDate; // 모달에 날짜 설정

        this.read(formattedDate);
      }
    },

    formatDate(day, monthYear) {
      // monthYear에서 연도와 월 추출
      const [year, month] = monthYear.split(" ");

      // 클릭된 날짜, 연도, 월을 합쳐서 YYYY-MM-DD 형식으로 반환
      return `${year}-${this.pad(this.monthToNumber(month))}-${this.pad(day)}`;
    },
    pad(value) {
      return String(value).padStart(2, "0");
    },
    monthToNumber(month) {
      const months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ];
      return months.indexOf(month) + 1;
    },
    addEvent() {
      // 일정 추가 로직을 구현합니다.
      console.log("일정 추가:", this.eventContent, "날짜:", this.formattedDate);
      if (this.eventContent == "") {
        alert("일정을 입력해주세요.");
        return false;
      }

      let loginInfo = this.$store.state.loginInfo;
      this.loginId = loginInfo.loginId;

      let params = new URLSearchParams();

      params.append("loginId", this.loginId);
      params.append("s_date", this.formattedDate);
      params.append("s_contents", this.eventContent);
      params.append("action", this.action);

      this.axios.post("/home/Save.do", params).then((response) => {
        console.log(response);
        if (response.data.resultMsg == "SUCCESS") {
          alert("일정을 저장하였습니다.");
          this.modalVisible = false;
        } else if (response.data.resultMsg == "UPDATED") {
          alert("일정을 수정하였습니다.");
          // this.$router.go(0);
          this.modalVisible = true;
        } else {
          alert("실패했습니다.");
        }
      });
    },
    cancelEvent() {
      // 모달을 닫습니다.
      this.modalVisible = false;
      // 입력 필드 초기화
      this.eventContent = "";
      // 클릭된 날짜 초기화
      this.clickedDate = "";
    },
    deleteEvent() {
      // 삭제 이벤트
      if (confirm("삭제 하시겠습니까?")) {
        let params = new URLSearchParams();
        params.append("s_date", this.formattedDate);

        // 화살표 함수를 사용하여 콜백 함수 내의 this를 컴포넌트 자체로 설정
        this.axios
          .post("/home/delete.do", params)
          .then((response) => {
            console.log(response);
            alert("삭제되었습니다.");
            this.modalVisible = false;
            this.$router.go(0);
          })
          .catch((error) => {
            console.error("Error deleting:", error);
          });
      }
    },
    read(formattedDate) {
      let loginInfo = this.$store.state.loginInfo;
      this.loginId = loginInfo.loginId;

      let params = new URLSearchParams();

      params.append("loginId", this.loginId);
      params.append("s_date", formattedDate);
      this.axios.post("/home/read.do", params).then((response) => {
        if (response.data.resultMsg == "SUCCESS") {
          console.log(response);
          const eventData = response.data;
          this.eventContent = eventData.contents;
          // 모달을 열고 날짜 전달
          this.modalVisible = true;
          this.formattedDate = formattedDate; // 모달에 날짜 설정
          this.eventContent = response.data.result.s_contents;
          this.title = "일정 수정";
          this.btnT = "수정";
          this.action = "U";
        } else {
          // DB에서 값이 없으면 일정을 등록할 수 있는 모달을 열도록 처리
          this.modalVisible = true;
          this.formattedDate = formattedDate; // 모달에 날짜 설정
          this.title = "일정 추가";
          this.btnT = "추가";
          this.action = "I";
        }
      });
    },
  },
};
</script>

<style scoped>
#homeContent {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70vh;
  padding: 40px;
  color: #333;
}
#homeContent:hover {
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
}
#homeContent h1 {
  font-size: 2rem;
  margin-top: 20px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.hero {
  text-align: center;
  max-width: 600px;
}

.logo {
  max-width: 120px;
  animation: float 3s ease-in-out infinite;
}

.hero-title {
  font-size: 2.5rem;
  margin-top: 20px;
  font-weight: bold;
  color: #333;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.hero-subtitle {
  font-size: 1.2rem;
  margin-top: 10px;
  color: #555;
}
.start-button {
  margin-top: 30px;
  font-size: 1.2rem;
  padding: 10px 20px;
  border-radius: 25px;
  transition: background-color 0.3s, transform 0.3s;
}
.start-button:hover {
  background-color: #8c62fd;
  transform: translateY(-3px);
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}
</style>
