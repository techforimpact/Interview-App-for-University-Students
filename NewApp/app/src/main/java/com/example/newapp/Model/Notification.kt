package com.example.newapp.Model

class Notification {

    private var studentUid: String = ""
    private var title: String = ""
    private var status: String = ""
    private var recruiterImage: String = ""
    private var recruiterName: String = ""
    private var jobUid: String = ""

    constructor()

    constructor(studentUid: String ,title: String, status: String, recruiterImage: String, recruiterName: String, jobUid: String)
    {
        this.studentUid = studentUid
        this.title = title
        this.status = status
        this.recruiterImage = recruiterImage
        this.recruiterName = recruiterName
        this.jobUid = jobUid
    }

    fun getStudentUid() : String{
        return studentUid
    }

    fun setStudentUid(studentUid: String)
    {
        this.studentUid = studentUid
    }

    fun getTitle() : String{
        return title
    }

    fun setTitle(title: String)
    {
        this.title = title
    }

    fun getStatus() : String{
        return status
    }

    fun setStatus(status: String)
    {
        this.status = status
    }

    fun getRecruiterImage() : String{
        return recruiterImage
    }

    fun setRecrtuierImage(recruiterImage: String)
    {
        this.recruiterImage = recruiterImage
    }

    fun getRecruiterName():String{
        return recruiterName
    }

    fun setRecruiterName(recruiterName: String)
    {
        this.recruiterName = recruiterName
    }


    fun getJobUid() : String{
        return jobUid
    }

    fun setJobUid(jobUid: String)
    {
        this.jobUid = jobUid
    }

}