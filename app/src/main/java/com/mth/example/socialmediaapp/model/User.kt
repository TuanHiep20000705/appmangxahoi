package com.mth.example.socialmediaapp.model

class User {
    private var id: Int = 0
    private var username: String = ""
    private var password: String = ""
    private var tenuser: String = ""
    private var birthday: String = ""
    private var quequan: String = ""
    private var mota: String = ""
    private var sdt: Int = 0
    private var hinhanh: String = ""
    private var anhbia: String = ""

    constructor(
        username: String,
        password: String,
        tenuser: String,
        birthday: String,
        quequan: String,
        mota: String,
        sdt: Int,
        hinhanh: String,
        anhbia: String
    ) {
        this.username = username
        this.password = password
        this.tenuser = tenuser
        this.birthday = birthday
        this.quequan = quequan
        this.mota = mota
        this.sdt = sdt
        this.hinhanh = hinhanh
        this.anhbia = anhbia
    }

    var Id: Int
        get() {
            return this.id
        }
        set(value) {
            this.id = value
        }
    var UserName: String
        get() {
            return this.username
        }
        set(value) {
            this.username = value
        }
    var PassWord: String
        get() {
            return this.password
        }
        set(value) {
            this.password = value
        }
    var TenUser: String
        get() {
            return this.tenuser
        }
        set(value) {
            this.tenuser = value
        }
    var Birthday: String
        get() {
            return this.birthday
        }
        set(value) {
            this.birthday = value
        }
    var QueQuan: String
        get() {
            return this.quequan
        }
        set(value) {
            this.quequan = value
        }
    var MoTa: String
        get() {
            return this.mota
        }
        set(value) {
            this.mota = value
        }

    var Sdt: Int
        get() {
            return this.sdt
        }
        set(value) {
            this.sdt = value
        }
    var HinhAnh: String
        get() {
            return this.hinhanh
        }
        set(value) {
            this.hinhanh = value
        }
    var AnhBia: String
        get() {
            return this.anhbia
        }
        set(value) {
            this.anhbia = value
        }
}