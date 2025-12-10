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
        const response = await axios.get(`${API_URL}/api/public/customer/session/${token}`)
        this.session = response.data
        this.token = token
        localStorage.setItem('customer_token', token)
        return true
      } catch (error) {
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
