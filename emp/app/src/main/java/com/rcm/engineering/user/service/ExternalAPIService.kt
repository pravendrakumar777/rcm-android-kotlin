package com.rcm.engineering.user.service

import com.rcm.engineering.user.models.Challan
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.response.AttendanceResponse
import com.rcm.engineering.user.response.DashboardResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ExternalAPIService {

    // CREATE EMPLOYEE
    @POST("/api/employees/create")
    suspend fun createEmployee(@Body employee: Employee): Employee

    @GET("/api/employees/fetch/{id}")
    suspend fun getEmployeeById(@Path("id") id: Long): Employee

    @GET("api/employees/list")
    suspend fun fetchAllEmployee(): Response<List<Employee>>

    @GET("api/employees/search")
    suspend fun searchEmployee(@Query("query") query: String): Response<Employee>

    @DELETE("/api/employees/delete/{id}")
    suspend fun deleteEmployee(@Path("id") id: Long): String

    @PUT("/api/employees/update/{ohr}")
    suspend fun updateEmployee(@Path("ohr") ohr: String, @Body employee: Employee): Employee

    @POST("/api/attendance/mark/{ohr}")
    fun markAttendance(@Query("ohr") ohr: String, @Query("date") date: String, @Query("status") status: String, @Query("checkInDateTime") checkInDateTime: String?, @Query("checkOutDateTime") checkOutDateTime: String?): Call<AttendanceResponse>

    @GET("/api/attendance/{ohr}")
    fun getAttendance(@Path("ohr") ohr: String, @Query("date") date: String): Call<AttendanceResponse>

    @GET("/api/attendance/{ohr}")
    suspend fun getAttendanceForEmployee(@Path("ohr") ohr: String): Response<AttendanceResponse>

    @GET("/api/attendance/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>

    // Challan Reports
    @GET("api/challans")
    fun getChallans(): Call<List<Challan>>

    @GET("/api/challan/download/{id}")
    @Streaming
    fun downloadPDFChallan(@Path("id") id: Long): Call<ResponseBody>

    @GET("/api/challan/download/excel/{id}")
    @Streaming
    fun downloadExcelChallan(@Path("id") id: Long): Call<ResponseBody>

    @GET("/api/challan/download/csv/{id}")
    @Streaming
    fun downloadCSVChallan(@Path("id") id: Long): Call<ResponseBody>
}