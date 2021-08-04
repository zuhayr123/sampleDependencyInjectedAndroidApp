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
import com.laaltentech.abou.myapplication.databinding.FragmentPageDataLayoutBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.userId
import com.laaltentech.abou.myapplication.game.observer.PageDataViewModel
import com.laaltentech.abou.myapplication.game.observer.PageDataViewModel.Companion.accessToken
import com.laaltentech.abou.myapplication.game.observer.PageDataViewModel.Companion.pageId
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.FragmentDataBindingComponent
import javax.inject.Inject

class FragmentPageData : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentPageDataLayoutBinding

    var dataBindingComponent = FragmentDataBindingComponent(this)

    private val pageDataViewModel: PageDataViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(PageDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_page_data_layout, container, false,dataBindingComponent)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        accessToken = savedInstanceState?.getString("access_token")?:FragmentPageDataArgs.fromBundle(requireArguments()).accessToken
        pageId = savedInstanceState?.getString("pageID")?:FragmentPageDataArgs.fromBundle(requireArguments()).pageID

        viewModelInit()

        pageDataViewModel.apiCall.value = "available"
        super.onActivityCreated(savedInstanceState)
    }

    fun viewModelInit() {
        pageDataViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->
                when (item.status) {
                    Status.SUCCESS -> {
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