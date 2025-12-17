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
    },

    // Admin Actions
    async createCategory(category) {
      try {
        const response = await axios.post(`${API_URL}/api/admin/menu/categories`, category)
        this.categories.push(response.data)
        return response.data
      } catch (err) {
        throw err
      }
    },

    async updateCategory(id, category) {
      try {
        const response = await axios.put(`${API_URL}/api/admin/menu/categories/${id}`, category)
        const index = this.categories.findIndex(c => c.id === id)
        if (index !== -1) {
          this.categories[index] = response.data
        }
        return response.data
      } catch (err) {
        throw err
      }
    },

    async deleteCategory(id) {
      try {
        await axios.delete(`${API_URL}/api/admin/menu/categories/${id}`)
        this.categories = this.categories.filter(c => c.id !== id)
      } catch (err) {
        throw err
      }
    },

    async createProduct(product) {
      try {
        const response = await axios.post(`${API_URL}/api/admin/menu/products`, product)
        this.products.push(response.data)
        return response.data
      } catch (err) {
        throw err
      }
    },

    async updateProduct(id, product) {
      try {
        const response = await axios.put(`${API_URL}/api/admin/menu/products/${id}`, product)
        const index = this.products.findIndex(p => p.id === id)
        if (index !== -1) {
          this.products[index] = response.data
        }
        return response.data
      } catch (err) {
        throw err
      }
    },

    async deleteProduct(id) {
      try {
        await axios.delete(`${API_URL}/api/admin/menu/products/${id}`)
        this.products = this.products.filter(p => p.id !== id)
      } catch (err) {
        throw err
      }
    },

    async uploadImage(file) {
      try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await axios.post(`${API_URL}/api/admin/upload`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        return response.data.url
      } catch (err) {
        throw err
      }
    },

    async fetchImages() {
      try {
        const response = await axios.get(`${API_URL}/api/admin/upload`)
        return response.data
      } catch (err) {
        throw err
      }
    },

    async deleteImage(fileName) {
      try {
        await axios.delete(`${API_URL}/api/admin/upload/${fileName}`)
      } catch (err) {
        throw err
      }
    }
  }
})
