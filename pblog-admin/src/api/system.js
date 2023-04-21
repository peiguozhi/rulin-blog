import request from '@/utils/request'

// 菜单管理
export function fetchMenu() {
  return request({
    url: '/system/menu/getMenuTree',
    method: 'get',
    params: {}
  })
}
export function fetchApi(params) {
  return request({
    url: '/system/menu/getMenuApi',
    method: 'get',
    params: params
  })
}
export function createMenu(data) {
  return request({
    url: '/system/menu/create',
    method: 'post',
    data
  })
}
export function updateMenu(data) {
  return request({
    url: '/system/menu/update',
    method: 'post',
    data
  })
}
export function removeMenu(id) {
  return request({
    url: '/system/menu/remove',
    method: 'delete',
    params: { id: id }
  })
}
// 角色管理
export function fetchRole(params) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: params
  })
}
export function queryRoleId(id) {
  return request({
    url: '/system/role/queryRoleId',
    method: 'get',
    params: {
      roleId: id
    }
  })
}
export function updateRole(data) {
  return request({
    url: '/system/role/update',
    method: 'post',
    data
  })
}
export function createRole(data) {
  return request({
    url: '/system/role/create',
    method: 'post',
    data
  })
}
export function removeRole(data) {
  return request({
    url: '/system/role/remove',
    method: 'delete',
    data
  })
}

