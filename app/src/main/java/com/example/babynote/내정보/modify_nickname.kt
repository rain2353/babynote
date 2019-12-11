package com.example.babynote.내정보

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import com.example.babynote.User.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_nickname.*

class modify_nickname : Activity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var nickname:String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_modify_nickname)

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

        // 유저가 선택한 라디오버튼.
        nicknameGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.mother -> {
                    textView10.text = "김철수 엄마"
                    this.nickname = "엄마"
                }
                R.id.father -> {
                    textView10.text = "김철수 아빠"
                    this.nickname = "아빠"
                }
                R.id.grand_mother -> {
                    textView10.text = "김철수 할머니"
                    this.nickname = "할머니"
                }
                R.id.grand_father -> {
                    textView10.text = "김철수 할아버지"
                    this.nickname = "할아버지"
                }
                R.id.radioButton -> {
                    textView10.text = "김철수 " + radioButton.text.toString()
                    this.nickname = radioButton.text.toString()
                }
            }
        }

        modify_nickname_button.setOnClickListener {
            if (nickname == null ){
                Toast.makeText(this,"호칭을 설정해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                compositeDisposable.add(myAPI.modify_myInfo(
                    UserDTO.num,
                    UserDTO.unique_id,
                    UserDTO.id,
                    UserDTO.name,
                    UserDTO.phone_number,
                    UserDTO.email,
                    UserDTO.encrypted_password,
                    UserDTO.state,
                    nickname.toString(),
                    UserDTO.salt,
                    UserDTO.created_at,
                    UserDTO.updated_at
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { message ->
                        Toast.makeText(
                            this,
                            "이름을 수정하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // 키보드를 내린다.
                        val imm =
                            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(radioButton.windowToken, 0)
                        finish()
                    })
            }
        }
    }
}
