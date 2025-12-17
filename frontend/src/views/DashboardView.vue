<template>
  <div class="min-h-screen bg-slate-50 font-sans">
    <!-- Navbar -->
    <nav class="bg-white/80 backdrop-blur-md sticky top-0 z-30 border-b border-slate-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center gap-3">
            <div class="bg-gradient-to-br from-indigo-600 to-violet-600 p-2 rounded-lg shadow-lg shadow-indigo-200">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
            <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-violet-600">
              Sales Dashboard
            </span>
          </div>
          <div class="flex items-center gap-4">
            <router-link 
              to="/" 
              class="text-slate-500 hover:text-indigo-600 font-medium text-sm transition-colors"
            >
              Back to Home
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <!-- Filter Bar -->
      <div class="bg-white p-4 rounded-2xl shadow-sm border border-slate-200 mb-6 flex flex-col sm:flex-row sm:items-center gap-4">
        <!-- Report Type Selector -->
        <div class="flex items-center gap-2">
          <label class="text-sm font-medium text-slate-700 whitespace-nowrap">Report Type:</label>
          <select v-model="reportType" class="rounded-lg border-slate-300 text-sm focus:ring-indigo-500 focus:border-indigo-500">
            <option value="DAILY">Daily</option>
            <option value="MONTHLY">Monthly</option>
            <option value="YEARLY">Yearly</option>
            <option value="CUSTOM">Custom Range</option>
          </select>
        </div>

        <!-- Dynamic Inputs -->
        <div class="flex flex-wrap items-center gap-4 flex-1">
          <!-- Daily -->
          <input 
            v-if="reportType === 'DAILY'"
            type="date" 
            v-model="selectedDate" 
            class="rounded-lg border-slate-300 text-sm focus:ring-indigo-500 focus:border-indigo-500"
          >

          <!-- Monthly -->
          <input 
            v-if="reportType === 'MONTHLY'"
            type="month" 
            v-model="selectedMonth" 
            class="rounded-lg border-slate-300 text-sm focus:ring-indigo-500 focus:border-indigo-500"
          >

          <!-- Yearly -->
          <select 
            v-if="reportType === 'YEARLY'"
            v-model="selectedYear"
            class="rounded-lg border-slate-300 text-sm focus:ring-indigo-500 focus:border-indigo-500"
          >
            <option v-for="year in availableYears" :key="year" :value="year">{{ year }}</option>
          </select>

          <!-- Custom Range -->
          <div v-if="reportType === 'CUSTOM'" class="flex items-center gap-2">
            <input 
              type="date" 
              v-model="customStartDate" 
              class="rounded-lg border-slate-300 text-sm focus:ring-indigo-500 focus:border-indigo-500"
            >
            <span class="text-slate-400">to</span>
            <input 
              type="date" 
              v-model="customEndDate" 
              class="rounded-lg border-slate-300 text-sm focus:ring-indigo-500 focus:border-indigo-500"
            >
          </div>
          
          <button 
            @click="loadReport"
            class="ml-auto px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors shadow-sm shadow-indigo-200"
          >
            Generate Report
          </button>
        </div>
      </div>

      <div v-if="reportStore.loading" class="flex justify-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>

      <div v-else-if="reportStore.summary" class="space-y-6">
        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <!-- Total Sales -->
          <div class="bg-white overflow-hidden shadow-sm rounded-2xl border border-slate-100">
            <div class="p-6">
              <div class="flex items-center">
                <div class="flex-shrink-0 bg-emerald-100 rounded-xl p-3">
                  <svg class="h-8 w-8 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <div class="ml-5 w-0 flex-1">
                  <dl>
                    <dt class="text-sm font-medium text-slate-500 truncate">Total Sales</dt>
                    <dd class="text-3xl font-bold text-slate-900">฿{{ reportStore.summary.totalSales }}</dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <!-- Total Transactions -->
          <div class="bg-white overflow-hidden shadow-sm rounded-2xl border border-slate-100">
            <div class="p-6">
              <div class="flex items-center">
                <div class="flex-shrink-0 bg-blue-100 rounded-xl p-3">
                  <svg class="h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                </div>
                <div class="ml-5 w-0 flex-1">
                  <dl>
                    <dt class="text-sm font-medium text-slate-500 truncate">Transactions</dt>
                    <dd class="text-3xl font-bold text-slate-900">{{ reportStore.summary.totalTransactions }}</dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <!-- Average Ticket -->
          <div class="bg-white overflow-hidden shadow-sm rounded-2xl border border-slate-100">
            <div class="p-6">
              <div class="flex items-center">
                <div class="flex-shrink-0 bg-violet-100 rounded-xl p-3">
                  <svg class="h-8 w-8 text-violet-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                  </svg>
                </div>
                <div class="ml-5 w-0 flex-1">
                  <dl>
                    <dt class="text-sm font-medium text-slate-500 truncate">Avg. Ticket</dt>
                    <dd class="text-3xl font-bold text-slate-900">
                      ฿{{ reportStore.summary.totalTransactions > 0 ? (reportStore.summary.totalSales / reportStore.summary.totalTransactions).toFixed(0) : 0 }}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <!-- Payment Methods Breakdown -->
          <div class="bg-white shadow-sm rounded-2xl border border-slate-200 p-6">
            <h3 class="text-lg font-bold text-slate-900 mb-4">Sales by Payment Method</h3>
            <div class="space-y-3">
              <div v-for="(amount, method) in reportStore.summary.salesByPaymentMethod" :key="method" 
                   class="flex items-center justify-between p-3 bg-slate-50 rounded-xl border border-slate-100">
                <div class="flex items-center gap-3">
                  <div class="p-2 bg-white rounded-lg shadow-sm">
                    <svg v-if="method === 'CASH'" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-emerald-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z" />
                    </svg>
                  </div>
                  <span class="text-sm font-medium text-slate-700">{{ method }}</span>
                </div>
                <span class="text-lg font-bold text-slate-900">฿{{ amount }}</span>
              </div>
            </div>
          </div>

          <!-- Top Selling Items -->
          <div class="bg-white shadow-sm rounded-2xl border border-slate-200 p-6">
            <h3 class="text-lg font-bold text-slate-900 mb-4">Top Selling Items</h3>
            <div class="space-y-4">
              <div v-for="(item, index) in reportStore.summary.topSellingItems" :key="index" 
                   class="flex items-center justify-between group hover:bg-slate-50 p-2 rounded-lg transition-colors -mx-2">
                <div class="flex items-center gap-3 px-2">
                  <div class="flex-shrink-0 h-8 w-8 rounded-full bg-indigo-50 text-indigo-600 flex items-center justify-center font-bold text-sm border border-indigo-100">
                    {{ index + 1 }}
                  </div>
                  <div>
                    <div class="text-sm font-medium text-slate-900">{{ item.productName }}</div>
                    <div class="text-xs text-slate-500">{{ item.quantitySold }} sold</div>
                  </div>
                </div>
                <div class="text-sm font-bold text-slate-900 px-2">฿{{ item.totalRevenue }}</div>
              </div>
              <div v-if="!reportStore.summary.topSellingItems?.length" class="text-center text-slate-500 py-8 bg-slate-50 rounded-xl border border-dashed border-slate-200">
                No sales data available for this period
              </div>
            </div>
          </div>
        </div>

        <!-- Recent Transactions Table -->
        <div class="bg-white shadow-sm rounded-2xl border border-slate-200 overflow-hidden">
          <div class="px-6 py-4 border-b border-slate-200">
            <h3 class="text-lg font-bold text-slate-900">Recent Transactions</h3>
          </div>
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200">
              <thead class="bg-slate-50">
                <tr>
                  <th class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Time</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Table</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Method</th>
                  <th class="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase tracking-wider">Amount</th>
                  <th class="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-slate-200">
                <tr v-for="transaction in reportStore.summary.recentTransactions" :key="transaction.id" class="hover:bg-slate-50">
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">
                    {{ new Date(transaction.paidAt).toLocaleTimeString() }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-900">
                    Table {{ transaction.session?.table?.tableNumber || '-' }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-slate-100 text-slate-800">
                      {{ transaction.paymentMethod }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-bold text-slate-900">
                    ฿{{ transaction.amount }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button 
                      @click="viewReceipt(transaction)" 
                      class="text-indigo-600 hover:text-indigo-900 bg-indigo-50 hover:bg-indigo-100 px-3 py-1 rounded-lg transition-colors"
                    >
                      Receipt
                    </button>
                  </td>
                </tr>
                <tr v-if="reportStore.summary.recentTransactions.length === 0">
                  <td colspan="5" class="px-6 py-12 text-center text-slate-500">
                    No transactions found for this date.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <!-- Receipt Modal -->
    <ReceiptModal 
      :isOpen="showReceiptModal" 
      :receipt="currentReceipt" 
      @close="showReceiptModal = false" 
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useReportStore } from '../stores/report'
import ReceiptModal from '../components/ReceiptModal.vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const reportStore = useReportStore()

// State
const showReceiptModal = ref(false)
const currentReceipt = ref(null)
const reportType = ref('DAILY')
const selectedDate = ref(new Date().toISOString().split('T')[0])
const selectedMonth = ref(new Date().toISOString().slice(0, 7))
const selectedYear = ref(new Date().getFullYear())
const customStartDate = ref(new Date().toISOString().split('T')[0])
const customEndDate = ref(new Date().toISOString().split('T')[0])

// Computed
const availableYears = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => currentYear - i)
})

onMounted(() => {
  loadReport()
})

const loadReport = () => {
  let start, end
  
  if (reportType.value === 'DAILY') {
    start = selectedDate.value
    end = selectedDate.value
  } else if (reportType.value === 'MONTHLY') {
    const [year, month] = selectedMonth.value.split('-')
    // Get last day of month
    const lastDay = new Date(year, month, 0).getDate()
    start = `${selectedMonth.value}-01`
    end = `${selectedMonth.value}-${lastDay}`
  } else if (reportType.value === 'YEARLY') {
    start = `${selectedYear.value}-01-01`
    end = `${selectedYear.value}-12-31`
  } else {
    start = customStartDate.value
    end = customEndDate.value
  }
  
  reportStore.fetchReportSummary(start, end)
}

const viewReceipt = async (transaction) => {
  try {
    const response = await axios.get(`${API_URL}/api/transactions/${transaction.id}/receipt`)
    currentReceipt.value = response.data
    showReceiptModal.value = true
  } catch (error) {
    console.error('Failed to fetch receipt:', error)
  }
}
</script>
