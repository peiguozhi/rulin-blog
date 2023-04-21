import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function hasAuth(perms, perm) {
  let hasA = false
  perms.forEach(p => {
    if (p.indexOf(perm) !== -1) {
      hasA = true
      return false
    }
  })
  return hasA
}
