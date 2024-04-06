<template>
  <form id="noticeform">
    <dl class="layerPop layerType2">
      <dt>
        <strong>{{ title }}</strong>
      </dt>
      <dd class="content">
        <!-- s : 여기에 내용입력 -->
        <table class="row">
          <caption>
            caption
          </caption>
          <colgroup>
            <col width="30%" />
            <col width="70%" />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">
                작성자 <span class="font_red" style="width: 100px">*</span>
              </th>
              <td style="width: 400px">
                <input
                  type="text"
                  class="inputTxt p100"
                  name="loginId"
                  id="loginId"
                  v-model="loginId"
                />
              </td>
            </tr>
            <tr>
              <th scope="row">
                제목 <span class="font_red" style="width: 100px">*</span>
              </th>
              <td style="width: 400px">
                <input
                  type="text"
                  class="inputTxt p100"
                  name="noticeTitle"
                  id="noticeTitle"
                  v-model="noticeTitle"
                />
              </td>
            </tr>
            <tr>
              <th scope="row" style="width: 100px">내용</th>
              <td style="width: 400px">
                <textarea
                  class="inputTxt p100"
                  name="noticeContent"
                  id="noticeContent"
                  v-model="noticeContent"
                >
                </textarea>
              </td>
            </tr>
            <tr>
              <th scope="row" style="width: 100px">파일</th>
              <td style="width: 400px">
                <input
                  type="file"
                  id="regfile"
                  name="regfile"
                  @change="selfile"
                />
              </td>
            </tr>
            <tr>
              <th scope="row" style="width: 100px">미리보기</th>
              <td style="width: 400px">
                <div v-html="preview"></div>
              </td>
            </tr>
            <tr>
              <th scope="row" style="width: 100px">Uploaded</th>
              <td style="width: 400px">
                <div v-html="orgpreview"></div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- e : 여기에 내용입력 -->

        <div class="btn_areaC mt30">
          <a
            class="btn btn-primary"
            id="btnUpdateNotice"
            name="btn"
            @click="save()"
            ><span>{{ btndis }}</span></a
          >
          <a
            class="btn btn-danger mx-2"
            id="btnDeleteNotice"
            name="btn"
            v-show="delshow"
            @click="deletenoice()"
            ><span>삭제</span></a
          >
          <a
            class="btn btn-light mx-2"
            id="btnClose"
            name="btn"
            @click="close()"
            ><span>취소</span></a
          >
        </div>
      </dd>
    </dl>
  </form>
</template>
<script>
import { closeModal } from 'jenesius-vue-modal';

