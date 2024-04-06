<template>
  <dl id="grpInfoWrap">
    <dd class="content"></dd>

    <!-- s : 여기에 내용입력 -->
    <table id="grpInfo">
      <colgroup>
        <col width="120px" />
        <col width="*" />
        <col width="120px" />
        <col width="*" />
      </colgroup>

      <tbody>
        <tr>
          <td colspan="4" class="text-center">
            <div class="my-4">
              <strong style="font-size: 30px">{{ title }}</strong>
            </div>
          </td>
        </tr>
        <tr v-if="action == 'U'">
          <th scope="row">번호<span class="font_red">*</span></th>
          <td>
            <input
              type="text"
              class="form-control"
              name="noticeNo"
              id="noticeNo"
              readonly
              v-model="no"
            />
          </td>
          <th scope="row">작성자</th>
          <td>
            <input
              type="text"
              class="form-control"
              name="loginName"
              id="loginName"
              readonly
              v-model="loginName"
            />
          </td>
        </tr>
        <tr>
          <th scope="row">제목<span class="font_red">*</span></th>
          <td colspan="3">
            <input
              type="text"
              class="form-control"
              name="noticeTitle"
              id="noticeTitle"
              v-model="noticeTitle"
            />
          </td>
        </tr>
        <tr>
          <th scope="row">내용<span class="font_red">*</span></th>
          <td colspan="3">
            <textarea
              name="noticeContent"
              id="noticeContent"
              v-model="content"
            ></textarea>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- e : 여기에 내용입력 -->

    <div class="btn_areaC mt30">
      <a
        @click="doNotice()"
        class="btn btn-primary mx-2"
        id="btnSaveGrpCod"
        name="btn"
      >
        <span>저장</span>
      </a>
      <a
        @click="notNotice()"
        class="btn btn-danger mx-2"
        v-show="
          this.action == 'U' &&
          this.$store.state.loginInfo.loginId == this.loginId
        "
      >
        <span>삭제</span>
      </a>
      <a @click="notNotice('C')" class="btn btn-danger mx-2">
        <span>취소</span>
      </a>
    </div>
  </dl>
</template>

<script>
import { closeModal } from "jenesius-vue-modal";

export default {
  // vue에서는 받아온 변수를 methods에서 직접 핸들링이 불가능하기 때문에
  // 임시 변수를 만들어서 받아온 변수를 넣어 줘야 함
  components: {},
  props: {
    title: String,
    noticeNo: Number,
    action: String,
  },
  data: function () {
    return {
      loginId: "",
      loginName: "",
      no: "",
      noticeTitle: "",
      content: "",
      writer: "",
      isShow: false,
      weather: "sunny",
      editor: null,
    };
  },

  computed: {},
  // html 로딩, 가상 dom 실행, 이 두 개 연결 시 작동
  mounted: function () {
    let vm = this;
    // 신규 등록 시
    if (this.noticeNo == null || this.noticeNo == "") {
      vm.no = "";
      vm.noticeTitle = "";
      vm.content = "";
      vm.loginName = "";

      vm.isShow = false;
    } else {
      //  수정 시 (grp_cod 에 해당하는 상세코드 정보 가져오기)
      let params = new URLSearchParams();
      params.append("noticeNo", this.noticeNo);

      this.axios
        .post("/system/detailNotice.do", params)
        .then(function (response) {
          vm.no = response.data.result.noticeNo;
          vm.noticeTitle = response.data.result.noticeTitle;
          vm.content = response.data.result.noticeContent;
          vm.loginId = response.data.result.loginId;
          vm.loginName = response.data.result.loginName;

          if (vm.$store.state.loginInfo.loginId == vm.loginId) vm.isShow = true;
        })
        .catch(function (error) {
          alert("에러! API 요청에 오류가 있습니다. " + error);
        });
    }
  },
  methods: {
    doNotice: function () {
      console.log("doNotice : ", this.action);

      if (confirm("저장하시겠습니까?")) {
        let vm = this;
        let params = new URLSearchParams();

        if (this.action == "U") params.append("noticeNo", this.no);
        params.append("loginId", this.$store.state.loginInfo.loginId);
        params.append("action", this.action);
        params.append("noticeTitle", this.noticeTitle);
        params.append("noticeContent", this.content);

        this.axios
          .post("/system/noticeSave.do", params)
          .then(function (response) {
            let status = response.status;
            let msg = response.data.resultMsg;
            if (status == 200) {
              alert(msg);
              closeModal(vm);
            } else {
              alert(msg);
            }
          })
          .catch(function (error) {
            alert("에러! API 요청에 오류가 있습니다. " + error);
          });
      }
    },
    notNotice: function (not) {
      if (not == "C") return closeModal(this);

      if (this.action == "U" && confirm("삭제하시겠습니까?")) {
        let vm = this;
        let params = new URLSearchParams();

        params.append("noticeNo", this.no);

        this.axios
          .post("/system/noticeDelete.do", params)
          .then(function (response) {
            let status = response.status;
            let msg = response.data.resultMsg;
            if (status == 200) {
              alert(msg);
              closeModal(vm);
            } else {
              alert(msg);
            }
          })
          .catch(function (error) {
            alert("에러! API 요청에 오류가 있습니다. " + error);
          });
      } else {
        closeModal(this);
      }
    },
  },
  created() {
    // 자식요소에서 부모 요소 method 호출
  },
};
</script>
