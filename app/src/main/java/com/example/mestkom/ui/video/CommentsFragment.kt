package com.example.mestkom.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.responses.CommentResponse
import com.example.mestkom.databinding.DialogCommentsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsFragment(
    private val comments: List<CommentResponse>,
    private val viewModel: VideoViewModel,
    private val idVideo: String,
    private val username: String
): BottomSheetDialogFragment() {
    lateinit var binding: DialogCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCommentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CommentAdapter(requireContext(), comments)
        val layoutManager = LinearLayoutManager(context)
        binding.comments.apply {
            setLayoutManager(layoutManager)
            setAdapter(adapter)
        }

        binding.sendButton.setOnClickListener {
            if (!binding.commentEditText.text.isNullOrEmpty())  {
                val response = viewModel.sendComment(idVideo, username, binding.commentEditText.text.toString())
                response.observe(viewLifecycleOwner) {
                    if (it is Resource.Success) {
                        Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
                        onDestroy()
                        onDetach()
                    } else
                    {
                        Toast.makeText(context, "Loading!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}