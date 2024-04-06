<template>
  <form id="myForm" action="" method="post">
    <div id="background_board">
      <div class="login_form shadow" align="center">
        <div class="login-form-right-side">
          <backStar></backStar>
          <span class="typing">D I A R Y</span>
        </div>
        <div class="login-form-left-side">
          <fieldset>
            <legend>로그인</legend>
            <p class="id">
              <label for="user_id">아이디</label>
              <input
                v-model="lgn_Id"
                id="EMP_ID"
                type="text"
                name="lgn_Id"
                placeholder="아이디"
                style="ime-mode: inactive"
                @keypress="checkCode"
              />
            </p>
            <p class="pw">
              <label for="user_pwd">비밀번호</label>
              <input
                v-model="pwd"
                id="EMP_PWD"
                type="password"
                name="pwd"
                placeholder="비밀번호"
                onfocus="this.placeholder=''; return true"
                @keypress="checkCode"
              />
            </p>
            <p class="member_info">
              <input
                v-model="saveId"
                id="cb_saveId"
                type="checkbox"
                @keypress="checkCode"
              />
              <span class="id_save">ID저장</span>
            </p>
            <div>
              <!-- @click.prevent="join" -->
              <!-- 기본 동작을 방지하고 페이지 이동이 아닌 메소드 실행됨 -->
              <a href="#" @click.prevent="showRegisterModal">
                <strong>[회원가입]</strong>
                <!-- RegisterModal 컴포넌트에 ref 속성 추가 -->
              </a>
              <register-modal ref="registerModal"></register-modal>
              <!--  -->
              <a href="#" @click.prevent="showFindAccountModal">
                <strong>[아이디/비밀번호 찾기]</strong>
              </a>
              <find-account-modal ref="findAccountModal"></find-account-modal>
              <!-- 소셜 로그인 버튼 -->
              <a href="#" @click.prevent="showSocialLoginModal">
                <strong>[소셜로그인]</strong>
              </a>
              <socialLogin-modal ref="SocialLoginModal"></socialLogin-modal>
            </div>
            <!-- Login Btn -->
            <a class="btn_login" id="btn_login" @click="fLoginProc">
              <strong>LOGIN</strong>
            </a>
          </fieldset>
        </div>
      </div>
    </div>
  </form>
</template>

<script>
//import Dashboard from "@/views/Dashboard.vue";
import RegisterModal from "@/views/user/RegisterModal.vue";
import FindAccountModal from "@/views/user/FindAccountModal.vue";
import SocialLoginModal from "@/views/user/SocialLoginModal.vue";
import backStar from "@/components/system/backStar.vue";

