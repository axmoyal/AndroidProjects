package edu.stanford.axmoyal.miniyelp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val BASE_URL = "https://api.yelp.com/v3/"
private const val TAG = "MainActivity"

private const val API_KEY="aSPhGFtX3qy2-7XpTENhSNzPX8ySxP5Cuqemzn52275DjAeQcP11y6HhrgbWqCIoegpTPoT5QqEpm5qj3fo7GxaTc5AbohFIAw-MKG2UbEQ0PXd3kgJUC9tb2G-dX3Yx"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter = RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

        val yelpService = retrofit.create(YelpService::class.java)

        //val b=isOnline();
        //Log.i(TAG, "Internet : $b")

        yelpService.searchRestaurants("Bearer $API_KEY", "Avocado Toast", "New York").enqueue(object :
            Callback<YelpSearchResult> {

            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Didn't receive any valid response body from Yelp ")
                    return
                }
                restaurants.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "Failure $t")
            }
        })
    }
    fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            Log.i(TAG, "Inside Internet")
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            Log.i(TAG, "Exit : $exitValue")
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

}