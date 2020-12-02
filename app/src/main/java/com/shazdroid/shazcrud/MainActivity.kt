package com.shazdroid.shazcrud

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.shazdroid.shazcrud.adapter.UserAdapterClickHandlers
import com.shazdroid.shazcrud.adapter.UserAdapter
import com.shazdroid.shazcrud.db.Dao
import com.shazdroid.shazcrud.db.UserDatabase
import com.shazdroid.shazcrud.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), UserAdapterClickHandlers {

    // insert
    lateinit var dao : Dao
    lateinit var recyclerView: RecyclerView
    lateinit var addUserBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init Ui
        initViews()

        // init db
        dao = UserDatabase.getInstance(this).userDao()

        // render user list
        renderUserList()

        // add user btn
        addUserBtn.setOnClickListener {
            startActivity(Intent(this,CreateUserActivity::class.java).apply {
                Bundle().apply {
                    putString("IS_FOR_UPDATE","N")
                    putInt("USER_ID",0)
                }
            })
        }

    }

    fun initViews(){
        recyclerView = findViewById(R.id.mainRecyclerView)
        addUserBtn = findViewById(R.id.addBtnMainActivity)
    }

    fun renderUserList(){
        var userModelList : List<UserModel>? = null
        // GET LIST OF USER
        CoroutineScope(Dispatchers.IO).launch {
            userModelList = dao.getAllUserList()

            // SHOW LIST OF USER
            CoroutineScope(Dispatchers.Main).launch {
                recyclerView.adapter = UserAdapter(userModelList!! as ArrayList,this@MainActivity)
            }

        }


    }

    override fun onDeleteClick(id: Int, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete")
        dialog.setMessage("Are you sure you want to delete it?")
        dialog.setPositiveButton("Delete now", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.deleteUser(id)

                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@MainActivity,"Deleted Successfully",Toast.LENGTH_SHORT).show()
                        recyclerView.adapter!!.notifyItemRemoved(position)
                        renderUserList()
                    }
                }
            }
        })

        dialog.setNegativeButton("Cancel",object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if (p0 != null) {
                    p0.dismiss()
                }
            }

        })

        dialog.create().show()
    }


    override fun onItemClick(id: Int) {
        val intent = Intent(this,CreateUserActivity::class.java)
        intent.putExtra("IS_FOR_UPDATE","Y")
        intent.putExtra("USER_ID",id)

        startActivity(intent)
    }




}