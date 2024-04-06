<template>
  <el-row class="tac">
    <el-col :span="12">
      <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        v-for="list in loginInfo.menulist"
        :key="list.mnu_id"
      >
        <el-sub-menu>
          <template #title>
            <span>{{ list.mnu_nm }}</span>
          </template>
          <el-menu-item-group v-for="i in list.nodeList" :key="i.mnu_id">
            <el-menu-item
              ><router-link :to="'/dashboard' + i.mnu_url.replace('.do', '')">{{
                i.mnu_nm
              }}</router-link></el-menu-item
            >
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>
    </el-col>
  </el-row>

  <div style="clear: both"></div>
</template>

<script>
// import SlideUpDown from "vue3-slide-up-down";
// import Dashboard from "@/views/Dashboard.vue";

export default {
  name: "leftMenu",
  // components: { slide: SlideUpDown },
  data: function () {
    return {
      activeIndex: "1",
      loginInfo: {
        menulist: [],
        loginId: "",
      },
    };
  },
  mounted: function () {
    let loginInfo = this.$store.state.loginInfo;
    loginInfo.usrMnuAtrt.forEach((item) => {
      item.isShow = false;
    });
    this.loginInfo.menulist = loginInfo.usrMnuAtrt;
    this.loginInfo.loginId = loginInfo.loginId;
  },
  methods: {
    handleOpen: function (key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose: function (key, keyPath) {
      console.log(key, keyPath);
    },
    logoutproc: function () {
      this.$store.commit("logout");

      this.axios
        .post("/loginOut.do")
        .then((resp) => {
          let data = resp.data;
          console.log(data.msg);
          alert("LogOut 되어ㅆ습니다.");
          this.$router.push("/");
        })
        .catch((error) => {
          console.log(error);
          alert("API 요청에 오류가 있습니다");
        });
    },
    /* menulink:function(path){
            let patharr=path.split('/');
            let type=patharr[1];
            let menu=patharr[2].replace('.do','');
            //  this.$router.push({path:'/dashboard'+path.replace('.do',''),params:{type:type,menu:menu}})
            this.$router.push('/dashboard/'+type+'/'+menu);
        }, */
    setlist: function (list) {
      this.menulist = list;
    },
    // slideDown: function (index, event) {
    //   // prevent a tag Default event
    //   event.preventDefault();
    //   // slide down chosen menu list
    //   this.loginInfo.menulist[index].isShow =
    //     !this.loginInfo.menulist[index].isShow;

    //   // slideUp exept choose one
    //   for (let i = 0; i < this.loginInfo.menulist.length; i++) {
    //     if (i != index) {
    //       this.loginInfo.menulist[i].isShow = false;
    //     }
    //   }
    // },
  },
};
</script>

<style>
.tac {
  width: 400px;
}
.el-sub-menu__title {
  font-weight: 800;
}
</style>
