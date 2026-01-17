package com.rcm.engineering.user.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EmployeeViewModel : ViewModel() {

    val users = MutableLiveData<List<Employee>>()
    val error = MutableLiveData<String>()
    val responseMessage = MutableLiveData<String>()
    private val api = RetrofitClient.apiService

    fun fetchAllEmployees() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<List<Employee>> = api.fetchAllEmployee()
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    val sortedList = list.sortedByDescending { it.id }
                    withContext(Dispatchers.Main) {
                        users.value = sortedList
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        error.value = "Error: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error.value = e.message
                }
            }
        }
    }

    fun createUser(employee: Employee) {
        viewModelScope.launch {
            try {
                val result = api.createEmployee(employee)
                responseMessage.postValue("Employee Created: ${result.id}")
                fetchAllEmployees()
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }


    fun updateEmployee(empCode: String, employee: Employee) {
        viewModelScope.launch {
            try {
                val result = api.updateEmployee(empCode, employee)
                responseMessage.postValue("Employee Updated: ${result.ohr}")
                fetchAllEmployees()
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }


    fun deleteEmployee(id: Long) {
        viewModelScope.launch {
            try {
                val msg = api.deleteEmployee(id)
                responseMessage.postValue(msg)
                fetchAllEmployees()
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }
}
