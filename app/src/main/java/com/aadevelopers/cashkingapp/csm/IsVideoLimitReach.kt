package com.aadevelopers.cashkingapp.csm

interface IsVideoLimitReach {
    fun onVideoLimitReach(videoLimitReach: Boolean, isError: Boolean)
}