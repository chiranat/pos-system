import { defineStore } from 'pinia'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useReportStore = defineStore('report', {
  state: () => ({
    summary: null,
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchDailyReport(date = null) {
      this.loading = true
      try {
        const params = date ? { date } : {}
        const response = await axios.get(`${API_URL}/api/admin/reports/daily`, { params })
        this.summary = response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to fetch report'
        console.error(err)
      } finally {
        this.loading = false
      }
    },

    async fetchReportSummary(startDate, endDate) {
      this.loading = true
      try {
        const params = { startDate, endDate }
        const response = await axios.get(`${API_URL}/api/admin/reports/summary`, { params })
        this.summary = response.data
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to fetch report'
        console.error(err)
      } finally {
        this.loading = false
      }
    }
  }
})
