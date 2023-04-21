import request from '@/utils/request'

export function fetchPages() {
  return request({
    url: '/system/page/list',
    method: 'get',
    params: {}
  })
} export function addPage(data) {
  return request({
    url: '/system/page/add',
    method: 'post',
    data
  })
} export function updatePage(data) {
  return request({
    url: '/system/page/update',
    method: 'post',
    data
  })
} export function removePage(id) {
  return request({
    url: '/system/page/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}
