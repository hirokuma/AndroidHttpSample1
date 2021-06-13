package com.example.httpsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call

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
        fun getRawResponseForPosts(@Path("id") id: String): Call<ResponseBody>

        @GET("/users/{id}")
        suspend fun getUser(@Path("id") id: String): User
    }

    class UserRepository constructor(
        private val userService: UserService
    ) {
        suspend fun getUserById(id: String): User {
            return userService.getUser(id)
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.github.com/")
            }.build()

            val service = retrofit.create(UserService::class.java)
            val repos = UserRepository(service)

            scope.launch {
                // コルーチンとかsuspend functionからしか呼べない
                val koko = service.getRawResponseForPosts("hirokuma")
                val responseBody = koko.execute()
                responseBody.body()?.let {
                    Log.d("launch", it.string())
                }
//                val user = repos.getUserById("hirokuma")
//                Log.d("launch", user.toString());
            }
        }).start()

    }
}