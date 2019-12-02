package com.example.babynote.귀가동의서

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.example.babynote.User.User
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_consent_write.*
import java.text.SimpleDateFormat
import java.util.*

class consent_write : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var week: String? = null
    var WhatTime: String? = null
    var userID: String? = null
    var kindergarten: String? = null
    var classname: String? = null
    var writer_nickname: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent_write)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)

        Picasso.get().load(Common.selected_baby?.baby_imagepath).into(imageView10)
        textView33.text = Common.selected_baby?.baby_name
        textView66.text = Common.selected_baby?.baby_class
        button6.setOnClickListener { showDate() }
        button7.setOnClickListener { showTime() }


    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(consent_write_toolbar)

        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("귀가동의서 작성하기")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.write_ok, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.write_ok -> {
                if (week == null){
                    Toast.makeText(this,"날짜를 지정해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else if (WhatTime == null) {
                    Toast.makeText(this,"시간을 지정해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else if ( editText6.text.isEmpty() ){
                    Toast.makeText(this, "귀가 방법을 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else if ( editText7.text.isEmpty() ){
                    Toast.makeText(this, "보호자와 원아와의 관계를 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else if ( editText8.text.isEmpty() ){
                    Toast.makeText(this, "보호자의 전화번호를 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else if ( editText9.text.isEmpty()) {
                    Toast.makeText(this, "비상연락망의 원아와의 관계를 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else if ( editText10.text.isEmpty() ){
                    Toast.makeText(this, "비상연락망의 전화번호를 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                } else {
                    write_consent(
                        Common.selected_baby?.baby_imagepath,
                        Common.selected_baby?.baby_name,
                        week,
                        WhatTime,
                        editText6.text.toString(),
                        editText7.text.toString(),
                        editText8.text.toString(),
                        editText9.text.toString(),
                        editText10.text.toString(),
                        Common.selected_baby?.baby_kindergarten,
                        Common.selected_baby?.baby_class,
                        userID
                    )
                }

            }
        }
        return super.onOptionsItemSelected(item)

    }

    // ---------------------------------------------------------------------------------------------
    // 날짜 지정하기.
    private fun showDate() {
        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        var date_listener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val simpledateformat = SimpleDateFormat("EEEE")
                val date = Date(year, month, dayOfMonth - 1)
                val dayOfWeek = simpledateformat.format(date)

                button6.setText("${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}")
                textView74.text =
                    "${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}"
                week =
                    "${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}"
                Toast.makeText(this@consent_write, week.toString(), Toast.LENGTH_SHORT).show()

            }
        }

        var builder = DatePickerDialog(this, date_listener, year, month, day)
        builder.show()
    }

    //----------------------------------------------------------------------------------------------
    // 귀가시간 설정하기
    private fun showTime() {
        var time = Calendar.getInstance()
        var hour = time.get(Calendar.HOUR)
        var minute = time.get(Calendar.MINUTE)

        var timeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                if (hourOfDay < 12) {
                    var day = "오전"
                    button7.setText("${day}" + " " + "${hourOfDay}" + " : " + "${minute}")
                    WhatTime = "${day}" + " " + "${hourOfDay}" + " : " + "${minute}"
                } else if (hourOfDay == 12) {
                    var day = "오후"
                    button7.setText("${day}" + " " + "${hourOfDay}" + " : " + "${minute}")
                    WhatTime = "${day}" + " " + "${hourOfDay}" + " : " + "${minute}"
                } else {
                    var day = "오후"
                    button7.setText("${day}" + " " + "${hourOfDay - 12}" + " : " + "${minute}")
                    WhatTime = "${day}" + " " + "${hourOfDay - 12}" + " : " + "${minute}"
                }

            }
        }
        var builder = TimePickerDialog(this, timeListener, hour, minute, false)
        builder.show()
    }

    // 레트로핏을 통해 mysql 저장하기
    private fun write_consent(
        baby_image: String?,
        babyname: String?,
        consent_day: String?,
        consent_time: String?,
        consent_how: String?,
        relation1: String?,
        call1: String?,
        relation2: String?,
        call2: String?,
        kindergarten: String?,
        classname: String?,
        parents_id: String?
    ) {
        compositeDisposable.add(myAPI.write_consent(
            baby_image,
            babyname,
            consent_day,
            consent_time,
            consent_how,
            relation1,
            call1,
            relation2,
            call2,
            kindergarten,
            classname,
            parents_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("success")) {
                    Toast.makeText(this, "귀가동의서 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Consent_form::class.java)
                    intent.putExtra("babyname", Common.selected_baby?.baby_name)
                    intent.putExtra("kindergarten", Common.selected_baby?.baby_kindergarten)
                    intent.putExtra("classname", Common.selected_baby?.baby_class)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
