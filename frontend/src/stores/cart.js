import { defineStore } from 'pinia'
import axios from 'axios'
import { useCustomerStore } from './customer'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    loading: false,
    error: null
  }),
  
  getters: {
    totalItems: (state) => state.items.reduce((total, item) => total + item.quantity, 0),
    totalPrice: (state) => state.items.reduce((total, item) => total + (item.price * item.quantity), 0)
  },
  
  actions: {
    addToCart(product) {
      const existingItem = this.items.find(item => item.id === product.id)
      if (existingItem) {
        existingItem.quantity++
      } else {
        this.items.push({ ...product, quantity: 1 })
      }
    },
    
    removeFromCart(productId) {
      const index = this.items.findIndex(item => item.id === productId)
      if (index !== -1) {
        this.items.splice(index, 1)
      }
    },
    
    updateQuantity(productId, delta) {
      const item = this.items.find(item => item.id === productId)
      if (item) {
        item.quantity += delta
        if (item.quantity <= 0) {
          this.removeFromCart(productId)
        }
      }
    },
    
    clearCart() {
      this.items = []
    },

    async placeOrder() {
      const customerStore = useCustomerStore()
      this.loading = true
      this.error = null
      
      if (!customerStore.token) {
        this.error = 'No session token found'
        this.loading = false
        throw new Error('No session token found')
      }

      const payload = {
        token: customerStore.token,
        items: this.items.map(item => ({
          productId: item.id,
          quantity: item.quantity,
          notes: item.notes || ''
        }))
      }

      try {
        const response = await axios.post(`${API_URL}/api/public/orders`, payload)
        this.clearCart()
        return response.data
      } catch (error) {
        console.error('Failed to place order:', error)
        if (error.response) {
            console.error('Error response:', error.response.data)
            console.error('Error status:', error.response.status)
        }
        this.error = error.response?.data?.message || error.response?.data?.error || 'Failed to place order'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})
