package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

data class Question(
    @StringRes val texResId: Int,
    val correctAnswer: Boolean,
    var userResult: Boolean?
)