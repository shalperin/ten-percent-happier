package com.blauhaus.android.redwood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val url = "http://10.0.2.2:5000/"

class MainActivity : AppCompatActivity() {

    val tag = "MAIN_ACTIVITY_ADDER"
    lateinit var model: OurViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProviders.of(this).get(OurViewModel::class.java)
        button_add.setOnClickListener (performAddition)
        model.result.observe(this, Observer {
            result -> field_result.text = result.toString()
        })
    }

    val performAddition = { view: View ->
        var a =  0f
        var b = 0f
        try {
            a = field_a.text.toString().toFloat()
            b = field_b.text.toString().toFloat()
        } catch(e:Exception) {
            Log.d(tag, e.toString())
        }
        model.add(a,b)
    }
}

class OurViewModel : ViewModel() {

    val repo = OurRepo()
    val result = MutableLiveData<Float>()

    fun add(a:Float, b:Float) {
        GlobalScope.launch {
            var response = repo.postAdd(a, b)
            result.postValue(response.sum)
        }
    }
}

class OurRepo {
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl(url)
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