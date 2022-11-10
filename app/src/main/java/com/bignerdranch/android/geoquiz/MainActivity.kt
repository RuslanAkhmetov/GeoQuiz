package com.bignerdranch.android.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

private const val TAG = "Main_Activity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate (index =  ${savedInstanceState?.getInt(KEY_INDEX)}) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0

        quizViewModel.currentIndex = currentIndex
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        questionTextView.setOnClickListener {
            nextButton.callOnClick()
        }

        nextButton.setOnClickListener {
            val result = (quizViewModel.moveToNext() * 100).toInt()
            if (result < 0) {
                updateQuestion()
            } else {
                Toast.makeText(
                    this,
                    "Ваш результат $result %",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            getResult.launch(intent)
            //startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        updateQuestion()
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {           //User does click "SHOW RESULT"
                quizViewModel.isCheater =
                    it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }
        }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstance() called")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        questionTextView.setText(quizViewModel.currentQuestionText)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val messageResId: Int
        if (quizViewModel.isCheater) {                           //User has sean Result
            messageResId = R.string.judgment_toast
        } else {
            if (quizViewModel.currentUserResult == null) {
                if (quizViewModel.currentQuestionAnswer == userAnswer) {
                    messageResId = R.string.correct_toast
                    quizViewModel.currentUserResult = true
                } else {
                    messageResId = R.string.incorrect_toast
                    quizViewModel.currentUserResult = false
                }
            } else {
                messageResId = R.string.answer_registered
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return            //User doesn't click "SHOW RESULT"

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }*/




}