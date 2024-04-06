<template>
  <div class="content">
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> PROTOTYPE </span>
      </template>
    </el-page-header>
    <div id="divNotice">
      <p class="conTitle">
        <span>에디터</span>
        <span>
          참고 사이트 :: https://element-plus.org/en-US/ <br /><br />-- npm
          install <br />npm install element-plus --save <br />npm i
          unplugin-auto-import unplugin-vue-components -D <br />npm install
          @element-plus/icons-vue <br /><br />-- main.js <br />import
          ElementPlus from "element-plus";<br />
          import "element-plus/dist/index.css"; <br />import * as
          ElementPlusIconsVue from '@element-plus/icons-vue'; <br /><br />for
          (const [key, component] of Object.entries(ElementPlusIconsVue)) {
          app.component(key, component) } <br /><br />app .use(store)
          <br />.use(router) <br />.use(VueSession, sessionOptions)
          <br />.use(Paginate) <br />//.use(twLib) <br />.use(VueDaumpostcode)
          <br />.use(ElementPlus) <br />.mount("#app");
        </span>
      </p>
    </div>

    <div id="divEditor">
      <quill-editor
        v-model:value="state.content"
        :options="state.editorOption"
        @change="onEditorChange($event)"
      />
    </div>
  </div>

  <div class="content">
    <div id="divNotice">
      <p class="conTitle">
        <span>SAMPLE</span>
        <span>
          참고 사이트 :: https://flyween.github.io/vue3-quill/
          <br />
          npm i vue3-quill
        </span>
      </p>
    </div>
    <table>
      <tbody>
        <tr>
          <th>날씨선택</th>
          <td>
            <el-form-item prop="weather">
              <el-radio-group v-model="weather">
                <el-radio-button label="sunny">
                  <el-icon size="20"><Sunny /></el-icon>
                </el-radio-button>
                <el-radio-button label="cloudy">
                  <el-icon size="20"><Cloudy /></el-icon>
                </el-radio-button>
                <el-radio-button label="pouring">
                  <el-icon size="20"><Pouring /></el-icon>
                </el-radio-button>
                <el-radio-button label="lightning">
                  <el-icon size="20"><Lightning /></el-icon>
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <th>날짜선택</th>
          <td>
            <div class="demo-date-picker">
              <div class="block">
                <el-date-picker
                  v-model="date"
                  type="date"
                  :disabled-date="disabledDate"
                  placeholder="날짜를 선택하세요."
                />
              </div>
            </div>
          </td>
        </tr>
        <tr>
          <th>검색아이콘</th>
          <td>
            <el-row>
              <el-button class="el-icon-edit" circle
                ><el-icon><Search /></el-icon
              ></el-button>
            </el-row>
          </td>
        </tr>
        <tr>
          <th>슬라이드</th>
          <td>
            <el-carousel indicator-position="outside" height="150px">
              <el-carousel-item v-for="item in slide" :key="item">
                <h3 text="2xl" justify="center">{{ item }}</h3>
              </el-carousel-item>
            </el-carousel>
          </td>
        </tr>
        <tr>
          <th>타이틀</th>
          <td>
            <el-page-header @back="goBack">
              <template #content>
                <span class="text-large font-600 mr-3">
                  타이틀 header 및 뒤로가기 재정의
                </span>
              </template>
            </el-page-header>
          </td>
        </tr>
        <tr>
          <th>날씨 API</th>
          <td>날씨</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
import { quillEditor } from "vue3-quill";
// import { reactive } from "vue";

export default {
  name: "App",
  components: {
    quillEditor, // 에디터
  },
  data() {
    return {
      weather: "sunny",
      date: "",
      state: {
        content: "<p>내용</p>",
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
        },
        disabled: false,
      },
      slide: [
        "주제1",
        "주제2",
        "주제3",
        "주제4",
        "주제5",
        "주제6",
        "주제7",
        "주제8",
        "주제9",
        "주제10",
      ],
    };
  },
  methods: {
    // DatePicker disabledDate
    disabledDate(time) {
      return time.getTime() > Date.now();
    },
    // Editor text -> html save
    onEditorChange({ html }) {
      // onEditorChange({ quill, html, text }) {

      this.state._content = html;
      console.log("editor change!", this.state._content);
    },
    // 뒤로가기 재정의
    goBack() {
      console.log("go back");
      this.$router.go(-1); // 이전페이지 이동
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
  padding: 20px;
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
  font-size: 3rem;
}

div.content p.conTitle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: auto !important;
}
</style>
