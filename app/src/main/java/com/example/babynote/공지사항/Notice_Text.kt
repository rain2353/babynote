package com.example.babynote.공지사항

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_notice__text.*


class Notice_Text : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var num: Int = 0
    var writer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.babynote.R.layout.activity_notice__text)
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

        mybaby(Common.selected_notice?.notice_writer, num)
        notice_text_title.setPaintFlags(notice_text_title.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        notice_text_title.text = Common.selected_notice?.notice_title
        notice_text_content.text = Common.selected_notice?.notice_content
        notice_text_write_time.text = Common.selected_notice?.notice_time
        notice_text_writer_nickname.text = Common.selected_notice?.notice_nickname
        Picasso.get().load(Common.selected_notice?.notice_image).into(notice_text_image)
        notice_text_comment(Common.selected_notice?.num)
        val lm = LinearLayoutManager(this)
        notice_text_recyclerview.layoutManager = lm
        notice_text_recyclerview.setHasFixedSize(true)
//        notice_text_recyclerview.adapter = notice_text_Adapter(this,notice_comment_list)

        notice_text_image.setOnClickListener {
            var intent = Intent(this, PhotoView::class.java)
            startActivity(intent)
        }
        notice_comment_button.setOnClickListener {
            if (notice_text_comment.text.isEmpty()) {
                return@setOnClickListener
            } else {
                Log.d("nickname", Common.selected_baby?.baby_name + UserDTO.nickname)
                notice_text_comment_read(
                    Common.selected_notice?.num,
                    UserDTO.id,
                    Common.selected_baby?.baby_name + " " + UserDTO.nickname,
                    notice_text_comment.text.toString()
                )
                // 키보드를 내린다.
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(notice_text_comment.windowToken, 0)
            }
        }
    }



    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(notice_text_toolbar)

        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(com.example.babynote.R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("공지사항 내용")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (Common.selected_notice?.notice_writer == writer) {
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.text_item, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정

        } else {

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.text_modify -> {
                var intent = Intent(this@Notice_Text, notice_modify::class.java)
//                intent.putExtra("유치원이름", kindergarten)
//                intent.putExtra("유치원반이름", classname)
//                intent.putExtra("작성자", babyname)
                startActivity(intent)

                //onBackPressed()
                return true
            }
            R.id.text_delete -> {

                var dialog = AlertDialog.Builder(this)
                dialog.setTitle("글 삭제")
                dialog.setMessage("글을 삭제 하시겠습니까?")
                fun toast_p() {

                }

                fun toast_n() {

                    notice_text_delete(Common.selected_notice?.num)
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

    // 작성자 정보 가져오는 메소드
    private fun mybaby(parents_id: String?, num: Int) {
        compositeDisposable.add(myAPI.mybaby(parents_id, num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Log.d("Main_kiz", message.toString())
                var gson = Gson()
                var Main_babys =
                    gson.fromJson(message, com.example.babynote.Kiz.Main_Kiz::class.java)
                Picasso.get().load(Main_babys.baby_imagepath).resize(100, 100)
                    .into(notice_text_writer_image)

            }
                , { thr ->
                    Toast.makeText(this, "Error mybaby load", Toast.LENGTH_SHORT).show()
                    Log.d("Main_kiz", thr.message.toString())

                }

            ))
    }


    // 글 번호에 따른 댓글 가져오기
    private fun notice_text_comment(notice_num: Int?) {
        compositeDisposable.add(myAPI.notice_text_comment_read(notice_num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ notice_comment_list ->
                notice_text_recyclerview.adapter =
                    notice_text_Adapter(baseContext, notice_comment_list)
                notice_text_recyclerview.adapter?.notifyDataSetChanged()
//
            }
                , { thr ->
                    Log.d("notice_text_comment", thr.message.toString())
                }

            ))
    }

    // 공지사항 글 삭제하기.
    private fun notice_text_delete(num: Int?) {
        compositeDisposable.add(myAPI.notice_text_delete(num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Toast.makeText(this, "글을 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                Log.d("notice_delete", message.toString())
                finish()

            }
                , { thr ->
                    Toast.makeText(this, "글을 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("notice_delete", thr.message.toString())

                }

            ))
    }

    // 댓글 쓰기
    private fun notice_text_comment_read(
        notice_num: Int?,
        comment_writer: String?,
        comment_nickname: String?,
        comment_content: String?
    ) {
        compositeDisposable.add(myAPI.notice_text_comment_write(
            notice_num,
            comment_writer,
            comment_nickname,
            comment_content
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("affectedRows")) {
                    notice_text_comment.setText("")
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    notice_text_comment(Common.selected_notice?.num)
//                    notice_text_recyclerview.adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        notice_text_comment(Common.selected_notice?.num)
        super.onResume()
    }

    override fun onPause() {
        notice_text_comment(Common.selected_notice?.num)
        super.onPause()
    }
}
