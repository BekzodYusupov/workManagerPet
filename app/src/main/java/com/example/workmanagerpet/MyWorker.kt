package com.example.workmanagerpet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay

/*class MyWorker(context:Context, workerParams:WorkerParameters):CoroutineWorker(context,workerParams) {
    override suspend fun doWork(): Result {
        val inputDataId = inputData.getString("id")
        val inputDataAmount = inputData
        Log.d("wwww","amount - ${inputDataAmount.getDouble("amount",0.0)}")
        return Result.success(inputDataAmount)
    }
}*/

class MyWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val inputDataId = inputData.getString("id")
        val inputDataAmount = inputData

        repeat(100){
            delay(1000)
            Log.d("wwww","index $it")
        }

        return Result.success()
    }

}
