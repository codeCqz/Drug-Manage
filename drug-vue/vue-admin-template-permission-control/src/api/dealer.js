import request from '@/utils/request'

export function fetchDeriveList(query) {
  return request({
    url: '/dealer/getderivedealer',
    method: 'get',
    params: {query}
  })
}


export function fetchList(query) {
  return request({
    url: '/dealer/getalldealer',
    method: 'get',
    params: {query}
  })
}


export function fetchSearchList(query) {
  return request({
    url: '/dealer/getsearchdealer',
    method: 'get',
    params: {query}
  })
}


export function editDealer(data) {
  return request({
    url: '/dealer/editdealer',
    method: 'get',
    params: {data}
  })
}


export function insertDealer(data) {
  return request({
    url: '/dealer/insertdealer',
    method: 'get',
    params: {data}
  })
}

export function insertManyDealer(data) {
  return request({
    url: '/dealer/insertmanydealer',
    method: 'get',
    params: {data}
  })
}


export function deleteDealer(data) {
  return request({
    url: '/dealer/deletedealer',
    method: 'get',
    params: {data}
  })
}





