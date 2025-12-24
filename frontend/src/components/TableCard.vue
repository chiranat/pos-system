<template>
  <div 
    class="group relative bg-white rounded-2xl p-6 shadow-sm border border-slate-100 hover:shadow-xl hover:border-indigo-100 transition-all duration-300 cursor-pointer overflow-hidden"
    @click="$emit('click', table)"
  >
    <!-- Background Decoration -->
    <div 
      class="absolute -right-6 -top-6 w-24 h-24 rounded-full opacity-5 transition-transform duration-500 group-hover:scale-150"
      :class="isOccupied ? 'bg-rose-500' : 'bg-emerald-500'"
    ></div>

    <!-- Status Badge -->
    <div class="flex justify-between items-start mb-4">
      <div 
        class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border transition-colors duration-300"
        :class="isOccupied 
          ? 'bg-rose-50 text-rose-700 border-rose-100' 
          : 'bg-emerald-50 text-emerald-700 border-emerald-100'"
      >
        <span 
          class="w-1.5 h-1.5 rounded-full mr-1.5"
          :class="isOccupied ? 'bg-rose-500' : 'bg-emerald-500 animate-pulse'"
        ></span>
        {{ isOccupied ? 'Occupied' : 'Available' }}
      </div>
      
      <!-- Icon -->
      <div class="flex gap-2">
        <button 
          @click.stop="$emit('edit', table)"
          class="p-2 rounded-xl bg-slate-50 text-slate-400 hover:bg-indigo-50 hover:text-indigo-500 transition-colors duration-300 z-10"
          title="Edit Table"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
          </svg>
        </button>
        <div 
          class="p-2 rounded-xl transition-colors duration-300"
          :class="isOccupied ? 'bg-rose-50 text-rose-500' : 'bg-slate-50 text-slate-400 group-hover:bg-emerald-50 group-hover:text-emerald-500'"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
        </div>
      </div>
    </div>

    <!-- Content -->
    <div class="relative">
      <h3 class="text-2xl font-bold text-slate-800 group-hover:text-indigo-600 transition-colors">
        Table {{ table.tableNumber }}
      </h3>
      <p class="text-sm text-slate-400 mt-1">Capacity: {{ table.capacity }} seats</p>
      
      <!-- Occupied Details -->
      <div v-if="isOccupied && table.currentSession" class="mt-4 pt-4 border-t border-slate-100">
        <div class="flex items-center text-sm text-rose-600 font-medium">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
          </svg>
          {{ table.currentSession.customerCount }} Customers
        </div>
      </div>

      <!-- Action Hint -->
      <div v-else class="mt-6 flex items-center text-sm font-medium text-indigo-600 opacity-0 transform translate-y-2 group-hover:opacity-100 group-hover:translate-y-0 transition-all duration-300">
        <span>Manage Table</span>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  table: {
    type: Object,
    required: true
  }
})

const isOccupied = computed(() => props.table.status === 'OCCUPIED')
</script>
