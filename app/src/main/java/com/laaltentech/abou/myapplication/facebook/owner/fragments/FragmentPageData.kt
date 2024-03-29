package com.laaltentech.abou.myapplication.facebook.owner.fragments

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentPageDataLayoutBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.facebook.data.FacebookPageData
import com.laaltentech.abou.myapplication.facebook.observer.PageDataViewModel
import com.laaltentech.abou.myapplication.facebook.observer.PageDataViewModel.Companion.accessToken
import com.laaltentech.abou.myapplication.facebook.observer.PageDataViewModel.Companion.isInternet
import com.laaltentech.abou.myapplication.facebook.observer.PageDataViewModel.Companion.pageId
import com.laaltentech.abou.myapplication.facebook.repository.SendDataJobService
import com.laaltentech.abou.myapplication.facebook.repository.SendDataWorkManager
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.Commons
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class FragmentPageData : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentPageDataLayoutBinding

    private var mScheduler: JobScheduler? = null

//    var dataBindingComponent = FragmentDataBindingComponent(this)

    private val pageDataViewModel: PageDataViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(PageDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_page_data_layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isInternet = Commons.isNetworkAvailable(requireContext())
        pageDataViewModel.facebookPageData = FacebookPageData()
        viewModelInit()
        binding.pageDataViewModel = pageDataViewModel

        mScheduler = context?.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler?

        accessToken = savedInstanceState?.getString("access_token")?:FragmentPageDataArgs.fromBundle(requireArguments()).accessToken
        pageId = savedInstanceState?.getString("pageID")?:FragmentPageDataArgs.fromBundle(requireArguments()).pageID
        Log.e("PAGE_ID", "PAGE ID IS $pageId")
        pageDataViewModel.apiCall.value = "available"
        pageDataViewModel.notifyChange()

    }

    override fun onResume() {
        isInternet = Commons.isNetworkAvailable(requireContext())
        pageDataViewModel.apiCall.value = "available"
        super.onResume()
    }

    fun viewModelInit() {
        pageDataViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->

                when(pageDataViewModel.apiCall.value){
                    "available" ->{
                        when (item.status) {
                            Status.SUCCESS -> {
                                pageDataViewModel.facebookPageData = item.data
                                Glide.with(binding.root).load(pageDataViewModel.facebookPageData?.cover).into(binding.cover)
                                Glide.with(binding.root).load(pageDataViewModel.facebookPageData?.picture).into(binding.profileImageView)
                                pageDataViewModel.notifyChange()
                                pageDataViewModel.apiCall.value = "postData"
                                binding.progress.visibility = View.GONE

                                executeWorkManagerFetch(data = item.data, context = requireContext()) //comment out this function while testing the job service version of the code

                                val componentName = ComponentName(requireContext(), SendDataJobService::class.java)
                                val bundle = PersistableBundle()
                                val serializedData = Gson().toJson(item.data)
                                bundle.putString("serializedData", serializedData)

                                val jobInfo = JobInfo.Builder(101, componentName)
                                    .setExtras(bundle)
                                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                    .setPersisted(true)
                                    .setPeriodic(15*60*1000)

//                                val myJobInfo: JobInfo = jobInfo.build() remove the comment code here to test the job service version of the notification trigger mechanism
//                                mScheduler?.schedule(myJobInfo)
                            }

                            Status.LOADING -> {
                                binding.progress.visibility = View.VISIBLE
                            }

                            Status.ERROR -> {
                                binding.progress.visibility = View.GONE
                            }
                        }
                    }

                    "postData" ->{
                        when (item.status) {
                            Status.SUCCESS -> {
                                pageDataViewModel.notifyChange()
                                binding.progress.visibility = View.GONE
                            }

                            Status.LOADING -> {
                                binding.progress.visibility = View.VISIBLE
                            }

                            Status.ERROR -> {
                                binding.progress.visibility = View.GONE
                            }
                        }
                    }
                }
            })
        }
    }

    fun executeWorkManagerFetch(data : FacebookPageData?, context: Context){
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(SendDataWorkManager::class.java, 16, TimeUnit.MINUTES, 2,TimeUnit.MINUTES)
                .setInputData(Data.Builder().putString("serializedData", Gson().toJson(data)).build())
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

        val workManager = WorkManager.getInstance(context)

        Log.e("ENQUE", "Enque was called")
        workManager.enqueue(periodicWorkRequest)
    }
}