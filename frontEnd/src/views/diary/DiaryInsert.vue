<template>
  <div class="content" style="">
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"
          >일기 {{ isCreating() ? "작성" : "수정" }}
        </span>
      </template>
    </el-page-header>

    <table
      style="
        border-collapse: collapse;
        width: 80%;
        text-align: center;
        display: flex;
        justify-content: center;
      "
    >
      <tbody>
        <tr>
          <th style="padding: 8px; border: none">제목</th>
          <td style="padding: 8px; border: none">
            <input
              ref="titleInput"
              type="text"
              id="title"
              v-model="title"
              placeholder="제목을 입력하세요"
              style="width: 100%; box-sizing: border-box; border: none"
            />
          </td>
        </tr>
        <tr>
          <th style="padding: 8px; border: none">날씨선택</th>
          <td style="padding: 8px; border: none" class="vertical-align">
            <el-form-item prop="weather">
              <el-radio-group v-model="weather" class="weather">
                <el-radio-button label="1">
                  <el-icon size="30"><Sunny /></el-icon>
                </el-radio-button>
                <el-radio-button label="2">
                  <el-icon size="30"><Cloudy /></el-icon>
                </el-radio-button>
                <el-radio-button label="3">
                  <el-icon size="30"><Pouring /></el-icon>
                </el-radio-button>
                <el-radio-button label="4">
                  <el-icon size="30"><Lightning /></el-icon>
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <th style="padding: 8px; border: none">날짜선택</th>
          <td style="padding: 8px; border: none" class="vertical-align">
            <div class="demo-date-picker">
              <div class="block">
                <el-date-picker
                  v-model="idate"
                  type="date"
                  placeholder="날짜를 선택하세요."
                  :disabled-date="disabledDate"
                  :shortcuts="shortcuts"
                />
              </div>
            </div>
          </td>
        </tr>
        <tr>
          <th style="padding: 8px; border: none">기분</th>
          <td
            style="padding: 8px; border: none"
            class="vertical-align mood-select"
          >
            <el-radio-group v-model="mood" class="mood">
              <el-radio-button label="1">
                <img src="/images/happy.png" alt="기쁨" />
              </el-radio-button>
              <el-radio-button label="2">
                <img src="/images/weird.png" alt="평범" />
              </el-radio-button>
              <el-radio-button label="3">
                <img src="/images/sad.png" alt="우울" />
              </el-radio-button>
              <el-radio-button label="4">
                <img src="/images/anger.png" alt="화남" />
              </el-radio-button>
              <el-radio-button label="5">
                <img src="/images/shock.png" alt="놀람" />
              </el-radio-button>
            </el-radio-group>
          </td>
        </tr>

        <div id="divEditor">
          <quill-editor
            v-model:value="state.content"
            :options="state.editorOption"
            @change="onEditorChange($event)"
          />
        </div>

        <span class="button" style="margin-bottom: 10px">
          <el-button name="Write" @click="focusInput">{{
            isCreating() ? "저장" : "수정 완료"
          }}</el-button>
          <el-button @click="reset">취소</el-button>
        </span>
        <br />
      </tbody>
    </table>
  </div>
</template>
<script>
import { quillEditor } from "vue3-quill";

