import request from '@/utils/request'

export function fetchSupplyList(query) {
  return request({
    url: '/supply/getallsupply',
    method: 'get',
    params: {query}
  })
}

export function fetchDealerInfoList() {
  return request({
    url: '/supply/getdealerinfo',
    method: 'get',
  })
}

export function insertSupply(query) {
  return request({
    url: '/supply/insertsupply',
    method: 'get',
    params: {query}
  })
}
export function updateSupplyStatus(query) {
  return request({
    url: '/supply/supplystatus',
    method: 'get',
    params: {query}
  })
}

export function updateSupply(query) {
  return request({
    url: '/supply/updatesupply',
    method: 'get',
    params: {query}
  })
}

export function deleteSupply(query) {
  return request({
    url: '/supply/deletesupply',
    method: 'get',
    params: {query}
  })
}

export function getAllData(query) {
  return request({
    url: '/getdata',
    method: 'get',
    params: {query}
  })
}

export function getDrugClass(query) {
  return request({
    url: '/getdrugclass',
    method: 'get',
    params: {query}
  })
}
export function getDayAdd(query) {
  return request({
    url: '/getdayadd',
    method: 'get',
    params: {query}
  })
}
export function getAllExpire(query) {
  return request({
    url: '/getallexpire',
    method: 'get',
    params: {query}
  })
}

export function getAllCount(query) {
  return request({
    url: '/getallcount',
    method: 'get',
    params: {query}
  })
}


export function getRadder() {
  return request({
    url: '/getradder',
    method: 'get',
  })
}

export function getAllPieData() {
  return request({
    url: '/getpiedata',
    method: 'get',
  })
}

export function getsearchsupply(query) {
  return request({
    url: '/supply/getsearchsupply',
    method: 'get',
    params: {query}
  })
}
export function getlinedata() {
  return request({
    url: '/getadmindataone',
    method: 'get',
  })
}