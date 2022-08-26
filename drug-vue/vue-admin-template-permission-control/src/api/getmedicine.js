import request from '@/utils/request'

export function allgetmedicinelist(query) {
  return request({
    url: '/getmedicine/allgetmedicinelist',
    method: 'get',
    params: {query}
  })
}

export function fetchGetMedicineList(query) {
  return request({
    url: '/getmedicine/getall',
    method: 'get',
    params: {query}
  })
}


export function deleteGetMedicine(query) {
  return request({
    url: '/getmedicine/delmedicine',
    method: 'get',
    params: {query}
  })
}


export function getsearch(query) {
  return request({
    url: '/getmedicine/getsearch',
    method: 'get',
    params: {query}
  })
}