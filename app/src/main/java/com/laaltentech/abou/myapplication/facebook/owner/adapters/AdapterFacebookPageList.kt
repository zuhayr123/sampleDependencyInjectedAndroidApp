package com.laaltentech.abou.myapplication.facebook.owner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.AdapterPageListLayoutBinding
import com.laaltentech.abou.myapplication.facebook.data.FacebookPageListData
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.DataBoundListAdapter

class AdapterFacebookPageList (private val dataBindingComponent : DataBindingComponent?, appExecutors: AppExecutors, private val callback:((FacebookPageListData, action:String)->Unit)?):
DataBoundListAdapter<FacebookPageListData, AdapterPageListLayoutBinding>(
    appExecutors = appExecutors,
    diffCallback = object :DiffUtil.ItemCallback<FacebookPageListData>(){
        override fun areItemsTheSame(
            oldItem: FacebookPageListData,
            newItem: FacebookPageListData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FacebookPageListData,
            newItem: FacebookPageListData
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }
){
    override fun createBinding(parent: ViewGroup): AdapterPageListLayoutBinding {
        val binding = DataBindingUtil
            .inflate<AdapterPageListLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_page_list_layout,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    override fun bind(
        binding: AdapterPageListLayoutBinding,
        item: FacebookPageListData,
        position: Int
    ) {
        binding.facebookPage = item

        binding.rootViewAdapter.setOnClickListener {
            callback?.invoke(item, "tapOnRootView")
        }
    }
}