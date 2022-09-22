package com.example.spotifylike.common.services

import android.util.Log
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.main.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1


class MainService(private val mainActivity: MainActivity) {
    val playlistService: PlaylistService = PlaylistService(this)
    val playerService: PlayerService = PlayerService(this, mainActivity)


    /** httpCallForObject: Generalise l'appel asynchrone aux endpoints de l'api
     *  param callback : Callback appelé après que l'appel soit effectué
     *  param apiCall : Endpoint appelé
     *  */
    @OptIn(DelicateCoroutinesApi::class)
    fun <T> httpCallForObject(
        callback: ObjectCallback<T>,
        apiCall: KSuspendFunction0<Response<T>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiCall()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    callback.callbackObject(content as T)
                } else {
                    Log.e("MainService", "httpCallForJson Error: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("MainService", "httpCallForJson Error Occurred: ${e.message}")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun <T> httpCallForObjectWithParams(
        callback: ObjectCallback<T>,
        apiCall: KSuspendFunction1<String, Response<T>>,
        param: String
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiCall(param)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    callback.callbackObject(content as T)
                } else {
                    Log.e("MainService", "httpCallForJsonWithParams Error: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("MainService", "httpCallForJsonWithParams Error Occurred: ${e.message}")
            }
        }
    }
}