package com.example.newapp.Model

import android.os.Parcel
import android.os.Parcelable

class Student: Parcelable {


    private var uid: String = ""
    private var fullname: String = ""
    private var email: String = ""
    private var university: String = ""
    private var program: String = ""
    private var degree: String = ""
    private var rollno: String = ""
    private var image: String = ""
    private var approved: String = ""
    private var role: String = ""


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(fullname)
        parcel.writeString(email)
        parcel.writeString(university)
        parcel.writeString(program)
        parcel.writeString(degree)
        parcel.writeString(image)
        parcel.writeString(rollno)
        parcel.writeString(approved)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel)
    {

    }


    constructor()

    constructor(uid: String, name: String, email: String, university: String, program: String, degree: String, image: String, approved: String, role: String)
    {
        this.uid = uid
        this.fullname = name
        this.email = email
        this.university = university
        this.program = program
        this.degree = degree
        this.image = image
        this.approved = approved
        this.role = role
    }


    fun getRole(): String{
        return role
    }

    fun setRole(role: String)
    {
        this.role = role
    }

    fun getUId() : String{
        return uid
    }



    fun setUId(uid: String)
    {
        this.uid = uid
    }
    fun getFullname() : String{
        return fullname
    }

    fun setFullname(fullname: String)
    {
        this.fullname = fullname
    }
    fun getEmail() : String{
        return email
    }



    fun setEmail(email: String)
    {
        this.email = email
    }
    fun getUniversity() : String{
        return university
    }

    fun setUniversity(university: String)
    {
        this.university = university
    }

    fun getProgram() : String{
        return program
    }

    fun setProgram(program: String)
    {
        this.program = program
    }

    fun getDegree() : String{
        return degree
    }

    fun setDegree(degree: String)
    {
        this.degree = degree
    }

    fun getImage() : String{
        return image
    }

    fun setImage(image: String)
    {
        this.image = image
    }

    fun getRollno(): String
    {
        return rollno
    }

    fun setRollno(rollno: String)
    {
        this.rollno = rollno
    }


    fun getApproved(): String
    {
        return approved
    }

    fun setApproved(approved: String)
    {
        this.approved = approved
    }

}