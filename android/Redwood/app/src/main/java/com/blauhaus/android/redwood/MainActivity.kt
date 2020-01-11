package com.blauhaus.android.redwood

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.features.calculator.CalculatorFragment
import com.blauhaus.android.redwood.features.calculator.CalculatorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/*
IDEAS:
    + KOIN injection (repo)
    + Testing
 */


class MainActivity : AppCompatActivity() {
    val tag = "MAIN_ACTIVITY_ADDER"
    val calc1Id = 1
    val calc2Id = 2
    val model by viewModel<CalculatorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCalculator1()
        addCalculator2()

        model.result(calc1Id)?.observe(this, Observer {
            result -> Toast.makeText(this, "model1: " + result.toString(), Toast.LENGTH_LONG).show()
        })
        model.result(calc2Id)?.observe(this, Observer {
                result -> Toast.makeText(this, "model2: " + result.toString(), Toast.LENGTH_LONG).show()
        })

    }

    fun addCalculator1() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val frag = CalculatorFragment.newInstance(calc1Id)
        fragmentTransaction.add(R.id.calculator1, frag,"calc1tag")
        fragmentTransaction.commit()
    }

    fun addCalculator2() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val frag = CalculatorFragment.newInstance(calc2Id)
        fragmentTransaction.add(R.id.calculator2, frag,"calc2tag")
        fragmentTransaction.commit()
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



