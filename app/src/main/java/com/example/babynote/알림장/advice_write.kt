package com.example.babynote.알림장

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
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
import com.example.babynote.User.User
import com.example.babynote.Utils.ProgressRequestBody
import com.google.gson.Gson
import com.ipaulpro.afilechooser.utils.FileUtils
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_advice_write.*
import okhttp3.MultipartBody
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Response

class advice_write : AppCompatActivity(), ProgressRequestBody.UploadCallbacks  {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }


    lateinit var myAPI: INodeJS
    //    lateinit var mService: INodeJS
    lateinit var dialog: ProgressDialog
    var compositeDisposable = CompositeDisposable()
    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    private var selectedFileUri: Uri? = null

    var feel: String? = null //기분
    var health: String? = null //건강
    var temperature: String? = null //온도
    var MealorNot: String? = null //식사
    var sleep: String? = null // 수면
    var poop: String? = null // 배변

    var nickname: String? = null // 작성자 호칭

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advice_write)
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
        nickname = UserDTO.nickname
        // 아이 선택.
        if (Common.selected_baby_list?.baby_name == null){
            textView92.text = "원아를 선택해주세요."
            imageView14.visibility = View.INVISIBLE
        } else {
            imageView14.visibility = View.VISIBLE
            Picasso.get().load(Common.selected_baby_list?.baby_imagepath).resize(200,200).into(imageView14)
            textView92.text =
                Common.selected_baby_list?.baby_name + " ( " + Common.selected_baby_list?.baby_class + " )"
        }
        textView92.setOnClickListener {
            var intent = Intent(this,select_baby::class.java)
            startActivity(intent)
            finish()
        }
        // --------------------------- Request runtime permission ---------------------------------
        if (ActivityCompat.checkSelfPermission(
                this@advice_write,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this@advice_write,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )
        //------------------------------------------------------------------------------------------

        // --------------------------- 갤러리에서 이미지 가져오기 -----------------------------------
        button8.setOnClickListener { openGallery() }
        //------------------------------------------------------------------------------------------

        //--------------------------- 기분 선택 -------------------------
        button9.setOnClickListener {
            button9.backgroundColor = Color.rgb(0, 153, 204)
            button9.textColor = Color.WHITE
            feel = "좋음"
            button10.backgroundColor = Color.WHITE
            button10.textColor = Color.rgb(50, 50, 50)
            button11.backgroundColor = Color.WHITE
            button11.textColor = Color.rgb(50, 50, 50)
        }
        button10.setOnClickListener {
            button10.backgroundColor = Color.rgb(0, 153, 204)
            button10.textColor = Color.WHITE
            feel = "보통"
            button9.backgroundColor = Color.WHITE
            button9.textColor = Color.rgb(50, 50, 50)
            button11.backgroundColor = Color.WHITE
            button11.textColor = Color.rgb(50, 50, 50)
        }
        button11.setOnClickListener {
            button11.backgroundColor = Color.rgb(0, 153, 204)
            button11.textColor = Color.WHITE
            feel = "나쁨"
            button9.backgroundColor = Color.WHITE
            button9.textColor = Color.rgb(50, 50, 50)
            button10.backgroundColor = Color.WHITE
            button10.textColor = Color.rgb(50, 50, 50)
        }
        //---------------------------------------------------------------
        //------------------------건강 선택 ------------------------------
        button12.setOnClickListener {
            button12.backgroundColor = Color.rgb(0, 153, 204)
            button12.textColor = Color.WHITE
            health = "좋음"
            button13.backgroundColor = Color.WHITE
            button13.textColor = Color.rgb(50, 50, 50)
            button14.backgroundColor = Color.WHITE
            button14.textColor = Color.rgb(50, 50, 50)
        }
        button13.setOnClickListener {
            button13.backgroundColor = Color.rgb(0, 153, 204)
            button13.textColor = Color.WHITE
            health = "보통"
            button12.backgroundColor = Color.WHITE
            button12.textColor = Color.rgb(50, 50, 50)
            button14.backgroundColor = Color.WHITE
            button14.textColor = Color.rgb(50, 50, 50)
        }
        button14.setOnClickListener {
            button14.backgroundColor = Color.rgb(0, 153, 204)
            button14.textColor = Color.WHITE
            health = "나쁨"
            button12.backgroundColor = Color.WHITE
            button12.textColor = Color.rgb(50, 50, 50)
            button13.backgroundColor = Color.WHITE
            button13.textColor = Color.rgb(50, 50, 50)
        }
        //---------------------------------------------------------------
        //------------------------체온체크 선택 ------------------------------
        button15.setOnClickListener {
            button15.backgroundColor = Color.rgb(0, 153, 204)
            button15.textColor = Color.WHITE
            temperature = "정상"
            button16.backgroundColor = Color.WHITE
            button16.textColor = Color.rgb(50, 50, 50)
            button17.backgroundColor = Color.WHITE
            button17.textColor = Color.rgb(50, 50, 50)
        }
        button16.setOnClickListener {
            button16.backgroundColor = Color.rgb(0, 153, 204)
            button16.textColor = Color.WHITE
            temperature = "미열"
            button15.backgroundColor = Color.WHITE
            button15.textColor = Color.rgb(50, 50, 50)
            button17.backgroundColor = Color.WHITE
            button17.textColor = Color.rgb(50, 50, 50)
        }
        button17.setOnClickListener {
            button17.backgroundColor = Color.rgb(0, 153, 204)
            button17.textColor = Color.WHITE
            temperature = "고열"
            button15.backgroundColor = Color.WHITE
            button15.textColor = Color.rgb(50, 50, 50)
            button16.backgroundColor = Color.WHITE
            button16.textColor = Color.rgb(50, 50, 50)
        }
        //---------------------------------------------------------------
        //------------------------식사여부 선택 ------------------------------
        button18.setOnClickListener {
            button18.backgroundColor = Color.rgb(0, 153, 204)
            button18.textColor = Color.WHITE
            MealorNot = "정량"
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.rgb(50, 50, 50)
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.rgb(50, 50, 50)
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.rgb(50, 50, 50)
        }
        button19.setOnClickListener {
            button19.backgroundColor = Color.rgb(0, 153, 204)
            button19.textColor = Color.WHITE
            MealorNot = "많이"
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.rgb(50, 50, 50)
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.rgb(50, 50, 50)
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.rgb(50, 50, 50)
        }
        button20.setOnClickListener {
            button20.backgroundColor = Color.rgb(0, 153, 204)
            button20.textColor = Color.WHITE
            MealorNot = "적게"
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.rgb(50, 50, 50)
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.rgb(50, 50, 50)
            button21.backgroundColor = Color.WHITE
            button21.textColor = Color.rgb(50, 50, 50)
        }
        button21.setOnClickListener {
            button21.backgroundColor = Color.rgb(0, 153, 204)
            button21.textColor = Color.WHITE
            MealorNot = "안했음"
            button18.backgroundColor = Color.WHITE
            button18.textColor = Color.rgb(50, 50, 50)
            button19.backgroundColor = Color.WHITE
            button19.textColor = Color.rgb(50, 50, 50)
            button20.backgroundColor = Color.WHITE
            button20.textColor = Color.rgb(50, 50, 50)
        }
        //---------------------------------------------------------------

        //------------------------수면시간 선택 ------------------------------
        button22.setOnClickListener {
            button22.backgroundColor = Color.rgb(0, 153, 204)
            button22.textColor = Color.WHITE
            sleep = "잠을 안잤어요"
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.rgb(50, 50, 50)
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.rgb(50, 50, 50)
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.rgb(50, 50, 50)
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.rgb(50, 50, 50)
        }
        button23.setOnClickListener {
            button23.backgroundColor = Color.rgb(0, 153, 204)
            button23.textColor = Color.WHITE
            sleep = "1시간 미만"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.rgb(50, 50, 50)
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.rgb(50, 50, 50)
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.rgb(50, 50, 50)
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.rgb(50, 50, 50)
        }
        button24.setOnClickListener {
            button24.backgroundColor = Color.rgb(0, 153, 204)
            button24.textColor = Color.WHITE
            sleep = "1시간~1시간30분"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.rgb(50, 50, 50)
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.rgb(50, 50, 50)
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.rgb(50, 50, 50)
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.rgb(50, 50, 50)
        }
        button25.setOnClickListener {
            button25.backgroundColor = Color.rgb(0, 153, 204)
            button25.textColor = Color.WHITE
            sleep = "1시간30분~2시간"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.rgb(50, 50, 50)
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.rgb(50, 50, 50)
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.rgb(50, 50, 50)
            button26.backgroundColor = Color.WHITE
            button26.textColor = Color.rgb(50, 50, 50)
        }
        button26.setOnClickListener {
            button26.backgroundColor = Color.rgb(0, 153, 204)
            button26.textColor = Color.WHITE
            sleep = "2시간 이상"
            button22.backgroundColor = Color.WHITE
            button22.textColor = Color.rgb(50, 50, 50)
            button23.backgroundColor = Color.WHITE
            button23.textColor = Color.rgb(50, 50, 50)
            button24.backgroundColor = Color.WHITE
            button24.textColor = Color.rgb(50, 50, 50)
            button25.backgroundColor = Color.WHITE
            button25.textColor = Color.rgb(50, 50, 50)
        }
        //---------------------------------------------------------------

        //------------------------배변상태 선택 ------------------------------
        button27.setOnClickListener {
            button27.backgroundColor = Color.rgb(0, 153, 204)
            button27.textColor = Color.WHITE
            poop = "보통"
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.rgb(50, 50, 50)
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.rgb(50, 50, 50)
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.rgb(50, 50, 50)
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.rgb(50, 50, 50)
        }
        button28.setOnClickListener {
            button28.backgroundColor = Color.rgb(0, 153, 204)
            button28.textColor = Color.WHITE
            poop = "딱딱"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.rgb(50, 50, 50)
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.rgb(50, 50, 50)
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.rgb(50, 50, 50)
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.rgb(50, 50, 50)
        }
        button29.setOnClickListener {
            button29.backgroundColor = Color.rgb(0, 153, 204)
            button29.textColor = Color.WHITE
            poop = "묽음"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.rgb(50, 50, 50)
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.rgb(50, 50, 50)
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.rgb(50, 50, 50)
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.rgb(50, 50, 50)
        }
        button30.setOnClickListener {
            button30.backgroundColor = Color.rgb(0, 153, 204)
            button30.textColor = Color.WHITE
            poop = "설사"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.rgb(50, 50, 50)
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.rgb(50, 50, 50)
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.rgb(50, 50, 50)
            button31.backgroundColor = Color.WHITE
            button31.textColor = Color.rgb(50, 50, 50)
        }
        button31.setOnClickListener {
            button31.backgroundColor = Color.rgb(0, 153, 204)
            button31.textColor = Color.WHITE
            poop = "안했음"
            button27.backgroundColor = Color.WHITE
            button27.textColor = Color.rgb(50, 50, 50)
            button28.backgroundColor = Color.WHITE
            button28.textColor = Color.rgb(50, 50, 50)
            button29.backgroundColor = Color.WHITE
            button29.textColor = Color.rgb(50, 50, 50)
            button30.backgroundColor = Color.WHITE
            button30.textColor = Color.rgb(50, 50, 50)
        }
        //---------------------------------------------------------------

    }
    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(Advice_note_write_toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setTitle("알림장 글 작성하기")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }
    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.write_ok, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.write_ok -> {
                if (Common.selected_baby_list?.baby_name == null ){
                    Toast.makeText(this,"원아를 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (editText11.text.isEmpty()){
                    Toast.makeText(this,"내용을 작성해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (selectedFileUri == null ){
                    Toast.makeText(this,"사진을 등록해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (feel == null ){
                    Toast.makeText(this,"아이의 기분을 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (health == null ){
                    Toast.makeText(this,"아이의 건강을 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (temperature == null ){
                    Toast.makeText(this,"아이의 체온을 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (MealorNot == null ){
                    Toast.makeText(this,"아이의 식사여부를 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (sleep == null ){
                    Toast.makeText(this,"아이의 수면시간을 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else if (poop == null ){
                    Toast.makeText(this,"아이의 배변상태를 선택해주세요.",Toast.LENGTH_SHORT).show()
                    return true
                }else{
                    upload_advice()
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }

    //-----------------------------------Permission -----------------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this@advice_write, "Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@advice_write, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------갤러리에서 이미지 가져오기 -----------------------------
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null) {
                    selectedFileUri = data.data
                    if (selectedFileUri != null && !selectedFileUri!!.path.isEmpty())
                        imageView12.setImageURI(selectedFileUri)
                }
            }

        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }

//    override fun onPause() {
//        Common.selected_baby_list = null
//        super.onPause()
//    }

    override fun onDestroy() {
        Common.selected_baby_list = null
        super.onDestroy()
    }

    private fun upload_advice() {
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setMessage("알림장을 등록중입니다...")
        dialog.isIndeterminate = false
        dialog.setCancelable(false)
        dialog.max = 100
        dialog.show()

        val file = FileUtils.getFile(this@advice_write, selectedFileUri)
        val requestFile = ProgressRequestBody(file, this@advice_write)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        Thread(Runnable {
            myAPI.upload_advice(
                Common.selected_baby_list?.baby_name,
                editText11.text.toString(),
                body,
                feel,
                health,
                temperature,
                MealorNot,
                sleep,
                poop,
                Common.selected_baby?.parents_id,
                Common.selected_baby?.baby_name + " " + nickname,
                Common.selected_baby?.baby_kindergarten,
                Common.selected_baby?.baby_class
            )
                .enqueue(object : retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@advice_write, t!!.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        dialog.dismiss()
                        Toast.makeText(this@advice_write, "알림장을 작성하였습니다!", Toast.LENGTH_SHORT)
                            .show()
                        var intent = Intent(this@advice_write, Advice_note::class.java)
                        intent.putExtra("kindergarten",Common.selected_baby?.baby_kindergarten)
                        intent.putExtra("classname",Common.selected_baby?.baby_class)
                        intent.putExtra("babyname",Common.selected_baby?.baby_name + " " + nickname)
                        startActivity(intent)
                        finish()
                    }

                })

        }).start()

    }
}
