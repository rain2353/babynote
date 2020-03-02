package com.example.babynote.Login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_find__password.*

class Find_Password : AppCompatActivity() {

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find__password)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)


        button3.setOnClickListener {
            if (editText_id.text.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                editText_id.requestFocus()
                return@setOnClickListener
            }
            if (editText_name.text.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                editText_name.requestFocus()
                return@setOnClickListener
            }
            if (editText_phonenumber.text.isEmpty()) {
                Toast.makeText(this, "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                editText_phonenumber.requestFocus()
                return@setOnClickListener
            }
            find_userPassword(
                editText_id.text.toString(),
                editText_name.text.toString(),
                editText_phonenumber.text.toString()
            )
        }

    }

    private fun find_userPassword(id: String, name: String, phonenumber: String) {
        compositeDisposable.add(myAPI.find_userPassword(id, name, phonenumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("encrypted_password")) {
                    val dialog_find_password = LayoutInflater.from(this@Find_Password)
                        .inflate(R.layout.dialog_find_password, null)
//                    Log.d("find_userid", message.toString())
                    var dialog = AlertDialog.Builder(this)
//                    dialog.setTitle("비밀번호 찾기")
//                    dialog.setMessage("변경할 비밀번호를 입력해주세요.")
                    dialog.setView(dialog_find_password)

                    fun toast_p() {
                        var edit_password =
                            dialog_find_password.findViewById<View>(R.id.editText_password) as EditText
                        val passMatches = Regex("^(?=.*[a-zA-Z0-9])(?=.*[!@#\$%^*+=-]).{8,20}$")
                        //비밀번호 유효성
                        if (edit_password.text.toString().matches(passMatches)) {
                            compositeDisposable.add(myAPI.change_userPassword(
                                id,
                                name,
                                phonenumber,
                                edit_password.text.toString()
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { message ->
                                    if (message.contains("비밀번호 변경이 완료되었습니다.")) {
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        var intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                    }
                                })
                        } else {
                            Toast.makeText(this, "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                            edit_password.requestFocus()
                            return
                        }

                    }

                    var dialog_listener = object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE ->
                                    toast_p()
                            }
                        }
                    }
                    dialog.setPositiveButton("확인", dialog_listener)
                    dialog.show()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar_find_password)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.title = "비밀번호 찾기"
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }
}
