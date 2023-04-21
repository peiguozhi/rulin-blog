import request from '@/utils/request'

// 面试题分类管理接口 By 程序儒 2023年4月13日 15点23分

// 查询分类列表
export function fetchQCategory(params) {
  return request({
    url: '/system/qCategory/list',
    method: 'GET',
    params: params
  })
}

// 批量删除
export function deleteBatch(data) {
  return request({
    url: '/system/qCategory/deleteBatch',
    method: 'delete',
    data
  })
}

// 删除
export function remove(id) {
  return request({
    url: '/system/qCategory/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}

export function add(data) {
  return request({
    url: '/system/qCategory/add',
    method: 'POST',
    data
  })
}

export function info(id) {
  return request({
    url: '/system/qCategory/info',
    method: 'get',
    params: {
      id: id
    }
  })
}

export function update(data) {
  return request({
    url: '/system/qCategory/update',
    method: 'post',
    data
  })
}
// 置顶
export function top(id) {
  return request({
    url: '/system/qCategory/top',
    method: 'get',
    params: {
      id: id
    }
  })
}
