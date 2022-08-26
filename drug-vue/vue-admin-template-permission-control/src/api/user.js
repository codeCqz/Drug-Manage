import request from '@/utils/request'

export function getText() {
  return request({
    url: '/captcha/getText',
    method: 'get',
  })
}


export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    },
    transformRequest: [
      function(data) {
        var ret = ''
        for (var it in data) {
          ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
        }
        ret = ret.substring(0, ret.lastIndexOf('&'))
        return ret
      }
    ],
    data
  })
}

export function getInfo() {
  return request({
    url: '/auth/getInfo',
    method: 'get',
  })
}


export function update(data) {
  return request({
    url: '/auth/update',
    method: 'get',
    params: {data}
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}


