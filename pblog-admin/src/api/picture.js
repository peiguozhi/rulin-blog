import request from '@/utils/request'

export function fetchPic(params) {
  return request({
    url: '/system/sysPic/list',
    method: 'get',
    params: params
  })
}

export function addPicture(data) {
  return request({
    url: '/system/sysPic/add',
    method: 'post',
    data
  })
}

export function deletePicture(data) {
  return request({
    url:'/system/sysPic/deleteBatch',
    method: 'post',
    data
  })
}

export function setCover(params) {
  return request({
    url:'/picture/setCover',
    method: 'post',
    data: params
  })
}

