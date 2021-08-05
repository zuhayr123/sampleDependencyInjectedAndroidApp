package com.laaltentech.abou.myapplication.game.owner.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentAndroidProfileBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.accessToken
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.userId
import com.laaltentech.abou.myapplication.game.owner.activity.GameActivity
import com.laaltentech.abou.myapplication.game.owner.adapters.AdapterFacebookPageList
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.FragmentDataBindingComponent
import javax.inject.Inject


class FacebookProfileFragment: Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentAndroidProfileBinding

    var dataBindingComponent = FragmentDataBindingComponent(this)

    private val newGameDataViewModel: GameDataViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(GameDataViewModel::class.java)
    }

    lateinit var adapter : AdapterFacebookPageList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_android_profile, container, false,dataBindingComponent)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        (activity as GameActivity).window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        ((activity as GameActivity) as AppCompatActivity?)!!.supportActionBar!!.show()

        viewModelInit()

        newGameDataViewModel.apiCall.value = "available"

        accessToken = savedInstanceState?.getString("token")?:FacebookProfileFragmentArgs.fromBundle(requireArguments()).token
        userId = savedInstanceState?.getString("userId")?:FacebookProfileFragmentArgs.fromBundle(requireArguments()).userId

        Log.e("AccessToken", "The token is $accessToken and the userId is : $userId")

        binding.profileViewModel = newGameDataViewModel

        val accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                if(currentAccessToken == null){
                    findNavController().popBackStack()
                }
                Log.e("LOGIN STATE", "STATUS CHANGED, $currentAccessToken")
            }
        }

        adapter = AdapterFacebookPageList(
            dataBindingComponent = DataBindingUtil.getDefaultComponent(),
            appExecutors = appExecutors) {item, tapAction ->

            when(tapAction){
                "tapOnRootView" -> {
                    val action = FacebookProfileFragmentDirections.actionFacebookProfileFragmentToFragmentPageData(item.accessToken.toString(),
                        item.id
                    )

                    findNavController().navigate(action)
                    Log.e("TAP", "TAP ON THE ROOT VIEW WAS DETECTED")
                }
            }
        }

        binding.adapterList.adapter = adapter

        super.onActivityCreated(savedInstanceState)
    }

    fun viewModelInit(){
        newGameDataViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->
                when(item.status){
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        newGameDataViewModel.data = item.data
                        Glide.with(binding.root).load(newGameDataViewModel.data?.url).into(binding.profileImageView)
                        newGameDataViewModel.notifyChange()
                        newGameDataViewModel.newApiCall.value = "available"
                        Log.e("TAG", "Data fetch was successful ${Gson().toJson(item.data)}")
                    }

                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                        Log.e("TAG", "Data fetch Loading ${Gson().toJson(item.data)}")
                    }

                    Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Log.e("TAG", "Data fetch error")
                    }
                }
            })

            it.newApiResults.observe(viewLifecycleOwner, Observer { item ->
                when(item.status){
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        adapter.submitList(item.data)

                        if(item.data?.size == 0){
                            Log.e("SIZE OF ADAPTER", "THE SIZE OF ADAPTER WAS 0")
                        }

                        newGameDataViewModel.notifyChange()
                        adapter.notifyDataSetChanged()
                        Log.e("TAG", "Data fetch was successful ${Gson().toJson(item.data)}")
                    }

                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                        Log.e("TAG", "Data fetch Loading ${Gson().toJson(item.data)}")
                    }

                    Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Log.e("TAG", "Data fetch error")
                    }
                }
            })
        }
    }
}