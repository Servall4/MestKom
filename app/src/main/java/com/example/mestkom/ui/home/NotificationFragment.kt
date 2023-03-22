package com.example.mestkom.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mestkom.R
import com.example.mestkom.data.repository.UserRepository
import com.example.mestkom.databinding.FragmentHomeBinding
import com.example.mestkom.ui.base.BaseFragment

class NotificationFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, UserRepository>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun getViewModel(): Class<HomeViewModel> {
        TODO("Not yet implemented")
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        TODO("Not yet implemented")
    }

    override fun getFragmentRepository(): UserRepository {
        TODO("Not yet implemented")
    }

}