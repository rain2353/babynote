package com.example.babynote.알림장

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_advice_text.*

class advice_text : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var num: Int = 0
    var writer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advice_text)
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

        // 알림장에 정보 채우기.
        mybaby(Common.selected_advice?.advice_writer, num)
        advice_text_writer_nickname.text = Common.selected_advice?.advice_nickname
        advice_text_write_time.text = Common.selected_advice?.advice_write_time
        Picasso.get().load(Common.selected_advice?.file).into(imageView15)
        textView101.text = Common.selected_advice?.advice_content
        textView103.text = Common.selected_advice?.feel
        textView105.text = Common.selected_advice?.health
        textView107.text = Common.selected_advice?.temperature
        textView109.text = Common.selected_advice?.MealorNot
        textView111.text = Common.selected_advice?.sleep
        textView113.text = Common.selected_advice?.poop

        // 이미지 클릭시 확대해서 보기.
        imageView15.setOnClickListener {
            var intent = Intent(this, advice_photoview::class.java)
            startActivity(intent)
        }

        // 알림장 댓글 리사이클러뷰 설정하고 아이템 불러오기.
        advice_comment(Common.selected_advice?.num)
        val lm = LinearLayoutManager(this)
        advice_comment_recyclerview.layoutManager = lm as RecyclerView.LayoutManager?
        advice_comment_recyclerview.setHasFixedSize(true)

        // 알림장 댓글작성 버튼 누르면 댓글을 작성한다. 하지만 댓글이 없을시엔 댓글 작성이 안된다.
        advice_comment_button.setOnClickListener {
            if (advice_text_comment.text.isEmpty()) {
                Toast.makeText(this,"댓글을 작성해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                Log.d("nickname", Common.selected_baby?.baby_name + UserDTO.nickname)
                advice_comment_write(
                    Common.selected_advice?.num,
                    UserDTO.id,
                    Common.selected_baby?.baby_name + " " + UserDTO.nickname,
                    advice_text_comment.text.toString()
                )
                // 키보드를 내린다.
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(advice_text_comment.windowToken, 0)
            }
        }
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(advice_text_toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setTitle("알림장 내용")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }
    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (Common.selected_advice?.advice_writer == writer) {
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.text_item, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정

        } else {

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.text_modify -> {
                var intent = Intent(this, advice_modify::class.java)
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

                    advice_text_delete(Common.selected_advice?.num)
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
                    .into(advice_text_writer_image)

            }
                , { thr ->
                    Toast.makeText(this, "Error mybaby load", Toast.LENGTH_SHORT).show()
                    Log.d("Main_kiz", thr.message.toString())

                }

            ))
    }
    // 알림장 글 삭제하기.
    private fun advice_text_delete(num: Int?) {
        compositeDisposable.add(myAPI.advice_text_delete(num)
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
    // 글 번호에 따른 댓글 가져오기
    private fun advice_comment(advice_num: Int?) {
        compositeDisposable.add(myAPI.advice_comment_read(advice_num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ advice_comment_list ->
                advice_comment_recyclerview.adapter =
                    advice_comment_Adapter(baseContext, advice_comment_list)
                advice_comment_recyclerview.adapter?.notifyDataSetChanged()
            }
                , { thr ->
                    Log.d("notice_text_comment", thr.message.toString())
                }

            ))
    }

    // 댓글 쓰기
    private fun advice_comment_write(
        advice_num: Int?,
        comment_writer: String?,
        comment_nickname: String?,
        comment_content: String?
    ) {
        compositeDisposable.add(myAPI.advice_comment_write(
            advice_num,
            comment_writer,
            comment_nickname,
            comment_content
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("affectedRows")) {
                    advice_text_comment.setText("")
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    advice_comment(Common.selected_advice?.num)
                    advice_comment_recyclerview.adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        advice_comment(Common.selected_advice?.num)
        super.onResume()
    }

    override fun onPause() {
        advice_comment(Common.selected_advice?.num)
        super.onPause()
    }
}
