package com.rcm.engineering.user.service

import com.rcm.engineering.user.models.Employee
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // CREATE USER
    @POST("/api/employees/create")
    suspend fun createEmployee(@Body employee: Employee): Employee

    @GET("/api/employees/fetch/{id}")
    suspend fun getEmployeeById(@Path("id") id: Long): Employee

    @GET("api/employees/list")
    suspend fun fetchAllEmployee(): Response<List<Employee>>

    @DELETE("/api/employees/delete/{id}")
    suspend fun deleteEmployee(@Path("id") id: Long): String

    @PUT("/api/employees/update/{empCode}")
    suspend fun updateEmployee(@Path("empCode") empCode: String): Employee

}