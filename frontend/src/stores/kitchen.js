import { defineStore } from 'pinia'
import axios from 'axios'
import { useAuthStore } from './auth'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useKitchenStore = defineStore('kitchen', {
  state: () => ({
    orders: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchActiveOrders() {
      const authStore = useAuthStore()
      this.loading = true
      try {
        const response = await axios.get(`${API_URL}/api/orders/active`, {
          headers: { Authorization: `Bearer ${authStore.token}` }
        })
        this.orders = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch orders'
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    async updateOrderStatus(orderId, status) {
      const authStore = useAuthStore()
      try {
        await axios.put(`${API_URL}/api/orders/${orderId}/status`, JSON.stringify(status), {
          headers: { 
            Authorization: `Bearer ${authStore.token}`,
            'Content-Type': 'application/json'
          }
        })
        // Refresh orders
        await this.fetchActiveOrders()
      } catch (error) {
        console.error('Failed to update status:', error)
        throw error
      }
    }
  }
})
