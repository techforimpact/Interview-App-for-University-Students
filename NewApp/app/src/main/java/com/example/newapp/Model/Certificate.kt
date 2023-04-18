package com.example.newapp.Model

import android.net.Uri

class Certificate {

    private var name: String = ""
    private var downloadUrl: String = ""

    constructor()
    {

    }

    constructor(name: String , downloadUrl: String)
    {
        this.name = name
        this.downloadUrl = downloadUrl
    }

    fun getName(): String
    {
        return name
    }

    fun setName(name: String)
    {
        this.name = name
    }

    fun getDownloadUrl(): String
    {
        return downloadUrl
    }

    fun setDownloadUrl(downloadUrl: String)
    {
        this.downloadUrl = downloadUrl
    }

}