export default {
  props: {
    title: String,
    noticeNo: Number,
    action: String,
  },
  data: function () {
    return {
      items: [],
      ptitle: '',
      pnoticeNo: 0,
      paction: '',
      delshow: false,
      loginId: '',
      noticeTitle: '',
      noticeContent: '',
      btndis: '',
      preview: '',
      orgpreview: '',
    };
  },
  mounted() {
    let loginInfo = this.$store.state.loginInfo;

    console.log(loginInfo.loginId);

    this.ptitle = this.title;
    this.pnoticeNo = this.noticeNo;
    this.paction = this.action;

    if (this.paction == 'I') {
      this.delshow = false;
      this.loginId = loginInfo.loginId;
      this.btndis = '저장';
    } else {
      this.delshow = true;
      this.btndis = '수정';

      let vm = this;

      let params = new URLSearchParams();
      params.append('noticeNo', this.pnoticeNo);

      this.axios
        .post('/system/detailNotice.do', params)
        .then(function (response) {
          //console.log(response);
          //console.log(response.data.notice);
          //console.log(response.data.noticeCnt);
          vm.loginId = response.data.result.loginId;
          vm.noticeTitle = response.data.result.noticeTitle;
          vm.noticeContent = response.data.result.noticeContent;
        })
        .catch(function (error) {
          alert('에러! API 요청에 오류가 있습니다. ' + error);
        });
    }
  },
  methods: {
    save: function () {
      let formData = new FormData(document.getElementById('noticeform'));
      formData.append('action', this.paction);
      this.axios
        .post('/system/noticeSave.do', formData)
        .then((resp) => {
          if (resp.status == 200) {
            alert('정상적으로 등록되었습니다');
            closeModal();
          }
        })
        .catch(function (error) {
          alert('에러! API 요청에 오류가 있습니다. ' + error);
        });

      //let vm = this;
      // { action:"I", noticeNo: "11", loginId : "aaa", noticeTitle : "sss", noticeContent : "ddd"}
      /* 
      let params = new URLSearchParams();
      params.append('action', this.paction);
      params.append('noticeNo', this.pnoticeNo);
      params.append('loginId', this.loginId);
      params.append('noticeTitle', this.noticeTitle);
      params.append('noticeContent', this.noticeContent);

      this.axios
        .post('/system/noticeSave.do', params)
        .then(function (response) {
          console.log(response);
          if (
            response.data.resultMsg == 'SUCCESS' ||
            response.data.resultMsg == 'UPDATED'
          ) {
            alert('저장 되었습니다.');
            closeModal();
          }

          //console.log(response.data.notice);
          //console.log(response.data.noticeCnt);
          //vm.loginId = response.data.result.loginId;
          //vm.noticeTitle = response.data.result.noticeTitle;
          //vm.noticeContent = response.data.result.noticeContent;
        })
        .catch(function (error) {
          alert('에러! API 요청에 오류가 있습니다. ' + error);
        });

         */
    },
    deletenoice: function () {
      //let vm = this;
      // { action:"I", noticeNo: "11", loginId : "aaa", noticeTitle : "sss", noticeContent : "ddd"}

      if (confirm('정말 삭제하겠습니까? \n 삭제시 복구불가합니다.')) {
        let params = new URLSearchParams();
        params.append('noticeNo', this.pnoticeNo);
        params.append('action', 'D');

        this.axios
          .post('/system/noticeDelete.do', params)
          .then(function (response) {
            console.log(response);

            if (response.data.result == 'SUCCESS') {
              alert(response.data.resultMsg);
              closeModal();
            } else {
              alert('실패 했습니다.');
            }
          })
          .catch(function (error) {
            alert('에러! API 요청에 오류가 있습니다. ' + error);
          });
      }
    },
    close: function () {
      closeModal();
    },
    selfile: function (event) {
      let filetag = event.target;

      if (filetag.files[0]) {
        //alert(window.URL.createObjectURL(image.files[0]));
        //this.imgpath = window.URL.createObjectURL(image.files[0]);
        let filePath = filetag.value;

        //전체경로를 \ 나눔.
        let filePathSplit = filePath.split('\\');

        //전체경로를 \로 나눈 길이.
        let filePathLength = filePathSplit.length;
        //마지막 경로를 .으로 나눔.
        let fileNameSplit = filePathSplit[filePathLength - 1].split('.');
        //파일명 : .으로 나눈 앞부분
        let fileName = fileNameSplit[0];
        //파일 확장자 : .으로 나눈 뒷부분
        let fileExt = fileNameSplit[1];
        //파일 크기
        let fileSize = filetag.files[0].size;

        console.log('파일 경로 : ' + filePath);
        console.log('파일명 : ' + fileName);
        console.log('파일 확장자 : ' + fileExt);
        console.log('파일 크기 : ' + fileSize);

        if (
          fileExt == 'jpg' ||
          fileExt == 'png' ||
          fileExt == 'gif' ||
          fileExt == 'jpeg'
        ) {
          this.preview =
            "<img src='" +
            window.URL.createObjectURL(filetag.files[0]) +
            "'  style='width: 100px; height: 100px;' />";
        } else {
          this.preview = '';
        }
      }
    },
  },
};
</script>
