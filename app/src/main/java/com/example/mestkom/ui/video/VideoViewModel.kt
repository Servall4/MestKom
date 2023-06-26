package com.example.mestkom.ui.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.responses.CommentResponse
import com.example.mestkom.ui.base.BaseViewModel
import com.example.mestkom.ui.repository.FileRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream


class VideoViewModel(
    private val repository: FileRepository
): BaseViewModel(repository) {
    fun downloadVideo(idVideo: String): LiveData<Resource<ResponseBody>> {
        val _downloadResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
        val downloadResponse: LiveData<Resource<ResponseBody>> = _downloadResponse
        viewModelScope.launch {
            _downloadResponse.value = Resource.Loading
            _downloadResponse.value = repository.downloadVideo(idVideo)
        }
        return downloadResponse
    }
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

    fun sendComment(idVideo: String, username: String, text: String): LiveData<Resource<ResponseBody>> {
        val _commentSendResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
        val commentSendResponse: LiveData<Resource<ResponseBody>> = _commentSendResponse
            _commentSendResponse.value = Resource.Loading
        viewModelScope.launch {
            _commentSendResponse.value = repository.sendComment(idVideo, username, text)
        }
        return commentSendResponse
    }
    fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String {
        if (body == null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }
}