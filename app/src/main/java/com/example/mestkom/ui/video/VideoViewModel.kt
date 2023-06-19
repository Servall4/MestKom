package com.example.mestkom.ui.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mestkom.data.network.Resource
import com.example.mestkom.ui.base.BaseViewModel
import com.example.mestkom.ui.repository.FileRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream


class VideoViewModel(
    private val repository: FileRepository
): BaseViewModel(repository) {

    private val _downloadResponse: MutableList<MutableLiveData<Resource<ResponseBody>>> = mutableListOf(MutableLiveData())
    val downloadResponse: List<LiveData<Resource<ResponseBody>>> = _downloadResponse

    fun downloadVideo(idVideo: String, position: Int) = viewModelScope.launch {
        _downloadResponse.add(MutableLiveData())
        _downloadResponse[position].value = Resource.Loading
        _downloadResponse[position].value = repository.downloadVideo(idVideo)
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