package com.example.mestkom.ui.video

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mestkom.R
import com.example.mestkom.data.responses.CommentResponse
import com.example.mestkom.databinding.ListCommentBinding

class CommentAdapter(
    val context: Context,
    private val comments: List<CommentResponse>
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ListCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val model = comments[position]
        holder.binding.username.text = model.username
        holder.binding.commentText.text = model.text
    }

    override fun getItemCount(): Int = comments.size

    class CommentViewHolder(val binding: ListCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val username = binding.username
        val text = binding.commentText
    }

}