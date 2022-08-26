import request from "@/utils/request";

export function fetchstoragedrugnameList() {
    return request({
        url: '/storage/getallstoragedrugname',
        method: 'get',
    })
}

export function fetchstoragenumberList(query) {
    return request({
        url: '/storage/getnumanddrugname',
        method: 'get',
        params: { query }
    })
}
export function fetchstorageList(query) {
    return request({
        url: '/storage/getallstock',
        method: 'get',
        params: { query }
    })
}

export function fetchstorageSearch(query) {
    return request({
        url: '/storage/getsearchstorage',
        method: 'get',
        params: { query }
    })
}

export function fetchdrugnameList(query) {
    return request({
        url: '/storage/getdrugnamelist',
        method: 'get',
        params: { query }
    })
}

export function updateStorage(query) {
    return request({
        url: '/storage/updatestorage',
        method: 'get',
        params: { query }
    })
}


export function insertStorage(query) {
    return request({
        url: '/storage/insertstorage',
        method: 'get',
        params: { query }
    })
}

export function deleteStorage(query) {
    return request({
        url: '/storage/deletestorage',
        method: 'get',
        params: { query }
    })
}

export function fetchExpList(query) {
    return request({
        url: '/storage/getallexp',
        method: 'get',
        params: { query }
    })
}

export function fetchexpnum(query) {
    return request({
        url: '/storage/fetchexpnum',
        method: 'get',
        params: { query }
    })
}

export function fetchprice(query) {
    return request({
        url: '/storage/fetchprice',
        method: 'get',
        params: { query }
    })
}