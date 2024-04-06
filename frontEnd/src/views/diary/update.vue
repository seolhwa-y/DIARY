<template>
  <!-- 수정 페이지 템플릿 -->
  <div>
    <!-- 등록 페이지와 동일한 컴포넌트 사용 -->
    <DiaryForm :diary="diary" v-model="modifiedDiary" />
  </div>
</template>

<script>
import DiaryForm from "./DiaryInsert.vue"; // 등록 페이지에서 사용한 컴포넌트 import

export default {
  components: {
    DiaryForm, // 등록 페이지에서 사용한 컴포넌트 등록
  },
  props: {
    diary: Object, // 받을 props 타입 설정
  },
  data() {
    return {
      modifiedDiary: {}, // 수정된 일기 정보를 저장할 data 속성
    };
  },
};
</script>
