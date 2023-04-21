import request from '@/utils/request'

export function getWebConfig() {
  return request({
    url: '/system/webConfig/list',
    method: 'get',
    params: {}
  })
}
export function update(data) {
  return request({
    url: '/system/webConfig/update',
    method: 'post',
    data
  })
}
