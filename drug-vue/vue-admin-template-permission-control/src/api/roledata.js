import request from '@/utils/request'

export function getBuyerUsers(query) {
  return request({
    url: '/roledata/getroleuser',
    method: 'get',
    params: {query}
  })
}
export function insertUser(query) {
  return request({
    url: '/roledata/insertroleuser',
    method: 'get',
    params: {query}
  })
}
export function updateUser(query) {
  return request({
    url: '/roledata/updateroleuser',
    method: 'get',
    params: {query}
  })
}
export function fetchroleInfoList() {
  return request({
    url: '/roledata/getallrole',
    method: 'get',
  })
}
export function deleteUser(query) {
  return request({
    url: '/roledata/deleteuser',
    method: 'get',
    params: {query}
  })
}
export function getSearchUser(query) {
  return request({
    url: '/roledata/search',
    method: 'get',
    params: {query}
  })
}