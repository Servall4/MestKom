package com.example.mestkom.ui.video


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.responses.CommentResponse
import com.example.mestkom.ui.base.BaseViewModel
import com.example.mestkom.ui.repository.FileRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class VideoViewModel(
    private val repository: FileRepository
) : BaseViewModel(repository) {
    fun getComments(idVideo: String): LiveData<Resource<List<CommentResponse>>> {
        val _commentsResponse: MutableLiveData<Resource<List<CommentResponse>>> =
            MutableLiveData()
        val commentResponse: LiveData<Resource<List<CommentResponse>>> = _commentsResponse
        viewModelScope.launch {
            _commentsResponse.value = Resource.Loading
            _commentsResponse.value = repository.getComments(idVideo)
        }
        return commentResponse
    }

    fun sendComment(
        idVideo: String,
        username: String,
        text: String
    ): LiveData<Resource<ResponseBody>> {
        val _commentSendResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
        val commentSendResponse: LiveData<Resource<ResponseBody>> = _commentSendResponse
        _commentSendResponse.value = Resource.Loading
        viewModelScope.launch {
            _commentSendResponse.value = repository.sendComment(idVideo, username, text)
        }
        return commentSendResponse
    }
}