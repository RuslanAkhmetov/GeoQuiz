package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d (TAG, "ViewModel instance about to be destroyed ")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true, null),
        Question(R.string.question_oceans, true, null),
        Question(R.string.question_mideast, false, null),
        Question(R.string.question_africa, false, null),
        Question(R.string.question_americas, true, null),
        Question(R.string.question_asia, true, null)
    )

    var currentIndex: Int = 0

    var isCheater = false

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].correctAnswer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].texResId

    var currentUserResult: Boolean?
        get() = questionBank[currentIndex].userResult
        set(value) {
            questionBank[currentIndex].userResult = value
        }

    fun moveToNext(): Double {
        currentIndex++
        if (currentIndex == questionBank.size) {
            currentIndex = 0
            return (questionBank.count { it.userResult == true }).toDouble() / questionBank.size
        } else return -1.0
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex == 0) questionBank.size - 1
                        else --currentIndex
    }
}