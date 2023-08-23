<template>
    <!-- 动画节点 -->
    <div id="loader-wrapper" v-if="isLoading">
        <div id="loader"></div>
        <div class="load_title">正在拼命加载中,请耐心等待....</div>
    </div>
</template>
<script>
export default {
    data() {
        return {
            isLoading: false
        };
    },
    mounted() {
        this.$bus.$on('showLoading', () => {
            this.isLoading = true;
            var mo = function (e) { e.preventDefault(); };
            document.body.style.overflow = 'hidden';
            document.addEventListener("touchmove", mo, false);//禁止页面滑动}, false)
        });
        this.$bus.$on('hideLoading', () => {
            this.isLoading = false;
            var mo = function (e) { e.preventDefault(); };
            document.body.style.overflow = '';//出现滚动条
            document.removeEventListener("touchmove", mo, false);
        });
    },
    beforeDestroy() {
        this.$bus.$off('showLoading');
        this.$bus.$off('hideLoading');
    }
};
</script>
<style lang="scss">
#loader-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 999999;
    background-color: rgba(0, 0, 0, 0.8);

    #loader {
        display: block;
        position: relative;
        left: 50%;
        top: 50%;
        width: 100px;
        height: 100px;
        margin: -75px 0 0 -75px;
        border-radius: 50%;
        border: 3px solid transparent;
        border-top-color: #409eff;
        -webkit-animation: spin 2s linear infinite;
        -ms-animation: spin 2s linear infinite;
        -moz-animation: spin 2s linear infinite;
        -o-animation: spin 2s linear infinite;
        animation: spin 2s linear infinite;
        z-index: 1001;

        &:before {
            content: "";
            position: absolute;
            top: 5px;
            left: 5px;
            right: 5px;
            bottom: 5px;
            border-radius: 50%;
            border: 3px solid transparent;
            border-top-color: #9999FFFF;
            -webkit-animation: spin 3s linear infinite;
            -moz-animation: spin 3s linear infinite;
            -o-animation: spin 3s linear infinite;
            -ms-animation: spin 3s linear infinite;
            animation: spin 3s linear infinite;
        }

        &:after {
            content: "";
            position: absolute;
            top: 15px;
            left: 15px;
            right: 15px;
            bottom: 15px;
            border-radius: 50%;
            border: 3px solid transparent;
            border-top-color: #409eff;
            -moz-animation: spin 1.5s linear infinite;
            -o-animation: spin 1.5s linear infinite;
            -ms-animation: spin 1.5s linear infinite;
            -webkit-animation: spin 1.5s linear infinite;
            animation: spin 1.5s linear infinite;
        }
    }

    .load_title {
        font-family: 'Open Sans';
        color: #ccc;
        font-size: 1rem;
        width: 100%;
        text-align: center;
        position: absolute;
        top: 55%;
        line-height: 30px;
    }

}

@-webkit-keyframes spin {
    0% {
        -webkit-transform: rotate(0deg);
        -ms-transform: rotate(0deg);
        transform: rotate(0deg);
    }

    100% {
        -webkit-transform: rotate(360deg);
        -ms-transform: rotate(360deg);
        transform: rotate(360deg);
    }
}

@keyframes spin {
    0% {
        -webkit-transform: rotate(0deg);
        -ms-transform: rotate(0deg);
        transform: rotate(0deg);
    }

    100% {
        -webkit-transform: rotate(360deg);
        -ms-transform: rotate(360deg);
        transform: rotate(360deg);
    }
}
</style>

