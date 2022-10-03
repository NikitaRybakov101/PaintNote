package com.example.foodnote.ui.splash_screen_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.foodnote.R
import com.example.foodnote.databinding.SplashScreenFragmentBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseViewBindingFragment<SplashScreenFragmentBinding>(SplashScreenFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_notesFragment)
        }, 6000)
    }
}