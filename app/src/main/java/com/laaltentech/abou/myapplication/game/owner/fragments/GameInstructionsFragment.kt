package com.laaltentech.abou.myapplication.game.owner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentGameCentralInstructionsBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject

class GameInstructionsFragment : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding : FragmentGameCentralInstructionsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_central_instructions, container, false)
        return binding.root
    }

}