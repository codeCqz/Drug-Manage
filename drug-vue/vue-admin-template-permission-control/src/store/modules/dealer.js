import { editDealer,insertDealer,deleteDealer} from '@/api/dealer'

const getDefaultState = () => {
    return {
      
    }
  }
  
  const state = getDefaultState()
  
  const mutations = {

    SET_TEXT:(state,text)=>{
      state.text = text
    }
  }
  
  const actions = {


    editDealer({ commit }, dealerInfo) {
      const data = dealerInfo
      return new Promise((resolve, reject) => {
        editDealer(data).then(response => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },



    insertDealer({ commit }, dealerInfo) {
      const data = dealerInfo
      return new Promise((resolve, reject) => {
        insertDealer(data).then(response => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
   
    deleteDealer({ commit }, dealerId) {
      const data = dealerId
      return new Promise((resolve, reject) => {
        deleteDealer(data).then(response => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

  }
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions
  }
  
  