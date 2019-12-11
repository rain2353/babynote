package com.example.babynote.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.babynote.Add_baby.Add_baby
import com.example.babynote.Api.INodeJS
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.example.babynote.User.User
import com.example.babynote.공지사항.Notice
import com.example.babynote.귀가동의서.Consent_form
import com.example.babynote.내정보.MyInfo
import com.example.babynote.식단표.Carte
import com.example.babynote.알림장.Advice_note
import com.example.babynote.앨범.Album
import com.example.babynote.투약의뢰서.Administration_request_form
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_nav_header.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var isNavigationOpen = false

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()

        myAPI = Common.api

        navigationView.setNavigationItemSelectedListener(this)
        //Init API
//        val retrofit = RetrofitClient.instance
//        myAPI = retrofit.create(INodeJS::class.java)


        // 메인 화면에 회원 정보 뿌리기
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)


//        if (UserDTO.state == "선생님") {
//            SonName.text = UserDTO.name + UserDTO.nickname
//        }

        num = intent.getIntExtra("num", 0)
        mybaby(UserDTO.id.toString(), num)

//        if (SonName.text.toString() == "자녀 이름") {
//            babyphoto.visibility = View.INVISIBLE
//            SonName.visibility = View.INVISIBLE
//            kindergartenName.visibility = View.INVISIBLE
//            ClassName.visibility = View.INVISIBLE
//        } else {
//            babyphoto.visibility = View.VISIBLE
//            SonName.visibility = View.VISIBLE
//            kindergartenName.visibility = View.VISIBLE
//            ClassName.visibility = View.VISIBLE
//        }
//        SonName.text = Main_babys.baby_name
//        kindergartenName.text = Main_babys.baby_kindergarten
//        ClassName.text = Main_babys.baby_class
        // 알림장 버튼 눌렀을때 알림장으로 이동.

        advice_note_move.setOnClickListener {
            val intent = Intent(this, Advice_note::class.java)
            intent.putExtra("babyname", SonName.text.toString())
            intent.putExtra("kindergarten", kindergartenName.text.toString())
            intent.putExtra("classname", ClassName.text.toString())
            startActivity(intent)
        }

        // 공지사항 버튼 눌렀을때 공지사항으로 이동.

        notice_move.setOnClickListener {
            val intent = Intent(this, Notice::class.java)
            intent.putExtra("babyname", SonName.text.toString())
            intent.putExtra("kindergarten", kindergartenName.text.toString())
            intent.putExtra("classname", ClassName.text.toString())
            startActivity(intent)
        }

        // 앨범 버튼 눌렀을때 앨범으로 이동.

        album_move.setOnClickListener {
            val intent = Intent(this, Album::class.java)
            intent.putExtra("babyname", SonName.text.toString())
            intent.putExtra("kindergarten", kindergartenName.text.toString())
            intent.putExtra("classname", ClassName.text.toString())
            startActivity(intent)
        }

        // 식단표 버튼 눌렀을때 식단표로 이동.

        carte_move.setOnClickListener {
            val intent = Intent(this, Carte::class.java)
            intent.putExtra("babyname", SonName.text.toString())
            intent.putExtra("kindergarten", kindergartenName.text.toString())
            intent.putExtra("classname", ClassName.text.toString())
            startActivity(intent)
        }

        // 투약의뢰서 버튼 눌렀을때 투약의뢰서로 이동.

        administration_request_form_move.setOnClickListener {
            val intent = Intent(this, Administration_request_form::class.java)
            intent.putExtra("babyname", SonName.text.toString())
            intent.putExtra("kindergarten", kindergartenName.text.toString())
            intent.putExtra("classname", ClassName.text.toString())
            startActivity(intent)
        }

        // 귀가동의서 버튼 눌렀을때 귀가동의서로 이동.

        consent_form_move.setOnClickListener {
            val intent = Intent(this, Consent_form::class.java)
            intent.putExtra("babyname", SonName.text.toString())
            intent.putExtra("kindergarten", kindergartenName.text.toString())
            intent.putExtra("classname", ClassName.text.toString())
            startActivity(intent)
        }


    }


    fun Add_son_Click(v: View?) {
//        Snackbar.make(toolbar,"자녀 추가",Snackbar.LENGTH_SHORT).show()
        val intent = Intent(this, Add_baby::class.java)
        startActivity(intent)
        drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
    }

    fun Myinfo_Click(v: View?) {
//        Snackbar.make(toolbar, "내 정보", Snackbar.LENGTH_SHORT).show()
        val intent = Intent(this, MyInfo::class.java)
        startActivity(intent)
        drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
//        return true
//    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 콜백
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item!!.itemId) {
            android.R.id.home -> { // 메뉴 버튼
                drawerLayout.openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
                // 로그인한 아이디를 네비게이션 드로어 아이디에 입력한다.
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID: String? = pref.getString("id", 0.toString())
                this.MyName.text = userID
                UserInfo(userID)
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                var userEmail = pref1.getString(userID, null)
                var gson = Gson()
                var UserDTO = gson.fromJson(userEmail, User::class.java)
                if (UserDTO.state == "선생님") {
                    Add_son.visibility = View.INVISIBLE
                }
                fetchbabys(userID)
                val lm = LinearLayoutManager(this)
                mRecyclerView.layoutManager = lm
                mRecyclerView.setHasFixedSize(true)


            }
//            R.id.menu_search-> Snackbar.make(toolbar,"Search menu pressed",Snackbar.LENGTH_SHORT).show()
//            R.id.menu_account-> Snackbar.make(toolbar,"Account menu pressed",Snackbar.LENGTH_SHORT).show()
//            R.id.menu_logout-> Snackbar.make(toolbar,"Logout menu pressed",Snackbar.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun mybaby(parents_id: String?, num: Int) {
        compositeDisposable.add(myAPI.mybaby(parents_id, num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                Log.d("Main_kiz", message.toString())
                var gson = Gson()
                var Main_babys =
                    gson.fromJson(message, com.example.babynote.Kiz.Main_Kiz::class.java)
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id", 0.toString())
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                var userEmail = pref1.getString(userID, null)
                var UserDTO = gson.fromJson(userEmail, User::class.java)
                if (UserDTO.state == "선생님") {
                    Picasso.get().load(Main_babys.baby_imagepath).resize(200, 200).into(babyphoto)
                    SonName.text = Main_babys.baby_name + " " + UserDTO.nickname
                    kindergartenName.text = Main_babys.baby_kindergarten
                    ClassName.text = Main_babys.baby_class

                } else {
                    Picasso.get().load(Main_babys.baby_imagepath).resize(200, 200).into(babyphoto)
                    SonName.text = Main_babys.baby_name
                    kindergartenName.text = Main_babys.baby_kindergarten
                    ClassName.text = Main_babys.baby_class
                }

//                if (SonName.text.toString() == "자녀 이름") {
//                    babyphoto.visibility = View.INVISIBLE
//                    SonName.visibility = View.INVISIBLE
//                    kindergartenName.visibility = View.INVISIBLE
//                    ClassName.visibility = View.INVISIBLE
//                } else {
//                    babyphoto.visibility = View.VISIBLE
//                    SonName.visibility = View.VISIBLE
//                    kindergartenName.visibility = View.VISIBLE
//                    ClassName.visibility = View.VISIBLE
//                }

            }
                , { thr ->
                    Toast.makeText(this, "Error mybaby load", Toast.LENGTH_SHORT).show()
                    Log.d("Main_kiz", thr.message.toString())

                }

            ))
    }

    private fun fetchbabys(parents_id: String?) {
        compositeDisposable.add(myAPI.getbabysList(parents_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Kiz ->
                mRecyclerView.adapter = main_drawerAdapter(baseContext, Kiz)


            }
                , { thr ->
                    Toast.makeText(this, "Error babys_drawer load", Toast.LENGTH_SHORT).show()
                    Log.d("babys_drawer", thr.message.toString())
                }

            ))
    }


    // 네비게이션 드로어 메뉴 클릭 리스너
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {  // 네비게이션 메뉴가 클릭되면 스낵바가 나타난다.

            R.id.account -> Snackbar.make(
                toolbar,
                "Navigation Account pressed",
                Snackbar.LENGTH_SHORT
            ).show()
            R.id.setting -> Snackbar.make(
                toolbar,
                "Navigation Setting pressed",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }

    private fun UserInfo(id: String?) {
        compositeDisposable.add(myAPI.userInfo(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                Log.d("userinfo", message.toString())

                // 로그인한 아이디를 네비게이션 드로어 아이디에 입력한다.
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id", 0.toString())
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                val ed = pref1.edit()
                ed.putString(userID, message.toString())
                ed.apply()

            })
    }

    /*
     * 뒤로가기 버튼으로 네비게이션 닫기
     *
     * 네비게이션 드로어가 열려 있을 때 뒤로가기 버튼을 누르면 네비게이션을 닫고,
     * 닫혀 있다면 기존 뒤로가기 버튼으로 작동한다.
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

}

