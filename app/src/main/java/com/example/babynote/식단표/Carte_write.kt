package com.example.babynote.식단표

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.User.User
import com.example.babynote.Utils.ProgressRequestBody
import com.google.gson.Gson
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_carte_write.*
import okhttp3.MultipartBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class Carte_write : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    var week = ""
    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST1: Int = 1001
    private val PICK_IMAGE_REQUEST2: Int = 1002
    private val PICK_IMAGE_REQUEST3: Int = 1003
    private var selectedFileUri1: Uri? = null
    private var selectedFileUri2: Uri? = null
    private var selectedFileUri3: Uri? = null
    lateinit var myAPI: INodeJS
    //    lateinit var mService: INodeJS
    lateinit var dialog: ProgressDialog
    var compositeDisposable = CompositeDisposable()

    var state: String? = null
    var kindergarten: String? = null
    var classname: String? = null
    var babyname: String? = null
    var UserID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.babynote.R.layout.activity_carte_write)
        setToolbar()
        showDate()
        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        UserID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(UserID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)
        state = UserDTO.state.toString()
        kindergarten = intent.getStringExtra("유치원이름")
        classname = intent.getStringExtra("유치원반이름")
        babyname = intent.getStringExtra("작성자")

        button_day.onClick {
            showDate()
        }
        // --------------------------- Request runtime permission ---------------------------------
        if (ActivityCompat.checkSelfPermission(
                this@Carte_write,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this@Carte_write,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )


        // --------------------------- 갤러리에서 이미지 가져오기 -------------------------------------
        button_image_1.setOnClickListener { openGallery1() }
        button_image_2.setOnClickListener { openGallery2() }
        button_image_3.setOnClickListener { openGallery3() }
        //------------------------------------------------------------------------------------------
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(carte_write_toolbar)
        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("식단표 작성하기")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(
            com.example.babynote.R.menu.image_upload,
            menu
        )       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            com.example.babynote.R.id.write_ok -> {
                if (selectedFileUri1 == null && selectedFileUri2 == null && selectedFileUri3 == null) {
                    Toast.makeText(this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if(edit_menu1.text.isEmpty()){
                    Toast.makeText(this, "오전 간식 메뉴를 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if(edit_menu2.text.isEmpty()){
                    Toast.makeText(this, "점심 식사 메뉴를 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }else if(edit_menu3.text.isEmpty()){
                    Toast.makeText(this, "오후 간식 메뉴를 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                } else {
                    uploadCarte()
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
                    Toast.makeText(this@Carte_write, "Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@Carte_write, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // 날짜 지정하기.
    private fun showDate() {
        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
//        var what = calendar.get(Calendar.DAY_OF_WEEK)
//
//        when (what) {
//            1 -> week = "일"
//            2 -> week = "월"
//            3 -> week = "화"
//            4 -> week = "수"
//            5 -> week = "목"
//            6 -> week = "금"
//            7 -> week = "토"
//        }

        var date_listener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val simpledateformat = SimpleDateFormat("EEEE")
                val date = Date(year, month, dayOfMonth - 1)
                val dayOfWeek = simpledateformat.format(date)

                button_day.setText("${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}")
                week =
                    "${year}" + "년 " + "${month + 1}" + "월 " + "${dayOfMonth}" + "일 " + "${dayOfWeek}"
                Toast.makeText(this@Carte_write, week.toString(), Toast.LENGTH_SHORT).show()

            }
        }

        var builder = DatePickerDialog(this, date_listener, year, month, day)
        builder.show()
    }

    // ------------------------------------갤러리에서 이미지 가져오기 -----------------------------
    private fun openGallery1() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, PICK_IMAGE_REQUEST1)
    }

    private fun openGallery2() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, PICK_IMAGE_REQUEST2)
    }

    private fun openGallery3() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, PICK_IMAGE_REQUEST3)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST1) {
                if (data != null) {
                    selectedFileUri1 = data.data
                    if (selectedFileUri1 != null && !selectedFileUri1!!.path!!.isEmpty())
                        carte_image_1.setImageURI(selectedFileUri1)
                }
            } else if (requestCode == PICK_IMAGE_REQUEST2) {
                if (data != null) {
                    selectedFileUri2 = data.data
                    if (selectedFileUri2 != null && !selectedFileUri2!!.path!!.isEmpty())
                        carte_image_2.setImageURI(selectedFileUri2)
                }
            } else if (requestCode == PICK_IMAGE_REQUEST3) {
                if (data != null) {
                    selectedFileUri3 = data.data
                    if (selectedFileUri3 != null && !selectedFileUri3!!.path!!.isEmpty())
                        carte_image_3.setImageURI(selectedFileUri3)
                }
            }
        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }

    // ------------------------------- 사진 회전하기 -----------------------------------------------
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    // --------------------------------------------------------------------------------------------

    private fun uploadCarte() {
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setMessage("식단표를 작성중입니다...")
        dialog.isIndeterminate = false
        dialog.setCancelable(false)
        dialog.max = 100
        dialog.show()

        val file1 = FileUtils.getFile(this@Carte_write, selectedFileUri1)
        val file2 = FileUtils.getFile(this@Carte_write, selectedFileUri2)
        val file3 = FileUtils.getFile(this@Carte_write, selectedFileUri3)
        val requestFile1 = ProgressRequestBody(file1, this@Carte_write)
        val requestFile2 = ProgressRequestBody(file2, this@Carte_write)
        val requestFile3 = ProgressRequestBody(file3, this@Carte_write)

        val body1 = MultipartBody.Part.createFormData("file1", file1.name, requestFile1)
        val body2 = MultipartBody.Part.createFormData("file2", file2.name, requestFile2)
        val body3 = MultipartBody.Part.createFormData("file3", file3.name, requestFile3)

        Thread(Runnable {
            myAPI.uploadCarte(
                body1,
                body2,
                body3,
                edit_menu1.text.toString(),
                edit_menu2.text.toString(),
                edit_menu3.text.toString(),
                UserID,
                babyname.toString(),
                kindergarten.toString(),
                classname.toString(),
                week
            )
                .enqueue(object : retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@Carte_write, t!!.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        dialog.dismiss()
                        Toast.makeText(this@Carte_write, "식단표를 작성하였습니다!", Toast.LENGTH_SHORT)
                            .show()
                        var intent = Intent(this@Carte_write, Carte::class.java)
                        intent.putExtra("kindergarten", kindergarten.toString())
                        intent.putExtra("classname", classname.toString())
                        intent.putExtra("babyname", babyname.toString())
                        startActivity(intent)
                        finish()
                    }

                })

        }).start()

    }
}
