<template>
  <div class="min-h-screen bg-slate-50 font-sans">
    <!-- Navbar -->
    <nav class="bg-white/80 backdrop-blur-md sticky top-0 z-30 border-b border-slate-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center gap-3 cursor-pointer" @click="$router.push('/')">
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
              <span class="text-sm font-semibold text-slate-600">{{ authStore.user?.username }}</span>
              <span class="text-xs text-slate-400 border-l border-slate-300 pl-2 ml-1">{{ authStore.userRole }}</span>
            </div>
            <router-link 
              to="/" 
              class="p-2 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-xl transition-all duration-200"
              title="Back to Home"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
        <div>
          <h2 class="text-3xl font-bold text-slate-800">User Management</h2>
          <p class="mt-1 text-slate-500">Manage system users and roles.</p>
        </div>
        <button 
          @click="openModal()"
          class="inline-flex items-center px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl shadow-lg shadow-indigo-200 font-medium transition-all duration-200"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          Add User
        </button>
      </div>

      <!-- Users Table -->
      <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-slate-200">
            <thead class="bg-slate-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Username</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Name</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Role</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Status</th>
                <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-slate-200">
              <tr v-for="user in users" :key="user.id" class="hover:bg-slate-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-900">{{ user.username }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">{{ user.firstName }} {{ user.lastName }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                    :class="{
                      'bg-purple-100 text-purple-800': user.role === 'ADMIN',
                      'bg-blue-100 text-blue-800': user.role === 'MANAGER',
                      'bg-green-100 text-green-800': user.role === 'WAITER',
                      'bg-orange-100 text-orange-800': user.role === 'KITCHEN'
                    }">
                    {{ user.role }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                    :class="user.isActive ? 'bg-emerald-100 text-emerald-800' : 'bg-red-100 text-red-800'">
                    {{ user.isActive ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button @click="openModal(user)" class="text-indigo-600 hover:text-indigo-900 mr-4">Edit</button>
                  <button @click="deleteUser(user.id)" class="text-rose-600 hover:text-rose-900">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <!-- Modal -->
    <Modal :isOpen="showModal" :title="isEditing ? 'Edit User' : 'Add New User'" @close="closeModal">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-slate-700">Username</label>
          <input v-model="form.username" type="text" :disabled="isEditing" class="mt-1 block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2.5">
        </div>
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-slate-700">First Name</label>
            <input v-model="form.firstName" type="text" class="mt-1 block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2.5">
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-700">Last Name</label>
            <input v-model="form.lastName" type="text" class="mt-1 block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2.5">
          </div>
        </div>
        <div>
          <label class="block text-sm font-medium text-slate-700">Role</label>
          <select v-model="form.role" class="mt-1 block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2.5">
            <option value="ADMIN">Admin</option>
            <option value="MANAGER">Manager</option>
            <option value="WAITER">Waiter</option>
            <option value="KITCHEN">Kitchen</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-slate-700">Password</label>
          <input v-model="form.password" type="password" :placeholder="isEditing ? 'Leave blank to keep current' : ''" class="mt-1 block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2.5">
        </div>
      </div>
      <template #footer>
        <button @click="saveUser" type="button" class="w-full inline-flex justify-center rounded-xl border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm">
          Save
        </button>
        <button @click="closeModal" type="button" class="mt-3 w-full inline-flex justify-center rounded-xl border border-slate-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-slate-700 hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
          Cancel
        </button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Modal from '../components/Modal.vue'

const authStore = useAuthStore()
const router = useRouter()
const users = ref([])
const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref(null)

const form = ref({
  username: '',
  firstName: '',
  lastName: '',
  role: 'WAITER',
  password: ''
})

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const fetchUsers = async () => {
  try {
    const response = await axios.get(`${API_URL}/api/users`, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    users.value = response.data
  } catch (error) {
    console.error('Error fetching users:', error)
  }
}

const openModal = (user = null) => {
  if (user) {
    isEditing.value = true
    editingId.value = user.id
    form.value = {
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      role: user.role,
      password: ''
    }
  } else {
    isEditing.value = false
    editingId.value = null
    form.value = {
      username: '',
      firstName: '',
      lastName: '',
      role: 'WAITER',
      password: ''
    }
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  form.value = { username: '', firstName: '', lastName: '', role: 'WAITER', password: '' }
}

const saveUser = async () => {
  try {
    const headers = { Authorization: `Bearer ${authStore.token}` }
    if (isEditing.value) {
      await axios.put(`${API_URL}/api/users/${editingId.value}`, form.value, { headers })
    } else {
      await axios.post(`${API_URL}/api/users`, form.value, { headers })
    }
    await fetchUsers()
    closeModal()
  } catch (error) {
    console.error('Error saving user:', error)
    alert(error.response?.data?.message || 'Error saving user')
  }
}

const deleteUser = async (id) => {
  if (!confirm('Are you sure you want to delete this user?')) return
  
  try {
    await axios.delete(`${API_URL}/api/users/${id}`, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    await fetchUsers()
  } catch (error) {
    console.error('Error deleting user:', error)
    alert('Error deleting user')
  }
}

onMounted(() => {
  fetchUsers()
})
</script>
