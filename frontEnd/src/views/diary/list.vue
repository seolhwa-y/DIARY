<template>
  <div style="height: 700px">
    <el-row type="flex" justify="center" class="list-main">
      <el-col :span="16">
        <div style="height: 50px">
          <span style="font-size: 20px"> Today's Weather : </span>
          <img
            v-if="iconUrl"
            :src="iconUrl"
            alt="Weather Icon"
            style="height: 50px; width: 50px"
          />
          <span style="font-size: 30px">{{ weather }}</span>
        </div>
        <!--리스트형,카드형,작성 검색 영역  -->
        <el-row>
          <!-- 작성(Write)로 이동 -->
          <el-col :span="19">
            <!-- (타임라인,카드) 토글 -->
            <el-radio-group v-model="radio" class="radio-group">
              <el-radio-button size="small" label="card"
                ><el-icon size="20"><Menu /></el-icon
              ></el-radio-button>
              <el-radio-button size="small" label="list"
                ><el-icon size="20"><tickets /></el-icon
              ></el-radio-button>
            </el-radio-group>

            <!-- 작성 버튼 -->
            <el-button @click="write" circle
              ><el-icon> <Edit /> </el-icon
            ></el-button>
          </el-col>

          <!-- 검색 부분 -->
          <el-col :span="5">
            <!-- 검색(input) -->
            <input
              style="height: 30px"
              v-model="search"
              ref="searchInput"
              placeholder="검색"
              @keyup.enter="list()"
            />
            <!-- 클리어 아이콘 -->
            <el-icon style="width: 20px" v-if="search != ''" @click="reset()"
              ><CircleCloseFilled
            /></el-icon>
          </el-col>
          <!-- /검색 부분 끝 -->
        </el-row>

        <el-divider></el-divider
        ><!--구분선-->

        <!-- 카드 형태 시작 -->
        <el-row v-show="radio == 'card'" class="scroll-box">
          <!-- 다이어리 목록 출력 부분 -->
          <el-row class="scroll" :gutter="12">
            <el-col :span="8" v-for="list in lists" :key="list.d_no">
              <!-- 카드영역 -->
              <div @click="detail(list.d_no)" class="card">
                <el-card shadow="hover">
                  <div class="card-date">
                    {{ formatCardDate(list.d_diarydt) }}
                    <span style="float: right">
                      <el-icon @click.stop="remove(list.d_no)"
                        ><CloseBold
                      /></el-icon>
                    </span>
                  </div>
                  <div class="card-title">
                    <b>{{ list.d_title }}</b>
                  </div>
                  <div class="card-content">
                    날씨:
                    <el-icon v-if="list.d_weather === '1'"><Sunny /></el-icon>
                    <el-icon v-else-if="list.d_weather === '2'"
                      ><MostlyCloudy
                    /></el-icon>
                    <el-icon v-else-if="list.d_weather === '3'"
                      ><Pouring
                    /></el-icon>
                    <el-icon v-else-if="list.d_weather === '4'"
                      ><Lightning
                    /></el-icon>
                    | 기분:
                    <span v-if="list.d_mood === '기쁨'">
                      <img src="/images/happy.png" alt="기쁨" />
                      <span>{{ list.d_mood }}</span>
                    </span>
                    <span v-else-if="list.d_mood === '평범'">
                      <img src="/images/weird.png" alt="평범" />
                      <span>{{ list.d_mood }}</span>
                    </span>
                    <span v-else-if="list.d_mood === '우울'">
                      <img src="/images/sad.png" alt="우울" />
                      <span>{{ list.d_mood }}</span>
                    </span>
                    <span v-else-if="list.d_mood === '화남'">
                      <img src="/images/anger.png" alt="화남" />
                      <span>{{ list.d_mood }}</span>
                    </span>
                    <span v-else-if="list.d_mood === '놀람'">
                      <img src="/images/shock.png" alt="놀람" />
                      <span>{{ list.d_mood }}</span>
                    </span>
                  </div>
                </el-card>
              </div>
              <!-- 카드영역 끝 -->
            </el-col>
          </el-row>
        </el-row>

        <!-- 카드 형태 끝 -->

        <!-- 타임라인 형태 시작 -->
        <el-row v-show="radio == 'list'">
          <!-- 다이어리 목록 출력 부분 -->
          <div class="scroll-box">
            <el-timeline class="scroll">
              <el-timeline-item
                v-for="list in lists"
                :key="list.d_no"
                placement="top"
              >
                {{ formatCardDate(list.d_diarydt) }}
                <el-card @click="detail(list.d_no)">
                  <div style="width: 1000px">
                    <h3>
                      <b>{{ list.d_title }}</b>
                    </h3>
                    <br />
                    <p class="card-date">
                      {{ formatTimelineDate(list.d_regdate) }}
                    </p>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
          <!-- /다이어리 목록 출력 부분 -->
        </el-row>
        <!-- 타임라인 형태 끝 -->
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      lists: [],
      radio: "card",
      search: "",
      loginId: "",
      action: "",
      weather: "",
      iconCode: "",
      iconUrl: "",
    };
  },
  mounted() {
    this.getWeather();
    this.list();
    let loginInfo = this.$store.state.loginInfo;
    this.loginId = loginInfo.loginId;
  },
  methods: {
    list: function () {
      let vm = this;
      let loginInfo = this.$store.state.loginInfo;
      this.loginId = loginInfo.loginId;

      let params = new URLSearchParams();
      params.append("title", this.search);
      params.append("userId", this.loginId); // 사용자의 ID를 요청 파라미터에 추가

      this.axios
        .post("/diary/list.do", params)
        .then(function (response) {
          console.log(response.data.diary);
          vm.lists = response.data.diary;
        })
        .catch(function (error) {
          alert("에러! API 요청에 오류가 있습니다. " + error);
        });
    },

    formatTimelineDate(date) {
      // 날짜를 Date 객체로 변환
      const formattedDate = new Date(date);

      // 날짜를 원하는 형식으로 변환
      const year = formattedDate.getFullYear();
      const month = String(formattedDate.getMonth() + 1).padStart(2, "0");
      const day = String(formattedDate.getDate()).padStart(2, "0");
      const hours = String(formattedDate.getHours()).padStart(2, "0");
      const minutes = String(formattedDate.getMinutes()).padStart(2, "0");
      const period = hours >= 12 ? "오후" : "오전";
      const formattedHours = hours % 12 || 12;

      return `${year}년 ${month}월 ${day}일 ${period} ${formattedHours}시 ${minutes}분 작성`;
    },

    formatCardDate(date) {
      // 문자열을 연, 월, 일로 분할
      const year = date.substring(0, 4);
      const month = date.substring(4, 6);
      const day = date.substring(6, 8);

      // 월과 일에 대해 0을 제거
      const formattedMonth = parseInt(month, 10).toString();
      const formattedDay = parseInt(day, 10).toString();

      // 연도, 월, 일을 조합하여 원하는 형식으로 반환
      return `${year}년 ${formattedMonth}월 ${formattedDay}일`;
    },

    reset() {
      // 검색어 초기화
      this.search = "";
      // 검색 입력란에 포커스 설정
      this.$refs.searchInput.focus();
      this.list();
    },

    // //목록의 자세히 보기 버튼
    detail: async function (d_no) {
      this.$router.push("/dashboard/diary/detail?d_no=" + d_no);
      // this.$route.query.no
    },

    //작성 버튼
    write: async function () {
      this.$router.push({
        path: "/dashboard/diary/DiaryInsert",
        query: { action: "I" },
      });
    },

    remove(d_no) {
      if (confirm("삭제 하시겠습니까?")) {
        let params = new URLSearchParams();
        params.append("d_no", d_no);

        // 화살표 함수를 사용하여 콜백 함수 내의 this를 컴포넌트 자체로 설정
        this.axios
          .post("/diary/delete.do", params)
          .then((response) => {
            console.log(response);
            this.$message({
              type: "info",
              message: "일기를 삭제하였습니다.",
            });
            this.$router.go(0);
          })
          .catch((error) => {
            console.error("Error deleting diary:", error);
          });
      }
    },

    getWeather() {
      const apiKey = "a336f06a784cfd11b597e2efd6109bcc";
      const city = "Seoul"; // 도시 이름 예시
      const url = `http://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}`;

      this.axios
        .get(url)
        .then((response) => {
          console.log(response);
          this.weather = response.data.weather[0].main;
          this.iconCode = response.data.weather[0].icon;

          this.iconUrl = `http://openweathermap.org/img/wn/${this.iconCode}.png`;
        })
        .catch((error) => {
          console.error("날씨 정보를 가져오는데 실패했습니다.", error);
        });
    },
  },
};
</script>

<style scoped>
.list-main {
  margin-top: 2%;
}
.el-divider--horizontal {
  margin: 10px 0px 40px 0px;
}
.radio-group {
  margin-right: 20px;
}
.card {
  margin-top: 5%;
}
.card-date {
  font-size: 13px;
  color: #9c97f0;
}
.card-title {
  margin-top: 5%;
  color: black;
  font-size: 13px;
}
.card-content {
  margin-top: 5%;
  font-size: 15px;
  color: gray;
}
.scroll-box {
  width: 100%;
  height: 500px;
  overflow: overlay;
  overflow-x: hidden;
}
.scroll {
  overflow: auto;
  padding: 0 4% 0 3%;
  width: 100%;
}

.el-card__body:hover {
  visibility: visible;
}
.el-card:hover {
  background-color: #f6f7fd;
}
.el-row {
  align-items: baseline;
}
img {
  width: 20px;
  height: 20px;
}
</style>
