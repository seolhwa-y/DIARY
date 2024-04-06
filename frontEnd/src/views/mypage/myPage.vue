<template>
  <el-page-header @back="goBack">
    <template #content>
      <span class="text-large font-600 mr-3"> 마이페이지 </span>
    </template>
  </el-page-header>

  <div id="myForm">
    <div id="div_el_form">
      <el-form :model="form" label-width="120px">
        <el-form-item label="ID">
          <el-input v-model="userInfo.userID" disabled />
        </el-form-item>
        <el-form-item label="이름">
          <el-input
            v-model="userInfo.userNm"
            ref="userNm"
            type="text"
            placeholder="이름을 입력하세요."
            maxlength="20"
          />
        </el-form-item>
        <el-form :model="userInfo" label-width="120px" class="demo-ruleForm">
          <el-form-item label="PASSWORD" prop="password">
            <el-input
              v-model="userInfo.password"
              id="password"
              ref="password"
              type="password"
              placeholder="비밀번호를 입력하세요."
              maxlength="100"
              @keyup="samePassword($event)"
            />
            <span class="el-form-item__error" v-show="isCheck1">
              {{ result1 }}
            </span>
          </el-form-item>
          <el-form-item label="RE-PASSWORD" prop="checkPassword">
            <el-input
              v-model="userInfo.checkPassword"
              id="checkPassword"
              ref="checkPassword"
              type="password"
              placeholder="다시 비밀번호를 입력하세요."
              maxlength="100"
              @keyup="samePassword()"
            />
            <span class="el-form-item__error" v-show="isCheck2">
              {{ result2 }}
            </span>
          </el-form-item>
        </el-form>
        <el-form-item label="연락처">
          <el-input
            v-model="userInfo.tell"
            ref="tell"
            type="tel"
            maxlength="14"
            placeholder="연락처를 입력하세요."
            show-word-limit
            @keyup="formTell()"
            required
          />
        </el-form-item>
        <el-form-item style="margin: 0% 35%">
          <el-button type="primary" @click="updShow = true">수정</el-button>
          <el-button @click="delShow = true">회원탈퇴</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 회원탈퇴 및 수정 -->
    <el-dialog v-model="updShow" title="회원정보 수정" width="500" align-center>
      <span>회원정보를 수정하시겠습니까?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="updUserInfo('U')">네</el-button>
          <el-button @click="updShow = false"> 아니요 </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="delShow" title="회원탈퇴" width="500" align-center>
      <span>정말 회원탈퇴를 하실건가요?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="updUserInfo('D')">네</el-button>
          <el-button @click="delShow = false"> 아니요 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import { ElMessage } from "element-plus";

