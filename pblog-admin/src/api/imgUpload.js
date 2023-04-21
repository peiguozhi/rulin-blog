import request from '@/utils/request'

export function upload(data) {
  return request({
    url: '/file/upload',
    method: 'POST',
    headers: { 'Content-Type': 'multipart/articles-data' },
    data
  })
}
export function delBatchFile(key) {
  return request({
    url: '/file/delBatchFile',
    method: 'POST',
    params: {
      key: key
    }
  })
}
