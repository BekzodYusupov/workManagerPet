package com.example.workmanagerpet

import androidx.work.Data
import androidx.work.InputMerger

class CustomInputMerger : InputMerger() {
    override fun merge(inputs: MutableList<Data>): Data {
        var totalAmount = 0.0
        inputs.forEach {
            val amount = it.getDouble("amount", 0.0)
            totalAmount += amount
        }
        return Data.Builder().putDouble("amount", totalAmount).build()
    }
}

