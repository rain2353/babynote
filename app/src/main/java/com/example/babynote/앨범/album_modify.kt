package com.example.babynote.앨범

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.example.babynote.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_album_modify.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class album_modify : Activity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    private var selectedFileUri: Uri? = null
    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_modify)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        Picasso.get().load(Common.selected_album?.album_image).resize(300,300).into(modify_image)
        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
// --------------------------- Request runtime permission ---------------------------------
        if (ActivityCompat.checkSelfPermission(
                this@album_modify,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this@album_modify,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )

        // --------------------------- 갤러리에서 이미지 가져오기 -------------------------------------
        button_image.setOnClickListener { openGallery() }
        //------------------------------------------------------------------------------------------
        // 삭제하기 버튼
        button_delete.setOnClickListener {
            compositeDisposable.add(myAPI.album_delete(Common.selected_album?.num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->
                    Toast.makeText(this, "사진을 삭제하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("album_delete", message.toString())
                    finish()

                }
                    , { thr ->
                        Toast.makeText(
                            this,
                            "사진을 삭제하지 못했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        Log.d("album_delete", thr.message.toString())

                    }

                ))
        }
        // 수정하기 버튼
        button_modify.setOnClickListener {
            if (selectedFileUri == null) {
                Toast.makeText(this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                dialog = ProgressDialog(this)
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                dialog.setMessage("앨범에 사진을 등록중입니다...")
                dialog.isIndeterminate = false
                dialog.setCancelable(false)
                dialog.max = 100
                dialog.show()

                val file = FileUtils.getFile(this@album_modify, selectedFileUri)
                val requestFile = ProgressRequestBody(file, this@album_modify)

                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                Thread(Runnable {
                    myAPI.album_modify(
                        body,
                        Common.selected_album?.num,
                        Common.selected_album?.album_time,
                        Common.selected_album?.album_writer,
                        Common.selected_album?.album_nickname,
                        Common.selected_album?.kindergarten,
                        Common.selected_album?.classname

                    )
                        .enqueue(object : retrofit2.Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                dialog.dismiss()
                                Toast.makeText(this@album_modify, t!!.message, Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }

                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@album_modify,
                                    "사진을 수정하였습니다!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                var intent = Intent(this@album_modify, Album::class.java)
                                intent.putExtra(
                                    "kindergarten",
                                    Common.selected_album?.kindergarten.toString()
                                )
                                intent.putExtra(
                                    "classname",
                                    Common.selected_album?.classname.toString()
                                )
                                intent.putExtra(
                                    "babyname",
                                    Common.selected_album?.album_nickname.toString()
                                )
                                startActivity(intent)
                                finish()
                            }

                        })

                }).start()
            }


        }
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
                    Toast.makeText(this@album_modify, "Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@album_modify, "Denied", Toast.LENGTH_SHORT).show()
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
                        modify_image.setImageURI(selectedFileUri)
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
