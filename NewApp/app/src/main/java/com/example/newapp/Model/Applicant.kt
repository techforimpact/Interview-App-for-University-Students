package com.example.newapp.Model

import android.os.Parcel
import android.os.Parcelable

class Applicant: Parcelable {

    private var studentUid: String = ""
    private var jobUid: String = ""
    private var name: String = ""
    private var email: String = ""
    private var contact: String = ""
    private var university: String = ""
    private var program: String = ""
    private var degree: String = ""
    private var coverLetter: String = ""
    private var resume: String = ""
    private var image: String = ""
    private var status: String = ""
    private var approved: String = ""


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(studentUid)
        parcel.writeString(jobUid)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(contact)
        parcel.writeString(university)
        parcel.writeString(program)
        parcel.writeString(degree)
        parcel.writeString(coverLetter)
        parcel.writeString(resume)
        parcel.writeString(image)
        parcel.writeString(status)
        parcel.writeString(approved)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Applicant> {
        override fun createFromParcel(parcel: Parcel): Applicant {
            return Applicant(parcel)
        }

        override fun newArray(size: Int): Array<Applicant?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel)
    {

    }


    constructor()

    constructor(studentUid: String, jobUid: String, name: String, email: String, university: String, program: String, degree: String, coverLetter: String, resume: String, image: String , status: String , approved: String)
    {
        this.studentUid = studentUid
        this.jobUid = jobUid
        this.name = name
        this.email = email
        this.university = university
        this.program = program
        this.degree = degree
        this.coverLetter = coverLetter
        this.resume = resume
        this.image = image
        this.status = status
        this.approved = approved
    }



    fun getStudentUid(): String
    {
        return this.studentUid
    }

    fun setStudentUid(studentUid: String)
    {
        this.studentUid = studentUid
    }

    fun getJobUid(): String
    {
        return jobUid
    }

    fun setJobUid(jobUid: String)
    {
        this.jobUid = jobUid
    }


    fun getName() : String{
        return name
    }

    fun setName(name: String)
    {
        this.name = name
    }

    fun getEmail() : String{
        return email
    }

    fun setEmail(email: String)
    {
        this.email = email
    }

    fun getContact() : String{
        return contact
    }

    fun setContact(contact: String)
    {
        this.contact = contact
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

    fun getCoverLetter(): String
    {
        return coverLetter
    }

    fun setCoverLetter(coverLetter: String)
    {
        this.coverLetter = coverLetter
    }

    fun getResume(): String
    {
        return resume
    }

    fun setResume(resume: String)
    {
        this.resume = resume
    }


    fun getImage() : String{
        return image
    }

    fun setImage(image: String)
    {
        this.image = image
    }

    fun getStatus(): String
    {
        return status
    }

    fun setStatus(status: String)
    {
        this.status = status
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