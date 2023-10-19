package com.example.mestkom.ui.home

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.*
import com.example.mestkom.data.UserPreferences
import com.example.mestkom.data.location.LocationRepository
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.responses.UpdateResponse
import com.example.mestkom.data.responses.User
import com.example.mestkom.ui.base.BaseViewModel
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors

class HomeViewModel(
    private val repository: UserRepository,
    private val fileRepository: FileRepository
) : BaseViewModel(repository) {

    private val _updateResponse: MutableLiveData<Resource<List<UpdateResponse>>> = MutableLiveData()
    val updateResponse: LiveData<Resource<List<UpdateResponse>>> = _updateResponse

    private val _user: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>> = _user

    private lateinit var locationRepository: LocationRepository
    fun startLocationUpdates() = locationRepository.startLocationUpdates()
    fun stopLocationUpdates() = locationRepository.stopLocationUpdates()

    fun initRepository(context: Context) {
        locationRepository = LocationRepository.getInstance(context.applicationContext, Executors.newSingleThreadExecutor())
    }

    fun getLocation(): LiveData<Pair<Float, Float>> {
        return locationRepository.getLocation()
    }

    fun isReceivingLocationUpdates(): LiveData<Boolean> {
        return locationRepository.receivingLocationUpdates
    }

    fun getUser(context: Context) {
        val userPreferences = UserPreferences(context)
        viewModelScope.launch {
            _user.value = repository.getUser(userPreferences.userId.firstOrNull().toString())
        }
    }

    fun uploadVideo(
        file: File,
        name: String,
        description: String,
        id: String,
        latitude: Double,
        longitude: Double
    ): LiveData<Resource<ResponseBody>> {
        val _uploadResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
        val uploadResponse: LiveData<Resource<ResponseBody>> = _uploadResponse
        _uploadResponse.value = Resource.Loading
        viewModelScope.launch {
            _uploadResponse.value = fileRepository.uploadVideo(
                file,
                name,
                id,
                description,
                latitude.toString(),
                longitude.toString()
            )
        }
        return uploadResponse
    }

    fun createImageFileAndroidQ(context: Context, uri: Uri): File? {
        return try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
            val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)

            val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            file
        } catch (e: Exception) {
            Log.e("LOG", "createImageFileAndroidQ Error : $e")
            null
        }
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    fun updateVideos() = viewModelScope.launch {
        _updateResponse.value = Resource.Loading
        _updateResponse.value = repository.getVideos()
    }
}