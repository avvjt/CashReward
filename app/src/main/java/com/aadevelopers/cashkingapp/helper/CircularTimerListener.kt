package com.aadevelopers.cashkingapp.helper

interface CircularTimerListener {
    fun updateDataOnTick(remainingTimeInMs: Long): String?
    fun onTimerFinished()
}