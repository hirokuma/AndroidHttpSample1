package com.example.httpsample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : AppCompatActivity() {
    // https://api.github.com/users/hirokuma
    //
    //login: "hirokuma"
    //id: 193099
    //...
    data class User(
        val login: String,
        val id: Int
    )

    interface UserService {
        @GET("/users/{id}")
        fun getUser(@Path("id") id: String): Call<User>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.github.com/")
                addConverterFactory(GsonConverterFactory.create())
            }.build()

            val service = retrofit.create(UserService::class.java)
            val call = service.getUser("hirokuma")
            val res = call.execute().body()
            Log.d("launch", res.toString())
        }).start()
    }
}