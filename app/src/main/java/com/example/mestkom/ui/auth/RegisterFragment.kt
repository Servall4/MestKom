package com.example.mestkom.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.mestkom.R
import com.example.mestkom.data.network.AuthApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.repository.AuthRepository
import com.example.mestkom.databinding.FragmentLoginBinding
import com.example.mestkom.databinding.FragmentRegisterBinding
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.enable
import com.example.mestkom.ui.handleApiError
import com.example.mestkom.ui.home.HomeActivity
import com.example.mestkom.ui.startNewActivity
import com.example.mestkom.ui.visible
import kotlinx.coroutines.launch


class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressBar3.visible(false)
        binding.loginbtn.enable(false)

        viewModel.registerResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar3.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        val transaction: FragmentTransaction? =
                            fragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmentContainerView, LoginFragment())?.commit()
                    }
                }

                is Resource.Failure -> handleApiError(it) { register() }

                is Resource.Loading -> {
                    binding.progressBar3.visible(true)
                }
            }
        })

        binding.email.addTextChangedListener {
            val username = binding.login.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmpassword.text.toString().trim()
            val email = binding.email.text.toString().trim()

            if (!username.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$")))
                binding.loginerr.isVisible = true
            else
                binding.loginerr.isVisible = false

            if (password != confirmPassword)
                binding.samepassworderr.isVisible = true
            else
                binding.samepassworderr.isVisible = false

            if (password.length < 8)
                binding.passwordlength.isVisible = true
            else

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
                binding.emailerr.isVisible = true

            binding.loginbtn.enable(username.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$")) && password == confirmPassword && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }

        binding.loginbtn.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val username = binding.login.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val email = binding.email.text.toString().trim()

        viewModel.register(username, password, email)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =  AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences )
}