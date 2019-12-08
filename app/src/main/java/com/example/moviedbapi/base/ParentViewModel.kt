package com.example.moviedbapi.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class ParentViewModel : ViewModel() {
    private val parentJob = SupervisorJob()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    protected val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    protected abstract fun handleError(e: Throwable)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}