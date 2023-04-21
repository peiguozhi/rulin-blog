import request from '@/utils/request'

export function getSystemConfig() {
  return request({
    url: '/system/config/getConfig',
    method: 'get',
    params: {}
  })
}
export function updateSystemConfig(data) {
  return request({
    url: '/system/config/update',
    method: 'post',
    data
  })
}
