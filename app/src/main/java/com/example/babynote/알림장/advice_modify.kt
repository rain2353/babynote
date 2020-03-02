package com.example.babynote.알림장

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.example.babynote.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_advice_modify.*
import okhttp3.MultipartBody
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Response

class advice_modify : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    lateinit var myAPI: INodeJS
    lateinit var dialog: ProgressDialog
    var compositeDisposable = CompositeDisposable()
    private val PICK_IMAGE_REQUEST: Int = 1001
    private var selectedFileUri: Uri? = null

    var feel: String? = null //기분
    var health: String? = null //건강
    var temperature: String? = null //온도
    var MealorNot: String? = null //식사
    var sleep: String? = null // 수면
    var poop: String? = null // 배변


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advice_modify)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        Picasso.get().load(Common.selected_advice?.baby_image).into(imageView14)
        textView92.text = Common.selected_advice?.advice_baby
        editText12.setText(Common.selected_advice?.advice_content)


        // --------------------------- 갤러리에서 이미지 가져오기 -----------------------------------
        button32.setOnClickListener { openGallery() }
        //------------------------------------------------------------------------------------------

        // 알림장 작성되있는걸로 변수를 넣기.
        feel = Common.selected_advice?.feel
        health = Common.selected_advice?.health
        temperature = Common.selected_advice?.temperature
        MealorNot = Common.selected_advice?.MealorNot
        sleep = Common.selected_advice?.sleep
        poop = Common.selected_advice?.poop

        // 기분
        if (feel == "좋음") {
            button9.setBackgroundResource(R.drawable.trumpet_click)
            button9.textColor = Color.WHITE
            button10.backgroundColor = Color.WHITE
            button10.textColor = Color.BLACK
            button11.backgroundColor = Color.WHITE
            button11.textColor = Color.BLACK
        } else if (feel == "보통") {
            button10.setBackgroundResource(R.drawable.trumpet_click)
            button10.textColor = Color.WHITE
            button9.backgroundColor = Color.WHITE
            button9.textColor = Color.BLACK
            button11.backgroundColor = Color.WHITE
            button11.textColor = Color.BLACK
        } else {
            button11.setBackgroundResource(R.drawable.trumpet_click)
            button11.textColor = Color.WHITE
            button9.backgroundColor = Color.WHITE
            button9.textColor = Color.BLACK
            button10.backgroundColor = Color.WHITE
            button10.textColor = Color.BLACK
        }
        //--------------------------- 기분 선택 -------------------------
        button9.setOnClickListener {
            button9.setBackgroundResource(R.drawable.trumpet_click)
            button9.textColor = Color.WHITE
            feel = "좋음"
            button10.backgroundColor = Color.WHITE
            button10.textColor = Color.BLACK
            button11.backgroundColor = Color.WHITE
            button11.textColor = Color.BLACK
        }
        button10.setOnClickListener {
            button10.setBackgroundResource(R.drawable.trumpet_click)
            button10.textColor = Color.WHITE
            feel = "보통"
            button9.backgroundColor = Color.WHITE
            button9.textColor = Color.BLACK
            button11.backgroundColor = Color.WHITE
            button11.textColor = Color.BLACK
        }
        button11.setOnClickListener {
            button11.setBackgroundResource(R.drawable.trumpet_click)
            button11.textColor = Color.WHITE
            feel = "나쁨"
            button9.backgroundColor = Color.WHITE
            button9.textColor = Color.BLACK
            button10.backgroundColor = Color.WHITE
            button10.textColor = Color.BLACK
        }
        //---------------------------------------------------------------
        // 건강
        if (health == "좋음") {
            button12.setBackgroundResource(R.drawable.trumpet_click)
            button12.textColor = Color.WHITE
            button13.backgroundColor = Color.WHITE
            button13.textColor = Color.BLACK
            button14.backgroundColor = Color.WHITE
            button14.textColor = Color.BLACK
        } else if (health == "보통") {
            button13.setBackgroundResource(R.drawable.trumpet_click)
            button13.textColor = Color.WHITE
            button12.backgroundColor = Color.WHITE
            button12.textColor = Color.BLACK
            button14.backgroundColor = Color.WHITE
            button14.textColor = Color.BLACK
        } else {
            button14.setBackgroundResource(R.drawable.trumpet_click)
            button14.textColor = Color.WHITE
            button12.backgroundColor = Color.WHITE
            button12.textColor = Color.BLACK
            button13.backgroundColor = Color.WHITE
            button13.textColor = Color.BLACK
        }
        //------------------------건강 선택 ------------------------------
        button12.setOnClickListener {
            button12.setBackgroundResource(R.drawable.trumpet_click)
            button12.textColor = Color.WHITE
            health = "좋음"
            button13.backgroundColor = Color.WHITE
            button13.textColor = Color.BLACK
            button14.backgroundColor = Color.WHITE
            button14.textColor = Color.BLACK
        }
        button13.setOnClickListener {
            button13.setBackgroundResource(R.drawable.trumpet_click)
            button13.textColor = Color.WHITE
            health = "보통"
            button12.backgroundColor = Color.WHITE
            button12.textColor = Color.BLACK
            button14.backgroundColor = Color.WHITE
            button14.textColor = Color.BLACK
        }
        button14.setOnClickListener {
            button14.setBackgroundResource(R.drawable.trumpet_click)
            button14.textColor = Color.WHITE
            health = "나쁨"
            button12.backgroundColor = Color.WHITE
            button12.textColor = Color.BLACK
            button13.backgroundColor = Color.WHITE
            button13.textColor = Color.BLACK
        }
        //---------------------------------------------------------------
        // 체온
        if (temperature == "정상") {
            button15.setBackgroundResource(R.drawable.trumpet_click)
            button15.textColor = Color.WHITE
            button16.backgroundColor = Color.WHITE
            button16.textColor = Color.BLACK
            button17.backgroundColor = Color.WHITE
            button17.textColor = Color.BLACK
        } else if (temperature == "미열") {
            button16.setBackgroundResource(R.drawable.trumpet_click)
            button16.textColor = Color.WHITE
            button15.backgroundColor = Color.WHITE
            button15.textColor = Color.BLACK
            button17.backgroundColor = Color.WHITE
            button17.textColor = Color.BLACK
        } else {
            button17.setBackgroundResource(R.drawable.trumpet_click)
            button17.textColor = Color.WHITE
            button15.backgroundColor = Color.WHITE
            button15.textColor = Color.BLACK
            button16.backgroundColor = Color.WHITE
            button16.textColor = Color.BLACK
        }
        //------------------------체온체크 선택 ------------------------------
        button15.setOnClickListener {
            button15.setBackgroundResource(R.drawable.trumpet_click)
            button15.textColor = Color.WHITE
            temperature = "정상"
            button16.backgroundColor = Color.WHITE
            button16.textColor = Color.BLACK
            button17.backgroundColor = Color.WHITE
            button17.textColor = Color.BLACK
        }
        button16.setOnClickListener {
            button16.setBackgroundResource(R.drawable.trumpet_click)
            button16.textColor = Color.WHITE
            temperature = "미열"
            button15.backgroundColor = Color.WHITE
            button15.textColor = Color.BLACK
            button17.backgroundColor = Color.WHITE
            button17.textColor = Color.BLACK
        }
        button17.setOnClickListener {
            button17.setBackgroundResource(R.drawable.trumpet_click)
            button17.textColor = Color.WHITE
            temperature = "고열"
            button15.backgroundColor = Color.WHITE
            button15.textColor = Color.BLACK
            button16.backgroundColor = Color.WHITE
            button16.textColor = Color.BLACK
        }
        //---------------------------------------------------------------
        // 식사여부
        if (MealorNot == "정량") {
            button18.setBackgroundResource(R.drawable.trumpet_click)
            button18.textColor = Color.WHITE
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.BLACK
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.BLACK
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.BLACK
        } else if (MealorNot == "많이") {
            button19.setBackgroundResource(R.drawable.trumpet_click)
            button19.textColor = Color.WHITE
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.BLACK
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.BLACK
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.BLACK
        } else if (MealorNot == "적게") {
            button20.setBackgroundResource(R.drawable.trumpet_click)
            button20.textColor = Color.WHITE
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.BLACK
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.BLACK
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.BLACK
        } else {
            button21.setBackgroundResource(R.drawable.trumpet_click)
            button21.textColor = Color.WHITE
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.BLACK
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.BLACK
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.BLACK
        }
        //------------------------식사여부 선택 ------------------------------
        button18.setOnClickListener {
            button18.setBackgroundResource(R.drawable.trumpet_click)
            button18.textColor = Color.WHITE
            MealorNot = "정량"
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.BLACK
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.BLACK
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.BLACK
        }
        button19.setOnClickListener {
            button19.setBackgroundResource(R.drawable.trumpet_click)
            button19.textColor = Color.WHITE
            MealorNot = "많이"
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.BLACK
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.BLACK
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.BLACK
        }
        button20.setOnClickListener {
            button20.setBackgroundResource(R.drawable.trumpet_click)
            button20.textColor = Color.WHITE
            MealorNot = "적게"
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.BLACK
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.BLACK
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.BLACK
        }
        button21.setOnClickListener {
            button21.setBackgroundResource(R.drawable.trumpet_click)
            button21.textColor = Color.WHITE
            MealorNot = "안했음"
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.BLACK
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.BLACK
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.BLACK
        }
        //---------------------------------------------------------------
        // 수면 시간
        if (sleep == "잠을 안잤어요") {
            button22.setBackgroundResource(R.drawable.trumpet_click)
            button22.textColor = Color.WHITE
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        } else if (sleep == "1시간 미만") {
            button23.setBackgroundResource(R.drawable.trumpet_click)
            button23.textColor = Color.WHITE
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        } else if (sleep == "1시간~1시간30분") {
            button24.setBackgroundResource(R.drawable.trumpet_click)
            button24.textColor = Color.WHITE
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        } else if (sleep == "1시간30분~2시간") {
            button25.setBackgroundResource(R.drawable.trumpet_click)
            button25.textColor = Color.WHITE
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        } else {
            button26.setBackgroundResource(R.drawable.trumpet_click)
            button26.textColor = Color.WHITE
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
        }
        //------------------------수면시간 선택 ------------------------------
        button22.setOnClickListener {
            button22.setBackgroundResource(R.drawable.trumpet_click)
            button22.textColor = Color.WHITE
            sleep = "잠을 안잤어요"
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        }
        button23.setOnClickListener {
            button23.setBackgroundResource(R.drawable.trumpet_click)
            button23.textColor = Color.WHITE
            sleep = "1시간 미만"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        }
        button24.setOnClickListener {
            button24.setBackgroundResource(R.drawable.trumpet_click)
            button24.textColor = Color.WHITE
            sleep = "1시간~1시간30분"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        }
        button25.setOnClickListener {
            button25.setBackgroundResource(R.drawable.trumpet_click)
            button25.textColor = Color.WHITE
            sleep = "1시간30분~2시간"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.BLACK
        }
        button26.setOnClickListener {
            button26.setBackgroundResource(R.drawable.trumpet_click)
            button26.textColor = Color.WHITE
            sleep = "2시간 이상"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.BLACK
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.BLACK
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.BLACK
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.BLACK
        }
        //---------------------------------------------------------------
        // 배변 상태
        if (poop == "보통") {
            button27.setBackgroundResource(R.drawable.trumpet_click)
            button27.textColor = Color.WHITE
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        } else if (poop == "딱딱") {
            button28.setBackgroundResource(R.drawable.trumpet_click)
            button28.textColor = Color.WHITE
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        } else if (poop == "묽음") {
            button29.setBackgroundResource(R.drawable.trumpet_click)
            button29.textColor = Color.WHITE
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        } else if (poop == "설사") {
            button30.setBackgroundResource(R.drawable.trumpet_click)
            button30.textColor = Color.WHITE
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        } else {
            button31.setBackgroundResource(R.drawable.trumpet_click)
            button31.textColor = Color.WHITE
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
        }
        //------------------------배변상태 선택 ------------------------------
        button27.setOnClickListener {
            button27.setBackgroundResource(R.drawable.trumpet_click)
            button27.textColor = Color.WHITE
            poop = "보통"
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        }
        button28.setOnClickListener {
            button28.setBackgroundResource(R.drawable.trumpet_click)
            button28.textColor = Color.WHITE
            poop = "딱딱"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        }
        button29.setOnClickListener {
            button29.setBackgroundResource(R.drawable.trumpet_click)
            button29.textColor = Color.WHITE
            poop = "묽음"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        }
        button30.setOnClickListener {
            button30.setBackgroundResource(R.drawable.trumpet_click)
            button30.textColor = Color.WHITE
            poop = "설사"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.BLACK
        }
        button31.setOnClickListener {
            button31.setBackgroundResource(R.drawable.trumpet_click)
            button31.textColor = Color.WHITE
            poop = "안했음"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.BLACK
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.BLACK
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.BLACK
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.BLACK
        }
        //---------------------------------------------------------------
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(advice_modify_toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.title = "알림장 글 수정하기"
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

                if (editText12.text.isEmpty()){
                    Toast.makeText(this,"내용을 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (selectedFileUri == null ){
                    Toast.makeText(this,"사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (feel == null ){
                    Toast.makeText(this,"아이의 기분을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (health == null ){
                    Toast.makeText(this,"아이의 건강을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (temperature == null ){
                    Toast.makeText(this,"아이의 체온을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (MealorNot == null ){
                    Toast.makeText(this,"아이의 식사여부를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (sleep == null ){
                    Toast.makeText(this,"아이의 수면시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if (poop == null ){
                    Toast.makeText(this,"아이의 배변상태를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else{
                    advice_text_modify()
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }
    // ------------------------------------갤러리에서 이미지 가져오기 -----------------------------
    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null) {
                    selectedFileUri = data.data
                    if (selectedFileUri != null && !selectedFileUri!!.path!!.isEmpty())
                        imageView16.visibility = View.VISIBLE
                        imageView16.setImageURI(selectedFileUri)
                }
            }

        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }
    private fun advice_text_modify() {
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setMessage("알림장을 수정중입니다...")
        dialog.isIndeterminate = false
        dialog.setCancelable(false)
        dialog.max = 100
        dialog.show()

        val file = FileUtils.getFile(this, selectedFileUri)
        val requestFile = ProgressRequestBody(file, this)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        Thread(Runnable {
            myAPI.advice_modify(
                Common.selected_advice?.num,
                Common.selected_advice?.advice_baby,
                editText12.text.toString(),
                body,
                feel,
                health,
                temperature,
                MealorNot,
                sleep,
                poop,
                Common.selected_advice?.advice_writer,
                Common.selected_advice?.advice_nickname,
                Common.selected_advice?.kindergarten,
                Common.selected_advice?.classname,
                Common.selected_advice?.advice_time,
                Common.selected_advice?.advice_write_time,
                Common.selected_advice?.baby_image
            )
                .enqueue(object : retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@advice_modify, t.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        dialog.dismiss()
                        Toast.makeText(this@advice_modify, "알림장을 수정하였습니다!", Toast.LENGTH_SHORT)
                            .show()
                        var intent = Intent(this@advice_modify, Advice_note::class.java)
                        intent.putExtra("kindergarten",Common.selected_advice?.kindergarten)
                        intent.putExtra("classname",Common.selected_advice?.classname)
                        intent.putExtra("babyname",Common.selected_advice?.advice_nickname)
                        startActivity(intent)
                        finish()
                    }

                })

        }).start()

    }

}
