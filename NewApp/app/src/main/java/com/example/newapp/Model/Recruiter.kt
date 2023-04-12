package com.example.newapp.Model

import android.os.Parcel
import android.os.Parcelable

class Recruiter: Parcelable  {

    private var uid: String = ""
    private var name: String = ""
    private var bio: String = ""
    private var location: String = ""
    private var contact: String = ""
    private var email: String = ""
    private var image: String = ""


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(bio)
        parcel.writeString(location)
        parcel.writeString(contact)
        parcel.writeString(email)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recruiter> {
        override fun createFromParcel(parcel: Parcel): Recruiter {
            return Recruiter(parcel)
        }

        override fun newArray(size: Int): Array<Recruiter?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel)
    {

    }



    constructor()

    constructor(uid: String, name: String, bio: String, location: String, contact: String, email: String, image: String)
    {
        this.uid = uid
        this.name = name
        this.bio = bio
        this.location = location
        this.contact = contact
        this.email = email
        this.image = image
    }

    fun getUId() : String{
        return uid
    }

    fun setUId(uid: String)
    {
        this.uid = uid
    }
    fun getName() : String{
        return name
    }

    fun setName(name: String)
    {
        this.name = name
    }
    fun getBio() : String{
        return bio
    }



    fun setBio(bio: String)
    {
        this.bio = bio
    }
    fun getLocation() : String{
        return location
    }

    fun setLocation(location: String)
    {
        this.location = location
    }

    fun getContact() : String{
        return contact
    }

    fun setContact(contact: String)
    {
        this.contact = contact
    }

    fun getEmail() : String{
        return email
    }

    fun setEmail(email: String)
    {
        this.email = email
    }

    fun getImage() : String{
        return image
    }

    fun setImage(image: String)
    {
        this.image = image
    }

}