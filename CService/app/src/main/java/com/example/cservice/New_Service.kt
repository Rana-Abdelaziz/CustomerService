package com.example.cservice

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_new__service.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class New_Service : AppCompatActivity() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    lateinit var adapter1: ArrayAdapter<CharSequence>
    lateinit var adapter2: ArrayAdapter<CharSequence>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new__service)
        if(savedInstanceState!=null)
        { val text1=savedInstanceState.getString("Sname")
            editText2.setText(text1)
            val text2=savedInstanceState.getString("date")
            editText.setText(text2)
            val text3=savedInstanceState.getString("description")
            editText3.setText(text3)
        }




        loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            button.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                editText2.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                editText.error = getString(loginState.passwordError)
            }
            if (loginState.sdescription != null) {
                editText3.error = getString(loginState.sdescription)
            }
        })
        editText2.afterTextChanged {
            loginDataChanged(
                editText2.text.toString(),
                editText.text.toString(),
                editText3.text.toString()
            )
        }
        editText3.afterTextChanged {
            loginDataChanged(
                editText2.text.toString(),
                editText.text.toString(),
                editText3.text.toString()
            )
        }
        editText.afterTextChanged {
            loginDataChanged(
                editText2.text.toString(),
                editText.text.toString(),
                editText3.text.toString()
            )
        }

        adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Service,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter1)
        adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.service_type,
            android.R.layout.simple_spinner_item
        )
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.setAdapter(adapter2)
        button.setOnClickListener {
    Toast.makeText(this,"${editText.text.toString()}",Toast.LENGTH_SHORT).show()
         }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Sname",editText2.text.toString())
        outState.putString("date",editText.text.toString())
        outState.putString("description",editText3.text.toString())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun loginDataChanged(Sname: String, Date: String, sDescription: String) {
        if (!service_name_valid(Sname)) {
            _loginForm.value = LoginFormState(usernameError = R.string.sname_validator)
        } else if (!checkDateFormat(Date)!!) {
            _loginForm.value = LoginFormState(passwordError = R.string.Date_valid)
        } else if (!service_name_valid(sDescription)) {
            _loginForm.value = LoginFormState(sdescription = R.string.sname_validator)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun service_name_valid(Sname: String): Boolean {
        return Sname.length > 0

    }

    fun checkDateFormat(date: String): Boolean {

        val format = SimpleDateFormat("dd/MM/yyyy")
        format.setLenient(false)
        try {
             format.parse(date);
            return true;
        } catch (e: ParseException) {
            return false;
        }
    }

        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            })
        }
    }





