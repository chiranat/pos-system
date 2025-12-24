<template>
  <div class="min-h-screen bg-slate-50 p-6">
    <header class="flex justify-between items-center mb-8">
      <div>
        <h1 class="text-3xl font-bold text-slate-800">Kitchen Display</h1>
        <p class="text-slate-500">Manage incoming orders</p>
      </div>
      <div class="flex gap-4">
        <button 
          @click="kitchenStore.fetchActiveOrders()" 
          class="p-2 bg-white rounded-lg shadow-sm hover:bg-slate-50 text-slate-600"
          title="Refresh"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
        </button>
        <router-link 
          v-if="authStore.userRole !== 'KITCHEN'"
          to="/" 
          class="px-4 py-2 bg-slate-200 text-slate-700 rounded-lg font-medium hover:bg-slate-300 transition-colors"
        >
          Back to Tables
        </router-link>
        <button 
          v-if="authStore.userRole === 'KITCHEN'"
          @click="handleLogout"
          class="px-4 py-2 bg-rose-100 text-rose-700 rounded-lg font-medium hover:bg-rose-200 transition-colors"
        >
          Logout
        </button>
      </div>
    </header>

    <div v-if="kitchenStore.loading" class="flex justify-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
    </div>

    <div v-else-if="kitchenStore.orders.length === 0" class="text-center py-12 text-slate-400">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto mb-4 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
      </svg>
      <p class="text-lg">No active orders</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div 
        v-for="order in kitchenStore.orders" 
        :key="order.id"
        class="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden flex flex-col"
      >
        <!-- Order Header -->
        <div class="p-4 bg-slate-50 border-b border-slate-100 flex justify-between items-center">
          <div>
            <span class="text-xs font-bold text-slate-500 uppercase tracking-wider">Table</span>
            <h3 class="text-2xl font-bold text-slate-800">{{ order.session?.table?.tableNumber || '?' }}</h3>
          </div>
          <div class="text-right">
            <span 
              class="inline-block px-2 py-1 rounded text-xs font-bold uppercase tracking-wider"
              :class="{
                'bg-yellow-100 text-yellow-700': order.status === 'PENDING',
                'bg-orange-100 text-orange-700': order.status === 'COOKING',
                'bg-green-100 text-green-700': order.status === 'SERVED'
              }"
            >
              {{ order.status }}
            </span>
            <p class="text-xs text-slate-400 mt-1">{{ formatTime(order.orderedAt) }}</p>
          </div>
        </div>

        <!-- Order Items -->
        <div class="p-4 flex-1 overflow-y-auto max-h-[300px]">
          <ul class="space-y-3">
            <li v-for="item in order.orderItems" :key="item.id" class="flex gap-3">
              <div class="w-8 h-8 bg-slate-100 rounded flex items-center justify-center font-bold text-slate-600 flex-shrink-0">
                {{ item.quantity }}x
              </div>
              <div>
                <p class="font-medium text-slate-800">{{ item.product.name }}</p>
                <p v-if="item.notes" class="text-sm text-rose-500 italic">Note: {{ item.notes }}</p>
              </div>
            </li>
          </ul>
        </div>

        <!-- Actions -->
        <div class="p-4 border-t border-slate-100 bg-slate-50 grid grid-cols-2 gap-3">
          <button 
            v-if="order.status === 'PENDING'"
            @click="kitchenStore.updateOrderStatus(order.id, 'COOKING')"
            class="col-span-2 w-full py-2 bg-indigo-600 text-white rounded-lg font-bold hover:bg-indigo-700 transition-colors"
          >
            Start Cooking
          </button>
          <template v-if="order.status === 'COOKING'">
            <button 
              @click="kitchenStore.updateOrderStatus(order.id, 'SERVED')"
              class="col-span-2 w-full py-2 bg-emerald-600 text-white rounded-lg font-bold hover:bg-emerald-700 transition-colors"
            >
              Mark Served
            </button>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useKitchenStore } from '../stores/kitchen'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const kitchenStore = useKitchenStore()
const authStore = useAuthStore()
const router = useRouter()
let pollInterval

const formatTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

onMounted(() => {
  kitchenStore.fetchActiveOrders()
  // Poll every 10 seconds
  pollInterval = setInterval(() => {
    kitchenStore.fetchActiveOrders()
  }, 10000)
})

onUnmounted(() => {
  if (pollInterval) clearInterval(pollInterval)
})
</script>
