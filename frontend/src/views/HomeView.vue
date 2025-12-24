<template>
  <div class="min-h-screen bg-slate-50 font-sans">
    <!-- Navbar -->
    <nav class="bg-white/80 backdrop-blur-md sticky top-0 z-30 border-b border-slate-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center gap-3">
            <div class="bg-gradient-to-br from-indigo-600 to-violet-600 p-2.5 rounded-xl shadow-lg shadow-indigo-200">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
              </svg>
            </div>
            <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-violet-600">
              POS System
            </span>
          </div>
          <div class="flex items-center gap-4">
            <div class="hidden md:flex items-center gap-2 bg-slate-100 px-4 py-2 rounded-full border border-slate-200">
              <div class="h-2.5 w-2.5 rounded-full bg-emerald-500 animate-pulse"></div>
              <span class="text-sm font-semibold text-slate-600">{{ authStore.user?.username }}</span>
              <span class="text-xs text-slate-400 border-l border-slate-300 pl-2 ml-1">{{ authStore.userRole }}</span>
            </div>
            <button 
              @click="handleLogout" 
              class="p-2 text-slate-400 hover:text-rose-500 hover:bg-rose-50 rounded-xl transition-all duration-200"
              title="Logout"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <!-- Header Section -->
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
        <div>
          <h2 class="text-3xl font-bold text-slate-800">Table Management</h2>
          <p class="mt-1 text-slate-500">Real-time overview of dining area.</p>
        </div>
        <div class="flex gap-3">
          <router-link 
            v-if="['ADMIN', 'MANAGER'].includes(authStore.userRole)"
            to="/admin/users" 
            class="inline-flex items-center px-5 py-2.5 bg-white border border-slate-300 rounded-xl shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
            Users
          </router-link>
          <router-link 
          v-if="['ADMIN', 'MANAGER'].includes(authStore.userRole)"
            to="/admin/reports" 
            class="inline-flex items-center px-5 py-2.5 bg-white border border-slate-300 rounded-xl shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
            Reports
          </router-link>
          <router-link 
            v-if="['ADMIN', 'MANAGER'].includes(authStore.userRole)"
            to="/admin/menu" 
            class="inline-flex items-center px-5 py-2.5 bg-white border border-slate-300 rounded-xl shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            Manage Menu
          </router-link>
          <router-link 
            to="/kitchen" 
            class="inline-flex items-center px-5 py-2.5 bg-indigo-600 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
            </svg>
            Kitchen View
          </router-link>
          <button 
            v-if="['ADMIN', 'MANAGER'].includes(authStore.userRole)"
            @click="openAddTableModal" 
            class="inline-flex items-center px-5 py-2.5 bg-emerald-600 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Add Table
          </button>
          <button 
            @click="tableStore.fetchTables()" 
            class="group inline-flex items-center px-5 py-2.5 bg-white border border-slate-200 rounded-xl shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 hover:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2 text-slate-400 group-hover:text-indigo-500 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Refresh Status
          </button>
        </div>
      </div>

      <!-- Stats Overview -->
      <div class="grid grid-cols-3 gap-4 mb-10">
        <!-- Total Tables -->
        <div class="relative overflow-hidden bg-white rounded-2xl p-6 shadow-sm border border-slate-100 group hover:shadow-md transition-all duration-300">
          <div class="absolute top-0 right-0 -mt-4 -mr-4 w-24 h-24 bg-indigo-50 rounded-full group-hover:scale-110 transition-transform duration-300"></div>
          <div class="relative">
            <dt class="text-sm font-medium text-slate-500 truncate">Total Tables</dt>
            <dd class="mt-2 flex items-baseline">
              <span class="text-4xl font-bold text-slate-800">{{ tableStore.tables.length }}</span>
              <span class="ml-2 text-sm text-slate-400">units</span>
            </dd>
          </div>
        </div>

        <!-- Available -->
        <div class="relative overflow-hidden bg-gradient-to-br from-emerald-500 to-teal-600 rounded-2xl p-6 shadow-lg shadow-emerald-200 text-white group hover:shadow-xl hover:shadow-emerald-300 transition-all duration-300">
          <div class="absolute top-0 right-0 -mt-4 -mr-4 w-24 h-24 bg-white/10 rounded-full group-hover:scale-110 transition-transform duration-300"></div>
          <div class="relative">
            <dt class="text-sm font-medium text-emerald-100 truncate">Available</dt>
            <dd class="mt-2 flex items-baseline">
              <span class="text-4xl font-bold">{{ tableStore.tables.filter(t => t.status === 'AVAILABLE').length }}</span>
              <span class="ml-2 text-sm text-emerald-100/70">tables</span>
            </dd>
          </div>
        </div>

        <!-- Occupied -->
        <div class="relative overflow-hidden bg-white rounded-2xl p-6 shadow-sm border border-slate-100 group hover:shadow-md transition-all duration-300">
          <div class="absolute top-0 right-0 -mt-4 -mr-4 w-24 h-24 bg-rose-50 rounded-full group-hover:scale-110 transition-transform duration-300"></div>
          <div class="relative">
            <dt class="text-sm font-medium text-slate-500 truncate">Occupied</dt>
            <dd class="mt-2 flex items-baseline">
              <span class="text-4xl font-bold text-rose-600">{{ tableStore.tables.filter(t => t.status === 'OCCUPIED').length }}</span>
              <span class="ml-2 text-sm text-slate-400">tables</span>
            </dd>
          </div>
        </div>
      </div>

      <!-- Table Grid -->
      <div v-if="tableStore.loading" class="flex justify-center items-center py-20">
        <div class="relative">
          <div class="w-12 h-12 rounded-full absolute border-4 border-solid border-gray-200"></div>
          <div class="w-12 h-12 rounded-full animate-spin absolute border-4 border-solid border-indigo-500 border-t-transparent"></div>
        </div>
      </div>

      <div v-else-if="tableStore.error" class="bg-rose-50 border border-rose-200 rounded-xl p-4 mb-8 flex items-center gap-3">
        <svg class="h-5 w-5 text-rose-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
        </svg>
        <p class="text-sm text-rose-700">{{ tableStore.error }}</p>
      </div>

      <div v-else class="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5">
        <TableCard 
          v-for="table in tableStore.tables" 
          :key="table.id" 
          :table="table"
          @click="handleTableClick(table)"
          @edit="openEditTableModal"
        />
      </div>
    </main>

    <!-- Open Table Modal -->
    <Modal :isOpen="showOpenModal" title="Open Table" @close="showOpenModal = false">
      <div class="mt-2">
        <p class="text-sm text-gray-500 mb-4">
          Enter the number of customers for Table {{ selectedTable?.tableNumber }}
        </p>
        <input 
          type="number" 
          v-model="customerCount" 
          min="1" 
          class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md p-2 border"
          placeholder="Number of customers"
        >
      </div>
      <template #footer>
        <button 
          type="button" 
          class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm"
          @click="confirmOpenTable"
        >
          Open Table
        </button>
        <button 
          type="button" 
          class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
          @click="showOpenModal = false"
        >
          Cancel
        </button>
      </template>
    </Modal>

    <!-- QR Code Modal -->
    <TableQrModal 
      :isOpen="showQrModal" 
      :qrCodeUrl="qrCodeUrl" 
      :tableNumber="selectedTable?.tableNumber"
      @close="showQrModal = false" 
    />

    <!-- Table Details Modal -->
    <Modal :isOpen="showDetailsModal" title="Table Details" @close="showDetailsModal = false">
      <div v-if="tableDetails" class="mt-2">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-bold text-slate-800">Table {{ tableDetails.table.tableNumber }}</h3>
          <span class="px-2 py-1 bg-indigo-100 text-indigo-700 rounded text-xs font-bold">{{ tableDetails.currentSession?.status }}</span>
        </div>
        
        <div class="mb-4">
          <p class="text-sm text-slate-500">Started at: {{ new Date(tableDetails.currentSession?.startTime).toLocaleTimeString() }}</p>
          <p class="text-sm text-slate-500">Customers: {{ tableDetails.currentSession?.customerCount }}</p>
          <div class="mt-2 p-2 bg-slate-50 rounded border border-slate-200 flex justify-between items-center">
            <div class="text-xs text-slate-500 truncate max-w-[200px]">
              {{ tableDetails.currentSession?.qrCodeUrl }}
            </div>
            <button 
              @click="viewQrCodeFromDetails"
              class="text-xs bg-white border border-slate-300 px-2 py-1 rounded hover:bg-slate-50 text-indigo-600 font-medium"
            >
              Print QR
            </button>
          </div>

          <div class="mt-2">
            <p class="text-xs text-slate-500 mb-1">Customer URL:</p>
            <div class="p-2 bg-slate-50 rounded border border-slate-200 flex justify-between items-center">
              <div class="text-xs text-slate-500 truncate max-w-[200px]">
                {{ getCustomerUrl(tableDetails.currentSession?.accessToken) }}
              </div>
              <button 
                @click="copyToClipboard(getCustomerUrl(tableDetails.currentSession?.accessToken))"
                class="text-xs bg-white border border-slate-300 px-2 py-1 rounded hover:bg-slate-50 text-indigo-600 font-medium"
              >
                Copy
              </button>
            </div>
          </div>
        </div>

        <div class="border-t border-slate-100 py-4">
          <h4 class="font-bold text-slate-700 mb-2">Orders</h4>
          <div v-if="tableDetails.orders.length === 0" class="text-slate-400 text-sm">No orders yet</div>
          <ul v-else class="space-y-2 max-h-60 overflow-y-auto">
            <li v-for="order in tableDetails.orders" :key="order.id" class="text-sm border-b border-slate-50 pb-2">
               <div class="flex justify-between mb-1">
                 <span class="font-medium">Order #{{ order.id.substring(0, 4) }}</span>
                 <span class="text-xs px-1.5 py-0.5 rounded bg-slate-100">{{ order.status }}</span>
               </div>
               <ul class="pl-2 text-slate-500">
                 <li v-for="item in order.orderItems" :key="item.id" class="flex justify-between">
                   <span>{{ item.quantity }}x {{ item.product.name }}</span>
                   <span>฿{{ item.subtotal }}</span>
                 </li>
               </ul>
            </li>
          </ul>
        </div>

        <div class="border-t border-slate-100 pt-4 flex justify-between items-center">
          <span class="font-bold text-slate-700">Total Amount</span>
          <span class="text-xl font-bold text-indigo-600">฿{{ tableDetails.totalAmount.toLocaleString() }}</span>
        </div>
      </div>
      <template #footer>
        <button 
          type="button" 
          class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-rose-600 text-base font-medium text-white hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-rose-500 sm:ml-3 sm:w-auto sm:text-sm"
          @click="confirmCloseTableFromDetails"
        >
          Close Table (Check Bill)
        </button>
        <button 
          type="button" 
          class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
          @click="showDetailsModal = false"
        >
          Close
        </button>
      </template>
    </Modal>

    <!-- Payment Modal -->
    <Modal :isOpen="showPaymentModal" title="Payment" @close="showPaymentModal = false">
      <div class="mt-2">
        <p class="text-sm text-gray-500 mb-4">
          Select payment method for Table {{ selectedTable?.tableNumber }}
        </p>
        <div class="space-y-3">
          <button 
            @click="selectedPaymentMethod = 'CASH'"
            class="w-full p-3 rounded-lg border-2 flex items-center gap-3 transition-all"
            :class="selectedPaymentMethod === 'CASH' ? 'border-indigo-600 bg-indigo-50 text-indigo-700' : 'border-slate-200 hover:border-indigo-300'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <span class="font-bold">Cash</span>
          </button>
          <button 
            @click="selectedPaymentMethod = 'QR_PROMPT_PAY'"
            class="w-full p-3 rounded-lg border-2 flex items-center gap-3 transition-all"
            :class="selectedPaymentMethod === 'QR_PROMPT_PAY' ? 'border-indigo-600 bg-indigo-50 text-indigo-700' : 'border-slate-200 hover:border-indigo-300'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z" />
            </svg>
            <span class="font-bold">QR PromptPay</span>
          </button>
          <button 
            @click="selectedPaymentMethod = 'CREDIT_CARD'"
            class="w-full p-3 rounded-lg border-2 flex items-center gap-3 transition-all"
            :class="selectedPaymentMethod === 'CREDIT_CARD' ? 'border-indigo-600 bg-indigo-50 text-indigo-700' : 'border-slate-200 hover:border-indigo-300'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
            </svg>
            <span class="font-bold">Credit Card</span>
          </button>
        </div>
      </div>
      <template #footer>
        <button 
          type="button" 
          class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm"
          @click="confirmPayment"
        >
          Confirm Payment
        </button>
        <button 
          type="button" 
          class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
          @click="showPaymentModal = false"
        >
          Cancel
        </button>
      </template>
    </Modal>

    <!-- Close Table Modal -->
    <Modal :isOpen="showCloseModal" title="Close Table" @close="showCloseModal = false">
      <div class="mt-2">
        <p class="text-sm text-gray-500">
          Are you sure you want to close Table {{ selectedTable?.tableNumber }}?
        </p>
        <p class="text-sm text-red-500 mt-2">
          This will end the current session and invalidate the QR code.
        </p>
      </div>
      <template #footer>
        <button 
          type="button" 
          class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm"
          @click="confirmCloseTable"
        >
          Close Table
        </button>
        <button 
          type="button" 
          class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
          @click="showCloseModal = false"
        >
          Cancel
        </button>
        <button 
          v-if="selectedTable?.currentSession?.qrCodeUrl"
          type="button" 
          class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
          @click="viewQrCode"
        >
          View QR
        </button>
      </template>
    </Modal>
    <!-- Receipt Modal -->
    <ReceiptModal 
      :isOpen="showReceiptModal" 
      :receipt="currentReceipt" 
      @close="showReceiptModal = false" 
    />

    <!-- Table Form Modal -->
    <Modal :isOpen="showTableFormModal" :title="editingTable ? 'Edit Table' : 'Add Table'" @close="showTableFormModal = false">
      <div class="mt-2 space-y-4">
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-1">Table Number</label>
          <input 
            type="text" 
            v-model="tableForm.tableNumber" 
            class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md p-2 border"
            placeholder="e.g. T-01"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-1">Capacity</label>
          <input 
            type="number" 
            v-model="tableForm.capacity" 
            min="1"
            class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md p-2 border"
            placeholder="Number of seats"
          >
        </div>
      </div>
      <template #footer>
        <div class="flex justify-between w-full">
          <button 
            v-if="editingTable"
            type="button" 
            class="inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-rose-600 text-base font-medium text-white hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-rose-500 sm:text-sm"
            @click="deleteTable"
          >
            Delete
          </button>
          <div class="flex gap-3 ml-auto">
            <button 
              type="button" 
              class="inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:text-sm"
              @click="showTableFormModal = false"
            >
              Cancel
            </button>
            <button 
              type="button" 
              class="inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:text-sm"
              @click="saveTable"
            >
              Save
            </button>
          </div>
        </div>
      </template>
    </Modal>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useAuthStore } from '../stores/auth'
