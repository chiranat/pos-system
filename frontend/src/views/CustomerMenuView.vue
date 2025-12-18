<template>
  <div class="min-h-screen bg-slate-50 pb-24">
    <!-- Loading State -->
    <div v-if="customerStore.loading || menuStore.loading" class="flex justify-center items-center h-screen">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
    </div>

    <!-- Error State -->
    <div v-else-if="customerStore.error" class="flex flex-col justify-center items-center h-screen p-6 text-center">
      <div class="bg-rose-100 p-4 rounded-full mb-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-rose-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
        </svg>
      </div>
      <h2 class="text-xl font-bold text-slate-800 mb-2">Unable to Access Menu</h2>
      <p class="text-slate-500">{{ customerStore.error }}</p>
      <div class="mt-4 p-2 bg-gray-100 rounded text-xs text-left overflow-auto max-w-full">
        <p><strong>Debug Info:</strong></p>
        <p>Token: {{ $route.query.token || 'None' }}</p>
        <p>API URL: {{ API_URL }}</p>
      </div>
      <p class="text-sm text-slate-400 mt-4">Please scan the QR code again or contact staff.</p>
    </div>

    <!-- Menu Content -->
    <div v-else>
      <!-- Header -->
      <header class="bg-white sticky top-0 z-40 shadow-sm">
        <div class="px-4 py-3 flex justify-between items-center">
          <div>
            <h1 class="text-lg font-bold text-slate-800">Table {{ customerStore.session?.table?.tableNumber }}</h1>
            <p class="text-xs text-slate-500">Welcome to our restaurant</p>
          </div>
          <button 
            @click="showCart = true"
            class="p-2 bg-slate-100 rounded-full relative hover:bg-slate-200 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <span v-if="cartStore.totalItems > 0" class="absolute -top-1 -right-1 bg-rose-500 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full animate-bounce">
              {{ cartStore.totalItems }}
            </span>
          </button>
          <button 
            @click="showMyOrders = true"
            class="ml-2 p-2 bg-slate-100 rounded-full hover:bg-slate-200 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
          </button>
        </div>
        
        <!-- Category Tabs -->
        <div class="overflow-x-auto whitespace-nowrap px-4 pb-3 scrollbar-hide">
          <button 
            v-for="category in menuStore.categories" 
            :key="category.id"
            @click="activeCategory = category.id"
            class="inline-block px-4 py-1.5 mr-2 rounded-full text-sm font-medium transition-colors"
            :class="activeCategory === category.id ? 'bg-indigo-600 text-white' : 'bg-slate-100 text-slate-600'"
          >
            {{ category.name }}
          </button>
        </div>
      </header>

      <!-- Product List -->
      <main class="p-4 space-y-4">
        <div v-for="product in activeProducts" :key="product.id" class="bg-white rounded-xl p-3 shadow-sm border border-slate-100 flex gap-3">
          <div class="w-24 h-24 bg-slate-200 rounded-lg flex-shrink-0 overflow-hidden">
            <img v-if="product.imageUrl" :src="getImageUrl(product.imageUrl)" class="w-full h-full object-cover" alt="">
            <div v-else class="w-full h-full flex items-center justify-center text-slate-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
          </div>
          <div class="flex-1 flex flex-col justify-between">
            <div>
              <h3 class="font-bold text-slate-800 line-clamp-1">{{ product.name }}</h3>
              <p class="text-xs text-slate-500 line-clamp-2 mt-1">{{ product.description }}</p>
            </div>
            <div class="flex justify-between items-end mt-2">
              <span class="font-bold text-indigo-600">฿{{ product.price }}</span>
              <button 
                @click="cartStore.addToCart(product)"
                class="bg-indigo-600 text-white p-1.5 rounded-lg shadow-sm active:scale-95 transition-transform hover:bg-indigo-700"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </main>

      <!-- Floating Cart Button (Bottom) -->
      <div v-if="cartStore.totalItems > 0" class="fixed bottom-6 left-0 right-0 px-4 z-30">
        <button 
          @click="showCart = true"
          class="w-full bg-slate-900 text-white p-4 rounded-2xl shadow-xl flex justify-between items-center"
        >
          <div class="flex items-center gap-3">
            <div class="bg-white/20 px-3 py-1 rounded-lg font-bold">{{ cartStore.totalItems }}</div>
            <span class="font-medium">View Order</span>
          </div>
          <span class="font-bold text-lg">฿{{ cartStore.totalPrice.toLocaleString() }}</span>
        </button>
      </div>

      <!-- My Orders Modal -->
      <div v-if="showMyOrders" class="fixed inset-0 z-50 z-[100]">
        <div class="absolute inset-0 bg-slate-900/60 backdrop-blur-sm transition-opacity" @click="showMyOrders = false"></div>
        <div class="absolute bottom-0 left-0 right-0 bg-white rounded-t-3xl shadow-2xl max-h-[85vh] flex flex-col transition-transform duration-300 transform translate-y-0">
          <div class="w-full flex justify-center pt-3 pb-1" @click="showMyOrders = false">
            <div class="w-12 h-1.5 bg-slate-200 rounded-full"></div>
          </div>
          <div class="px-6 py-4 border-b border-slate-100 flex justify-between items-center">
            <h2 class="text-xl font-bold text-slate-800">My Orders</h2>
            <button @click="orderStore.fetchMyOrders()" class="text-sm text-indigo-600 font-medium">Refresh</button>
          </div>
          <div class="flex-1 overflow-y-auto p-6 space-y-6">
            <div v-if="orderStore.loading" class="flex justify-center py-8">
              <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
            </div>
            <div v-else-if="orderStore.orders.length === 0" class="text-center py-10 text-slate-400">
              <p>No orders yet</p>
            </div>
            <div v-else v-for="order in orderStore.orders" :key="order.id" class="bg-slate-50 rounded-xl p-4 border border-slate-100">
              <div class="flex justify-between items-center mb-3">
                <span class="text-xs text-slate-500">{{ new Date(order.orderedAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) }}</span>
                <span 
                  class="px-2 py-1 rounded text-xs font-bold uppercase tracking-wider"
                  :class="{
                    'bg-yellow-100 text-yellow-700': order.status === 'PENDING',
                    'bg-orange-100 text-orange-700': order.status === 'COOKING',
                    'bg-green-100 text-green-700': order.status === 'SERVED'
                  }"
                >
                  {{ order.status }}
                </span>
              </div>
              <ul class="space-y-2">
                <li v-for="item in order.orderItems" :key="item.id" class="flex justify-between text-sm">
                  <span class="text-slate-700"><span class="font-bold">{{ item.quantity }}x</span> {{ item.product.name }}</span>
                  <span class="text-slate-500">฿{{ item.subtotal }}</span>
                </li>
              </ul>
            </div>
          </div>
          <div class="p-6 border-t border-slate-100 bg-slate-50">
            <div class="flex justify-between items-center">
              <span class="text-slate-500 font-medium">Grand Total</span>
              <span class="text-2xl font-bold text-slate-900">฿{{ totalOrderAmount.toLocaleString() }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Cart Modal / Bottom Sheet -->
      <div v-if="showCart" class="fixed inset-0 z-50 z-[100]">
        <!-- Backdrop -->
        <div class="absolute inset-0 bg-slate-900/60 backdrop-blur-sm transition-opacity" @click="showCart = false"></div>
        
        <!-- Sheet -->
        <div class="absolute bottom-0 left-0 right-0 bg-white rounded-t-3xl shadow-2xl max-h-[85vh] flex flex-col transition-transform duration-300 transform translate-y-0">
          <!-- Handle -->
          <div class="w-full flex justify-center pt-3 pb-1" @click="showCart = false">
            <div class="w-12 h-1.5 bg-slate-200 rounded-full"></div>
          </div>

          <!-- Header -->
          <div class="px-6 py-4 border-b border-slate-100 flex justify-between items-center">
            <h2 class="text-xl font-bold text-slate-800">Your Order</h2>
            <button @click="cartStore.clearCart()" class="text-sm text-rose-500 font-medium hover:text-rose-600">Clear All</button>
          </div>

          <!-- Cart Items -->
          <div class="flex-1 overflow-y-auto p-6 space-y-6">
            <div v-if="cartStore.items.length === 0" class="text-center py-10 text-slate-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto mb-4 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              <p>Your cart is empty</p>
              <button @click="showCart = false" class="mt-4 text-indigo-600 font-medium">Start Ordering</button>
            </div>

            <div v-else v-for="item in cartStore.items" :key="item.id" class="flex gap-4">
              <div class="w-20 h-20 bg-slate-100 rounded-xl overflow-hidden flex-shrink-0">
                <img v-if="item.imageUrl" :src="item.imageUrl" class="w-full h-full object-cover" alt="">
              </div>
              <div class="flex-1">
                <div class="flex justify-between items-start mb-1">
                  <h3 class="font-bold text-slate-800">{{ item.name }}</h3>
                  <span class="font-bold text-slate-900">฿{{ item.price * item.quantity }}</span>
                </div>
                <p class="text-xs text-slate-400 mb-3">฿{{ item.price }} / unit</p>
                
                <!-- Quantity Control -->
                <div class="flex items-center gap-4">
                  <div class="flex items-center bg-slate-100 rounded-lg p-1">
                    <button 
                      @click="cartStore.updateQuantity(item.id, -1)"
                      class="w-8 h-8 flex items-center justify-center bg-white rounded-md shadow-sm text-slate-600 active:scale-95 transition-transform"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                      </svg>
                    </button>
                    <span class="w-8 text-center font-bold text-slate-700">{{ item.quantity }}</span>
                    <button 
                      @click="cartStore.updateQuantity(item.id, 1)"
                      class="w-8 h-8 flex items-center justify-center bg-white rounded-md shadow-sm text-indigo-600 active:scale-95 transition-transform"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Footer -->
          <div class="p-6 border-t border-slate-100 bg-slate-50 pb-8">
            <div class="flex justify-between items-center mb-4">
              <span class="text-slate-500">Total Amount</span>
              <span class="text-2xl font-bold text-slate-900">฿{{ cartStore.totalPrice.toLocaleString() }}</span>
            </div>
            <button 
              @click="handlePlaceOrder"
              :disabled="cartStore.items.length === 0 || cartStore.loading"
              class="w-full bg-indigo-600 text-white py-4 rounded-xl font-bold text-lg shadow-lg shadow-indigo-200 disabled:opacity-50 disabled:cursor-not-allowed active:scale-[0.98] transition-transform flex justify-center items-center"
            >
              <span v-if="cartStore.loading" class="animate-spin rounded-full h-6 w-6 border-b-2 border-white mr-2"></span>
              <span>{{ cartStore.loading ? 'Placing Order...' : 'Place Order' }}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useCustomerStore } from '../stores/customer'
