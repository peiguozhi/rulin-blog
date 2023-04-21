import { login, logout, getInfo,getMenuTree } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, {asyncRoutes, resetRouter} from '@/router'
import store from "@/store";

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    role: [],
    routes: [],
    pres: [],
    avatar: ''
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
  SET_ROLE: (state, role) => {
    state.role = role
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
  SET_PRES: (state, btnList) => {
    state.pres = btnList
  }
}

const actions = {
  // user login
  login({ commit }, res) {
    // const { username, password,code,uuid} = userInfo
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', res.token)
      commit('SET_ROLE', 'admin')
      setToken(res.token)
      resolve(res)
    })
  },
  getMenu({ commit, state }) {
    return new Promise((resolve, reject) => {
      getMenuTree().then(response => {
        let menuArr = response.data
        commit('SET_ROUTES', menuArr)
        let btnList =[]
        menuArr.forEach(item =>{
          item.children.forEach(item1 =>{
            let children = item1.children;
            if (children != null){
              item1.children.forEach(item2 =>{
                btnList.push(item2.url)
              })
            }

          })
        })
        commit('SET_PRES', btnList)
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        if (!data) {
          return reject('Verification failed, please Login again.')
        }
        const { username, avatar } = data
        commit('SET_NAME', username)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
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

