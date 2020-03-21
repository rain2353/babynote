package com.example.babynote.Add_baby

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_babyclass.*

class modify_babyclass : Activity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var kindergartenName : String? = null     // 아이의 유치원 이름
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_babyclass)
        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        kindergartenName = intent.getStringExtra("유치원이름")
        textView116.text = kindergartenName

        var gson = Gson()
        val pref1 = getSharedPreferences("babyinfo", Context.MODE_PRIVATE)
        var babyinfo = pref1.getString("babyinfo", null)
        var babyDTO = gson.fromJson(babyinfo, Kiz::class.java)

        modify_babyclass_button.setOnClickListener {
            if (!editText_babyclass.text.isEmpty()){
                compositeDisposable.add(myAPI.modify_baby(
                    babyDTO.num,
                    babyDTO.baby_name,
                    babyDTO.baby_birth,
                    babyDTO.baby_gender,
                    kindergartenName,   // 유치원 이름
                    editText_babyclass.text.toString(),   // 반 이름
                    babyDTO.baby_imagepath,
                    babyDTO.parents_id,
                    babyDTO.state
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { message ->
                        Toast.makeText(
                            this,
                            "반 이름을 수정하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // 키보드를 내린다.
                        val imm =
                            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editText_babyclass.windowToken, 0)
                        finish()

                    })
            } else {
                Toast.makeText(this, "변경하실 반 이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
                editText_babyclass.requestFocus()
                return@setOnClickListener
            }
        }
    }
}
