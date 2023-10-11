package com.example.mestkom.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mestkom.data.network.AuthApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.ui.repository.AuthRepository
import com.example.mestkom.databinding.FragmentRegisterBinding
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.enable
import com.example.mestkom.ui.handleApiError
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.visible
import kotlinx.coroutines.launch


class RegisterFragment :
    BaseFragment<AuthViewModel, FragmentRegisterBinding, List<BaseRepository>>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.enable(false)
        binding.progressBar3.visible(false)
        viewModel.registerResponse.observe(viewLifecycleOwner) {
            binding.progressBar3.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        val username = binding.login.text.toString()
                        val action =
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(
                                username
                            )
                        findNavController().navigate(action)
                    }
                }

                is Resource.Failure -> {
                    if (it.errorCode == 409)
                        Toast.makeText(context, it.errorBody?.string(), Toast.LENGTH_LONG).show()
                    else
                        handleApiError(it)
                }

                is Resource.Loading -> {
                    binding.progressBar3.visible(true)
                }
            }
        }

        binding.email.addTextChangedListener {
            val username = binding.login.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.passwordConfirm.text.toString().trim()
            val email = binding.email.text.toString().trim()
            binding.signInButton.enable(username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && email.isNotEmpty())
        }

        binding.signInButton.setOnClickListener {
            register()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(
                    ""
                )
            )
        }
    }

    private fun register() {
        val username = binding.login.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.passwordConfirm.text.toString().trim()
        val email = binding.email.text.toString().trim()

        val validate = viewModel.validateRegisterInput(username, password, confirmPassword, email)
        val errors = mutableListOf(
            binding.loginError,
            binding.passwordLengthError,
            binding.samePasswordsError,
            binding.emailError
        )
        if (validate.contains(false)) {
            validate.forEachIndexed { index, b ->
                if (!b) {
                    errors[index].isVisible = true
                }

            }
        } else {
            errors.forEach {
                it.isVisible = false
            }
            viewModel.register(username, password, email)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        listOf(AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences))
}