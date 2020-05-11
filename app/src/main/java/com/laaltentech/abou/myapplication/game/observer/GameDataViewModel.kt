package com.laaltentech.abou.myapplication.game.observer

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.laaltentech.abou.myapplication.game.data.GameData
import com.laaltentech.abou.myapplication.game.data.GameDataWithIndividualRelation
import com.laaltentech.abou.myapplication.game.repository.GameDataRepository
import com.laaltentech.abou.myapplication.network.Resource
import com.laaltentech.abou.myapplication.util.AbsentLiveData
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject

class GameDataViewModel@Inject constructor(
    private val repository: GameDataRepository,
    var executors: AppExecutors
) : ViewModel(), Observable {

    val apiCall = MutableLiveData<String>()
    var gameData = GameData()
    var results: LiveData<Resource<GameDataWithIndividualRelation>>

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)    }

    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    init{
        results = Transformations.switchMap(apiCall){
            when(apiCall.value){
                "available" -> {
                    repository.insertGameData(gameId = "123", data = gameData)
                }
                else -> {
                    AbsentLiveData.create()
                }
            }
        }
    }

}