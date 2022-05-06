package com.example.cservice

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {

    private val _loginForm = MutableLiveData<LoginFormState>()

    val loginFormState: LiveData<LoginFormState> = _loginForm

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val shared=getPreferences(Context.MODE_PRIVATE)

        if(savedInstanceState!=null)
        { val text1=savedInstanceState.getString("password")
            password.setText(text1)
        }
        if (shared.getString("Uname","")!=""&&shared.getString("Password","")!="")
        {
            username.setText(shared.getString("Uname",""))
            password.setText(shared.getString("Password",""))
            _loginForm.value = LoginFormState(isDataValid = true)

        }

        loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        username.afterTextChanged {
           loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        password.apply {
            afterTextChanged {
                loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }
        }


        login.setOnClickListener(){

                val editor =shared?.edit()
                editor?.putString("Uname",username.text.toString())
                editor?.putString("Password",password.text.toString())
                editor?.commit()
            loading.visibility = View.VISIBLE
            val myintent = Intent(this, Service::class.java)
            startActivity(myintent)
        }
        Sing_in.setOnClickListener {
            val myintent = Intent(this, Sing_In::class.java)
            startActivity(myintent)

        }
    }



    override fun onPause() {
        super.onPause()
        loading.visibility= View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("password",password.text.toString())
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
