import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/system/jobLog/list',
    method: 'get',
    params: params
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/jobLog/deleteBatch',
    method: 'post',
    data
  })
}
export function clean() {
  return request({
    url: '/system/jobLog/clean',
    method: 'get',
    params: {}
  })
}
