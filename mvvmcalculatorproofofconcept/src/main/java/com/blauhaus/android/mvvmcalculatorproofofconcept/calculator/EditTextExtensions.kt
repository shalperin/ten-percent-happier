package com.blauhaus.android.mvvmcalculatorproofofconcept.calculator

import android.widget.EditText
import com.blauhaus.android.mvvmcalculatorproofofconcept.calculator.Resource


fun EditText.parseFloat(): com.blauhaus.android.mvvmcalculatorproofofconcept.calculator.Resource<Float> {
    try {
        return com.blauhaus.android.mvvmcalculatorproofofconcept.calculator.Resource.Success(this.text.toString().toFloat())
    } catch (e: Exception) {
        return com.blauhaus.android.mvvmcalculatorproofofconcept.calculator.Resource.Error(null, e.message?: "No details")
    }
}