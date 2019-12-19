package com.example.babynote.Add_baby

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_babybirth.*

class modify_babybirth : Activity() {

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var year = ""
    var month = ""
    var day = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_modify_babybirth)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 수정할 아이 정보
        var gson = Gson()
        val pref1 = getSharedPreferences("babyinfo", Context.MODE_PRIVATE)
        var babyinfo = pref1.getString("babyinfo", null)
        var babyDTO = gson.fromJson(babyinfo, Kiz::class.java)

        // ----------------------------- 3중 스피너 ------------------------------------------------

        var modify_baby_year = findViewById(R.id.spinner5) as Spinner
        var modify_baby_month = findViewById(R.id.spinner6) as Spinner
        var modify_baby_day = findViewById(R.id.spinner7) as Spinner
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.생년,
            android.R.layout.simple_spinner_item
        )
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.월,
            android.R.layout.simple_spinner_item
        )
        val adapter3 = ArrayAdapter.createFromResource(
            this,
            R.array.일,
            android.R.layout.simple_spinner_item
        )
        modify_baby_year.adapter = adapter1
        modify_baby_year.prompt = "태어난 해 선택"

        modify_baby_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                year = modify_baby_year.getItemAtPosition(position).toString()
            }
        }
        modify_baby_month.adapter = adapter2
        modify_baby_month.prompt = "태어난 월 선택"

        modify_baby_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                month = modify_baby_month.getItemAtPosition(position).toString()
            }
        }
        modify_baby_day.adapter = adapter3
        modify_baby_day.prompt = "태어난 일 선택"

        modify_baby_day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                day = modify_baby_day.getItemAtPosition(position).toString()
            }
        }
        // ----------------------------------------------------------------------------------------

        modify_babybirth_button.setOnClickListener {
            if (year == "태어난 해") {
                Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (month == "태어난 월") {
                Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (day == "태어난 일") {
                Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else{
                compositeDisposable.add(myAPI.modify_baby(
                    babyDTO.num,
                    babyDTO.baby_name,
                    year.toString() + " . " + month.toString() + " . " + day.toString(),
                    babyDTO.baby_gender,
                    babyDTO.baby_kindergarten,
                    babyDTO.baby_class,
                    babyDTO.baby_imagepath,
                    babyDTO.parents_id,
                    babyDTO.state
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { message ->
                        Toast.makeText(
                            this,
                            "생년월일을 수정하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()

                    })
            }
        }
    }
}
