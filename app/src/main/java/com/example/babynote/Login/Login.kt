package com.example.babynote.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Main.MainActivity
import com.example.babynote.R
import com.example.babynote.Resister.Join_membership
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    lateinit var myAPI:INodeJS
    var compositeDisposable = CompositeDisposable ()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)



        // 로그인 버튼 눌렀을때 메인화면으로 이동.

        login_button.setOnClickListener {
            login(edit_id.text.toString() , edit_password.text.toString())
            SaveId("id",edit_id.text.toString())
        }

        // 회원가입 버튼 눌렀을때 회원가입으로 이동.

        join_membership_move.setOnClickListener {
            val intent = Intent(this, Join_membership::class.java)
            startActivity(intent)

        }

        // 아이디 찾기 버튼 눌렀을떄 아이디 찾기로 이동.

        button_find_id.setOnClickListener {
            val intent = Intent(this, Find_ID::class.java)
            startActivity(intent)

        }

        // 비밀번호 찾기 버튼 눌렀을떄 비밀번호 찾기로 이동.

        button_find_password.setOnClickListener {
            val intent = Intent(this, Find_Password::class.java)
            startActivity(intent)

        }

    }

    private fun login(id: String, password: String) {
        compositeDisposable.add(myAPI.loginUser(id,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                if(message.contains("encrypted_password")) {
                    val pref = getSharedPreferences("UserId",Context.MODE_PRIVATE)
                    var User: String? = pref.getString("id", 0.toString())
                    UserInfo(User)
                    Toast.makeText(this, User +"님 로그인 하셨습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun SaveId(Key: String,Value: String){
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        val ed = pref.edit()
        ed.putString(Key,Value)
        ed.apply()
    }
    private fun UserInfo(id: String?) {
        compositeDisposable.add(myAPI.userInfo(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                Log.d("userinfo", message.toString())

                // 로그인한 아이디를 네비게이션 드로어 아이디에 입력한다.
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id",0.toString())
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                val ed = pref1.edit()
                ed.putString(userID,message.toString())
                ed.apply()
            })
    }
}


