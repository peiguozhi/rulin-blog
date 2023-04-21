import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/system/picSort/list',
    method: 'get',
    params: params
  })
}

export function addPictureSort(data) {
  return request({
    url: '/system/picSort/add',
    method: 'post',
    data
  })
}

export function editPictureSort(data) {
  return request({
    url: '/system/picSort/update',
    method: 'post',
    data
  })
}

export function deletePictureSort(id) {
  return request({
    url: '/system/picSort/delete',
    method: 'get',
    params: {
      id:id
    }
  })
}

export function stickPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/stick',
    method: 'post',
    data: params
  })
}

export function getPictureSortByUid(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/getPictureSortByUid',
    method: 'post',
    data: params
  })
}
