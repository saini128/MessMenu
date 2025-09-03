package com.singhropar.messmenu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singhropar.messmenu.data.MessMenuItem
import com.singhropar.messmenu.data.MessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessViewModel : ViewModel() {
    private val repository = MessRepository()

    private val _menu = MutableStateFlow<List<MessMenuItem>>(emptyList())
    val menu: StateFlow<List<MessMenuItem>> = _menu

    init {
        fetchMenu()
    }

    fun fetchMenu() {
        viewModelScope.launch {
            try {
                val data = repository.getMenu()
                _menu.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
