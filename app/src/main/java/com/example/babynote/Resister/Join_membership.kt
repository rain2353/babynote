package com.example.babynote.Resister

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_join_membership.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast

class Join_membership : AppCompatActivity() {

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_membership)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 취소 버튼 눌렀을때 로그인으로 이동.

        cancel.setOnClickListener {
            onBackPressed()
        }

        // 확인 눌렀을때 다이얼로그 팝업창이 나와서 학부모인지 선생님인지 선택.
        select.setOnClickListener {
            val passMatches = Regex("^(?=.*[a-zA-Z0-9])(?=.*[!@#\$%^*+=-]).{8,20}$")
            //비밀번호 유효성
            if (edit_password.text.toString().matches(passMatches)){

            }else{
                Toast.makeText(this, "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                edit_password.requestFocus()
                return@setOnClickListener
            }
            //핸드폰번호 유효성
            val phoneMatches = Regex("^01[016789][0-9]{3,4}[0-9]{4}$")
            if (edit_phonenumber.text.toString().matches(phoneMatches)) {

            }else {
                Toast.makeText(this, "올바른 핸드폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                edit_phonenumber.requestFocus()
                return@setOnClickListener
            }
            //이메일형식체크
            if (EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {

            }else{
                Toast.makeText(this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
                edit_email.requestFocus()
                return@setOnClickListener
            }

            // 다이얼로그에 표시할 목록을 생성합니다.
            val people = listOf("학부모", "선생님")

            // 리스트 다이얼로그를 생성하고 표시합니다.
            selector(title = "누구신가요?", items = people) { _, selection ->

                // 항목을 선택했을 때 수행할 동작을 구현합니다.
                toast("You selected ${people[selection]}")
                if (people[selection] == "학부모") {
                    var intent = Intent(this, Family_Title_set::class.java)
                    intent.putExtra("id",edit_id.text.toString())
                    intent.putExtra("name",edit_name.text.toString())
                    intent.putExtra("phonenumber",edit_phonenumber.text.toString())
                    intent.putExtra("email",edit_email.text.toString())
                    intent.putExtra("password",edit_password.text.toString())
                    intent.putExtra("state","학부모")
                    startActivity(intent)
                } else {
                    var intent = Intent(this, Teacher_Title_set::class.java)
                    intent.putExtra("id",edit_id.text.toString())
                    intent.putExtra("name",edit_name.text.toString())
                    intent.putExtra("phonenumber",edit_phonenumber.text.toString())
                    intent.putExtra("email",edit_email.text.toString())
                    intent.putExtra("password",edit_password.text.toString())
                    intent.putExtra("state","선생님")
                    startActivity(intent)
                }
            }
        }


    }



    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar8)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("회원가입")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }
    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}

