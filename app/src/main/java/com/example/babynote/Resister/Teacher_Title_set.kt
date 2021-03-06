package com.example.babynote.Resister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_teacher_title_set.*
import kotlinx.android.synthetic.main.activity_title_set.select


class Teacher_Title_set : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var nickname:String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_title_set)
        setToolbar()
        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)



        // 유저가 선택한 라디오버튼.
        teacher_nicknameGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.teacher -> {
                    teacher_nickname_EX.text = "별님반 교사"
                    this.nickname = "교사"
                }
                R.id.teacher_radioButton -> {
                    teacher_nickname_EX.text = "별님반 " + teacher_radioButton.text.toString()
                    this.nickname = teacher_radioButton.text.toString()
                }
            }
        }

        //Join_membership 에서 인텐트로 받은 회원 정보들음 get 한다.
        // 받아온 데이터들.
//        intent.putExtra("id",edit_id.text.toString())
//        intent.putExtra("name",edit_name.text.toString())
//        intent.putExtra("phonenumber",edit_phonenumber.text.toString())
//        intent.putExtra("email",edit_email.text.toString())
//        intent.putExtra("password",edit_password.text.toString())
//        intent.putExtra("state","학부모")
        var id = intent.getStringExtra("id")
        var name = intent.getStringExtra("name")
        var phonenumber = intent.getStringExtra("phonenumber")
        var email = intent.getStringExtra("email")
        var password = intent.getStringExtra("password")
        var state = intent.getStringExtra("state")

//        d(id,id)
//        d(name,name)
//        d(phonenumber,phonenumber)
//        d(email,email)
//        d(password,password)
//        d(state,state)
//        d(nickname, nickname)


        // 확인 버튼 눌렀을때 회원가입 완료.

        select.setOnClickListener {
            d(id,id)
            d(name,name)
            d(phonenumber,phonenumber)
            d(email,email)
            d(password,password)
            d(state,state)
            d(nickname, nickname.toString())
            register(id, name, phonenumber, email, password, state, nickname.toString())

        }


    }
    private fun register(id: String, name: String, phonenumber: String, email: String, password: String, state: String, nickname: String?) {
        compositeDisposable.add(myAPI.registerUser(id,name,phonenumber, email, password, state, nickname)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                if(message.contains("encrypted_password")) {
                    val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                    val ed = pref.edit()
                    ed.putString("id",id)
                    ed.apply()
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Teacher_Info::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar10)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("호칭설정")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

}
