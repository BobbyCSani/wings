package com.store.wings.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.wings.model.TransactionModel
import com.store.wings.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val repository: TransactionRepository): ViewModel() {

    private val _cartLiveData = MutableLiveData<Boolean>()
    val cartLiveData: LiveData<Boolean>
        get() = _cartLiveData

    private val _transactionLiveData = MutableLiveData<List<TransactionModel>>()
    val transactionLiveData: LiveData<List<TransactionModel>>
        get() = _transactionLiveData

    private val _transactionHeaderLiveData = MutableLiveData<String>()
    val transactionHeaderLiveData: LiveData<String>
        get() = _transactionHeaderLiveData

    fun changeQty(username: String, data: TransactionModel, quantity: Int) = viewModelScope.launch {
        repository.changeQty(username, data, quantity).collect{
            _cartLiveData.postValue(it)
        }
    }

    fun getTransactions(username: String) = viewModelScope.launch {
        repository.getTransactions(username).collect{
            _transactionLiveData.postValue(it)
        }
    }

    fun getTransactionHeader(username: String) = viewModelScope.launch {
        repository.getTransactionHeader(username).collect{
            _transactionHeaderLiveData.postValue(it)
        }
    }

    fun setTransactionHeader(username: String, list: List<TransactionModel>) = viewModelScope.launch {
        repository.setTransactionHeader(username, list)
    }
}