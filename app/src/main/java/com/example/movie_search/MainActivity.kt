package com.example.movie_search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movie_search.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding //데이터 바인딩
    val clientId = "C2jyA9RotsAvaTVEIGS1"
    val clientSecret = "Y3tcktybWT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.act = this

        binding.btnSearchMovie.setOnClickListener {

            if (binding.edtSearchName.text.isEmpty()) {
                //입력창이 비어있으면 return
                return@setOnClickListener
            }
            binding.rvMovieInfoList.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvMovieInfoList.setHasFixedSize(true)
            //API
            fetchJson(binding.edtSearchName.text.toString())

            //fetchJson 함수

            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.edtSearchName.windowToken, 0)
            //키보드 숨기기


        }




    //funfetchJson / okHttp

    }

    fun fetchJson(vararg str : String){
        val text = URLEncoder.encode("${str[0]}","UTF-8")
        println(text)
        val url = URL("https://openapi.naver.com/v1/search/movie.json?query=${text}&display=10&start=1&genre=")

        /*val formBody = FormBody.Builder()
            .add("query", "${text}")
            .add("display", "10")
            .add("start", "1")
            .add("genre", "1")
            .build()*/

        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientId)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .method("GET", null)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                print("성공 : $body")

                val gson = GsonBuilder().create()

                val homefeed = gson.fromJson(body, Homefeed::class.java)

                runOnUiThread{
                    binding.rvMovieInfoList.adapter = RecyclerViewAdapter(homefeed)
                    binding.edtSearchName.setText("")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("실패")
            }
        })
    }

    data class Homefeed (val items : List<Item>)
    data class Item(
        val title : String,
        val link : String,
        val image : String,
        val subtitle : String,
        val pubDate : String,
        val director : String,
        val actor : String,
        val usrRating : String
    )
}