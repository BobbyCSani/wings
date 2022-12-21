package com.store.wings.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.wings.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    private val _authLiveData = MutableLiveData<String?>()
    val authLiveData: LiveData<String?>
        get() = _authLiveData

    fun login(username: String, password: String) = viewModelScope.launch {
        repository.login(username, password).collect{
            _authLiveData.postValue(it)
        }
    }

    fun register(username: String, password: String) = viewModelScope.launch {
        repository.register(username, password).collect{
            _authLiveData.postValue(it)
        }
    }

}