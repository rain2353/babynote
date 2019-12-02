package com.example.babynote.투약의뢰서

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_administration_request_form.*

class Administration_request_form : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var state: String? = null
    var kindergarten: String? = null
    var classname: String? = null
    var babyname: String? = null
    var UserID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administration_request_form)
        setToolbar()
        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)
        state = UserDTO.state.toString()
        UserID = UserDTO.id
        kindergarten = intent.getStringExtra("kindergarten")
        classname = intent.getStringExtra("classname")
        babyname = intent.getStringExtra("babyname")


        val lm = LinearLayoutManager(this)
        lm.reverseLayout = true   // 리사이클러뷰 역순 출력.
        lm.stackFromEnd = true    // 리사이클러뷰 역순 출력.
        request_recyclerview.layoutManager = lm
        request_recyclerview.setHasFixedSize(true)

        if (state == "선생님") {

            request_list_all(kindergarten, classname)
        } else {

            request_list(kindergarten, classname, UserDTO.id, Common.selected_baby?.baby_name)
        }
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar6)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("투약의뢰서")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (state == "학부모") {
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.main_menu, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정

        } else {

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.menu_write -> {
                if (state == "학부모") {
                    Toast.makeText(this, "글쓰기 클릭", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, request_write::class.java)
                    intent.putExtra("유치원이름", kindergarten)
                    intent.putExtra("유치원반이름", classname)
                    intent.putExtra("작성자", babyname)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "글쓰기 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }
                //onBackPressed()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }


    }

    // 선생님이라면 같은 반 학생들 전체 리스트 불러오기
    private fun request_list_all(kindergarten: String?, classname: String?) {
        compositeDisposable.add(myAPI.request_list_all(kindergarten, classname)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ administration_request_list ->
                request_recyclerview.adapter =
                    request_recyclerviewAdapter(baseContext, administration_request_list)
                (request_recyclerview.adapter as request_recyclerviewAdapter).notifyDataSetChanged()
            }
                , { thr ->
                    Log.d("request_recyclerview", thr.message.toString())
                }

            ))
    }

    // 학부모라면 자기가 쓴 글들만 불러오기
    private fun request_list(
        kindergarten: String?,
        classname: String?,
        parents_id: String?,
        babyname: String?
    ) {
        compositeDisposable.add(myAPI.request_list(kindergarten, classname, parents_id, babyname)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ administration_request_list ->
                request_recyclerview.adapter =
                    request_recyclerviewAdapter(baseContext, administration_request_list)
                (request_recyclerview.adapter as request_recyclerviewAdapter).notifyDataSetChanged()
            }
                , { thr ->
                    Log.d("request_recyclerview", thr.message.toString())
                }

            ))
    }

    override fun onResume() {
        if (state == "선생님") {

            request_list_all(kindergarten, classname)
        } else {

            request_list(kindergarten, classname, UserID, Common.selected_baby?.baby_name)
        }
        super.onResume()
    }

}
