package com.example.mestkom.ui.video

import com.example.mestkom.data.responses.CommentResponse

class ActionCommentFragment {
    companion object {
        const val TAG = "ActionCommentFragment"
        fun newInstance(comments: List<CommentResponse>, viewModel: VideoViewModel, idVideo: String, username: String):CommentsFragment{
            return CommentsFragment(comments, viewModel, idVideo, username)
        }
    }
}