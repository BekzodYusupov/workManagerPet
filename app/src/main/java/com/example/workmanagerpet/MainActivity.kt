package com.example.workmanagerpet

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.work.ArrayCreatingInputMerger
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.InputMerger
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.model.WorkTypeConverters.StateIds.ENQUEUED
import com.example.workmanagerpet.ui.theme.WorkManagerPetTheme
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

const val workTag = "work1"
val uUIDWorker: UUID = UUID.randomUUID()

class MainActivity : ComponentActivity() {
    val workManager = WorkManager
        .getInstance(this@MainActivity)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val constraints = Constraints(
            requiredNetworkType = NetworkType.CONNECTED,
            requiresCharging = false,
            requiresDeviceIdle = false,
            requiresBatteryNotLow = true,
            requiresStorageNotLow = true,
            contentTriggerMaxDelayMillis = 1,//no idea
            contentTriggerUpdateDelayMillis = 1,//no idea
            contentUriTriggers = setOf()//no idea
        )

        val inputData = Data.Builder()
            .putString("id","workerId")
            .putInt("number",77777777)
            .putDouble("amount",99.99)
            .build()


/*        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .addTag(workTag)
            .setId(uUIDWorker)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofSeconds(10))
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
//            .setInputMerger()
            .keepResultsForAtLeast(Duration.ofSeconds(100))
            .setConstraints(constraints)
            .setInitialDelay(Duration.ofSeconds(10))
            .setInitialRunAttemptCount(5)
            .setInitialState(WorkInfo.State.BLOCKED)
            .setInputData(inputData)
            .setScheduleRequestedAt(1000, TimeUnit.SECONDS)
            .build()*/


        workManager.getWorkInfoByIdLiveData(uUIDWorker)
            .observe(this) { workInfo->
                if (workInfo != null) {
                    when(workInfo.state){
                        WorkInfo.State.ENQUEUED -> {
                            Log.d("wwww","Work request is enqueued but not yet started.")
                        }
                        WorkInfo.State.RUNNING -> {
                            Log.d("wwww","Work request is currently running..")
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            Log.d("wwww","Work request has completed successfully.")
                        }
                        WorkInfo.State.FAILED -> {
                            Log.d("wwww","Work request has failed.")
                        }
                        WorkInfo.State.CANCELLED -> {
                            Log.d("wwww","Work request has been cancelled.")
                        }

                        else -> {}
                    }
                }
            }

        //set custom inputMerger example
        val workRequest1 = OneTimeWorkRequestBuilder<MyWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(Data.Builder().putDouble("amount",100.0).build())
            .build()

        val workRequest2 = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(Data.Builder().putDouble("amount",50.0).build())
            .build()

        val mergedRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputMerger(CustomInputMerger::class.java)
            .build()

        setContent {
            WorkManagerPetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        Button(onClick = {
                            workManager
                                .enqueue(workRequest1)
                        },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(text = "start workManager")
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("wwww", "Activity Stop start")
        workManager.getWorkInfoByIdLiveData(uUIDWorker)
            .observe(this) { workInfo->
                if (workInfo != null) {
                    when(workInfo.state){
                        WorkInfo.State.ENQUEUED -> {
                            Log.d("wwww","Work request is enqueued but not yet started.")
                        }
                        WorkInfo.State.RUNNING -> {
                            Log.d("wwww","Work request is currently running..")
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            Log.d("wwww","Work request has completed successfully.")
                        }
                        WorkInfo.State.FAILED -> {
                            Log.d("wwww","Work request has failed.")
                        }
                        WorkInfo.State.CANCELLED -> {
                            Log.d("wwww","Work request has been cancelled.")
                        }

                        else -> {}
                    }
                }
            }
        Log.d("wwww", "Activity Stop end")

    }
}
