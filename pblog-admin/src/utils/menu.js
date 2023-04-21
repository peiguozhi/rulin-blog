import { getToken } from '@/utils/auth'
import Layout from '@/layout'

export function arraySort(property) {
  return function(a, b) {
    const value1 = a[property]
    const value2 = b[property]
    return value1 - value2
  }
}
export function filterAsyncRouter(data) {
  data.sort(arraySort('sortNo'))
  data.forEach(item => item.children.sort(arraySort('sortNo')))
  const menus = []
  data.forEach(item => {
    if (item.type !== 'btn') {
      let hidden = false
      if (item.hidden === 0) { hidden = true }
      const menu = {
        path: item.url,
        children: [],
        name: item.name,
        // isShow: hidden,
        hidden: hidden,
        sortNo: item.sortNo,
        meta: { title: item.title, keepAlive: true }
      }
      if (item.url.indexOf('token') >= 0) {
        menu.path = item.url + getToken()
      }
      if (item.component != null && item.component !== '') {
        menu.component = item.component === 'Layout' ? Layout : resolve => require([`@/views${item.component}`], resolve)
      }
      if (item.redirect != null && item.redirect !== '') {
        menu.redirect = item.redirect
      }
      if (item.icon != null && item.icon !== '') {
        menu.meta.icon = item.icon
      } else {
        menu.meta.icon = 'task'
      }
      if (item.component === 'Layout') {
        generaMenu(menu.children, item.children)
      }
      menus.push(menu)
    }
  })
  menus.push({ path: '*', redirect: '/404', hidden: true })
  return menus
}
export function generaMenu(routes, data) {
  data.forEach(item => {
    if (item.type !== 'btn') {
      let hidden = false
      if (item.hidden === 0) { hidden = true }
      const menu = {
        path: item.url,
        children: [],
        name: item.name,
        // isShow: hidden,
        hidden: hidden,
        sortNo: item.sortNo,
        meta: { title: item.title, keepAlive: true }
      }
      if (item.url.indexOf('token') >= 0) {
        menu.path = item.url + getToken()
      }
      if (item.component != null && item.component !== '') {
        menu.component = item.component === 'Layout' ? Layout : resolve => require([`@/views${item.component}`], resolve)
      }
      if (item.redirect != null && item.redirect !== '') {
        menu.redirect = item.redirect
      }
      if (item.icon != null && item.icon !== '') {
        menu.meta.icon = item.icon
      } else {
        menu.meta.icon = 'task'
      }
      if (item.component === 'Layout') {
        generaMenu(menu.children, item.children)
      }
      routes.push(menu)
    }
  })
  return routes
}
