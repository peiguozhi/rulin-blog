<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import {report} from '@/api/home'
export default {
  name: 'App',
  created() {
      // 上传访客信息
      report();
  },
}
</script>
