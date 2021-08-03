package com.laaltentech.abou.myapplication.util

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

class FragmentDataBindingComponent(var fragment: Fragment) : DataBindingComponent {

    override fun getActivityBindingAdapters() = ActivityBindingAdapters(fragment.requireActivity())

    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter

}