import request from '@/utils/request'

// 留言管理
export function fetchMessage(params) {
  return request({
    url: '/system/message/list',
    method: 'get',
    params: params
  })
}
export function passBatch(data) {
  return request({
    url: '/system/message/passBatch',
    method: 'post',
    data
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/message/deleteBatch',
    method: 'delete',
    data
  })
}
export function remove(id) {
  return request({
    url: '/system/message/remove',
    method: 'delete',
    params: { id: id }
  })
}
