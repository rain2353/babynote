package com.example.babynote.내정보

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import com.example.babynote.User.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_info.*

class MyInfo : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var User_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        //쉐어드에 저장된 내 정보 불러오기.
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)
        User_id = UserDTO.id
        UserInfo(UserDTO.id)

        // 내 아이디
        myinfo_id.text = UserDTO.id.toString()
        // 이름
        myInfo_name.text = UserDTO.name.toString()
        // 이메일
        myInfo_email.text = UserDTO.email.toString()
        // 휴대전화번호
        myInfo_phonenumber.text = UserDTO.phone_number.toString()
        // 호칭설정
        myInfo_nickname.text = UserDTO.nickname.toString()

        // 이름 변경
        name.setOnClickListener {
            var intent = Intent(this, com.example.babynote.내정보.modify_name::class.java)
            startActivity(intent)
        }
        // 이메일 변경
        email.setOnClickListener {
            var intent = Intent(this, com.example.babynote.내정보.modify_email::class.java)
            startActivity(intent)
        }
        // 휴대전화번호 변경
        phone_number.setOnClickListener {
            var intent = Intent(this, com.example.babynote.내정보.modify_phone_number::class.java)
            startActivity(intent)
        }
        // 호칭 설정
        nickname.setOnClickListener {
            var intent = Intent(this, com.example.babynote.내정보.modify_nickname::class.java)
            startActivity(intent)
        }

        // 비밀번호 변경
        modify_password.setOnClickListener {
            var intent = Intent(this, com.example.babynote.내정보.modify_password::class.java)
            startActivity(intent)
        }


    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(myInfo_toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.title = "내 정보"
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    private fun UserInfo(id: String?) {
        compositeDisposable.add(myAPI.userInfo(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                Log.d("userinfo", message.toString())

                // 로그인한 아이디를 네비게이션 드로어 아이디에 입력한다.
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id", 0.toString())
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                val ed = pref1.edit()
                ed.putString(userID, message.toString())
                ed.apply()
                //쉐어드에 저장된 내 정보 불러오기.
                val pref2 = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID1 = pref2.getString("id", 0.toString())
                val pref3 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                var userEmail = pref3.getString(userID1, null)
                var gson = Gson()
                var UserDTO = gson.fromJson(userEmail, User::class.java)
                User_id = UserDTO.id
                UserInfo(UserDTO.id)

                // 내 아이디
                myinfo_id.text = UserDTO.id.toString()
                // 이름
                myInfo_name.text = UserDTO.name.toString()
                // 이메일
                myInfo_email.text = UserDTO.email.toString()
                // 휴대전화번호
                myInfo_phonenumber.text = UserDTO.phone_number.toString()
                // 호칭설정
                myInfo_nickname.text = UserDTO.nickname.toString()
            })
    }

    override fun onPause() {
        UserInfo(User_id)
        super.onPause()
    }
}
