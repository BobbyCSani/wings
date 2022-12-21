package com.store.wings.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.wings.model.ProductModel
import com.store.wings.repository.ProductRepository
import com.store.wings.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val transactionRepo: TransactionRepository): ViewModel() {

    private val _productLiveData = MutableLiveData<List<ProductModel>>()
    val productLiveData: LiveData<List<ProductModel>>
    get() = _productLiveData

    private val _transactionLiveData = MutableLiveData<Boolean>()
    val transactionLiveData: LiveData<Boolean>
        get() = _transactionLiveData

    fun getAllProduct() = viewModelScope.launch {
        productRepo.getAllProduct().collect{ result ->
            _productLiveData.postValue(result)
        }
    }

    fun buyProduct(username: String, data: ProductModel) = viewModelScope.launch {
        transactionRepo.buyProduct(username, data).collect{
            _transactionLiveData.postValue(it)
        }
    }

}