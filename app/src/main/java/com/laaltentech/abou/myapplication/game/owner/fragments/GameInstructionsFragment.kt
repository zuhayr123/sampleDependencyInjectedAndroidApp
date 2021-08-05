package com.laaltentech.abou.myapplication.game.owner.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.R
import com.laaltentech.abou.myapplication.databinding.FragmentGameCentralInstructionsBinding
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.email
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.hometown
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.pages_show_list
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.user_age_range
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.user_birthday
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.user_gender
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.user_likes
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.user_link
import com.laaltentech.abou.myapplication.game.observer.GameDataViewModel.Companion.user_location
import com.laaltentech.abou.myapplication.game.owner.activity.GameActivity
import com.laaltentech.abou.myapplication.network.Status
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject


class GameInstructionsFragment : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding : FragmentGameCentralInstructionsBinding

    lateinit var callbackManager : CallbackManager

    private val newGameDataViewModel: GameDataViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
            .get(GameDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callbackManager = CallbackManager.Factory.create()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_central_instructions, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {


        (activity as GameActivity).window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ((activity as GameActivity) as AppCompatActivity?)!!.supportActionBar!!.hide()

        viewModelInit()
        binding.loginButton.fragment = this

        binding.loginButton.setReadPermissions(listOf(email, hometown,user_link,user_age_range,user_birthday,user_gender,user_location,user_likes,pages_show_list ))


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if(isLoggedIn){
            val action = GameInstructionsFragmentDirections.actionViewDetailsToFacebookProfileFragment(accessToken?.token.toString(), accessToken?.userId.toString())
            findNavController().navigate(action)
        }

        Log.e("LOGIN STATUS", "status of login can be seen as $isLoggedIn")

        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {

//                Log.e("Json Data Test : ", "The data being sent from the ${Gson().toJson(result)}")
                Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()

                val action = GameInstructionsFragmentDirections.actionViewDetailsToFacebookProfileFragment(result?.accessToken?.token.toString(), result?.accessToken?.userId.toString())
                findNavController().navigate(action)
            }

            override fun onCancel() {
                Toast.makeText(context, "Login cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context, "Login unsuccessful", Toast.LENGTH_LONG).show()
            }

        })
        super.onActivityCreated(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun viewModelInit(){
        newGameDataViewModel.let {
            it.results.observe(viewLifecycleOwner, Observer { item ->
                when(item.status){
                    Status.SUCCESS -> {
                        newGameDataViewModel.data = item.data
                        newGameDataViewModel.notifyChange()
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