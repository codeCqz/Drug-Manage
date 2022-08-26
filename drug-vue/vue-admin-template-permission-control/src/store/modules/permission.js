import { asyncRoutes, constantRoutes } from '@/router'
import { getRouters } from '@/api/role.js'
import Layout from '@/layout'



// 映射路由表
const componentsMap = {
    '/views/dashboard/index': () =>
        import ('@/views/dashboard/index'),
    '/views/user/buyer/index': () =>
        import ('@/views/user/buyer/index'),
    '/views/user/picker/index': () =>
        import ('@/views/user/picker/index'),
    '/views/buyer/dealer/index': () =>
        import ('@/views/buyer/dealer/index'),
    '/views/buyer/upload/index': () =>
        import ('@/views/buyer/upload/index'),
    '/views/purchase/index': () =>
        import ('@/views/purchase/index'),
    '/views/profile/index': () =>
        import ('@/views/profile/index'),
    '/views/purchase/upload/index': () =>
        import ('@/views/purchase/upload/index'),
    '/views/picker/index': () =>
        import ('@/views/picker/index'),
    '/views/enterdrug/index': () =>
        import ('@/views/enterdrug/index'),
    '/views/repo/index': () =>
        import ('@/views/repo/index'),
    '/views/information/index': () =>
        import ('@/views/information/index'),
    '/views/stock/index': () =>
        import ('@//views/stock/index')
};
/**
 * 把后台返回菜单组装成routes要求的格式
 * @param {*} routes
 */
export function getAsyncRoutes(routes) {
    const res = []
    const keys = ['path', 'name', 'children', 'redirect', 'meta']
    routes.forEach(item => {
        const newItem = {}
        if (item.component) {
            if (item.component == 'Layout') {
                newItem.component = Layout
            } else {
                newItem['component'] = componentsMap[item.component]
            }
        }

        for (const key in item) {
            if (keys.includes(key)) {
                if (item[key]) {
                    newItem[key] = item[key]
                }
            }
        }

        const metaKeys = ['title', 'icon']

        const newMeta = {}
        for (const key in item.meta) {
            if (metaKeys.includes(key)) {
                newMeta[key] = item.meta[key]
            }
        }
        newItem.meta = newMeta

        if (newItem.children) {
            newItem.children = getAsyncRoutes(item.children)
        }
        res.push(newItem)
    })
    return res
}






/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
    if (route.meta && route.meta.roles) {
        return roles.some(role => route.meta.roles.includes(role))
    } else {
        return true
    }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
    const res = []

    routes.forEach(route => {
        const tmp = {...route }
        if (hasPermission(roles, tmp)) {
            if (tmp.children) {
                tmp.children = filterAsyncRoutes(tmp.children, roles)
            }
            res.push(tmp)
        }
    })

    return res
}

const state = {
    routes: [],
    addRoutes: []
}

const mutations = {
    SET_ROUTES: (state, routes) => {
        state.addRoutes = routes //路由访问
        state.routes = constantRoutes.concat(routes) //菜单显示
    }
}

const actions = {
    generateRoutes({ commit }, roles) {
        return new Promise(async resolve => {
            getRouters().then(response => {
                const { data } = response
                // 获取到后台路由

                const asyncRoutes = getAsyncRoutes(data.router) // 对路由格式进行处理

                const accessRoutes = filterAsyncRoutes(asyncRoutes, roles)

                commit('SET_ROUTES', accessRoutes)


                resolve(accessRoutes)
            })


        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}