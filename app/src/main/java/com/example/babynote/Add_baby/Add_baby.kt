package com.example.babynote.Add_baby

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.babynote.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_add_baby.*
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.net.URLEncoder


class Add_baby : AppCompatActivity() {


    // 네이버 api 클라이언트 ID , 클라이언트 secret
    val clientID = "QOxjMFTWZUPQbXiHFMFU"
    val clientSecret = "lo5yoS8YSy"

    var choice_do = ""
    var choice_se = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_baby)
        setToolbar()


        search_button.setOnClickListener {

            // 키워드가 없으면
            if (edit_kindergartenName.text.isEmpty()) {
                return@setOnClickListener
            }

            // 리사이클러뷰 레이아웃 매니저 설정
            search.layoutManager =
                LinearLayoutManager(this@Add_baby, LinearLayout.VERTICAL, false)
            search.setHasFixedSize(true)
            Log.d("검색", edit_kindergartenName.text.toString())
            // 네이버 API
            fetchSearch(edit_kindergartenName.text.toString())

            // 키보드를 내린다.
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edit_kindergartenName.windowToken, 0)

        }

        // --------------------------------------------- 이중 스피너 ------------------------------------------------
        var spn = findViewById(R.id.spinner) as Spinner
        var spn2 = findViewById(R.id.spinner2) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.city,
            android.R.layout.simple_spinner_item
        )

        edit_kindergartenName.visibility = View.INVISIBLE
        search_button.visibility = View.INVISIBLE
        // 첫번째 스피너
        spn.adapter = adapter
        spn.prompt = "시/도 선택"

        spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (spn.getItemAtPosition(position).equals("시/도 선택")) {
                    Toast.makeText(this@Add_baby, "시/도를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.시도선택,
                        android.R.layout.simple_spinner_item
                    )
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.INVISIBLE
                            search_button.visibility = View.INVISIBLE
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("서울특별시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.서울특별시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("부산광역시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.부산광역시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("대구광역시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.대구광역시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("인천광역시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.인천광역시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }

                    }
                } else if (spn.getItemAtPosition(position).equals("광주광역시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.광주광역시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("대전광역시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.대전광역시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("울산광역시")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.울산광역시,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("세종특별자치도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.세종특별자치도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("경기도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.경기도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("강원도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.강원도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("충청북도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.충청북도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("충청남도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.충청남도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("전라북도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.전라북도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("전라남도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.전라남도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("경상북도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.경상북도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("경상남도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.경상남도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                } else if (spn.getItemAtPosition(position).equals("제주특별자치도")) {
                    var adapter2 = ArrayAdapter.createFromResource(
                        this@Add_baby,
                        R.array.제주특별자치도,
                        android.R.layout.simple_spinner_item
                    )
                    choice_do = spn.getItemAtPosition(position).toString()
                    spn2.adapter = adapter2
                    spn2.prompt = "지역 선택"
                    spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            edit_kindergartenName.visibility = View.VISIBLE
                            search_button.visibility = View.VISIBLE
                            choice_se = spn2.getItemAtPosition(position).toString()
                        }
                    }
                }
            }

        }


    } // -------------------------------- 이중 스피너 끝 -------------------------------------------

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(toolbar10)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("원 찾기")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    fun fetchSearch(vararg p0: String) {
        // OkHttp로 요청하기
        val text = URLEncoder.encode("${p0[0]}", "UTF-8")
        println(text)
        Log.d("text", text)
        val url =
            URL("https://openapi.naver.com/v1/search/local.json?query=${choice_do}+${choice_se}+${text}&display=20&start=1&genre=")
        Log.d("url", url.toString())
        val formBody = FormBody.Builder()
            .add("query", "${choice_do}+${choice_se}+${text}")
            .add("display", "20")
            .add("start", "1")
            .add("genre", "1")
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientID)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .method("GET", null)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
                Log.d("검색결과", e.toString())
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println("Success to execute request : $body")

                // Gson을 Kotlin 에서 사용 가능한 object로 만든다.
                val gson = GsonBuilder().create()
                val homefeed = gson.fromJson(body, Homefeed::class.java)
                Log.d("검색결과", homefeed.toString())
                runOnUiThread {
                    search.adapter = search_RecyclerViewAdapter(baseContext, homefeed)
                    edit_kindergartenName.setText("")
                }


            }
        })
    }
}

data class Homefeed(val items: List<Item>)
data class Item(
    val title: String,
    val roadAddress: String,
    val telephone: String,
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val usrRating: String
)