export default {
  data() {
    return {
      userInfo: {
        userID: "",
        userNm: "",
        password: "",
        checkPassword: "",
        tell: "",
      },
      action: "",
      updShow: false,
      delShow: false,
      result1: "",
      isCheck1: false,
      result2: "",
      isCheck2: false,
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1); // 이전페이지 이동
    },

    getInfo: function () {
      this.userInfo.userID = this.$store.state.loginInfo.loginId;
      this.userInfo.password = "";
      this.userInfo.checkPassword = "";
      this.result = "";

      let vm = this;
      let params = new URLSearchParams();
      params.append("loginId", this.userInfo.userID);

      this.axios
        .get("/mypage/myPage.do", params)
        .then(function (response) {
          vm.userInfo.userNm = response.data.myInfo.NAME;
          vm.userInfo.tell = response.data.myInfo.TELL;
        })
        .catch(function (error) {
          alert("에러! API 요청에 오류가 있습니다. " + error);
        });
    },

    formTell: function () {
      if (this.userInfo.tell.length > 3 && this.userInfo.tell.length < 9) {
        this.userInfo.tell = this.userInfo.tell.replaceAll("-", "");
        this.userInfo.tell =
          this.userInfo.tell.substring(0, 3) +
          "-" +
          this.userInfo.tell.substring(3, 7);
      } else if (this.userInfo.tell.length == 14) {
        this.userInfo.tell = this.userInfo.tell.replaceAll("-", "");
        this.userInfo.tell =
          this.userInfo.tell.substring(0, 4) +
          "-" +
          this.userInfo.tell.substring(4, 8) +
          "-" +
          this.userInfo.tell.substring(8);
      } else if (
        this.userInfo.tell.length > 9 &&
        this.userInfo.tell.length < 14
      ) {
        this.userInfo.tell = this.userInfo.tell.replaceAll("-", "");
        this.userInfo.tell =
          this.userInfo.tell.substring(0, 3) +
          "-" +
          this.userInfo.tell.substring(3, 7) +
          "-" +
          this.userInfo.tell.substring(7);
      }
    },

    samePassword: function () {
      if (this.userInfo.password === "" && this.userInfo.checkPassword === "") {
        this.changeColor(2, "", "");

        return "";
      } else {
        if (this.userInfo.password === "") {
          this.changeColor(1, "FAIL", "password");
        } else if (this.userInfo.checkPassword === "") {
          this.changeColor(1, "FAIL", "checkPassword");
        } else if (this.userInfo.password !== this.userInfo.checkPassword) {
          this.changeColor(2, "FAIL", "checkPassword");
        } else if (this.userInfo.password === this.userInfo.checkPassword) {
          this.changeColor(2, "", "");

          return true;
        }

        return null;
      }
    },

    changeColor: function (no, that, id) {
      let pass =
        document.getElementById("password").parentNode.parentNode.parentNode
          .parentNode;
      let checkPass =
        document.getElementById("checkPassword").parentNode.parentNode
          .parentNode.parentNode;

      if (no == 2) {
        if (that != "FAIL") {
          this.isCheck1 = false;
          this.result1 = "";
          this.isCheck2 = false;
          this.result2 = "";

          pass.className = "el-form-item asterisk-left is-focus";
          checkPass.className = "el-form-item asterisk-left is-focus";
        } else {
          this.isCheck1 = false;
          this.result1 = "";
          this.isCheck2 = true;
          this.result2 = "비밀번호가 일치하지 않습니다.";

          pass.className =
            "el-form-item asterisk-left el-form-item--feedback is-error";
          checkPass.className =
            "el-form-item asterisk-left el-form-item--feedback is-error";
        }
      } else if (no == 1) {
        if (that === "SUCCESS") {
          if (id == "password") {
            pass.className = "el-form-item asterisk-left is-focus";
          } else {
            checkPass.className = "el-form-item asterisk-left is-focus";
          }
        } else if (that === "FAIL") {
          if (id === "password") {
            this.isCheck1 = true;
            this.result1 = "비밀번호를 입력하세요.";
            this.isCheck2 = false;
            this.result2 = "";

            pass.className =
              "el-form-item asterisk-left el-form-item--feedback is-error";
            checkPass.className = "el-form-item asterisk-left is-focus";
          } else if (id === "checkPassword") {
            this.isCheck1 = false;
            this.result1 = "";
            this.isCheck2 = true;
            this.result2 = "비밀번호를 입력하세요.";

            pass.className = "el-form-item asterisk-left is-focus";
            checkPass.className =
              "el-form-item asterisk-left el-form-item--feedback is-error";
          }
        }
      }
    },

    checkValidition: function () {
      let params = new URLSearchParams();

      if (this.userInfo.userNm != "") {
        params.append("name", this.userInfo.userNm);

        if (this.samePassword()) {
          params.append("password", this.userInfo.checkPassword);
        } else if (this.samePassword() == null) {
          ElMessage({
            message: "사용자 비밀번호를 확인해주세요.",
            type: "warning",
          });

          this.$refs.checkPassword.focus();

          return null;
        }

        if (
          /[0-9]{3}-[0-9]{4}-[0-9]{4}/.test(this.userInfo.tell) &&
          this.userInfo.tell != ""
        ) {
          params.append("tell", this.userInfo.tell);
        } else {
          ElMessage({
            message: "사용자 연락처를 확인해주세요.",
            type: "warning",
          });

          this.$refs.tell.focus();

          return null;
        }

        return params;
      } else {
        ElMessage({
          message: "사용자 이름을 확인해주세요.",
          type: "warning",
        });

        this.$refs.userNm.focus();

        return null;
      }
    },

    updUserInfo: function (act) {
      let vm = this;

      if (act == "D") {
        // 회원탈퇴
        this.axios
          .post("/mypage/resign.do")
          .then(function (response) {
            if (response.data.MESSAGE == "SUCCESS") {
              vm.delShow = false;

              ElMessage({
                message: "회원탈퇴가 완료되었습니다.",
                type: "success",
              });

              vm.$router.push("/");
            }
          })
          .catch(function (error) {
            ElMessage.error("에러! API 요청에 오류가 있습니다. " + error);
          });
      } else if (act == "U") {
        // 회원정보 수정
        this.updShow = false;

        const param = this.checkValidition();

        if (param == null) {
          return false;
        }

        this.axios
          .post("/mypage/updInfo.do", param)
          .then(function (response) {
            if (response.data.MESSAGE == "SUCCESS") {
              vm.$store.state.loginInfo.userNm = vm.userInfo.userNm;
              vm.getInfo();

              ElMessage({
                message: "회원정보 수정이 완료되었습니다.",
                type: "success",
              });
            }
          })
          .catch(function (error) {
            ElMessage.error("에러! API 요청에 오류가 있습니다. " + error);
          });
      }
    },
  },
  mounted() {
    this.getInfo();
  },
};
</script>
<style>
#myForm {
  padding: 3% 10% 0% 10%;
}
</style>
