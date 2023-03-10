package com.example.mestkom.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mestkom.R
import com.example.mestkom.databinding.FragmentLoginBinding
import com.example.mestkom.data.network.AuthApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.repository.AuthRepository
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.enable
import com.example.mestkom.ui.handleApiError
import com.example.mestkom.ui.home.HomeActivity
import com.example.mestkom.ui.startNewActivity
import com.example.mestkom.ui.visible
import kotlinx.coroutines.launch
import java.util.Observer
import kotlin.math.log

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {
    private lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressBar.visible( false)
        binding.loginbtn.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.user.salt!!)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Failure -> handleApiError(it) { login() }

                is Resource.Loading -> {
                    binding.progressBar.visible(true)
                }
            }
        })

        binding.password.addTextChangedListener{
            val username = binding.login.text.toString().trim()
            binding.loginbtn.enable(username.isNotEmpty() && it.toString().isNotEmpty())
        }
        binding.loginbtn.setOnClickListener {
            login()
        }

        binding.registerbtn.setOnClickListener {
            val transaction: FragmentTransaction? =
                fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainerView, RegisterFragment())?.commit()
        }
    }

    private fun login() {
        val username = binding.login.text.toString().trim()
        val password = binding.password.text.toString().trim()

        //@todo add input validations
        viewModel.login(username, password)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =  AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences )

}