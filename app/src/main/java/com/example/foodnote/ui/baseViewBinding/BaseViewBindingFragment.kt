package com.example.foodnote.ui.baseViewBinding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewBindingFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding: VB? = null
    val binding
        get() = _binding
            ?: throw IllegalStateException("Trying to access binding")

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    protected var idUser = ""
    protected val uiScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            idUser = currentUser.email ?: currentUser.uid
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        uiScope.cancel()
    }
}
