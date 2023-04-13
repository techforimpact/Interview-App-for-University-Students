package com.example.newapp.Model

import android.icu.text.CaseMap.Title
import com.google.firebase.auth.ktx.userProfileChangeRequest

class Job {
    private var title: String = ""
    private var category: String = ""
    private var courses: String = ""
    private var seats: String = ""
    private var location: String = ""
    private var deadline: String = ""
    private var details: String = ""
    private var recruiterImage: String = ""
    private var recruiterName: String = ""
    private var uid: String = ""

    constructor()

    constructor(title: String, category: String, courses: String, seats: String, location: String, deadline: String, details: String,recruiterImage: String , recruiterName: String, uid: String )
    {
        this.title = title
        this.category = category
        this.courses = courses
        this.seats = seats
        this.location = location
        this.deadline = deadline
        this.details = details
        this.recruiterImage = recruiterImage
        this.recruiterName = recruiterName
        this.uid = uid
    }

    fun getTitle() : String{
        return title
    }

    fun setTitle(title: String)
    {
        this.title = title
    }
    fun getCategory() : String{
        return category
    }

    fun setCategory(category: String)
    {
        this.category = category
    }
    fun getCourses() : String{
        return courses
    }



    fun setCourses(courses: String)
    {
        this.courses = courses
    }
    fun getSeats() : String{
        return seats
    }

    fun setSeats(seats: String)
    {
        this.seats = seats
    }

    fun getLocation() : String{
        return location
    }

    fun setLocation(location: String)
    {
        this.location = location
    }

    fun getDeadline() : String{
        return deadline
    }

    fun setDeadline(deadline: String)
    {
        this.deadline = deadline
    }

    fun getDetails() : String{
        return details
    }

    fun setDetails(details: String)
    {
        this.details = details
    }

    fun getRecruiterImage() : String{
        return recruiterImage
    }

    fun setRecruiterImage(recruiterImage: String)
    {
        this.recruiterImage = recruiterImage
    }

    fun getRecruiterName() : String{
        return recruiterName
    }

    fun setRecruiterName(recruiterName: String)
    {
        this.recruiterName = recruiterName
    }


    fun getUid() : String{
        return uid
    }

    fun setUid(uid: String)
    {
        this.uid = uid
    }
}