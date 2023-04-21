import request from '@/utils/request'

export function fetchDataList(params) {
  return request({
    url: '/system/dictData/list',
    method: 'get',
    params: params
  })
}
export function addDictData(data) {
  return request({
    url: '/system/dictData/add',
    method: 'post',
    data
  })
}
export function updateDictData(data) {
  return request({
    url: '/system/dictData/update',
    method: 'post',
    data
  })
}
export function deleteDictData(id) {
  return request({
    url: '/system/dictData/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}
export function deleteBatchDictData(data) {
  return request({
    url: '/system/dictData/deleteBatch',
    method: 'delete',
    data
  })
}
export function getDataByDictType(data) {
  return request({
    url: '/system/dictData/getDataByDictType',
    method: 'post',
    data
  })
}
