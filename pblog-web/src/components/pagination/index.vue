<template>
    <div class=''>
        <!-- 分页按钮 -->
        <div class="onPage" v-if="pageNo < pages">
            加载更多
        </div>
        <div style="text-align: center;color: #909399;" v-else>
            我也是有底线的--
        </div>
    </div>
</template>

<script>

export default {
    props: {
        pageNo: {
            type: Number,
            default: 0
        },
        pages: {
            type: Number,
            default: 0
        }
    },
    name: '',
    data() {
        return {
        }
    },
    created() {
        // console.log('------created--------');
    },
    methods: {

    },
}
</script>

<style lang='scss' scoped>
.onPage {
    text-align: center;
    background-color: rgba(0, 0, 0, .8);
    width: 120px;
    height: 30px;
    line-height: 30px;
    border-radius: 50px;
    margin: 0 auto;
    color: #fff;
}
</style>
