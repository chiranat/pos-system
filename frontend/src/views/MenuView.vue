<template>
  <div class="min-h-screen bg-slate-50 font-sans">
    <!-- Navbar -->
    <nav class="bg-white/80 backdrop-blur-md sticky top-0 z-30 border-b border-slate-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center gap-3">
            <div class="bg-gradient-to-br from-indigo-600 to-violet-600 p-2 rounded-lg shadow-lg shadow-indigo-200">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-violet-600">
              Menu Management
            </span>
          </div>
          <div class="flex items-center gap-4">
            <router-link 
              to="/" 
              class="text-slate-500 hover:text-indigo-600 font-medium text-sm transition-colors"
            >
              Back to Dashboard
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <!-- Tabs -->
      <div class="border-b border-slate-200 mb-8">
        <nav class="-mb-px flex space-x-8" aria-label="Tabs">
          <button
            @click="activeTab = 'categories'"
            :class="[
              activeTab === 'categories'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-slate-500 hover:text-slate-700 hover:border-slate-300',
              'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition-colors'
            ]"
          >
            Categories
          </button>
          <button
            @click="activeTab = 'products'"
            :class="[
              activeTab === 'products'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-slate-500 hover:text-slate-700 hover:border-slate-300',
              'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition-colors'
            ]"
          >
            Products
          </button>
          <button
            @click="activeTab = 'images'"
            :class="[
              activeTab === 'images'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-slate-500 hover:text-slate-700 hover:border-slate-300',
              'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition-colors'
            ]"
          >
            Images
          </button>
        </nav>
      </div>

      <!-- Categories Tab -->
      <div v-if="activeTab === 'categories'" class="space-y-6">
        <div class="flex justify-between items-center">
          <div>
            <h3 class="text-lg font-medium leading-6 text-slate-900">Categories</h3>
            <p class="mt-1 text-sm text-slate-500">Manage your menu categories and their display order.</p>
          </div>
          <button 
            @click="openCategoryModal()"
            class="inline-flex items-center px-4 py-2 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Add Category
          </button>
        </div>

        <div class="bg-white shadow-sm rounded-xl border border-slate-200 overflow-hidden">
          <table class="min-w-full divide-y divide-slate-200">
            <thead class="bg-slate-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Name</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Sort Order</th>
                <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-slate-200">
              <tr v-for="category in menuStore.categories" :key="category.id" class="hover:bg-slate-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-900">{{ category.name }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">{{ category.sortOrder }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button @click="openCategoryModal(category)" class="text-indigo-600 hover:text-indigo-900 mr-4">Edit</button>
                  <button @click="deleteCategory(category.id)" class="text-rose-600 hover:text-rose-900">Delete</button>
                </td>
              </tr>
              <tr v-if="menuStore.categories.length === 0">
                <td colspan="3" class="px-6 py-12 text-center text-slate-500">
                  No categories found. Create one to get started.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Products Tab -->
      <div v-if="activeTab === 'products'" class="space-y-6">
        <div class="flex justify-between items-center">
          <div>
            <h3 class="text-lg font-medium leading-6 text-slate-900">Products</h3>
            <p class="mt-1 text-sm text-slate-500">Manage your menu items, prices, and availability.</p>
          </div>
          <button 
            @click="openProductModal()"
            class="inline-flex items-center px-4 py-2 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Add Product
          </button>
        </div>

        <div class="bg-white shadow-sm rounded-xl border border-slate-200 overflow-hidden">
          <table class="min-w-full divide-y divide-slate-200">
            <thead class="bg-slate-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Product</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Category</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Price</th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">Status</th>
                <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-slate-200">
              <tr v-for="product in menuStore.products" :key="product.id" class="hover:bg-slate-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="h-10 w-10 flex-shrink-0">
                      <img v-if="product.imageUrl" :src="getImageUrl(product.imageUrl)" class="h-10 w-10 rounded-lg object-cover bg-slate-100" alt="" />
                      <div v-else class="h-10 w-10 rounded-lg bg-slate-100 flex items-center justify-center text-slate-400">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-slate-900">{{ product.name }}</div>
                      <div class="text-sm text-slate-500 truncate max-w-xs">{{ product.description }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-slate-100 text-slate-800">
                    {{ product.category?.name || 'Uncategorized' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-900 font-medium">฿{{ product.price }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="[
                    product.isAvailable ? 'bg-emerald-100 text-emerald-800' : 'bg-rose-100 text-rose-800',
                    'px-2 inline-flex text-xs leading-5 font-semibold rounded-full'
                  ]">
                    {{ product.isAvailable ? 'Available' : 'Unavailable' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button @click="openProductModal(product)" class="text-indigo-600 hover:text-indigo-900 mr-4">Edit</button>
                  <button @click="deleteProduct(product.id)" class="text-rose-600 hover:text-rose-900">Delete</button>
                </td>
              </tr>
              <tr v-if="menuStore.products.length === 0">
                <td colspan="5" class="px-6 py-12 text-center text-slate-500">
                  No products found. Create one to get started.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- Images Tab -->
      <div v-if="activeTab === 'images'" class="space-y-6">
        <div class="flex justify-between items-center">
          <div>
            <h3 class="text-lg font-medium leading-6 text-slate-900">Image Gallery</h3>
            <p class="mt-1 text-sm text-slate-500">Manage uploaded images.</p>
          </div>
          <div class="relative">
             <input type="file" @change="handleGalleryUpload" accept="image/*" class="hidden" id="gallery-upload">
             <label for="gallery-upload" class="cursor-pointer inline-flex items-center px-4 py-2 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" />
                </svg>
                Upload Image
             </label>
          </div>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
          <div v-for="image in images" :key="image.name" class="group relative aspect-square bg-slate-100 rounded-xl overflow-hidden border border-slate-200">
            <img :src="getImageUrl(image.url)" class="w-full h-full object-cover" loading="lazy">
            <div class="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
               <button @click="copyToClipboard(image.url)" class="p-2 bg-white rounded-full text-slate-700 hover:text-indigo-600" title="Copy URL">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                  </svg>
               </button>
               <button @click="deleteImageFile(image.name)" class="p-2 bg-white rounded-full text-slate-700 hover:text-rose-600" title="Delete">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
               </button>
            </div>
          </div>
          <div v-if="images.length === 0" class="col-span-full py-12 text-center text-slate-500">
            No images found. Upload one to get started.
          </div>
        </div>
      </div>

    </main>

    <!-- Category Modal -->
    <div v-if="showCategoryModal" class="relative z-50" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-slate-900/75 backdrop-blur-sm transition-opacity"></div>

      <div class="fixed inset-0 z-10 overflow-y-auto">
        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <div class="relative transform overflow-hidden rounded-2xl bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div class="mt-3 text-center sm:mt-0 sm:text-left w-full">
                <h3 class="text-lg leading-6 font-bold text-slate-900" id="modal-title">
                  {{ editingCategory ? 'Edit Category' : 'Add Category' }}
                </h3>
                <div class="mt-6 space-y-6">
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Name</label>
                    <input v-model="categoryForm.name" type="text" class="block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm py-2.5">
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Sort Order</label>
                    <input v-model="categoryForm.sortOrder" type="number" class="block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm py-2.5">
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-slate-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="saveCategory" type="button" class="w-full inline-flex justify-center rounded-xl border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm">
                Save
              </button>
              <button @click="closeCategoryModal" type="button" class="mt-3 w-full inline-flex justify-center rounded-xl border border-slate-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-slate-700 hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Product Modal -->
    <div v-if="showProductModal" class="relative z-50" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-slate-900/75 backdrop-blur-sm transition-opacity"></div>

      <div class="fixed inset-0 z-10 overflow-y-auto">
        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <div class="relative transform overflow-hidden rounded-2xl bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div class="mt-3 text-center sm:mt-0 sm:text-left w-full">
                <h3 class="text-lg leading-6 font-bold text-slate-900" id="modal-title">
                  {{ editingProduct ? 'Edit Product' : 'Add Product' }}
                </h3>
                <div class="mt-6 space-y-6">
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Name</label>
                    <input v-model="productForm.name" type="text" class="block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm py-2.5">
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Category</label>
                    <select v-model="productForm.category" class="block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm py-2.5">
                      <option :value="null">Select Category</option>
                      <option v-for="cat in menuStore.categories" :key="cat.id" :value="cat">
                        {{ cat.name }}
                      </option>
                    </select>
                  </div>
                  <div class="grid grid-cols-2 gap-4">
                    <div>
                      <label class="block text-sm font-medium text-slate-700 mb-1">Price</label>
                      <div class="relative rounded-md shadow-sm">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                          <span class="text-slate-500 sm:text-sm">฿</span>
                        </div>
                        <input v-model="productForm.price" type="number" class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pl-7 pr-12 sm:text-sm border-slate-300 rounded-xl py-2.5" placeholder="0.00">
                      </div>
                    </div>
                    <div class="flex items-center pt-6">
                      <input v-model="productForm.isAvailable" id="isAvailable" type="checkbox" class="h-5 w-5 text-indigo-600 focus:ring-indigo-500 border-slate-300 rounded">
                      <label for="isAvailable" class="ml-2 block text-sm text-slate-900">
                        Available for order
                      </label>
                    </div>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Description</label>
                    <textarea v-model="productForm.description" rows="3" class="block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm py-2.5"></textarea>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Product Image</label>
                    <div class="mt-1 flex items-center gap-4">
                      <div v-if="productForm.imageUrl" class="relative h-20 w-20 rounded-lg overflow-hidden border border-slate-200">
                        <img :src="getImageUrl(productForm.imageUrl)" class="h-full w-full object-cover" alt="Preview">
                        <button @click="productForm.imageUrl = ''" class="absolute top-0 right-0 bg-rose-500 text-white p-0.5 rounded-bl-lg hover:bg-rose-600">
                          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                          </svg>
                        </button>
                      </div>
                      <div class="flex-1">
                        <div class="flex gap-2 mb-2">
                           <button type="button" @click="openImagePicker" class="px-3 py-1.5 bg-indigo-100 text-indigo-700 rounded-lg text-sm font-medium hover:bg-indigo-200 transition-colors">
                             Select from Gallery
                           </button>
                        </div>
                        <input type="file" @change="handleFileUpload" accept="image/*" class="block w-full text-sm text-slate-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100 transition-all">
                        <p class="mt-1 text-xs text-slate-500">PNG, JPG, GIF up to 5MB</p>
                      </div>
                    </div>
                    <!-- Hidden input for manual URL entry if needed, or just store it -->
                    <input v-model="productForm.imageUrl" type="text" class="mt-2 block w-full rounded-xl border-slate-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm py-2.5 text-slate-400" placeholder="Image URL will appear here..." readonly>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-slate-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="saveProduct" type="button" class="w-full inline-flex justify-center rounded-xl border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm">
                Save
              </button>
              <button @click="closeProductModal" type="button" class="mt-3 w-full inline-flex justify-center rounded-xl border border-slate-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-slate-700 hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Image Picker Modal -->
    <div v-if="showImagePickerModal" class="relative z-[60]" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-slate-900/75 backdrop-blur-sm transition-opacity"></div>

      <div class="fixed inset-0 z-10 overflow-y-auto">
        <div class="flex min-h-full items-center justify-center p-4 text-center">
          <div class="relative transform overflow-hidden rounded-2xl bg-white text-left shadow-xl transition-all w-full max-w-4xl max-h-[80vh] flex flex-col">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 border-b border-slate-200 flex justify-between items-center">
              <h3 class="text-lg leading-6 font-bold text-slate-900">Select Image</h3>
              <button @click="showImagePickerModal = false" class="text-slate-400 hover:text-slate-500">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div class="p-6 overflow-y-auto">
              <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
                <div v-for="image in images" :key="image.name" 
                     @click="selectImageFromPicker(image.url)"
                     class="group relative aspect-square bg-slate-100 rounded-xl overflow-hidden border-2 border-transparent hover:border-indigo-500 cursor-pointer transition-all">
                  <img :src="image.url" class="w-full h-full object-cover" loading="lazy">
                  <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-colors"></div>
                </div>
              </div>
              <div v-if="images.length === 0" class="text-center py-12 text-slate-500">
                No images found. Please upload some images first.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { useMenuStore } from '../stores/menu'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const menuStore = useMenuStore()
const activeTab = ref('categories')
const images = ref([])

const getImageUrl = (path) => {
  if (!path) return null
  if (path.startsWith('http') || path.startsWith('data:')) return path
  return `${API_URL}/${path}`
}

// Category State
const showCategoryModal = ref(false)
const editingCategory = ref(null)
const categoryForm = reactive({
  name: '',
  sortOrder: 0
})

// Product State
const showProductModal = ref(false)
const editingProduct = ref(null)
const productForm = reactive({
  name: '',
  category: null,
  price: 0,
  description: '',
  imageUrl: '',
  isAvailable: true
})

onMounted(() => {
  menuStore.fetchMenu()
})

watch(activeTab, async (newTab) => {
  if (newTab === 'images') {
    await loadImages()
  }
})

const loadImages = async () => {
  try {
    images.value = await menuStore.fetchImages()
  } catch (error) {
    console.error('Failed to load images:', error)
  }
}

const handleGalleryUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  try {
    await menuStore.uploadImage(file)
    await loadImages()
  } catch (error) {
    console.error('Failed to upload image:', error)
    alert('Failed to upload image')
  }
}

const deleteImageFile = async (fileName) => {
  if (!confirm('Are you sure you want to delete this image?')) return
  try {
    await menuStore.deleteImage(fileName)
    await loadImages()
  } catch (error) {
    console.error('Failed to delete image:', error)
    alert('Failed to delete image')
  }
}

const copyToClipboard = (text) => {
  navigator.clipboard.writeText(text)
  alert('URL copied to clipboard!')
}

// Category Methods
const openCategoryModal = (category = null) => {
  editingCategory.value = category
  if (category) {
    categoryForm.name = category.name
    categoryForm.sortOrder = category.sortOrder
  } else {
    categoryForm.name = ''
    categoryForm.sortOrder = 0
  }
  showCategoryModal.value = true
}

const closeCategoryModal = () => {
  showCategoryModal.value = false
  editingCategory.value = null
}

const saveCategory = async () => {
  try {
    if (editingCategory.value) {
      await menuStore.updateCategory(editingCategory.value.id, categoryForm)
    } else {
      await menuStore.createCategory(categoryForm)
    }
    closeCategoryModal()
  } catch (error) {
    console.error('Failed to save category:', error)
    alert('Failed to save category')
  }
}

const deleteCategory = async (id) => {
  if (!confirm('Are you sure?')) return
  try {
    await menuStore.deleteCategory(id)
  } catch (error) {
    console.error('Failed to delete category:', error)
    alert('Failed to delete category')
  }
}

// Product Methods
const openProductModal = (product = null) => {
  editingProduct.value = product
  if (product) {
    productForm.name = product.name
    if (product.category) {
        productForm.category = menuStore.categories.find(c => c.id === product.category.id) || null
    } else {
        productForm.category = null
    }
    productForm.price = product.price
    productForm.description = product.description
    productForm.imageUrl = product.imageUrl
    productForm.isAvailable = product.isAvailable
  } else {
    productForm.name = ''
    productForm.category = null
    productForm.price = 0
    productForm.description = ''
    productForm.imageUrl = ''
    productForm.isAvailable = true
  }
  showProductModal.value = true
}

const closeProductModal = () => {
  showProductModal.value = false
  editingProduct.value = null
}

const saveProduct = async () => {
  try {
    if (editingProduct.value) {
      await menuStore.updateProduct(editingProduct.value.id, productForm)
    } else {
      await menuStore.createProduct(productForm)
    }
    closeProductModal()
  } catch (error) {
    console.error('Failed to save product:', error)
    alert('Failed to save product')
  }
}

const deleteProduct = async (id) => {
  if (!confirm('Are you sure?')) return
  try {
    await menuStore.deleteProduct(id)
  } catch (error) {
    console.error('Failed to delete product:', error)
    alert('Failed to delete product')
  }
}

const showImagePickerModal = ref(false)

const openImagePicker = async () => {
  await loadImages()
  showImagePickerModal.value = true
}

const selectImageFromPicker = (url) => {
  productForm.imageUrl = url
  showImagePickerModal.value = false
}

const handleFileUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  try {
    const url = await menuStore.uploadImage(file)
    productForm.imageUrl = url
  } catch (error) {
    console.error('Failed to upload image:', error)
    alert('Failed to upload image')
  }
}
</script>
