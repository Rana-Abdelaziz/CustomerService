package com.example.cservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_service.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*
import kotlin.math.absoluteValue

class Service : AppCompatActivity() {
    lateinit var login:View

    lateinit var  adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_service)
        login = LayoutInflater.from(this).inflate(R.layout.login, null)
        login.loading.visibility = View.GONE

        var arra = arrayListOf<String>(getString(R.string.health),getString(R.string.sport),getString(R.string.Entertainment) , getString(R.string.Jobs),getString(R.string.other_service) )
        add.setOnClickListener {
            val myintent = Intent(this, New_Service::class.java)
            startActivity(myintent)

        }

        adapter=ArrayAdapter<String>(this, R.layout.custom_array,R.id.textView,arra)
        list_view.adapter=adapter
        list_view.setOnItemClickListener(){ list, item, index, id ->
            val myintent = Intent(this, Specific_service::class.java)
            startActivity(myintent)
        }
    }
}
