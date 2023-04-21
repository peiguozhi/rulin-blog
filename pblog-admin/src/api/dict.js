import request from '@/utils/request'

export function fetchDictList(params) {
  return request({
    url: '/system/dict/list',
    method: 'get',
    params: params
  })
}
export function addDict(data) {
  return request({
    url: '/system/dict/add',
    method: 'post',
    data
  })
}
export function updateDict(data) {
  return request({
    url: '/system/dict/update',
    method: 'post',
    data
  })
}
export function deleteDict(id) {
  return request({
    url: '/system/dict/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}
export function deleteBatchDict(data) {
  return request({
    url: '/system/dict/deleteBatch',
    method: 'delete',
    data
  })
}

