package com.example.babynote.Add_baby

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_babygender.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class modify_babygender : Activity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var gender: String? = null // 아이 성별
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_modify_babygender)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 수정할 아이 정보
        var gson = Gson()
        val pref1 = getSharedPreferences("babyinfo", Context.MODE_PRIVATE)
        var babyinfo = pref1.getString("babyinfo", null)
        var babyDTO = gson.fromJson(babyinfo, Kiz::class.java)

        gender = babyDTO.baby_gender

        // 아이 성별에 따라서 버튼이 눌러있는걸 보여준다.
        if (gender == "남아") {
            button34.setBackgroundResource(R.drawable.trumpet_click)
            button34.textColor = Color.WHITE
            button35.backgroundColor = Color.TRANSPARENT
            button35.textColor = Color.BLACK

        } else {
            button35.setBackgroundResource(R.drawable.trumpet_click)
            button35.textColor = Color.WHITE
            button34.backgroundColor = Color.TRANSPARENT
            button34.textColor = Color.BLACK
        }

        // 버튼을 클릭하면 색상이 바뀌고 선택이 된다.
        button34.setOnClickListener {
            button34.setBackgroundResource(R.drawable.trumpet_click)
            button34.textColor = Color.WHITE
            gender = "남아"
            button35.backgroundColor = Color.TRANSPARENT
            button35.textColor = Color.BLACK

        }
        button35.setOnClickListener {
            button35.setBackgroundResource(R.drawable.trumpet_click)
            button35.textColor = Color.WHITE
            gender = "여아"
            button34.backgroundColor = Color.TRANSPARENT
            button34.textColor = Color.BLACK
        }
        modify_babygender_button.setOnClickListener {       // 성별 변경 버튼.
            compositeDisposable.add(myAPI.modify_baby(
                babyDTO.num,
                babyDTO.baby_name,
                babyDTO.baby_birth,
                gender,
                babyDTO.baby_kindergarten,
                babyDTO.baby_class,
                babyDTO.baby_imagepath,
                babyDTO.parents_id,
                babyDTO.state
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { message ->
                    Toast.makeText(
                        this,
                        "성별을 수정하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                })
        }
    }
}
