package com.example.babynote.Resister

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Login.Login
import com.example.babynote.R
import com.example.babynote.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_teacher_image_info.*
import okhttp3.MultipartBody
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Response

class Teacher_image_info : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    lateinit var myAPI: INodeJS
    //    lateinit var mService: INodeJS
    lateinit var dialog: ProgressDialog
    var compositeDisposable = CompositeDisposable()
    var year = ""
    var month = ""
    var day = ""
    var gender: String = "남자"
    var userID = ""
    var kindergartenName = ""
    private val OPEN_GALLERY = 1
    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_image_info)
        setToolbar()

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        kindergartenName = intent.getStringExtra("유치원이름")
        textView_kindergarten.text = kindergartenName.trim()

        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        userID = pref.getString("id", 0.toString())

        // --------------------------- Request runtime permission ---------------------------------
        if (ActivityCompat.checkSelfPermission(
                this@Teacher_image_info,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this@Teacher_image_info,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )

        // Service
//        mService = myAPI


        //------------------------------------------------------------------------------------------
        // --------------------------- 갤러리에서 이미지 가져오기 -----------------------------------
        teacher_image_button.setOnClickListener { openGallery() }

        // ---------------------------아이 성별 선택------------------------------------------------

        button.setOnClickListener {
            button.backgroundColor = Color.rgb(0, 153, 204)
            button.textColor = Color.WHITE
            button2.backgroundColor = Color.WHITE
            button2.textColor = Color.BLACK
            gender = button.text.toString()
            Log.d("남자 선택", gender)
        }
        button2.setOnClickListener {
            button2.backgroundColor = Color.rgb(0, 153, 204)
            button2.textColor = Color.WHITE
            button.backgroundColor = Color.WHITE
            button.textColor = Color.BLACK
            gender = button2.text.toString()
            Log.d("여자 선택", gender)
        }
        //-----------------------------------------------------------------------------------------
        // ----------------------------- 3중 스피너 ------------------------------------------------

        var add_teacher_year = findViewById(R.id.add_teacher_year) as Spinner
        var add_teacher_month = findViewById(R.id.add_teacher_month) as Spinner
        var add_teacher_day = findViewById(R.id.add_teacher_day) as Spinner
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.태어난해,
            android.R.layout.simple_spinner_item
        )
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.태어난월,
            android.R.layout.simple_spinner_item
        )
        val adapter3 = ArrayAdapter.createFromResource(
            this,
            R.array.태어난일,
            android.R.layout.simple_spinner_item
        )
        add_teacher_year.adapter = adapter1
        add_teacher_year.prompt = "태어난 해 선택"

        add_teacher_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                year = add_teacher_year.getItemAtPosition(position).toString()
            }
        }
        add_teacher_month.adapter = adapter2
        add_teacher_month.prompt = "태어난 월 선택"

        add_teacher_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                month = add_teacher_month.getItemAtPosition(position).toString()
            }
        }
        add_teacher_day.adapter = adapter3
        add_teacher_day.prompt = "태어난 일 선택"

        add_teacher_day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                day = add_teacher_day.getItemAtPosition(position).toString()
            }
        }
        // ----------------------------------------------------------------------------------------
        // 아이 등록하기 버튼을 누르면 메인 액티비티로 이동한다.
        button_teacherAdd.setOnClickListener {
            if (editText2.text.toString() == null) {
                editText2.requestFocus()
                Toast.makeText(this, "선생님 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (year == "태어난 해") {
                Toast.makeText(this, "선생님 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (month == "태어난 월") {
                Toast.makeText(this, "선생님 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (day == "태어난 일") {
                Toast.makeText(this, "선생님 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (edit_class.text.toString() == null) {
                edit_class.requestFocus()
                Toast.makeText(this, "선생님의 반을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (selectedFileUri == null) {
                Toast.makeText(this, "선생님의 사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                uploadFile()
            }
            Log.d("teachername", editText2.text.toString())
            Log.d("teacherbirth", year.toString() + "/" + month.toString() + "/" + day.toString())
            Log.d("teachergender", gender.toString())
            Log.d("teacher_유치원", kindergartenName.trim())
            Log.d("teacher_class", edit_class.text.toString())
            Log.d("parents_id", userID.toString())

        }
    }

    private fun uploadFile() {
        if (selectedFileUri != null) {
            dialog = ProgressDialog(this)
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            dialog.setMessage("선생님을 등록중입니다...")
            dialog.isIndeterminate = false
            dialog.setCancelable(false)
            dialog.max = 100
            dialog.show()

            val file = FileUtils.getFile(this@Teacher_image_info, selectedFileUri)
            val requestFile = ProgressRequestBody(file, this@Teacher_image_info)

            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            Thread(Runnable {
                myAPI.uploadFile(
                    body,
                    editText2.text.toString(),
                    year.toString() + " . " + month.toString() + " . " + day.toString(),
                    gender.toString(),
                    kindergartenName.trim(),
                    edit_class.text.toString(),
                    userID.toString(),
                    "선생님"
                )
                    .enqueue(object : retrofit2.Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            dialog.dismiss()
                            Toast.makeText(this@Teacher_image_info, t!!.message, Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            dialog.dismiss()
                            Toast.makeText(this@Teacher_image_info, "선생님을 추가하였습니다!", Toast.LENGTH_SHORT)
                                .show()
                            var intent = Intent(this@Teacher_image_info, Login::class.java)
                            startActivity(intent)
                            finish()
                        }

                    })

            }).start()
        } else {
            Toast.makeText(this, "Please choose file by click to button", Toast.LENGTH_SHORT).show()
        }
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(teacher_toolbar11)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("선생님 프로필")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
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
                    Toast.makeText(this@Teacher_image_info, "Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@Teacher_image_info, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------갤러리에서 이미지 가져오기 -----------------------------
    private fun openGallery() {
//        val getContentIntent = FileUtils.createGetContentIntent()
//        val intent = Intent.createChooser(getContentIntent,"Select a file")
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(Intent.createChooser(intent, "Select image"), OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null) {
                    selectedFileUri = data.data
                    if (selectedFileUri != null && !selectedFileUri!!.path.isEmpty())
                        teacher_image.setImageURI(selectedFileUri)
                }
            }
//            if (requestCode == OPEN_GALLERY) {
//
//                var currentImageUrl: Uri? = data!!.data
//                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
//                    //val rotatedBitmap = bitmap.rotate(90F) // value must be float
//                    baby_image.setImageBitmap(bitmap)
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
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
}