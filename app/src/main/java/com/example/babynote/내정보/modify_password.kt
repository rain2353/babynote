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
import kotlinx.android.synthetic.main.activity_modify_password.*

class modify_password : Activity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_modify_password)

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


        modify_password_button.setOnClickListener {
            if (!editText_password.text.isEmpty()) {
                val passMatches = Regex("^(?=.*[a-zA-Z0-9])(?=.*[!@#\$%^*+=-]).{8,20}$")
                if (editText_password.text.toString().matches(passMatches)) {
                    compositeDisposable.add(myAPI.change_userPassword(
                        UserDTO.id,
                        UserDTO.name,
                        UserDTO.phone_number,
                        editText_password.text.toString()
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { message ->
                            Toast.makeText(
                                this,
                                "비밀번호를 수정하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // 키보드를 내린다.
                            val imm =
                                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(editText_password.windowToken, 0)
                            finish()

                        })
                } else {
                    Toast.makeText(this, "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                    editText_password.requestFocus()
                    return@setOnClickListener
                }

            } else {
                Toast.makeText(this, "변경하실 비밀번호를 작성해주세요.", Toast.LENGTH_SHORT).show()
                editText_password.requestFocus()
                return@setOnClickListener
            }
        }
    }
//    private fun find_userPassword(id: String, name: String, phonenumber: String) {
//        compositeDisposable.add(myAPI.find_userPassword(id, name, phonenumber)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { message ->
//                if (message.contains("encrypted_password")) {
//                    val dialog_find_password = LayoutInflater.from(this@Find_Password)
//                        .inflate(R.layout.dialog_find_password, null)
////                    Log.d("find_userid", message.toString())
//                    var dialog = AlertDialog.Builder(this)
////                    dialog.setTitle("비밀번호 찾기")
////                    dialog.setMessage("변경할 비밀번호를 입력해주세요.")
//                    dialog.setView(dialog_find_password)
//
//                    fun toast_p() {
//                        var edit_password =
//                            dialog_find_password.findViewById<View>(R.id.editText_password) as EditText
//                        val passMatches = Regex("^(?=.*[a-zA-Z0-9])(?=.*[!@#\$%^*+=-]).{8,20}$")
//                        //비밀번호 유효성
//                        if (edit_password.text.toString().matches(passMatches)) {
//                            compositeDisposable.add(myAPI.change_userPassword(
//                                id,
//                                name,
//                                phonenumber,
//                                edit_password.text.toString()
//                            )
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe { message ->
//                                    if (message.contains("비밀번호 변경이 완료되었습니다.")) {
//                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                                        var intent = Intent(this, Login::class.java)
//                                        startActivity(intent)
//                                        finish()
//                                    } else {
//                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                                    }
//                                })
//                        } else {
//                            Toast.makeText(this, "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
//                            edit_password.requestFocus()
//                            return
//                        }
//
//                    }
//
//                    var dialog_listener = object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                            when (which) {
//                                DialogInterface.BUTTON_POSITIVE ->
//                                    toast_p()
//                            }
//                        }
//                    }
//                    dialog.setPositiveButton("확인", dialog_listener)
//                    dialog.show()
//                } else {
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
}
