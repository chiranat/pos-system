<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm print:bg-white print:block print:static print:inset-auto">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden print:shadow-none print:w-full print:max-w-none print:rounded-none">
        <!-- Header (Hidden on print) -->
        <div class="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50 print:hidden">
          <h3 class="text-lg font-bold text-slate-900">Receipt Preview</h3>
          <button @click="$emit('close')" class="p-2 hover:bg-slate-100 rounded-full transition-colors text-slate-400 hover:text-slate-600">
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <!-- Receipt Content -->
        <div class="p-8 print:p-0" id="receipt-content">
          <div class="text-center mb-8">
            <div class="inline-flex items-center justify-center w-12 h-12 rounded-full bg-indigo-100 text-indigo-600 mb-3 print:hidden">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <h1 class="text-2xl font-bold text-slate-900 tracking-tight">POS System</h1>
            <p class="text-slate-500 text-sm mt-2">123 Restaurant St., Bangkok</p>
            <p class="text-slate-500 text-sm">Tel: 02-123-4567</p>
          </div>

          <div class="bg-slate-50 rounded-xl p-4 mb-6 border border-slate-100 print:bg-transparent print:border-none print:p-0">
            <div class="flex justify-between mb-2 text-sm">
              <span class="text-slate-500">Date</span>
              <span class="font-medium text-slate-900">{{ formatDate(receipt?.paidAt) }}</span>
            </div>
            <div class="flex justify-between mb-2 text-sm">
              <span class="text-slate-500">Table</span>
              <span class="font-medium text-slate-900">{{ receipt?.tableNumber }}</span>
            </div>
            <div class="flex justify-between text-sm">
              <span class="text-slate-500">Transaction ID</span>
              <span class="font-mono text-xs bg-white px-2 py-0.5 rounded border border-slate-200 print:border-none print:bg-transparent print:p-0 print:text-sm">
                {{ receipt?.transactionId?.slice(0, 8) }}
              </span>
            </div>
          </div>

          <div class="space-y-4 mb-8">
            <div v-for="(item, index) in receipt?.items" :key="index" class="flex justify-between text-sm group">
              <div class="flex-1 pr-4">
                <div class="font-medium text-slate-900">{{ item.productName }}</div>
                <div class="text-slate-500 text-xs mt-0.5">{{ item.quantity }} x {{ formatCurrency(item.price) }}</div>
              </div>
              <div class="font-bold text-slate-900">{{ formatCurrency(item.subtotal) }}</div>
            </div>
          </div>

          <div class="border-t-2 border-dashed border-slate-200 pt-6 mb-8">
            <div class="flex justify-between items-center mb-3">
              <span class="text-slate-600">Subtotal</span>
              <span class="font-medium text-slate-900">{{ formatCurrency(receipt?.totalAmount) }}</span>
            </div>
            <div class="flex justify-between items-center text-xl font-bold text-slate-900">
              <span>Total</span>
              <span>{{ formatCurrency(receipt?.totalAmount) }}</span>
            </div>
            <div class="flex justify-between items-center mt-4 text-sm">
              <span class="text-slate-500">Payment Method</span>
              <span class="font-medium text-slate-900 bg-slate-100 px-2 py-1 rounded print:bg-transparent print:p-0">
                {{ receipt?.paymentMethod }}
              </span>
            </div>
          </div>

          <div class="text-center text-xs text-slate-400 print:mt-12">
            <p>Thank you for dining with us!</p>
            <p class="mt-1">Please come again.</p>
          </div>
        </div>

        <!-- Footer (Hidden on print) -->
        <div class="px-6 py-4 bg-slate-50 border-t border-slate-200 flex justify-end gap-3 print:hidden">
          <button 
            @click="$emit('close')" 
            class="px-4 py-2 bg-white border border-slate-300 rounded-xl text-slate-700 font-medium hover:bg-slate-50 hover:border-slate-400 transition-all"
          >
            Close
          </button>
          <button 
            @click="printReceipt" 
            class="px-4 py-2 bg-indigo-600 text-white rounded-xl font-medium hover:bg-indigo-700 shadow-lg shadow-indigo-200 hover:shadow-indigo-300 transition-all flex items-center gap-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z" />
            </svg>
            Print Receipt
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
const props = defineProps({
  isOpen: Boolean,
  receipt: Object
})

const emit = defineEmits(['close'])

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('th-TH', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('th-TH', {
    style: 'currency',
    currency: 'THB'
  }).format(amount)
}

const printReceipt = () => {
  window.print()
}
</script>

<style>
@media print {
  #app {
    display: none;
  }
  body {
    background: white;
  }
}
</style>
