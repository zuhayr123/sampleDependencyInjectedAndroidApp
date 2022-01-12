package com.laaltentech.abou.myapplication.facebook.owner.fragments

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
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentAndroidProfileBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.facebook.observer.FacebookProfileViewModel
import com.laaltentech.abou.myapplication.facebook.observer.FacebookProfileViewModel.Companion.accessToken
import com.laaltentech.abou.myapplication.facebook.observer.FacebookProfileViewModel.Companion.isInternet
import com.laaltentech.abou.myapplication.facebook.observer.FacebookProfileViewModel.Companion.userId
import com.laaltentech.abou.myapplication.facebook.owner.activity.FacebookCentralActivity
import com.laaltentech.abou.myapplication.facebook.owner.adapters.AdapterFacebookPageList
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.Commons
import com.laaltentech.abou.myapplication.util.FragmentDataBindingComponent
import javax.inject.Inject


class FacebookProfileFragment: Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentAndroidProfileBinding

    var dataBindingComponent = FragmentDataBindingComponent(this)

    private val newFacebookProfileViewModel: FacebookProfileViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(FacebookProfileViewModel::class.java)
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
        isInternet = Commons.isNetworkAvailable(requireContext())
        (activity as FacebookCentralActivity).window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        ((activity as FacebookCentralActivity) as AppCompatActivity?)!!.supportActionBar!!.show()

        viewModelInit()

        newFacebookProfileViewModel.apiCall.value = "available"

        accessToken = savedInstanceState?.getString("token")?:FacebookProfileFragmentArgs.fromBundle(requireArguments()).token
        userId = savedInstanceState?.getString("userId")?:FacebookProfileFragmentArgs.fromBundle(requireArguments()).userId

        Log.e("AccessToken", "The token is $accessToken and the userId is : $userId")

        binding.profileViewModel = newFacebookProfileViewModel

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
        newFacebookProfileViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->
                when(item.status){
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        newFacebookProfileViewModel.data = item.data
                        Glide.with(binding.root).load(newFacebookProfileViewModel.data?.url).into(binding.profileImageView)
                        newFacebookProfileViewModel.notifyChange()
                        newFacebookProfileViewModel.newApiCall.value = "available"
//                        Log.e("TAG", "Data fetch was successful ${Gson().toJson(item.data)}")
                    }

                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
//                        Log.e("TAG", "Data fetch Loading ${Gson().toJson(item.data)}")
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

                        newFacebookProfileViewModel.notifyChange()
                        adapter.notifyDataSetChanged()
//                        Log.e("TAG", "Data fetch was successful ${Gson().toJson(item.data)}")
                    }

                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
//                        Log.e("TAG", "Data fetch Loading ${Gson().toJson(item.data)}")
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