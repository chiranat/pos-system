import { defineStore } from 'pinia'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useMenuStore = defineStore('menu', {
  state: () => ({
    categories: [],
    products: [],
    loading: false,
    error: null
  }),
  
  getters: {
    productsByCategory: (state) => (categoryId) => {
      return state.products.filter(p => p.category?.id === categoryId)
    }
  },
  
  actions: {
    async fetchMenu() {
      this.loading = true
      try {
        const [categoriesRes, productsRes] = await Promise.all([
          axios.get(`${API_URL}/api/public/menu/categories`),
          axios.get(`${API_URL}/api/public/menu/products`)
        ])
        
        this.categories = categoriesRes.data
        this.products = productsRes.data
      } catch (error) {
        this.error = 'Failed to load menu'
        console.error(error)
      } finally {
        this.loading = false
      }
    }
  }
})
