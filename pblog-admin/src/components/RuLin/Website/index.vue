<template>
  <div>
    <svg-icon class="websiteSvg" icon-class="website" style="height: 2.5em;font-size: 18px;cursor: pointer" @click.native="goto" />
  </div>
</template>

<script>
export default {
  name: 'RuLinGit',
  data() {
    return {
      url: process.env.VUE_APP_BLOG_WEB_API
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
<style scoped>

</style>
