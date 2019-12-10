package com.example.babynote.투약의뢰서

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.example.babynote.User.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_request_write.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class request_write : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var week = ""
    var week1 = ""
    var userID : String? = null
    var kindergarten: String? = null
    var classname: String? = null
    var writer_nickname: String? = null
    var medicine: String? = null

    var choice_do = "1회"
    var choice_se = "실온"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_write)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        kindergarten = intent.getStringExtra("유치원이름")
        classname = intent.getStringExtra("유치원반이름")
        writer_nickname = intent.getStringExtra("작성자")
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)
        babyname.text =
            Common.selected_baby?.baby_name + " ( " + Common.selected_baby?.baby_class + " )"
        requset_day.setOnClickListener { showDate() }
        button5.setOnClickListener {
            // 다이얼로그에 표시할 목록을 생성합니다.
            val people = listOf("물약과 가루약", "물약", "가루약","연고")

            // 리스트 다이얼로그를 생성하고 표시합니다.
            selector(title = "누구신가요?", items = people) { _, selection ->

                // 항목을 선택했을 때 수행할 동작을 구현합니다.
                toast("You selected ${people[selection]}")
                if (people[selection] == "물약과 가루약") {
                    button5.text = "물약과 가루약"
                    medicine = "물약과 가루약"
                } else if (people[selection] == "물약") {
                    button5.text = "물약"
                    medicine = "물약"
                } else if (people[selection] == "가루약") {
                    button5.text = "가루약"
                    medicine = "가루약"
                } else if (people[selection] == "연고") {
                    button5.text = "연고"
                    medicine = "연고"
                }else {
                    return@selector
                }
            }
        }

        var spn = findViewById<Spinner>(R.id.spinner3)
        var spn2 = findViewById<Spinner>(R.id.spinner4)
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.투약횟수,
            android.R.layout.simple_spinner_item
        )
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.보관방법,
            android.R.layout.simple_spinner_item
        )
        // 첫번째 스피너
        spn.adapter = adapter1
        spn.prompt = "투약횟수 선택"

        spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                choice_do = spn.getItemAtPosition(position).toString()
            }
        }
        // 두번째 스피너
        spn2.adapter = adapter2
        spn2.prompt = "보관방법 선택"

        spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                choice_se = spn2.getItemAtPosition(position).toString()
            }
        }
        time()
        textView47.text = UserDTO.name
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(request_write_toolbar)

        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.title = "투약의뢰서 작성하기"
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.write_ok, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            R.id.write_ok -> {
                if( week == null){
                    Toast.makeText(this,"날짜를 지정해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if(editText.text.isEmpty()){
                    Toast.makeText(this,"증상을 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if(medicine == null){
                    Toast.makeText(this,"약의 종류를 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if(editText3.text.isEmpty()){
                    Toast.makeText(this,"투약용량을 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if(editText4.text.isEmpty()){
                    Toast.makeText(this,"투약시간을 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if(editText5.text.isEmpty()){
                    Toast.makeText(this,"특이사항을 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else{
                    request_medicine(
                        Common.selected_baby?.baby_name,
                        week,
                        editText.text.toString(),
                        medicine,
                        editText3.text.toString() + " cc/ml",
                        choice_do,
                        editText4.text.toString(),
                        choice_se,
                        editText5.text.toString(),
                        Common.selected_baby?.baby_kindergarten,
                        Common.selected_baby?.baby_class,
                        Common.selected_baby?.parents_id,
                        Common.selected_baby?.baby_imagepath
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

                requset_day.text = "${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}"
                textView46.text =
                    "${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}"
                week =
                    "${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}"
                Toast.makeText(this@request_write, week.toString(), Toast.LENGTH_SHORT).show()

            }
        }

        var builder = DatePickerDialog(this, date_listener, year, month, day)
        builder.show()
    }

    //----------------------------------------------------------------------------------------------
    // 현재 날짜.
    private fun time() {
        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var what = calendar.get(Calendar.DAY_OF_WEEK)

        when (what) {
            1 -> week1 = "일"
            2 -> week1 = "월"
            3 -> week1 = "화"
            4 -> week1 = "수"
            5 -> week1 = "목"
            6 -> week1 = "금"
            7 -> week1 = "토"
        }
        textView46.text =
            "${year}" + "년 " + "${month + 1}" + "월 " + "${day}" + "일 " + "${week1}" + "요일"
    }

    // 레트로핏을 통해 mysql 저장하기
    private fun request_medicine(
        babyname: String?,
        request_day: String?,
        symptom: String?,
        medicine: String?,
        cc: String?,
        numberoftimes: String?,
        medicine_time: String?,
        storage: String?,
        baby_comment: String?,
        kindergarten: String?,
        classname: String?,
        parents_id: String?,
        baby_image: String?
    ) {
        compositeDisposable.add(myAPI.request_medicine(
            babyname,
            request_day,
            symptom,
            medicine,
            cc,
            numberoftimes,
            medicine_time,
            storage,
            baby_comment,
            kindergarten,
            classname,
            parents_id,
            baby_image
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("success")) {
                    Toast.makeText(this, "투약의뢰서 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Administration_request_form::class.java)
                    intent.putExtra("babyname",Common.selected_baby?.baby_name )
                    intent.putExtra("kindergarten",Common.selected_baby?.baby_kindergarten)
                    intent.putExtra("classname", Common.selected_baby?.baby_class)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
