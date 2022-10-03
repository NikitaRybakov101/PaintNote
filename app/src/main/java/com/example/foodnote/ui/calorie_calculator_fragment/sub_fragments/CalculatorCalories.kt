package com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments

import com.example.foodnote.data.sharedPreference.SharedPreference
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.profile.ProfileSettings

object CalculatorCalories {

    fun getMaxCalories(activity: MainActivity): Int {

        var weight = SharedPreference.loadDataString(ProfileSettings.WEIGHT, activity)?.toInt()
        var height = SharedPreference.loadDataString(ProfileSettings.HEIGHT, activity)?.toInt()
        var age = SharedPreference.loadDataString(ProfileSettings.AGE, activity)?.toInt()

        if (weight == null) weight = 0
        if (height == null) height = 0
        if (age == null) age = 0

        val calories = if (SharedPreference.loadDataBoolean(ProfileSettings.MALE, activity)) {
            (10 * weight) + (6.25f * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25f * height) - (5 * age) - 161
        }

        return calories.toInt()
    }

}