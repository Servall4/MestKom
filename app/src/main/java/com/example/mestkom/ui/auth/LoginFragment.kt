package com.example.mestkom.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mestkom.R
import com.example.mestkom.databinding.FragmentLoginBinding
import com.example.mestkom.network.AuthApi
import com.example.mestkom.repository.AuthRepository
import com.example.mestkom.ui.base.BaseFragment

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {


    //    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false)
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =  AuthRepository(remoteDataSource.buildApi(AuthApi::class.java))

}