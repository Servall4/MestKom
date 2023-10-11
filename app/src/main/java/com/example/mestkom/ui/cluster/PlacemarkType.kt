package com.example.mestkom.ui.cluster

import android.os.Parcel
import android.os.Parcelable


data class PlacemarkUserData(
    val name: String,
    val description: String,
    val idVideo: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(idVideo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlacemarkUserData> {
        override fun createFromParcel(source: Parcel): PlacemarkUserData {
            return PlacemarkUserData(source)
        }

        override fun newArray(size: Int): Array<PlacemarkUserData?> {
            return arrayOfNulls(size)
        }
    }
}