import { useMenuStore } from '../stores/menu'
import { useCartStore } from '../stores/cart'
import { useOrderStore } from '../stores/order'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const route = useRoute()
const customerStore = useCustomerStore()
const menuStore = useMenuStore()
const cartStore = useCartStore()
const orderStore = useOrderStore()

const getImageUrl = (path) => {
  if (!path) return null
  if (path.startsWith('http') || path.startsWith('data:')) return path
  return `${API_URL}/${path}`
}

const activeCategory = ref(null)
const showCart = ref(false)
const showMyOrders = ref(false)

const activeProducts = computed(() => {
  if (!activeCategory.value) return []
  return menuStore.productsByCategory(activeCategory.value)
})

const totalOrderAmount = computed(() => {
  return orderStore.orders.reduce((total, order) => {
    if (order.status === 'CANCELLED') return total
    return total + order.orderItems.reduce((sub, item) => sub + item.subtotal, 0)
  }, 0)
})

const handlePlaceOrder = async () => {
  try {
    await cartStore.placeOrder()
    showCart.value = false
    alert('Order placed successfully! Please wait for your food.')
    // Refresh orders
    await orderStore.fetchMyOrders()
    showMyOrders.value = true
  } catch (error) {
    alert('Failed to place order: ' + (cartStore.error || 'Unknown error'))
  }
}

watch(showMyOrders, (newVal) => {
  if (newVal) {
    orderStore.fetchMyOrders()
  }
})

onMounted(async () => {
  const token = route.query.token || customerStore.token
  if (token) {
    const isValid = await customerStore.validateSession(token)
    if (isValid) {
      await menuStore.fetchMenu()
      if (menuStore.categories.length > 0) {
        activeCategory.value = menuStore.categories[0].id
      }
    }
  } else {
    customerStore.error = 'No session token provided'
  }
})
</script>

<style scoped>
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}
.scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>
