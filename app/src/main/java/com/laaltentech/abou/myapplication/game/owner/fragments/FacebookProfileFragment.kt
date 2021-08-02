package com.laaltentech.abou.myapplication.game.owner.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentAndroidProfileBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.accessToken
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.userId
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject

class FacebookProfileFragment: Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentAndroidProfileBinding

    private val newGameDataViewModel: GameDataViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(GameDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_android_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModelInit()
        accessToken = savedInstanceState?.getString("token")?:FacebookProfileFragmentArgs.fromBundle(requireArguments()).token
        userId = savedInstanceState?.getString("userId")?:FacebookProfileFragmentArgs.fromBundle(requireArguments()).userId

        Log.e("AccessToken", "The token is $accessToken and the userId is : $userId")

        newGameDataViewModel.apiCall.value = "available"

        binding.profileViewModel = newGameDataViewModel

        super.onActivityCreated(savedInstanceState)
    }

    fun viewModelInit(){
        newGameDataViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->
                when(item.status){
                    Status.SUCCESS -> {
                        newGameDataViewModel.data = item.data
                        Glide.with(binding.root).load(newGameDataViewModel.data?.url).into(binding.profileImageView)
                        newGameDataViewModel.notifyChange()
                        Log.e("TAG", "Data fetch was successful ${Gson().toJson(item.data)}")
                    }

                    Status.LOADING -> {
                        Log.e("TAG", "Data fetch Loading ${Gson().toJson(item.data)}")
                    }

                    Status.ERROR -> {
                        Log.e("TAG", "Data fetch error")
                    }
                }
            })
        }
    }
}