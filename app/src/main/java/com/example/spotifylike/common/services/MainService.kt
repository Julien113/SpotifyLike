package com.example.spotifylike.common.services

import android.util.Log
import com.example.spotifylike.common.model.APIPlaylist
import com.example.spotifylike.common.utils.ObjectCallback
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.reflect.KSuspendFunction0


class MainService() {
    val playerService: PlayerService = PlayerService(this)


    /** httpCallForObject: Generalise l'appel asynchrone aux endpoints de l'api
     *  param callback : Callback appelé après que l'appel soit effectué
     *  param apiCall : Endpoint appelé
     *  */
    @OptIn(DelicateCoroutinesApi::class)
    fun <T> httpCallForObject(
        callback: ObjectCallback<T>,
        apiCall: KSuspendFunction0<Response<APIPlaylist>>
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
}