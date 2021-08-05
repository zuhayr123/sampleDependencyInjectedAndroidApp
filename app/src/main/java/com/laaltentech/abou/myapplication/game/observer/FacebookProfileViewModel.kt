package com.laaltentech.abou.myapplication.game.observer

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.laaltentech.abou.myapplication.game.data.FacebookPageListData
import com.laaltentech.abou.myapplication.game.data.FacebookProfileData
import com.laaltentech.abou.myapplication.game.repository.FacebookDataRepository
import com.laaltentech.abou.myapplication.network.Resource
import com.laaltentech.abou.myapplication.util.AbsentLiveData
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject

class FacebookProfileViewModel@Inject constructor(
    private val repository: FacebookDataRepository,
    var executors: AppExecutors
) : ViewModel(), Observable {

    companion object{
        val email = "email"
        val hometown = "user_hometown"
        val user_link = "user_link"
        val user_age_range = "user_age_range"
        val user_birthday = "user_birthday"
        val user_gender = "user_gender"
        val user_location = "user_location"
        val pages_show_list = "pages_show_list"
        val user_likes = "user_likes"
        var userId : String = ""
        var accessToken: String = ""
        var isInternet = true
    }

    var data : FacebookProfileData? = FacebookProfileData()

    val apiCall = MutableLiveData<String>()
    val newApiCall = MutableLiveData<String>()
    var newApiResults: LiveData<Resource<List<FacebookPageListData>>>
    var results: LiveData<Resource<FacebookProfileData>>



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
                    repository.fetchProfileData(userID = userId, accessToken = accessToken, isInternet = isInternet)
                }
                else -> {
                    AbsentLiveData.create()
                }
            }
        }

        newApiResults = Transformations.switchMap(newApiCall){
            when(apiCall.value){
                "available" ->{
                    repository.fetchPageList(userID = userId, accessToken = accessToken, isInternet = isInternet)
                }

                else -> {
                    AbsentLiveData.create()
                }
            }
        }
    }

}