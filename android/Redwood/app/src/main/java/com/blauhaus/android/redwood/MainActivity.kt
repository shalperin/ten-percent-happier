package com.blauhaus.android.redwood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


//val url = "http://10.0.2.2:5000/"
val url = "https://redwood-264500.appspot.com"
/*
IDEAS:
    + KOIN injection (repo)
    + Testing
 */


class MainActivity : AppCompatActivity() {

    val tag = "MAIN_ACTIVITY_ADDER"
    lateinit var viewModel: OurViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_add.setOnClickListener(performAddition)
        bindViewModel()
    }

    fun bindViewModel() {
        viewModel = ViewModelProviders.of(this).get(OurViewModel::class.java)
        viewModel.result.observe(this, Observer { result ->
            field_result.text = result.toString()
        })
        viewModel.errorMessage.observe(this, Observer { msg ->
            Toast.makeText(this, "error: " + msg, Toast.LENGTH_LONG).show()
        })
        viewModel.loading.observe(this, Observer { loading ->
            if (loading) {
                loading_notification.visibility = View.VISIBLE
            } else {
                loading_notification.visibility = View.GONE
            }
        })
    }

    val performAddition = { view: View ->
        var ra = field_a.parseFloat()
        var rb = field_b.parseFloat()
        if (ra is Resource.Error) {
            viewModel.errorMessage.postValue(ra.message)
        } else if (rb is Resource.Error) {
            viewModel.errorMessage.postValue(rb.message)
        } else {
            viewModel.add(ra.data ?: 0f, rb.data ?: 0f)
        }
    }
}

fun EditText.parseFloat(): Resource<Float> {
    try {
        return Resource.Success(this.text.toString().toFloat())
    } catch (e: Exception) {
        return Resource.Error(null, e.message?: "No details")
    }
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null, var refreshing: Boolean = false) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
}

class OurViewModel : ViewModel() {
    val repo = OurRepo()
    val result = MutableLiveData<Float>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun add(a:Float, b:Float) {
        GlobalScope.launch {
            loading.postValue(true)
            try {
                var response = repo.postAdd(a, b)
                result.postValue(response.sum)
            } catch (e:Exception) {
                errorMessage.postValue(e.message)
            } finally {
                loading.postValue(false)
            }
        }
        //TODO: See this doc for another approach that lets you peel out the response status code.
        // https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter/issues/16
    }
}

class OurRepo {
    fun shortTimeoutHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(shortTimeoutHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(Webservice::class.java)
    }

    suspend fun postAdd(a: Float, b:Float) = webservice.postAdd(a, b)
}

data class OurModel(val sum:Float) {}

interface Webservice {
    @POST("/add")
    @FormUrlEncoded
    suspend fun postAdd(@Field("a") a:Float, @Field("b") b:Float): OurModel
}