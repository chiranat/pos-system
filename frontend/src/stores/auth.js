import { defineStore } from 'pinia'
import axios from 'axios'
import { jwtDecode } from 'jwt-decode'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// Initialize axios header if token exists
const token = localStorage.getItem('token')
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user')) || null,
    error: null
  }),
  
  getters: {
    isAuthenticated: (state) => !!state.token,
    currentUser: (state) => state.user,
    userRole: (state) => state.user?.role
  },
  
  actions: {
    async login(username, password) {
      try {
        this.error = null
        const response = await axios.post(`${API_URL}/api/auth/login`, {
          username,
          password
        })
        
        const { token, role } = response.data
        
        this.token = token
        this.user = { username, role }
        
        localStorage.setItem('token', token)
        localStorage.setItem('user', JSON.stringify(this.user))
        
        // Set default axios header
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
        
        return true
      } catch (err) {
        this.error = err.response?.data?.message || 'Login failed'
        return false
      }
    },
    
    async register(userData) {
      try {
        this.error = null
        await axios.post(`${API_URL}/api/auth/register`, userData)
        return true
      } catch (err) {
        this.error = err.response?.data?.message || 'Registration failed'
        return false
      }
    },
    
    logout() {
      this.token = null
      this.user = null
      this.error = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      delete axios.defaults.headers.common['Authorization']
    }
  }
})
