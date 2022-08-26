import request from '@/utils/request'

export function fetchpickdrug() {
    return request({
        url: '/api/getbardata',
        method: 'get',
    })
}

export function insertpickdrug(query) {
    return request({
        url: '/pickdrug/insert',
        method: 'get',
        params: { query }
    })
}

export function getprofit() {
    return request({
        url: '/getprofit',
        method: 'get',
    })
}

export function updatepickdrug(query) {
    return request({
        url: '/pickdrug/update',
        method: 'get',
        params: { query }
    })
}