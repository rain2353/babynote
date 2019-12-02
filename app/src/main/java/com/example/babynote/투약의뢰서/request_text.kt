package com.example.babynote.투약의뢰서

import android.content.Context
import android.content.DialogInterface
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_request_text.*

class request_text : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var num: Int = 0
    var writer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_text)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 메인 화면에 회원 정보 뿌리기
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)
        writer = UserDTO.id.toString()

        baby_name.text = Common.selected_administration_request_form?.babyname + " ( " + Common.selected_administration_request_form?.classname + " )"
        textView49.text = Common.selected_administration_request_form?.symptom
        textView52.text = Common.selected_administration_request_form?.medicine
        textView54.text = Common.selected_administration_request_form?.cc
        textView56.text = Common.selected_administration_request_form?.numberoftimes
        textView58.text = Common.selected_administration_request_form?.medicine_time
        textView60.text = Common.selected_administration_request_form?.storage
        textView62.text = Common.selected_administration_request_form?.baby_comment
        textView64.text = Common.selected_administration_request_form?.request_day
        textView65.text = Common.selected_administration_request_form?.parents_id

        val lm = LinearLayoutManager(this)
        request_text_recyclerview.layoutManager = lm
        request_text_recyclerview.setHasFixedSize(true)

        request_comment_button.setOnClickListener {
            if (request_text_comment.text.isEmpty()) {
                return@setOnClickListener
            } else {
                Log.d("nickname", Common.selected_baby?.baby_name + UserDTO.nickname)
                request_text_comment_read(
                    Common.selected_administration_request_form?.num,
                    UserDTO.id,
                    Common.selected_baby?.baby_name + " " + UserDTO.nickname,
                    request_text_comment.text.toString()
                )
                // 키보드를 내린다.
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(request_text_comment.windowToken, 0)
            }
        }
    }
    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(request_text_toolbar)

        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(com.example.babynote.R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("투약의뢰서 내용")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (Common.selected_administration_request_form?.parents_id == writer) {
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

                    request_text_delete(Common.selected_administration_request_form?.num)
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

    // 투약의뢰서 글 삭제하기.
    private fun request_text_delete(num: Int?) {
        compositeDisposable.add(myAPI.request_text_delete(num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Toast.makeText(this, "글을 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                Log.d("request_delete", message.toString())
                finish()

            }
                , { thr ->
                    Toast.makeText(this, "글을 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("request_delete", thr.message.toString())

                }

            ))
    }
    // 글 번호에 따른 댓글 가져오기
    private fun request_text_comment(request_num: Int?) {
        compositeDisposable.add(myAPI.request_comment_read(request_num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ request_comment_list ->
                request_text_recyclerview.adapter =
                    request_text_Adapter(baseContext, request_comment_list)
                request_text_recyclerview.adapter?.notifyDataSetChanged()
//
            }
                , { thr ->
                    Log.d("notice_text_comment", thr.message.toString())
                }

            ))
    }
    // 댓글 쓰기
    private fun request_text_comment_read(
        request_num: Int?,
        comment_writer: String?,
        comment_nickname: String?,
        comment_content: String?
    ) {
        compositeDisposable.add(myAPI.request_comment_write(
            request_num,
            comment_writer,
            comment_nickname,
            comment_content
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("affectedRows")) {
                    request_text_comment.setText("")
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    request_text_comment(Common.selected_administration_request_form?.num)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        request_text_comment(Common.selected_administration_request_form?.num)
        super.onResume()
    }

    override fun onPause() {
        request_text_comment(Common.selected_administration_request_form?.num)
        super.onPause()
    }

}
