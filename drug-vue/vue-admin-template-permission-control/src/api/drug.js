import request from '@/utils/request'

export function fetchdrugnameList() {
  return request({
    url: '/drug/getalldrugname',
    method: 'get',
  })
}

export function fetchbydrugname(query) {
  return request({
    url: '/drug/getbydrugname',
    method: 'get',
    params: {query}
  })
}

export function getdruginfo(query) {
  return request({
    url: '/drug/getdruginfo',
    method: 'get',
    params: {query}
  })
}
export function getdrug(query) {
  return request({
    url: '/drug/getinsertdrug',
    method: 'get',
    params: {query}
  })
}
export function getsearch(query) {
  return request({
    url: '/drug/search',
    method: 'get',
    params: {query}
  })
}

export function insertdrug(query) {
  return request({
    url: '/drug/insertdrug',
    method: 'get',
    params: {query}
  })
}

export function deleteDrug(query) {
  return request({
    url: '/drug/deletedrug',
    method: 'get',
    params: {query}
  })
}