import { useTableStore } from '../stores/table'
import { useRouter } from 'vue-router'
import TableCard from '../components/TableCard.vue'
import Modal from '../components/Modal.vue'
import ReceiptModal from '../components/ReceiptModal.vue'
import TableQrModal from '../components/TableQrModal.vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const authStore = useAuthStore()
const tableStore = useTableStore()
const router = useRouter()

const showOpenModal = ref(false)
const showCloseModal = ref(false)
const showQrModal = ref(false)
const showDetailsModal = ref(false)
const showPaymentModal = ref(false)
const showReceiptModal = ref(false)
const selectedTable = ref(null)
const customerCount = ref(1)
const qrCodeUrl = ref(null)
const tableDetails = ref(null)
const selectedPaymentMethod = ref('CASH')
const currentReceipt = ref(null)

onMounted(() => {
  tableStore.fetchTables()
})

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const handleTableClick = async (table) => {
  selectedTable.value = table
  if (table.status === 'AVAILABLE') {
    customerCount.value = 1
    showOpenModal.value = true
  } else {
    try {
      tableDetails.value = await tableStore.fetchTableDetails(table.id)
      showDetailsModal.value = true
    } catch (error) {
      console.error(error)
    }
  }
}

const confirmOpenTable = async () => {
  if (!selectedTable.value) return
  
  try {
    const session = await tableStore.openTable(selectedTable.value.id, customerCount.value)
    showOpenModal.value = false
    qrCodeUrl.value = session.qrCodeUrl
    showQrModal.value = true
  } catch (error) {
    console.error('Failed to open table:', error)
  }
}

