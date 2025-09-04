package com.singhropar.messmenu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.singhropar.messmenu.data.*
import com.singhropar.messmenu.ui.widget.MessMenuWidgetProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MessViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MessRepository()
    private val prefs = UserPreferences(app)

    private val _menu = MutableStateFlow<List<MessMenuItem>>(emptyList())
    val menu: StateFlow<List<MessMenuItem>> = _menu.asStateFlow()

    private val _selectedHostel = MutableStateFlow<String?>(null)
    val selectedHostel: StateFlow<String?> = _selectedHostel.asStateFlow()

    init {
        // Load cached data + selected hostel
        viewModelScope.launch {
            prefs.getCachedMenu().collect { json ->
                if (!json.isNullOrEmpty()) {
                    val type = object : TypeToken<List<MessMenuItem>>() {}.type
                    _menu.value = Gson().fromJson(json, type)
                } else {
                    refreshMenu()
                }
            }
        }
        viewModelScope.launch {
            prefs.getSelectedHostel().collect { hostel ->
                _selectedHostel.value = hostel
            }
        }
    }

    fun refreshMenu() {
        viewModelScope.launch {
            try {
                val result = repository.getMenu()
                _menu.value = result
                val json = Gson().toJson(result)
                prefs.saveCachedMenu(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        MessMenuWidgetProvider.refreshAllWidgets(getApplication())

    }

    fun selectHostel(hostel: String) {
        _selectedHostel.value = hostel
        viewModelScope.launch {
            prefs.saveSelectedHostel(hostel)
        }
    }
}
