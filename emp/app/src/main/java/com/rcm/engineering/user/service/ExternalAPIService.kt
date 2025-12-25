package com.rcm.engineering.user.service

import com.rcm.engineering.user.models.Challan
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.response.AttendanceResponse
import com.rcm.engineering.user.response.DashboardResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ExternalAPIService {

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
    suspend fun updateEmployee(@Path("empCode") empCode: String, @Body employee: Employee): Employee

    @POST("/api/attendance/mark/{empCode}")
    fun markAttendance(@Query("empCode") empCode: String, @Query("date") date: String, @Query("status") status: String, @Query("checkInDateTime") checkInDateTime: String?, @Query("checkOutDateTime") checkOutDateTime: String?): Call<AttendanceResponse>

    @GET("/api/attendance/{empCode}")
    suspend fun getAttendanceForEmployee(@Path("empCode") empCode: String): Response<AttendanceResponse>

    @GET("/api/attendance/dashboard") suspend fun getDashboard(): Response<DashboardResponse>

    // Challan Reports
    @GET("api/challans") fun getChallans(): Call<List<Challan>>
}