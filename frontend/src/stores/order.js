import { defineStore } from 'pinia'
import axios from 'axios'
import { useCustomerStore } from './customer'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useOrderStore = defineStore('order', {
  state: () => ({
    orders: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchMyOrders() {
      const customerStore = useCustomerStore()
      if (!customerStore.token) return

      this.loading = true
      try {
        const response = await axios.get(`${API_URL}/api/public/orders/my-orders`, {
          params: { token: customerStore.token }
        })
        this.orders = response.data
      } catch (error) {
        console.error('Failed to fetch orders:', error)
        this.error = error.response?.data?.message || 'Failed to fetch orders'
      } finally {
        this.loading = false
      }
    }
  }
})
