<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <template v-if="loginInfo.loginId">
          <backStar></backStar>
          <div class="el-page-header__header">
            <div class="el-page-header__left header">
              <div class="el-page-header__content" @click="moveDashBoard">
                <el-icon :size="30" color="#FFFFFF" class="mr-3"
                  ><House
                /></el-icon>
                <span>HOME</span>
              </div>

              <div
                class="el-divider el-divider--vertical"
                role="separator"
                style="--el-border-style: solid"
              ></div>

              <div class="el-page-header__content">
                <span class="flex items-center" @click="movePage">
                  <el-avatar
                    class="mr-3"
                    :size="30"
                    src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                  />
                </span>
                <span @click="movePage">
                  {{ loginInfo.userNm }}
                </span>
              </div>
            </div>
          </div>
          <!-- 로그아웃 -->
          <div class="flex items-center">
            <!-- <el-button
              type="primary"
              color="#626aef"
              round
              @click="logoutproc()"
              >LOGOUT</el-button
            > -->
            <el-button color="#182a53" round @click="logoutproc()"
              >LOGOUT</el-button
            >
          </div>
        </template>
      </el-header>
      <el-container>
        <el-aside width="200px"><Menu></Menu></el-aside>
        <el-main>
          <router-view :type="type" :menu="menu"></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import Menu from "@/components/leftMenu.vue";
import backStar from "@/components/system/backStar.vue";

export default {
  data: function () {
    return {
      type: this.$route.params.type,
      menu: this.$route.params.menu,
      loginInfo: {
        menulist: [],
        loginId: "",
        userNm: "",
      },
    };
  },

  methods: {
    moveDashBoard: async function () {
      // 대쉬보드 이동
      this.$router.push("/dashboard/home");
    },
    movePage: async function () {
      // 마이페이지 이동
      this.$router.push("/dashboard/mypage/myPage");
    },

    logoutproc() {
      console.table(
        "this.$session.get('type') : ",
        this.$session.get("loginType")
      );
      // let params = new URLSearchParams();
      // params.append("type", this.sch_title);

      let url = "";
      if (this.$session.get("loginType") != "naver") {
        url = "/api/logout";
      } else if (this.$session.get("loginType") == "naver") {
        url = "/api/logout/naver";
      }
      this.axios
        .post(url)
        .then(() => {
          alert("로그아웃 되었습니다.");
          // Vuex 상태 초기화 또는 사용자 인증 관련 데이터 제거
          this.$store.dispatch("logout");
          // 세션 스토리지 클리어 (vue-session 사용 시)
          this.$session.clear();

          // 네이버 로그인 상태 확인 후 네이버 로그아웃 처리
          if (this.$store.state.isNaverLoggedIn) {
            console.log("@@@@@@@@@@@@@@@@@", this.$store.state.isNaverLoggedIn);
            //window.location.href = "https://nid.naver.com/nidlogin.logout";
          }

          // 네이버 로그아웃 후의 리다이렉트 처리는 네이버 로그아웃 페이지에서 설정
          else if (this.$store.state.isKakaoLoggedIn) {
            if (!window.Kakao.Auth.getAccessToken()) {
              console.log("Not logged in.");
              return;
            }
            window.Kakao.Auth.logout(function () {
              alert("로그아웃 되었습니다.");
            });
          } else {
            // 네이버로 로그인하지 않았다면, 일반적인 로그아웃 후 홈으로 리다이렉트
            this.$router.push("/");
          }

          // // 네이버 로그인 상태 확인 후 네이버 로그아웃 처리
          // if (this.$store.state.isNaverLoggedIn) {
          //   // window.location.href = "https://nid.naver.com/nidlogin.logout";
          //   // 네이버 로그아웃 후의 리다이렉트 처리는 네이버 로그아웃 페이지에서 설정
          // } else {
          //   // 네이버로 로그인하지 않았다면, 일반적인 로그아웃 후 홈으로 리다이렉트
          //   this.$router.push("/");
          // }

          this.$router.push("/");
        })
        .catch((error) => {
          console.error("로그아웃 처리 중 에러 발생", error);
        });
    },
  },

  mounted: function () {
    let loginInfo = this.$store.state.loginInfo;
    loginInfo.usrMnuAtrt.forEach((item) => {
      item.isShow = false;
    });
    console.table(this.$store.state.loginInfo);
    this.loginInfo.menulist = loginInfo.usrMnuAtrt;
    this.loginInfo.loginId = loginInfo.loginId;
    this.loginInfo.userNm = loginInfo.userNm;
  },
  watch: {
    $route(to) {
      this.type = to.params.type;
      this.menu = to.params.menu;
    },
  },
  beforeRouteUpdate: function (to, from, next) {
    this.$router.options.methods.checkAuthed(to, from, next);
  },
  created: function () {
    console.log(this.$store.state.loginInfo);
    this.type = this.$route.params.type;
    this.menu = this.$route.params.menu;
  },

  components: { Menu, backStar },
};
</script>
<style>
header.el-header .header > .el-page-header__content > span {
  justify-content: space-between;
  font-family: "나눔바른고딕", NanumBarunGothic;
  font-size: 18px;
  font-weight: bold;
  color: white;
  text-shadow: 1px 1px 1px gray;
  align-items: center;
}

header.el-header {
  display: flex;
  /* background-color: rgba(93, 157, 255, 0.7); */
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(#29234f, slategrey);
  position: sticky;
}

header.el-header .el-page-header__header {
  position: absolute;
}

.el-main .el-page-header .el-page-header__header {
  font-family: "나눔바른고딕", NanumBarunGothic;
  line-height: 60px;
  font-size: 28px;
  font-weight: bold;
  margin: 0px 0px 20px;
}

.el-aside {
  overflow: hidden !important;
  border-right: solid 1px var(--el-menu-border-color);
}

section.el-container.is-vertical {
  min-height: 100vh;
  overflow: hidden;
}

.common-layout {
  background-color: white;
}
</style>
