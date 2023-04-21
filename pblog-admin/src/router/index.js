import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes friendLink mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard', affix: true }
    }]
  },
  /* {
    path: '/articles',
    component: Layout,
    redirect: '/articles/index',
    name: 'articles',
    meta: { title: '文章管理', icon: 'article' },
    children: [
      {
        path: 'index',
        name: 'articles',
        component: () => import('@/views/articles/index'),
        meta: { title: '文章列表', icon: 'articleList',noCache: true}
      },
      {
        path: 'tags',
        name: 'tags',
        component: () => import('@/views/tags'),
        meta: { title: '标签管理', icon: 'tag',noCache: true }
      }
      /!*{
        path: 'add',
        name: 'add_articles',
        component: () => import('@/views/articles/add'),
        meta: { title: '添加文章', icon: 'form'},
        hidden:true
      },
      {
        path: 'edit',
        name: 'edit_articles',
        component: () => import('@/views/articles/edit'),
        meta: { title: '修改文章', icon: 'form'},
        hidden:true
      }*!/
    ]
  },
  {
    path: '/site',
    component: Layout,
    redirect: '/friendLink/index',
    name: 'site',
    meta: { title: '网站管理', icon: 'site' },
    children: [
      {
        path: 'friendLink',
        name: 'friendLink',
        component: () => import('@/views/friendLink'),
        meta: { title: '友链管理', icon: 'friend',noCache: true }
      },
      {
        path: 'news',
        name: 'news',
        component: () => import('@/views/news'),
        meta: { title: '留言管理', icon: 'news',noCache: true }
      },
      {
        path: 'config',
        name: 'config',
        component: () => import('@/views/system/config'),
        meta: { title: '配置管理', icon: 'config',noCache: true }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/menu',
    name: 'system',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'menu',
        name: 'menu',
        component: () => import('@/views/system/menu'),
        meta: { title: '菜单管理', icon: 'menu',noCache: true}
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/views/system/role'),
        meta: { title: '角色管理', icon: 'role' ,noCache: true}
      },{
        path: 'user',
        name: 'user',
        component: () => import('@/views/system/user'),
        meta: { title: '用户管理', icon: 'user',noCache: true }
      },

    ]
  },*/
  // 404 page must be placed at the end !!!
  {
    path: '*',
    redirect: '/404',
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  }
]

/* 动态路由 */
export const asyncRoutes = []

const createRouter = () => new Router({
  // mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
