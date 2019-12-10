package com.example.babynote.앨범

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
import kotlinx.android.synthetic.main.activity_album_photoview.*

class Album_photoview : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var num: Int = 0
    var writer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_photoview)
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

        mybaby(Common.selected_album?.album_writer, num)

        Picasso.get().load(Common.selected_album?.album_image).into(album_photoView)
        album_photoView.setOnClickListener {
            var intent = Intent(this, album_Image::class.java)
            startActivity(intent)
        }
        album_comment_read(Common.selected_album?.num)
        val lm = LinearLayoutManager(this)
        album_comment_recyclerview.layoutManager = lm
        album_comment_recyclerview.setHasFixedSize(true)
        album_photo_comment_button.setOnClickListener {
            if (album_photo_comment.text.isEmpty()) {
                return@setOnClickListener
            } else {
                Log.d("nickname", Common.selected_baby?.baby_name + UserDTO.nickname)
                album_comment_write(
                    Common.selected_album?.num,
                    UserDTO.id,
                    Common.selected_baby?.baby_name + " " + UserDTO.nickname,
                    album_photo_comment.text.toString()
                )
                // 키보드를 내린다.
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(album_photo_comment.windowToken, 0)
            }
        }
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
                    .into(album_writer_image)
                album_writer_nickname.text = Common.selected_album?.album_nickname
                album_write_time.text = Common.selected_album?.album_time
            }
                , { thr ->
                    Toast.makeText(this, "Error mybaby load", Toast.LENGTH_SHORT).show()
                    Log.d("Main_kiz", thr.message.toString())

                }

            ))
    }

    // 글 번호에 따른 댓글 가져오기
    private fun album_comment_read(album_num: Int?) {
        compositeDisposable.add(myAPI.album_comment_read(album_num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ album_comment_list ->
                album_comment_recyclerview.adapter =
                    album_comment_Adapter(baseContext, album_comment_list)
                album_comment_recyclerview.adapter?.notifyDataSetChanged()
            }
                , { thr ->
                    Log.d("album_comment", thr.message.toString())
                }

            ))
    }

    // 댓글 쓰기
    private fun album_comment_write(
        album_num: Int?,
        comment_writer: String?,
        comment_nickname: String?,
        comment_content: String?
    ) {
        compositeDisposable.add(myAPI.album_comment_write(
            album_num,
            comment_writer,
            comment_nickname,
            comment_content
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("affectedRows")) {
                    album_photo_comment.setText("")
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    album_comment_read(Common.selected_album?.num)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        album_comment_read(Common.selected_album?.num)
        super.onResume()
    }

    override fun onPause() {
        album_comment_read(Common.selected_album?.num)
        super.onPause()
    }


}
