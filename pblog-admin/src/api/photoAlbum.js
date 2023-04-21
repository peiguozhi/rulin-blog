import request from '@/utils/request'

export function fetchAlbum(params) {
  return request({
    url: '/system/album/list',
    method: 'get',
    params
  })
}
export function info(id) {
  return request({
    url: '/system/album/info',
    method: 'get',
    params:{
      id:id
    }
  })
}
export function addAlbum(data) {
  return request({
    url: '/system/album/add',
    method: 'post',
    data
  })
}
export function updateAlbum(data) {
  return request({
    url: '/system/album/update',
    method: 'post',
    data
  })
}
export function remove(id) {
  return request({
    url: '/system/album/delete',
    method: 'delete',
    params:{
      id:id
    }
  })
}
