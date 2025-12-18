import { defineStore } from 'pinia'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useCustomerStore = defineStore('customer', {
  state: () => ({
    session: null,
    token: localStorage.getItem('customer_token') || null,
    loading: false,
    error: null
  }),
  
  actions: {
    async validateSession(token) {
      this.loading = true
      this.error = null
      try {
        console.log('Validating session with token:', token)
        console.log('API URL:', API_URL)
        const response = await axios.get(`${API_URL}/api/public/customer/session/${token}`)
        console.log('Session response:', response.data)
        this.session = response.data
        this.token = token
        localStorage.setItem('customer_token', token)
        return true
      } catch (error) {
        console.error('Session validation error:', error)
        this.error = error.response?.data?.message || 'Invalid or expired session'
        this.session = null
        this.token = null
        localStorage.removeItem('customer_token')
        return false
      } finally {
        this.loading = false
      }
    }
  }
})
