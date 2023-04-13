package com.example.newapp.Model

class Applicant
{

    private var uid: String = ""
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


    constructor()

    constructor(uid: String, name: String, email: String, university: String, program: String, degree: String, coverLetter: String, resume: String, image: String , status: String)
    {
        this.uid = uid
        this.name = name
        this.email = email
        this.university = university
        this.program = program
        this.degree = degree
        this.coverLetter = coverLetter
        this.resume = resume
        this.image = image
        this.status = status
    }


    fun getUId() : String{
        return uid
    }

    fun setUId(uid: String)
    {
        this.uid = uid
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

}