export default {
  name: "App",
  components: {
    quillEditor,
  },
  data: function () {
    return {
      action: "",
      weather: "",
      idate: "", // 다이어리 등록 날짜
      title: "", // 제목
      mood: "", // 기분상태 5단계
      lists: [],
      loginId: "",
      shortcuts: [
        {
          text: "오늘",
          value: new Date(),
        },
        {
          text: "어제",
          value: new Date(new Date().setDate(new Date().getDate() - 1)),
        },
        {
          text: "1주 전",
          value: new Date(new Date().setDate(new Date().getDate() - 7)),
        },
      ],
      state: {
        content: "",
        _content: "",
        editorOption: {
          placeholder: "내용을 작성해주세요.",
          modules: {
            toolbar: [
              ["bold", "italic", "underline", "strike"],
              ["blockquote", "code-block"],
              [{ header: 1 }, { header: 2 }],
              [{ list: "ordered" }, { list: "bullet" }],
              [{ script: "sub" }, { script: "super" }],
              [{ indent: "-1" }, { indent: "+1" }],
              [{ direction: "rtl" }],
              [{ size: ["small", false, "large", "huge"] }],
              [{ header: [1, 2, 3, 4, 5, 6, false] }],
              [{ color: [] }, { background: [] }],
              [{ font: [] }],
              [{ align: [] }],
              ["clean"],
              ["link", "image", "video"],
            ],
          },
          // more options
        },
        disabled: false,
      },
    };
  },
  //////////////////////////////methods////////////////////////////////////////////////////////////
  mounted() {
    this.action = this.$route.query.action || "";
    console.log("Received action query parameter:", this.action);

    const d_no = this.$route.query.d_no;
    console.log("Received d_no query parameter:", d_no);

    if (this.action === "U") {
      let params = new URLSearchParams();
      params.append("d_no", d_no);
      console.log("params: ", d_no);

      this.axios.post("/diary/detail.do", params).then((response) => {
        console.log("response", response.data.result);

        this.lists = {
          d_no: response.data.result.d_no,
          d_diarydt: response.data.result.d_diarydt,
          d_mood: response.data.result.d_mood,
          d_title: response.data.result.d_title,
          d_weather: response.data.result.d_weather,
          d_contents: response.data.result.d_contents,
        };

        if (this.lists.d_title) {
          this.title = this.lists.d_title;
          this.weather = this.lists.d_weather;
          this.idate = this.lists.d_diarydt;

          const moodMap = {
            기쁨: "1",
            평범: "2",
            우울: "3",
            화남: "4",
            놀람: "5",
          };
          this.mood = moodMap[this.lists.d_mood];
          this.state.content = this.lists.d_contents;
        }
      });
    }
  },

  methods: {
    isCreating() {
      // 'action' 값이 'I'인 경우 true 반환, 그렇지 않으면 false 반환
      return this.action === "I";
    },
    goBack() {
      this.$router.go(-1); // 이전페이지 이동
    },
    onEditorChange: function ({ html }) {
      // 텍스트 편집기 내용이 변경될때 호출디는 콜백 함수 onEditorChange 정의
      this.state._content = html;
    },
    focusInput() {
      if (!this.valid()) {
        return false;
      }
      let loginInfo = this.$store.state.loginInfo;
      this.loginId = loginInfo.loginId;

      let params = new URLSearchParams();

      console.log("this.state._content : ", this.state._content);
      const d_no = this.$route.query.d_no;
      params.append("d_no", d_no);
      params.append("loginId", this.loginId);
      params.append("d_title", this.title);
      if (this.isCreating()) {
        params.append("d_diarydt", this.formatDate(this.idate));
      } else {
        params.append("d_diarydt", this.formatDate2(this.idate));
        console.log(this.formatDate2(this.idate));
      }
      params.append("d_mood", this.mood);
      params.append("d_contents", this.state.content);
      params.append("action", this.action);
      console.log("this.action", this.action);

      params.append("d_weather", this.weather);
      params.append("noticeContent", this.state._content);
      //params.append("diarydt", this.diarydt);

      this.axios.post("/diary/Save.do", params).then((response) => {
        console.log(response);
        if (response.data.resultMsg == "SUCCESS") {
          alert("일기를 저장하였습니다.");
          this.$router.push("/dashboard/diary/list");
        } else if (response.data.resultMsg == "UPDATED") {
          alert("일기를 수정하였습니다.");
          this.$router.go(0);
        } else if (response.data.resultMsg == "FAILED") {
          alert("해당 날짜에 해당하는 일기가 존재합니다.");
        } else {
          alert("실패했습니다.");
        }
      });
    },
    Write: async function goWritepage() {
      // await createRouter.push("/DiaryInsert.vue/"); //경로
    },

    disabledDate(date) {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // 오늘의 시간을 00:00:00으로 설정
      const selectedDate = new Date(date);
      selectedDate.setHours(0, 0, 0, 0); // 선택한 날짜의 시간을 00:00:00으로 설정

      return selectedDate > today;
    },
    formatDate(date) {
      const year = date.getFullYear();
      let month = (1 + date.getMonth()).toString().padStart(2, "0");
      let day = date.getDate().toString().padStart(2, "0");

      return year + month + day;
    },

    formatDate2(date) {
      const year = date.getFullYear();
      let month = (1 + date.getMonth()).toString().padStart(2, "0");
      let day = date.getDate().toString().padStart(2, "0");

      return year + month + day;
    },

    reset() {
      this.$router.push("/dashboard/diary/list");
    },

    valid() {
      if (this.title == "") {
        alert("제목을 입력해주세요.");
        this.$refs.titleInput.focus();
        return false;
      }
      if (this.weather == "") {
        alert("날씨를 선택해주세요.");
        return false;
      }
      if (this.idate == "") {
        alert("날짜를 선택해주세요.");
        return false;
      }
      if (this.mood == "") {
        alert("기분을 선택해주세요.");
        return false;
      }
      if (this.state.content == "") {
        alert("내용을 입력해주세요.");
        return false;
      }
      return true;
    },
  },
};
</script>
<style>
tr,
th,
td {
  height: 70px;
  min-width: 108px;
  text-align: center;
}
#container ul li.contents .content {
  min-height: 450px;
}
#divEditor,
#divWeather {
  padding: 10px;
}
.ql-container {
  height: 200px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}
.list-main {
  margin-top: 2%;
}
.el-divider--horizontal {
  margin: 10px 0px 40px 0px;
}
.el-icon-edit {
  background-color: #9c97f0;
  color: white;
}
.el-icon-edit:hover {
  color: black;
}
.radio-group {
  margin-right: 20px;
}
.content input,
.content select {
  border: none;
}
.demonstration {
  color: var(--el-text-color-secondary);
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
.el-page-header__header {
  font-family: "나눔바른고딕", NanumBarunGothic;
  line-height: 60px;
  font-size: 28px;
  font-weight: bold;
  margin: 0px 0px 20px;
}
div.content p.conTitle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: auto !important;
}

table {
  width: 100%;
}
.vertical-align {
  vertical-align: middle;
}

/* 제목과 해당 입력 상자를 왼쪽에 정렬 */
th,
td {
  text-align: left;
}

/* 기분 select box를 오른쪽에 정렬 */
.mood-select {
  display: inline-block; /* inline-block으로 변경 */
  vertical-align: middle; /* 수직 정렬 */
}

img {
  width: 30px;
  height: 30px;
}

.el-radio-button {
  border: none; /* 테두리 없앰 */
  padding: 0; /* 패딩 없앰 */
  background-color: transparent; /* 배경색을 투명으로 설정 */
  opacity: 0.5;
}

/* el-radio-button 내부의 라벨에 대한 스타일 수정 */
.el-radio-button__inner {
  border: none; /* 테두리 없앰 */
  padding: 10; /* 패딩 없앰 */
  background-color: transparent; /* 배경색을 투명으로 설정 */
}

.el-radio-button.is-active,
.el-radio-button.is-focus {
  opacity: 1 !important;
}

.weather .el-radio-button.is-active .el-radio-button__inner {
  background-color: gray;
}

.mood .el-radio-button.is-active .el-radio-button__inner {
  background-color: transparent;
}
</style>