const confirmCloseTable = async () => {
  if (!selectedTable.value) return
  
  try {
    await tableStore.closeTable(selectedTable.value.id)
    showCloseModal.value = false
  } catch (error) {
    console.error('Failed to close table:', error)
  }
}

const confirmCloseTableFromDetails = async () => {
  showDetailsModal.value = false
  selectedPaymentMethod.value = 'CASH'
  showPaymentModal.value = true
}

const confirmPayment = async () => {
  if (!selectedTable.value) return

  try {
    const transaction = await tableStore.processPayment(selectedTable.value.id, selectedPaymentMethod.value)
    showPaymentModal.value = false
    
    // Fetch receipt details
    const response = await axios.get(`${API_URL}/api/transactions/${transaction.id}/receipt`)
    currentReceipt.value = response.data
    showReceiptModal.value = true
  } catch (error) {
    console.error('Failed to process payment:', error)
    alert('Payment failed')
  }
}

const viewQrCode = () => {
  if (selectedTable.value?.currentSession?.qrCodeUrl) {
    qrCodeUrl.value = selectedTable.value.currentSession.qrCodeUrl
    showCloseModal.value = false
    showQrModal.value = true
  }
}

const viewQrCodeFromDetails = () => {
  if (tableDetails.value?.currentSession?.qrCodeUrl) {
    qrCodeUrl.value = tableDetails.value.currentSession.qrCodeUrl
    selectedTable.value = tableDetails.value.table // Ensure selectedTable is set for the modal
    showDetailsModal.value = false
    showQrModal.value = true
  }
}