import "@/assets/css/admin/login.css";
// import "../assets/js/common.js";
export default {
  data: function () {
    return {
      lgn_Id: "",
      pwd: "",
      saveId: false,
    };
  },
  components: {
    RegisterModal,
    FindAccountModal,
    SocialLoginModal,
    backStar,
  },
  mounted: function () {
    let savedID = this.getCookie("EMP_ID");
    this.lgn_Id = savedID || "";
    this.savedID = savedID != "" ? true : false;
  },
  methods: {
    // Vue 공식 홈페이지에 있는 방식으로는 ()=>{} 화살표 함수형식으로의 작성을
    // 권장하지 않는다. 특히 this.var... 처럼 Vue 인스턴스의 값을 얻어오려 할때
    // 화살표 함수로 작성한 메소드의 경우 받아오지 못하는 현상이 발생한다.
    // ref :
    // http://1004lucifer.blogspot.com/2020/06/vuejs-methods-this-this.html

    // 소셜 로그인 모달을 표시합니다.
    showSocialLoginModal() {
      console.log("클릭");
      if (this.$refs.SocialLoginModal) {
        console.log(
          "this.$refs.SocialLoginModal : ",
          this.$refs.SocialLoginModal
        );
        this.$refs.SocialLoginModal.showModal();
      }
    },
    // 아이디 비밀번호 찾기 모달
    showFindAccountModal() {
      console.log("클릭");
      if (this.$refs.findAccountModal) {
        this.$refs.findAccountModal.showModal();
      }
    },
    // 회원가입 모달
    showRegisterModal() {
      if (this.$refs.registerModal) {
        this.$refs.registerModal.showModal();
      }
    },

    checkCode: function (event) {
      if (event.keyCode == 13) this.fLoginProc();
    },
    fLoginProc: function () {
      if (this.isValidated()) {
        let vm = this;

        console.log("Proc login,,,");
        this.saveCookie("EMP_ID", this.lgn_Id, this.saveId == true ? 7 : -1);

        this.axios
          .post(
            "/loginProc.do",
            new URLSearchParams({ lgn_Id: this.lgn_Id, pwd: this.pwd })
          )
          .then((resp) => {
            let data = resp.data;
            console.log("data : ", data);
            if (data.result == "SUCCESS") {
              this.$store.commit("logged", {
                loginId: data.loginId,
                userNm: data.userNm,
                userType: data.userType,
                serverName: data.serverName,
                usrMnuAtrt: data.usrMnuAtrt,
              });
              this.$store.commit("auth", { type: data.userType });

              // guide for making vue files
              data.usrMnuAtrt.forEach(function (item) {
                console.log(item.mnu_nm);
                item.nodeList.forEach(function (item) {
                  let purl = item.mnu_url.replace(".do", ".vue");
                  let vueFilePath = "@/views" + purl;
                  console.log(
                    "  메뉴명: " + item.mnu_nm + " || 파일경로 : " + vueFilePath
                  );
                });
              });
              ////////////////////////////////

              this.$session.set("loginId", resp.data.loginId);
              this.$session.set("userNm", resp.data.userNm);
              this.$session.set("userType", resp.data.userType);
              this.$session.set("serverName", resp.data.serverName);
              this.$session.set("usrMnuAtrt", resp.data.usrMnuAtrt);
              this.$session.set("loginType", "id");

              vm.$router.push("/dashboard/home");
            } else {
              if (data.resultMsg.indexOf("회원가입") > -1)
                alert(data.resultMsg);
              else alert("ID 혹은 비밀번호가 틀립니다");
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    isValidated: function () {
      let chk = this.checkNotEmpty([
        ["EMP_ID", "아이디를 입력해 주세요."],
        ["EMP_PWD", "비밀번호를 입력해 주세요."],
      ]);
      return chk;
    },
    checkNotEmpty: function (arr) {
      for (var i = 0, len = arr.length; i < len; i++) {
        var elem = document.getElementById(arr[i][0]);
        console.log("elem is...");
        console.log(elem);
        if (elem.length <= 0) {
          continue;
        }
        var elemValue = elem.value;
        var alertMsg = arr[i][1];

        console.log(elemValue);
        if (elemValue == "") {
          alert(alertMsg);
          elem.focus();
          return false;
        }
      }
      return true;
    },
    saveCookie: function (nm, id, day) {
      let today = new Date();
      today.setDate(today.getDate() + day);
      document.cookie =
        nm + "=" + id + "; path=/; expires=" + today.toGMTString() + ";";
    },
    getCookie: function (nm) {
      // userid 쿠키에서 id 값을 가져온다.
      var cook = document.cookie + ";";
      var idx = cook.indexOf(nm, 0);
      var val = "";

      if (idx != -1) {
        cook = cook.substring(idx, cook.length);
        let begin = cook.indexOf("=", 0) + 1;
        let end = cook.indexOf(";", begin);
        val = unescape(cook.substring(begin, end));
      }
      return val;
    },
  },
};
</script>

<style>
/* 애니메이션 */
.typing {
  width: 7em;
  /*animation: typing 2s steps(7), blink 0.3s step-end infinite alternate;
    border-right: 8px solid;*/
  white-space: nowrap;
  overflow: hidden;
  padding: 20px;
  font-size: 6em;
  font-family: "Nanum Myeongjo", serif;
  font-weight: 800;
  background: -webkit-linear-gradient(white, #6c8cab);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
#btn_login {
  background: linear-gradient(#29234f, slategrey);
}
#btn_login > strong {
  background: -webkit-linear-gradient(white, #6c8cab);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
/*
  @keyframes typing {
    from {
      width: 0;
    }
  }
  @keyframes blink {
    50% {
      border-color: transparent;
    }
  }
  */
/* background */
.login-form-right-side {
  flex-direction: unset;
}

.login-form-right-side {
  height: 100%;
  background: linear-gradient(#29234f, slategrey);
  overflow: hidden;
}
</style>
