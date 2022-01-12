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
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentFacebookLoginBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.facebook.observer.FacebookProfileViewModel
import com.laaltentech.abou.myapplication.facebook.owner.activity.FacebookCentralActivity
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject


class FacebookLoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding : FragmentFacebookLoginBinding


    private val newFacebookProfileViewModel: FacebookProfileViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(FacebookProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_facebook_login, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {


        (activity as FacebookCentralActivity).window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ((activity as FacebookCentralActivity) as AppCompatActivity?)!!.supportActionBar!!.hide()

        viewModelInit()


        super.onActivityCreated(savedInstanceState)
    }

    fun viewModelInit(){
        newFacebookProfileViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->
                when(item.status){
                    Status.SUCCESS -> {
                        newFacebookProfileViewModel.data = item.data
                        newFacebookProfileViewModel.notifyChange()
//                        Log.e("TAG", "Data fetch was successful ${Gson().toJson(item.data)}")
                    }

                    Status.LOADING -> {
//                        Log.e("TAG", "Data fetch Loading ${Gson().toJson(item.data)}")
                    }

                    Status.ERROR -> {
                        Log.e("TAG", "Data fetch error")
                    }
                }
            })
        }
    }

}