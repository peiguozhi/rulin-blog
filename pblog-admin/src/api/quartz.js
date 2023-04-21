import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/system/job/list',
    method: 'get',
    params:params
  })
}
export function info(id) {
  return request({
    url: '/system/job/info',
    method: 'get',
    params:{
      jobId:id
    }
  })
}
export function add(data) {
  return request({
    url: '/system/job/add',
    method: 'post',
    data
  })
}
export function update(data) {
  return request({
    url: '/system/job/update',
    method: 'post',
    data
  })
}
export function remove(id) {
  return request({
    url: '/system/job/delete',
    method: 'get',
    params:{
      jobId:id
    }
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/job/deleteBatch',
    method: 'post',
    data
  })
}
export function change(data) {
  return request({
    url: '/system/job/change',
    method: 'post',
    data
  })
}
export function run(data) {
  return request({
    url: '/system/job/run',
    method: 'post',
    data
  })
}
