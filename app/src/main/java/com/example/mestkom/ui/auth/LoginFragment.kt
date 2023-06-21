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
import androidx.navigation.fragment.navArgs
import com.example.mestkom.databinding.FragmentLoginBinding
import com.example.mestkom.data.network.AuthApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.ui.repository.AuthRepository
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.enable
import com.example.mestkom.ui.handleApiError
import com.example.mestkom.ui.home.HomeActivity
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.startNewActivity
import com.example.mestkom.ui.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, List<BaseRepository>>() {

    private val args: LoginFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!args.username.isNullOrBlank())
            binding.login.setText(args.username)
        binding.signInButton.enable(false)
        binding.progressBar.visible(false)

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.token)
                        viewModel.saveUserId(it.value.id)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Failure -> {
                    if (it.errorCode == 409)
                        Toast.makeText(context, it.errorBody?.string(),Toast.LENGTH_LONG).show()
                    else
                        handleApiError(it)
                }
                is Resource.Loading -> {
                    binding.progressBar.visible(true)
                }
            }
        }

        binding.password.addTextChangedListener{
            val username = binding.login.text.toString().trim()
            binding.signInButton.enable(username.isNotEmpty() && it.toString().isNotEmpty())
        }
        binding.signInButton.setOnClickListener {
            login()
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun login() {
        val username = binding.login.text.toString().trim()
        val password = binding.password.text.toString().trim()

        val validate = viewModel.validateLoginInput(username, password)
        val errors = mutableListOf(binding.loginError, binding.passwordError)
        if (validate.contains(false)) {
            validate.forEachIndexed { index, bool ->
                if (!bool) {
                    errors[index].isVisible = true
                }
            }
        } else {
            errors.forEach{
                it.isVisible = false
            }
            viewModel.login(username, password)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =  listOf(AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences))

}