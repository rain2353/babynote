package com.example.babynote.식단표

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_carte_read.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class carte_read : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var num: Int = 0
    var writer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carte_read)
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

        // 작성자 정보와 작성 날짜.
        carte_writer_nickname.text = Common.selected_carte?.writer_nickname
        carte_write_time.text = Common.selected_carte?.carte_write_time
        mybaby(Common.selected_carte?.writer_id, num)
        // 식단표 글
        Picasso.get().load(Common.selected_carte?.file1).resize(400, 400).into(imageView7)
        Picasso.get().load(Common.selected_carte?.file2).resize(400, 400).into(imageView8)
        Picasso.get().load(Common.selected_carte?.file3).resize(400, 400).into(imageView9)
        textView29.text = "오전 간식 : " + Common.selected_carte?.menu1
        textView30.text = "점심 식사 : " + Common.selected_carte?.menu2
        textView31.text = "오후 간식 : " + Common.selected_carte?.menu3

        imageView7.onClick {
            var intent = Intent(this@carte_read, carte_photoview::class.java)
            intent.putExtra("Image", Common.selected_carte?.file1)
            startActivity(intent)
        }
        imageView8.onClick {
            var intent = Intent(this@carte_read, carte_photoview::class.java)
            intent.putExtra("Image", Common.selected_carte?.file2)
            startActivity(intent)
        }
        imageView9.onClick {
            var intent = Intent(this@carte_read, carte_photoview::class.java)
            intent.putExtra("Image", Common.selected_carte?.file3)
            startActivity(intent)
        }

        val lm = LinearLayoutManager(this)
        carte_comment_recyclerview.layoutManager = lm
        carte_comment_recyclerview.setHasFixedSize(true)
        // 댓글 읽어오기
        carte_comment(Common.selected_carte?.num)

        // 댓글 작성버튼 눌럿을떄
        carte_comment_button.setOnClickListener {
            if (edit_carte_comment.text.isEmpty()) {
                return@setOnClickListener
            } else {
                Log.d("nickname", Common.selected_baby?.baby_name + UserDTO.nickname)
                carte_comment_write(
                    Common.selected_carte?.num,
                    UserDTO.id,
                    Common.selected_baby?.baby_name + " " + UserDTO.nickname,
                    edit_carte_comment.text.toString()
                )
                // 키보드를 내린다.
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edit_carte_comment.windowToken, 0)
            }
        }
    }
    // 댓글 작성
    private fun carte_comment_write(
        carte_num: Int?,
        comment_writer: String?,
        comment_nickname: String?,
        comment_content: String?
    ) {
        compositeDisposable.add(myAPI.carte_comment_write(
            carte_num,
            comment_writer,
            comment_nickname,
            comment_content
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("affectedRows")) {
                    edit_carte_comment.setText("")
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    carte_comment(Common.selected_carte?.num)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })

    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(carte_read_toolbar)

        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("식단표")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
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
                    .into(carte_writer_image)

            }
                , { thr ->
                    Toast.makeText(this, "Error mybaby load", Toast.LENGTH_SHORT).show()
                    Log.d("Main_kiz", thr.message.toString())

                }

            ))
    }

    // 글 번호에 따른 댓글 가져오기
    private fun carte_comment(carte_num: Int?) {
        compositeDisposable.add(myAPI.carte_comment_read(carte_num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ carte_comment_list ->
                carte_comment_recyclerview.adapter =
                    carte_comment_adapter(baseContext, carte_comment_list)
                carte_comment_recyclerview.adapter?.notifyDataSetChanged()
//
            }
                , { thr ->
                    Log.d("notice_text_comment", thr.message.toString())
                }

            ))
    }

    override fun onResume() {
        carte_comment(Common.selected_carte?.num)
        super.onResume()
    }

    override fun onPause() {
        carte_comment(Common.selected_carte?.num)
        super.onPause()
    }
}
