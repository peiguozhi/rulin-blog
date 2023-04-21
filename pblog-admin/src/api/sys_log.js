import request from '@/utils/request'

export function fetchUserLog(params) {
  return request({
    url: '/system/userLog/list',
    method: 'get',
    params
  })
}
export function deleteUserLog(data) {
  return request({
    url: '/system/userLog/delete',
    method: 'delete',
    data
  })
}
export function fetchAdminLog(params) {
  return request({
    url: '/system/adminLog/list',
    method: 'get',
    params
  })
}
export function deleteAdminLog(data) {
  return request({
    url: '/system/adminLog/delete',
    method: 'delete',
    data
  })
}
export function fetchExceptionLog(params) {
  return request({
    url: '/system/exceptionLog/list',
    method: 'get',
    params
  })
}
export function deleteExceptionLog(data) {
  return request({
    url: '/system/exceptionLog/delete',
    method: 'delete',
    data
  })
}
