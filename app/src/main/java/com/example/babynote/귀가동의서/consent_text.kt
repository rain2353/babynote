package com.example.babynote.귀가동의서

import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
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
import kotlinx.android.synthetic.main.activity_consent_text.*

class consent_text : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var userID: String? = null
    var kindergarten: String? = null
    var classname: String? = null
    var writer_nickname: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent_text)
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


        // 각각 텍스트 정보 넣어주기.
        Picasso.get().load(Common.selected_consent_form?.baby_image).into(imageView11)
        textView75.setText(Common.selected_consent_form?.babyname)
        textView76.setText(Common.selected_consent_form?.classname)
        textView79.setText(Common.selected_consent_form?.consent_day)
        textView80.setText(Common.selected_consent_form?.consent_time)
        textView82.setText(Common.selected_consent_form?.consent_how)
        textView84.setText(Common.selected_consent_form?.relation1)
        textView85.setText(Common.selected_consent_form?.call1)
        textView87.setText(Common.selected_consent_form?.relation2)
        textView88.setText(Common.selected_consent_form?.call2)
        textView91.setText(Common.selected_consent_form?.consent_day)

        // 텍스트 밑줄긋기
        textView79.setPaintFlags(textView79.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        textView80.setPaintFlags(textView80.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        textView82.setPaintFlags(textView82.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        textView84.setPaintFlags(textView84.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        textView85.setPaintFlags(textView85.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        textView87.setPaintFlags(textView87.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        textView88.setPaintFlags(textView88.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        // 댓글 리사이클러뷰 레이아웃매니저
        val lm = LinearLayoutManager(this)
        consent_text_recyclerview.layoutManager = lm
        consent_text_recyclerview.setHasFixedSize(true)

        // 댓글 등록 버튼
        consent_comment_button.setOnClickListener {
            if (consent_comment.text.isEmpty()) {
                return@setOnClickListener
            } else {
                Log.d("nickname", Common.selected_baby?.baby_name + UserDTO.nickname)
                consent_comment_read(
                    Common.selected_consent_form?.num,
                    UserDTO.id,
                    Common.selected_baby?.baby_name + " " + UserDTO.nickname,
                    consent_comment.text.toString()
                )
                // 키보드를 내린다.
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(consent_comment.windowToken, 0)
            }
        }

    }
    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(consent_text_toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setTitle("귀가동의서")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (Common.selected_consent_form?.parents_id == userID) {
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.request_text_item, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정

        } else {

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.text_delete -> {

                var dialog = AlertDialog.Builder(this)
                dialog.setTitle("글 삭제")
                dialog.setMessage("글을 삭제 하시겠습니까?")
                fun toast_p() {

                }

                fun toast_n() {

                    consent_delete(Common.selected_consent_form?.num)
                }

                var dialog_listener = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> toast_p()
                            DialogInterface.BUTTON_NEGATIVE -> toast_n()
                        }
                    }

                }
                dialog.setPositiveButton("취소", dialog_listener)
                dialog.setNegativeButton("확인", dialog_listener)

                dialog.show()
                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }

    // 귀가동의서 글 삭제하기.
    private fun consent_delete(num: Int?) {
        compositeDisposable.add(myAPI.consent_delete(num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Toast.makeText(this, "글을 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                Log.d("consent_delete", message.toString())
                finish()

            }
                , { thr ->
                    Toast.makeText(this, "글을 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("consent_delete", thr.message.toString())

                }

            ))
    }

    // 글 번호에 따른 댓글 가져오기
    private fun consent_comment(consent_num: Int?) {
        compositeDisposable.add(myAPI.consent_comment_read(consent_num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ consent_comment_list ->
                consent_text_recyclerview.adapter =
                    consent_text_Adapter(baseContext, consent_comment_list)

//
            }
                , { thr ->
                    Log.d("notice_text_comment", thr.message.toString())
                }

            ))
    }
    // 댓글 쓰기
    private fun consent_comment_read(
        consent_num: Int?,
        comment_writer: String?,
        comment_nickname: String?,
        comment_content: String?
    ) {
        compositeDisposable.add(myAPI.consent_comment_write(
            consent_num,
            comment_writer,
            comment_nickname,
            comment_content
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("affectedRows")) {
                    consent_comment.setText("")
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    consent_comment(Common.selected_consent_form?.num)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        consent_comment(Common.selected_consent_form?.num)
        super.onResume()
    }

    override fun onPause() {
        consent_comment(Common.selected_consent_form?.num)
        super.onPause()
    }
}
