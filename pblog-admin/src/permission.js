import router from './router'
import store from './store'
import { Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import { filterAsyncRouter } from '@/utils/menu' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

var getRouter
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasGetUserInfo = store.getters.name
      if (hasGetUserInfo) {
        if (!getRouter) {
          if (!global.antRouter) {
            await store.dispatch('user/getMenu') // 请求获取路由信息
            if (store.getters.menu.length < 1) {
              global.antRouter = []
              next()
            }
            const menus = filterAsyncRouter(store.getters.menu) // 1.过滤路由
            router.addRoutes(menus) // 2.动态添加路由
            global.antRouter = menus // 3.将路由数据传递给全局变量，做侧边栏菜单渲染工作
            next({
              ...to,
              replace: true
            })
          }
        } else {
          // getRouter变量存在 说明路由信息存在 直接通过
          next()
        }
        next()
      } else {
        try {
          // get user info
          await store.dispatch('user/getInfo') // 请求获取用户信息
          await store.dispatch('user/getMenu') // 请求获取路由信息
          if (store.getters.menu.length < 1) {
            global.antRouter = []
            next()
          }
          const menus = filterAsyncRouter(store.getters.menu) // 1.过滤路由
          router.addRoutes(menus) // 2.动态添加路由
          global.antRouter = menus // 3.将路由数据传递给全局变量，做侧边栏菜单渲染工作
          next({
            ...to,
            replace: true
          })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error({ message: error || 'Has Error' })
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
