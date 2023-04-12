package com.example.newapp.Model

class User {

    private var uid: String = ""
    private var email: String = ""
    private var role: String = ""

    constructor()

    constructor(uid: String, email: String, role: String)
    {
        this.uid = uid
        this.email = email
        this.role = role
    }

    fun getUId() : String{
        return uid
    }

    fun setUId(uid: String)
    {
        this.uid = uid
    }


    fun getEmail() : String{
        return email
    }

    fun setEmail(email: String)
    {
        this.email = email
    }


    fun getRole() : String{
        return role
    }

    fun setRole(role: String)
    {
        this.role = role
    }


}