package com.example.babynote.Login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import com.example.babynote.User.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_find__id.*

class Find_ID : AppCompatActivity() {

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find__id)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)



        button3.setOnClickListener {
            if (editText_name.text.isEmpty()){
                Toast.makeText(this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show()
                editText_name.requestFocus()
                return@setOnClickListener
            }
            if (editText_phonenumber.text.isEmpty()) {
                Toast.makeText(this,"휴대폰 번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
                editText_phonenumber.requestFocus()
                return@setOnClickListener
            }
            find_userid(editText_name.text.toString(), editText_phonenumber.text.toString())
        }

    }
    private fun find_userid(name: String, phonenumber: String) {
        compositeDisposable.add(myAPI.find_userid(name,phonenumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                if(message.contains("id")) {
                    val pref1 = getSharedPreferences("find_id", Context.MODE_PRIVATE)
                    val ed = pref1.edit()
                    ed.putString("find_id",message.toString())
                    ed.apply()
                    val pref2 = getSharedPreferences("find_id", Context.MODE_PRIVATE)
                    var userId = pref2.getString("find_id",null)
                    var gson = Gson()
                    var UserDTO = gson.fromJson(userId, User::class.java)
                    Log.d("find_userid", message.toString())
                    var dialog = AlertDialog.Builder(this)
                    dialog.setTitle("아이디 찾기")
                    dialog.setMessage("ID : "+ UserDTO.id + " 입니다.")
                    fun toast_p(){
                        var intent = Intent(this,Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                    var dialog_listener = object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which){
                                DialogInterface.BUTTON_POSITIVE->
                                    toast_p()
                            }
                        }
                    }
                    dialog.setPositiveButton("확인",dialog_listener)
                    dialog.show()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }
    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar_find_id)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.title = "아이디 찾기"
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }
}
