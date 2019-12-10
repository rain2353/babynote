package com.example.babynote.앨범

import android.app.Activity
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
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.R
import com.example.babynote.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_album_write.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class Album_write : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    private var selectedFileUri: Uri? = null
    lateinit var myAPI: INodeJS
    //    lateinit var mService: INodeJS
    lateinit var dialog: ProgressDialog
    var compositeDisposable = CompositeDisposable()

    var userID : String? = null
    var kindergarten: String? = null
    var classname: String? = null
    var writer_nickname: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_write)
        setToolbar()
        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
        kindergarten = intent.getStringExtra("유치원이름")
        classname = intent.getStringExtra("유치원반이름")
        writer_nickname = intent.getStringExtra("작성자")
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        userID = pref.getString("id", 0.toString())
        Log.d(kindergarten, "info")
        Log.d(classname, "info")
        Log.d(writer_nickname, "info")

        // --------------------------- Request runtime permission ---------------------------------
        if (ActivityCompat.checkSelfPermission(
                this@Album_write,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this@Album_write,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )

        // --------------------------- 갤러리에서 이미지 가져오기 -------------------------------------
        button_album_image.setOnClickListener { openGallery() }
        //------------------------------------------------------------------------------------------
    }

    // 툴바 사용 설정
    private fun setToolbar() {
        setSupportActionBar(album_write_toolbar)

        // 툴바 왼쪽 버튼 설정
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setTitle("앨범 사진 등록하기")
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.image_upload, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {

            R.id.write_ok -> {
                if (selectedFileUri == null) {
                    Toast.makeText(this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                } else {
                    uploadAlbum()
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
                    Toast.makeText(this@Album_write, "Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@Album_write, "Denied", Toast.LENGTH_SHORT).show()
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
                    if (selectedFileUri != null && !selectedFileUri!!.path!!.isEmpty())
                        image_album_write.setImageURI(selectedFileUri)
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
    private fun uploadAlbum() {
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setMessage("앨범에 사진을 등록중입니다...")
        dialog.isIndeterminate = false
        dialog.setCancelable(false)
        dialog.max = 100
        dialog.show()

        val file = FileUtils.getFile(this@Album_write, selectedFileUri)
        val requestFile = ProgressRequestBody(file, this@Album_write)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        Thread(Runnable {
            myAPI.uploadAlbum(
                body,
                userID,
                writer_nickname.toString(),
                kindergarten.toString(),
                classname.toString()
            )
                .enqueue(object : retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@Album_write, t!!.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        dialog.dismiss()
                        Toast.makeText(this@Album_write, "앨범에 사진을 등록하였습니다!", Toast.LENGTH_SHORT)
                            .show()
                        var intent = Intent(this@Album_write, Album::class.java)
                        intent.putExtra("kindergarten", kindergarten.toString())
                        intent.putExtra("classname", classname.toString())
                        intent.putExtra("babyname", writer_nickname.toString())
                        startActivity(intent)
                        finish()
                    }

                })

        }).start()

    }

}