const getCustomerUrl = (token) => {
  return `${window.location.origin}/menu?token=${token}`
}

const copyToClipboard = async (text) => {
  try {
    if (navigator.clipboard && window.isSecureContext) {
      await navigator.clipboard.writeText(text)
    } else {
      // Fallback for non-secure contexts
      const textArea = document.createElement("textarea")
      textArea.value = text
      textArea.style.position = "fixed"
      textArea.style.left = "-9999px"
      textArea.style.top = "0"
      document.body.appendChild(textArea)
      textArea.focus()
      textArea.select()
      document.execCommand('copy')
      document.body.removeChild(textArea)
    }
    alert('URL copied to clipboard!')
  } catch (err) {
    console.error('Failed to copy:', err)
    alert('Failed to copy URL')
  }
}

// Table Management
const showTableFormModal = ref(false)
const editingTable = ref(null)
const tableForm = reactive({
  tableNumber: '',
  capacity: 4
})

const openAddTableModal = () => {
  editingTable.value = null
  tableForm.tableNumber = ''
  tableForm.capacity = 4
  showTableFormModal.value = true
}

const openEditTableModal = (table) => {
  editingTable.value = table
  tableForm.tableNumber = table.tableNumber
  tableForm.capacity = table.capacity
  showTableFormModal.value = true
}

const saveTable = async () => {
  try {
    if (editingTable.value) {
      await tableStore.updateTable(editingTable.value.id, tableForm)
    } else {
      await tableStore.createTable(tableForm)
    }
    showTableFormModal.value = false
    await tableStore.fetchTables()
  } catch (error) {
    console.error('Failed to save table:', error)
    alert('Failed to save table')
  }
}

const deleteTable = async () => {
  if (!editingTable.value) return
  if (!confirm(`Are you sure you want to delete Table ${editingTable.value.tableNumber}?`)) return

  try {
    await tableStore.deleteTable(editingTable.value.id)
    showTableFormModal.value = false
  } catch (error) {
    console.error('Failed to delete table:', error)
    alert('Failed to delete table')
  }
}
</script>
