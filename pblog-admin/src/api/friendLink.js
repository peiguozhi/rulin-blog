import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/system/friend/list',
    method: 'get',
    params: params
  })
}
export function update(data) {
  return request({
    url: '/system/friend/update',
    method: 'post',
    data
  })
}
export function create(data) {
  return request({
    url: '/system/friend/create',
    method: 'post',
    data
  })
}
export function remove(data) {
  return request({
    url: '/system/friend/remove',
    method: 'delete',
    data
  })
}
export function top(id) {
  return request({
    url: '/system/friend/top',
    method: 'get',
    params: {
      id: id
    }
  })
}
