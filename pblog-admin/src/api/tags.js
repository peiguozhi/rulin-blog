import request from '@/utils/request'

export function fetchTags(params) {
  return request({
    url: '/system/tags/list',
    method: 'GET',
    params:params
  })
}
export function remove(id) {
  return request({
    url: '/system/tags/remove',
    method: 'delete',
    params:{
      id:id
    }
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/tags/deleteBatch',
    method: 'delete',
    data
  })
}
export function add(data) {
  return request({
    url: '/system/tags/add',
    method: 'POST',
    data
  })
}
export function info(id) {
  return request({
    url: '/system/tags/info',
    method: 'get',
    params:{
      id:id
    }
  })
}
export function update(data) {
  return request({
    url: '/system/tags/update',
    method: 'post',
    data
  })
}
export function top(id) {
  return request({
    url: '/system/tags/top',
    method: 'get',
    params:{
      id:id
    }
  })
}
