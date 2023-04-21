import request from '@/utils/request'

export function fetchArticle(data) {
  return request({
    url: '/system/article/list',
    method: 'post',
    data
  })
}
export function info(id) {
  return request({
    url: '/system/article/info',
    method: 'get',
    params: {
      id: id
    }
  })
}
export function save(data) {
  return request({
    url: '/system/article/add',
    method: 'post',
    data
  })
}
export function update(data) {
  return request({
    url: '/system/article/update',
    method: 'post',
    data
  })
}
export function top(data) {
  return request({
    url: '/system/article/top',
    method: 'post',
    data
  })
}
export function pubOrShelf(data) {
  return request({
    url: '/system/article/pubOrShelf',
    method: 'post',
    data
  })
}
export function remove(id) {
  return request({
    url: '/system/article/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}
export function deleteBatch(data) {
  return request({
    url: '/system/article/deleteBatch',
    method: 'delete',
    data
  })
}
export function baiduSeo(data) {
  return request({
    url: '/system/article/baiduSeo',
    method: 'post',
    data
  })
}
export function reptile(url) {
  return request({
    url: '/system/article/reptile',
    method: 'get',
    params: {
      url: url
    }
  })
}
export function randomImg() {
  return request({
    url: '/system/article/randomImg',
    method: 'get',
    params: {}
  })
}
