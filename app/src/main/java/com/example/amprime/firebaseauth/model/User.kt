package com.example.amprime.firebaseauth.model


class User : Comparable<User> {
    lateinit var uid: String
    lateinit var fullname: String

    lateinit var address: String

    lateinit var date: Any
    lateinit var time: Any
    lateinit var designation: String

    lateinit var token: String

    constructor() {}

    constructor(uid: String, Fullname: String, Address: String, date: Any, time: Any, token: String, designation: String) {
        this.fullname = Fullname
        this.uid = uid
        this.address = Address

        this.date = date

        this.time = time

        this.designation = designation

        this.token = token


    }


    override fun compareTo(o: User): Int {
        return compareTo(o.time as User)
    }
}
