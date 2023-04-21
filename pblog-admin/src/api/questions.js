import request from '@/utils/request'

export function fetchQuestion(data) {
  return request({
    url: '/system/question/list',
    method: 'post',
    data
  })
}
export function info(id) {
  return request({
    url: '/system/question/info',
    method: 'get',
    params: {
      id: id
    }
  })
}
export function save(data) {
  return request({
    url: '/system/question/add',
    method: 'post',
    data
  })
}
export function update(data) {
  return request({
    url: '/system/question/update',
    method: 'post',
    data
  })
}
export function top(data) {
  return request({
    url: '/system/question/top',
    method: 'post',
    data
  })
}
export function pubOrShelf(data) {
  return request({
    url: '/system/question/pubOrShelf',
    method: 'post',
    data
  })
}
export function remove(id) {
  return request({
    url: '/system/question/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/question/deleteBatch',
    method: 'delete',
    data
  })
}

