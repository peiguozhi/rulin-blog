import request from '@/utils/request'

export function fetchPhoto(params) {
  return request({
    url: '/system/photo/list',
    method: 'get',
    params
  })
}
export function infoPhoto(id) {
  return request({
    url: '/system/photo/info',
    method: 'get',
    params:{
      id:id
    }
  })
}
export function addPhoto(data) {
  return request({
    url: '/system/photo/add',
    method: 'post',
    data
  })
}
export function updatePhoto(data) {
  return request({
    url: '/system/photo/update',
    method: 'post',
    data
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/photo/deleteBatch',
    method: 'delete',
    data
  })
}
export function movePhoto(data) {
  return request({
    url: '/system/photo/movePhoto',
    method: 'post',
    data
  })
}

