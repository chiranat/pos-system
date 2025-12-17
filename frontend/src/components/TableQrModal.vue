<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm print:bg-white print:block print:static print:inset-auto">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden print:shadow-none print:w-full print:max-w-none print:rounded-none">
        <!-- Header (Hidden on print) -->
        <div class="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50 print:hidden">
          <h3 class="text-lg font-bold text-slate-900">Table QR Code</h3>
          <button @click="$emit('close')" class="p-2 hover:bg-slate-100 rounded-full transition-colors text-slate-400 hover:text-slate-600">
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <!-- QR Content -->
        <div class="p-8 print:p-0 flex flex-col items-center text-center">
          <div class="mb-6 print:mb-4">
            <div class="inline-flex items-center justify-center w-12 h-12 rounded-full bg-indigo-100 text-indigo-600 mb-3 print:hidden">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z" />
              </svg>
            </div>
            <h1 class="text-2xl font-bold text-slate-900 tracking-tight">POS System</h1>
            <p class="text-slate-500 text-sm mt-2">Scan to Order</p>
          </div>

          <div class="bg-white p-4 rounded-xl border-2 border-slate-100 mb-6 print:border-none print:p-0">
             <img v-if="qrCodeUrl" :src="qrCodeUrl" alt="Table QR Code" class="w-64 h-64 object-contain mx-auto">
          </div>

          <div class="mb-8">
            <h2 class="text-3xl font-bold text-slate-900 mb-1">Table {{ tableNumber }}</h2>
            <p class="text-slate-400 text-sm">{{ new Date().toLocaleString() }}</p>
          </div>

          <div class="text-xs text-slate-400 print:hidden">
             <p class="break-all max-w-xs mx-auto">{{ qrCodeUrl }}</p>
          </div>
          
          <div class="hidden print:block text-center text-xs text-slate-400 mt-8">
            <p>Please scan this QR code to view the menu and order.</p>
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
            @click="printQr" 
            class="px-4 py-2 bg-indigo-600 text-white rounded-xl font-medium hover:bg-indigo-700 shadow-lg shadow-indigo-200 hover:shadow-indigo-300 transition-all flex items-center gap-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z" />
            </svg>
            Print QR
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
const props = defineProps({
  isOpen: Boolean,
  qrCodeUrl: String,
  tableNumber: String
})

const emit = defineEmits(['close'])

const printQr = () => {
  window.print()
}
</script>
