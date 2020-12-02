package com.shazdroid.shazcrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shazdroid.shazcrud.db.Dao
import com.shazdroid.shazcrud.db.UserDatabase
import com.shazdroid.shazcrud.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateUserActivity : AppCompatActivity() {
    lateinit var userName: EditText
    lateinit var phoneNo: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var addBtn: Button

    lateinit var dao: Dao
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        // init views
        initViews()

        // init db
        dao = UserDatabase.getInstance(this).userDao()

        // check if is for update or add
        if (intent.hasExtra("IS_FOR_UPDATE")) {
            val result = intent.extras?.getString("IS_FOR_UPDATE")
            if (result.equals("Y")) {
                userId = intent.extras!!.getInt("USER_ID", 0)
                addBtn.text = "Update User"
                addBtn.tag = "update"
                populateData()
            } else {
                addBtn.tag = "add"
            }
        }else{
            addBtn.tag = "add"
            Log.d("SHAZ", "onCreate: no extras ")
        }


        addBtn.setOnClickListener {
            if (addBtn.tag.equals("add")) {
                validateInputs()

                // construct user
                val user = UserModel(
                    0,
                    userName.text.toString().trim(),
                    email.text.toString().trim(),
                    phoneNo.text.toString().toLong(),
                    password.text.toString()
                )

                // insert into db
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertUser(user)

                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@CreateUserActivity, "User added", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@CreateUserActivity, MainActivity::class.java))
                    }
                }
            }else if (addBtn.tag.equals("update")){
                validateInputs()

                // construct user
                val user = UserModel(
                    userId,
                    userName.text.toString().trim(),
                    email.text.toString().trim(),
                    phoneNo.text.toString().toLong(),
                    password.text.toString()
                )

                // insert into db
                CoroutineScope(Dispatchers.IO).launch {
                    dao.updateUser(user)
                    Log.d("UPDATE_USER", "updated : ${dao.getUserById(userId)}")
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@CreateUserActivity, "User added", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@CreateUserActivity, MainActivity::class.java))
                    }
                }
            }
        }

    }

    fun initViews() {
        userName = findViewById(R.id.userName)
        phoneNo = findViewById(R.id.phoneNo)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        addBtn = findViewById(R.id.addBtn)
    }

    fun validateInputs() {
        if (userName.text.isNullOrEmpty()) {
            userName.error = "Require field"
            return
        } else if (email.text.isNullOrEmpty()) {
            userName.error = null
            email.error = "Require field"
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Invalid email"
        } else if (phoneNo.text.isNullOrEmpty()) {
            email.error = null
            phoneNo.error = "Require field"
            return
        } else if (password.text.isNullOrEmpty()) {
            phoneNo.error = null
            password.error = "Require field"
            return
        } else if(phoneNo.text.toString().length < 10) {
            phoneNo.error = "Invalid phone no"
        }else if (confirmPassword.text.isNullOrEmpty()) {
            phoneNo.error =  null
            password.error = null
            confirmPassword.error = "Require field"
            return
        } else if (!password.text.toString().equals(confirmPassword.text.toString())) {
            Toast.makeText(this, "Confirm Password does not match!", Toast.LENGTH_SHORT).show()
            return
        }
    }

    fun populateData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userData = dao.getUserById(userId)

            CoroutineScope(Dispatchers.Main).launch {
                userData.let {
                    userName.setText(it.name)
                    email.setText(it.email)
                    phoneNo.setText(it.phone.toString())
                    password.setText(it.password)
                    confirmPassword.setText(it.password)
                }
            }
        }
    }
}