import { defineStore } from 'pinia'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useTableStore = defineStore('table', {
  state: () => ({
    tables: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchTables() {
      this.loading = true
      try {
        const response = await axios.get(`${API_URL}/api/tables`)
        this.tables = response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to fetch tables'
      } finally {
        this.loading = false
      }
    },

    async createTable(tableData) {
      try {
        const response = await axios.post(`${API_URL}/api/tables`, tableData)
        this.tables.push(response.data)
        return response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to create table'
        throw err
      }
    },

    async updateTable(id, tableData) {
      try {
        const response = await axios.put(`${API_URL}/api/tables/${id}`, tableData)
        const index = this.tables.findIndex(t => t.id === id)
        if (index !== -1) {
          this.tables[index] = response.data
        }
        return response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to update table'
        throw err
      }
    },

    async deleteTable(id) {
      try {
        await axios.delete(`${API_URL}/api/tables/${id}`)
        this.tables = this.tables.filter(t => t.id !== id)
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to delete table'
        throw err
      }
    },

    async openTable(tableId, customerCount) {
      try {
        const response = await axios.post(`${API_URL}/api/tables/${tableId}/open`, {
          customerCount
        })
        // Update local state
        const tableIndex = this.tables.findIndex(t => t.id === tableId)
        if (tableIndex !== -1) {
          this.tables[tableIndex].status = 'OCCUPIED'
          this.tables[tableIndex].currentSession = response.data
        }
        return response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to open table'
        throw err
      }
    },

    async fetchTableDetails(tableId) {
      try {
        const response = await axios.get(`${API_URL}/api/tables/${tableId}/details`)
        return response.data
      } catch (err) {
        console.error('Failed to fetch table details:', err)
        throw err
      }
    },

    async closeTable(tableId) {
      try {
        await axios.post(`${API_URL}/api/tables/${tableId}/close`)
        // Update local state
        const tableIndex = this.tables.findIndex(t => t.id === tableId)
        if (tableIndex !== -1) {
          this.tables[tableIndex].status = 'AVAILABLE'
          this.tables[tableIndex].currentSession = null
        }
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to close table'
        throw err
      }
    },

    async processPayment(tableId, paymentMethod) {
      try {
        const response = await axios.post(`${API_URL}/api/transactions/payment/${tableId}`, {
          paymentMethod
        })
        // Update local state (Table becomes available)
        const tableIndex = this.tables.findIndex(t => t.id === tableId)
        if (tableIndex !== -1) {
          this.tables[tableIndex].status = 'AVAILABLE'
          this.tables[tableIndex].currentSession = null
        }
        return response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to process payment'
        throw err
      }
    }
  }
})

