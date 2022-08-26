import { getText,login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    text:'',
    name: '',
    avatar: '',
    user:'',
    roles: [],
    role:''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_USER:(state, user)=>{
    state.user = user
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_TEXT:(state,text)=>{
    state.text = text
  },
  SET_ROLE:(state,role)=>{
    state.role = role
  }
}

const actions = {

  getText({ commit }){
    return new Promise((resolve, reject) => {
      getText().then(response=>{
        const {data} = response
        commit('SET_TEXT',data.text)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user login
  login({ commit }, userInfo) {
    const data = userInfo
    return new Promise((resolve, reject) => {
      login(data).then(response => {
        const {data} = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {

        const {data} = response
 
        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const {roles, name, avatar, user,role} = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }
 
        commit('SET_NAME', name)
        

        commit('SET_ROLES', roles)
     
        commit('SET_AVATAR', avatar)

        commit('SET_USER', user)   

        commit('SET_ROLE', role)   
        resolve(data)
        
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state ,dispatch}) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()
        sessionStorage.setItem("tabViews", []);
        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews',  state, { root: true })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit ,dispatch}) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken() // must remove  token  first
      dispatch('tagsView/delAllViews', null, { root: true })
      resetRouter()

      // reset visited views and cached views
      // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

