package com.aadevelopers.cashkingapp

interface UpdateListener {
    fun UpdateListener(
        coin: String?,
        time: String?,
        link: String?,
        browser: String?,
        id: String?,
        index: Int,
        description: String?,
        logo: String?,
        packages: String?,
    )
}