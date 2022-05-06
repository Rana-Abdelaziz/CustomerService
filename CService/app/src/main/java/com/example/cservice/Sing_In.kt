package com.example.cservice

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_sing__in.*
import kotlinx.android.synthetic.main.login.*

class Sing_In : AppCompatActivity() {


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing__in)
        val username= findViewById<EditText>(R.id.editUser)
        val password= findViewById<EditText>(R.id.EditPassword)
        if(savedInstanceState!=null)
        { val text1=savedInstanceState.getString("email")
            EditEmail.setText(text1)
            val text2=savedInstanceState.getString("uname")
            editUser.setText(text2)
            val text3=savedInstanceState.getString("pass")
            EditPassword.setText(text3)
        }

        loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            save_btn.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
            if (loginState.sdescription != null) {
                EditEmail.error = getString(loginState.sdescription)
            }
        })
        username.afterTextChanged {
            loginDataChanged(
                username.text.toString(),
                password.text.toString(),
                EditEmail.text.toString()
            )
        }
        password.apply {
            afterTextChanged {
                loginDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    EditEmail.text.toString()
                )
            }
        }
        EditEmail.afterTextChanged {
            loginDataChanged(
                username.text.toString(),
                password.text.toString(),
                EditEmail.text.toString()
            )
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email",EditEmail.text.toString())
        outState.putString("uname",editUser.text.toString())
        outState.putString("pass",EditPassword.text.toString())
    }

    fun loginDataChanged(username: String, password: String,email: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else if (!service_name_valid(email)) {
            _loginForm.value = LoginFormState(sdescription = R.string.email_valid)
        }
        else {
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
    fun service_name_valid(Sname: String): Boolean {
        return if (!Sname.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(Sname).matches()
        }
        else {
            Sname.isNotBlank()
        }
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
