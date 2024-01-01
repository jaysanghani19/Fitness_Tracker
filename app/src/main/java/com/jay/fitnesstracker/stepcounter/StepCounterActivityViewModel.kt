package com.jay.fitnesstracker.stepcounter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jay.fitnesstracker.R
import java.util.Timer
import java.util.TimerTask

class StepCounterActivityViewModel : ViewModel() {
    private val _initialStep = MutableLiveData<Float>()
    val initialStep: LiveData<Float>
        get() = _initialStep


    private val _stepByUser = MutableLiveData<Float>()
    val stepsByUser: LiveData<Float>
        get() = _stepByUser

    private val _distance = MutableLiveData<Float>()
    val distance: LiveData<Float>
        get() = _distance

    private val distancePerStep = 0.762f

    private var timer: Timer? = null
    private var secondsElapsed = 0

    private val _elapsedTime = MutableLiveData<Int>()
    val elapsedTime: LiveData<Int> get() = _elapsedTime

    init {
        _initialStep.value = 0f
        _elapsedTime.value = 0
    }

    public fun setStepsAndDistance(totalSteps: Float) {
        _stepByUser.value = totalSteps - _initialStep.value!!
        _distance.value = ((_stepByUser.value)?.times(distancePerStep))?.div(1000f)
        Log.i("Steps", "Total Steps: $totalSteps Steps By User : ${_stepByUser.value}")
    }

    public fun setInitialStep(initialStep: Float) {
        _initialStep.value = initialStep
    }

    fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                secondsElapsed++
                _elapsedTime.postValue(secondsElapsed)
            }
        }, 0, 1000)
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}