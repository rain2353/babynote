package com.example.babynote.Add_baby

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.Main.MainActivity
import com.example.babynote.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_baby.*

class modify_baby : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_baby)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 수정할 아이 정보 불러오기.
        select_baby(Common.selected_baby?.num)

        // 아이 사진 변경
        button33.setOnClickListener {
            var intent = Intent (this, modify_babyimage::class.java)
            startActivity(intent)
        }

        // 이름 변경
        select_modify_babyname.setOnClickListener {
            var intent = Intent(this,com.example.babynote.Add_baby.modify_babyname::class.java)
            startActivity(intent)
        }

        // 생일 변경
        select_modify_babybirth.setOnClickListener {
            var intent = Intent(this,com.example.babynote.Add_baby.modify_babybirth::class.java)
            startActivity(intent)
        }

        // 성별 변경
        select_modify_babygender.setOnClickListener {
            var intent = Intent(this,com.example.babynote.Add_baby.modify_babygender::class.java)
            startActivity(intent)
        }

        // 소속 변경
        select_modify_kindergarten.setOnClickListener {
            var intent = Intent(this,com.example.babynote.Add_baby.modify_kindergarten::class.java)
            startActivity(intent)
        }

        // 아이 삭제하기 눌렀을때 다이얼로그가 나오고 삭제하기를 누르면 삭제, 취소를 누르면 삭제가 되지 않는다.
        textView123.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("아이 삭제하기")
            dialog.setMessage("아이를 삭제하시겠습니까?")


            fun toast_p() {    // 아이 삭제하는 버튼.
                compositeDisposable.add(myAPI.delete_baby(
                    Common.selected_baby?.num, Common.selected_baby?.baby_name
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { message ->
                        if (message.contains("아이를 삭제하였습니다.")) {
                            Toast.makeText(this, "아이를 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    })
            }

            fun toast_n() {
            }

            var dialog_listener = object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE ->
                            toast_p()
                        DialogInterface.BUTTON_NEGATIVE ->
                            toast_n()
                    }
                }
            }
            dialog.setPositiveButton("삭제하기", dialog_listener)
            dialog.setNegativeButton("취소", dialog_listener)
            dialog.show()
        }
    }


    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(modify_baby_toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.title = "프로필 설정하기"
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    private fun select_baby(num: Int?) {    // 아이 DB를 가져와 사용자에게 보여준다.
        compositeDisposable.add(myAPI.select_baby(num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                var gson = Gson()
                var select_babys =
                    gson.fromJson(message, com.example.babynote.Kiz.Kiz::class.java)
                Picasso.get().load(select_babys.baby_imagepath).into(imageView17)
                modify_babyname.text = select_babys.baby_name
                modify_babybirth.text = select_babys.baby_birth
                modify_babygender.text = select_babys.baby_gender
                modify_babykindergarten.text = select_babys.baby_kindergarten + " . " + select_babys.baby_class
                if (select_babys.state == "선생님"){
                    select_modify_babybirth.visibility = View.GONE
                    select_modify_babygender.visibility = View.GONE
                }
                val pref = getSharedPreferences("babyinfo", Context.MODE_PRIVATE)
                val ed = pref.edit()
                ed.putString("babyinfo", message.toString())
                ed.apply()
            }
                , { thr ->
                    Toast.makeText(this, "Error babys_modify_select", Toast.LENGTH_SHORT).show()
                    Log.d("babys_modify_select", thr.message.toString())
                }

            ))
    }

    override fun onResume() {
        select_baby(Common.selected_baby?.num)
        super.onResume()
    }
}
