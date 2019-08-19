package com.rohitrj.dmme.data

class User(val name:String, val email:String, val password:String){
    fun User(){}
    constructor() : this("", "", "")
}