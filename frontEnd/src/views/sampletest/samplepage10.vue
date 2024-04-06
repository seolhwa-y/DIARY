<template>
  <div id="container">
    <ul>
      <li class="contents">
        <!-- contents -->
        <h3 class="hidden">contents 영역</h3>
        <!-- content -->
        <div class="content">
          <p class="Location">
            <a href="#" class="btn_set home">메인으로</a>
            <a href="#" class="btn_nav bold">시스템 관리</a>
            <span class="btn_nav bold">공지 사항</span>
            <a href="#" class="btn_set refresh">새로고침</a>
          </p>

          <p class="conTitle">
            <span>공지 사항 </span>
            <span class="fr">
              <a class="btnType blue" name="modal" @click="newreg()">
                <span>신규등록</span></a
              >
            </span>
          </p>

          <!--검색창  -->
          <table
            width="100%"
            cellpadding="5"
            cellspacing="0"
            border="1"
            align="left"
            style="border-collapse: collapse; border: 1px #50bcdf"
          >
            <tr style="border: 0px; border-color: blue">
              <td height="25" style="font-size: 120%">&nbsp;&nbsp;</td>

              <td height="25" style="font-size: 100%">제목</td>
              <td height="25" style="font-size: 100%">
                <input
                  type="text"
                  style="width: 120px"
                  id="title"
                  name="title"
                  v-model="sch_title"
                />
              </td>
              <td height="25" style="font-size: 100%">작성일</td>
              <td height="25" style="font-size: 100%">
                <input
                  type="date"
                  style="width: 120px"
                  id="from_date"
                  name="from_date"
                  v-model="sch_from_date"
                />
              </td>
              <td height="25" style="font-size: 100%">
                <input
                  type="date"
                  style="width: 120px"
                  id="to_date"
                  name="to_date"
                  v-model="sch_to_date"
                />
              </td>
              <td height="60" style="font-size: 120%">
                <a
                  class="btn btn-light mx-2"
                  id="searchBtn"
                  name="btn"
                  @click="newreg()"
                  ><span>등 록</span></a
                >
                <a
                  class="btn btn-primary mx-2"
                  id="searchBtn"
                  name="btn"
                  @click="search()"
                  ><span>검 색</span></a
                >
              </td>
            </tr>
          </table>

          <div class="divNoticeList">
            <table class="col">
              <caption>
                caption
              </caption>

              <colgroup>
                <col width="50px" />
                <col width="200px" />
                <col width="60px" />
                <col width="50px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">공지 번호</th>
                  <th scope="col">공지 제목</th>
                  <th scope="col">공지 날짜</th>
                  <th scope="col">작성자</th>
                </tr>
              </thead>
              <tbody id="noticeList">
                <template v-if="totalCnt == 0">
                  <tr>
                    <td colspan="4">데이터가 없습니다</td>
                  </tr>
                </template>
                <template v-else>
                  <tr v-for="item in items" :key="item.noticeNo">
                    <td>{{ item.noticeNo }}</td>
                    <td @click="notcemod(item.noticeNo)">
                      {{ item.noticeTitle }}
                    </td>
                    <td>{{ item.noticeRegdate }}</td>
                    <td>{{ item.loginName }}</td>
                  </tr>
                </template>
              </tbody>
            </table>

            <!-- 페이징 처리  -->
            <div class="paging_area" id="pagingnavi">
              <paginate
                class="justify-content-center"
                v-model="currentPage"
                :page-count="page()"
                :page-range="5"
                :margin-pages="0"
                :click-handler="search"
                :prev-text="'이전'"
                :next-text="'다음'"
                :container-class="'pagination'"
                :page-class="'page-item'"
              >
              </paginate>
            </div>
          </div>
        </div>
        <!--// content -->
      </li>
    </ul>
  </div>
</template>

<script>
import { openModal } from 'jenesius-vue-modal';
import noticepop from './noticepop.vue';
import Paginate from 'vuejs-paginate-next';

export default {
  data: function () {
    return {
      items: [],
      sch_title: '',
      sch_from_date: '',
      sch_to_date: '',
      currentPage: 1,
      pageSize: 10,
      totalCnt: 0,
      action: '',
    };
  },
  components: {
    paginate: Paginate,
  },
  mounted() {
    this.search();
  },
  methods: {
    search: function () {
      //alert('search');

      let vm = this;

      let params = new URLSearchParams();
      params.append('title', this.sch_title);
      params.append('currentPage', this.currentPage);
      params.append('pageSize', this.pageSize);
      params.append('from_date', this.sch_from_date);
      params.append('to_date', this.sch_to_date);

      this.axios
        .post('/system/noticeListvue.do', params)
        .then(function (response) {
          //console.log(response);
          console.log(response.data.notice);
          console.log(response.data.noticeCnt);
          vm.items = response.data.notice;
          vm.totalCnt = response.data.noticeCnt;
          //vm.totalPage2 = vm.page2();
        })
        .catch(function (error) {
          alert('에러! API 요청에 오류가 있습니다. ' + error);
        });
    },
    page: function () {
      let total = this.totalCnt;
      let page = this.pageSize;
      let xx = total % page;
      let result = parseInt(total / page);

      if (xx == 0) {
        return result;
      } else {
        result = result + 1;
        return result;
      }
    },
    newreg: async function () {
      this.action = 'I';

      const modal = await openModal(noticepop, {
        title: '공지사항 등록',
        noticeNo: '',
        action: this.action,
      });

      modal.onclose = () => {
        //alert('창 닫음');
        this.search();
        //return false; //Modal will not be closed
      };
    },
    notcemod: async function (pnoticeNo) {
      this.action = 'U';

      const modal = await openModal(noticepop, {
        title: '공지사항 수정',
        noticeNo: pnoticeNo,
        action: this.action,
      });

      modal.onclose = () => {
        //alert('창 닫음');
        this.search();
        //return false; //Modal will not be closed
      };
    },
  },
};
</script>
