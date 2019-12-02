package com.example.babynote.식단표

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
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.example.babynote.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_carte_modify.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class carte_modify : Activity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST1: Int = 1001
    private val PICK_IMAGE_REQUEST2: Int = 1002
    private val PICK_IMAGE_REQUEST3: Int = 1003
    private var selectedFileUri1: Uri? = null
    private var selectedFileUri2: Uri? = null
    private var selectedFileUri3: Uri? = null
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_carte_modify)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // --------------------------- Request runtime permission ---------------------------------
        if (ActivityCompat.checkSelfPermission(
                this@carte_modify,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this@carte_modify,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )
// --------------------------- 갤러리에서 이미지 가져오기 -------------------------------------
        button_modify_image_1.setOnClickListener { openGallery1() }
        button_modify_image_2.setOnClickListener { openGallery2() }
        button_modify_image_3.setOnClickListener { openGallery3() }
        //------------------------------------------------------------------------------------------
        // 삭제하기 버튼
        button_delete.setOnClickListener {
            compositeDisposable.add(myAPI.carte_delete(Common.selected_carte?.num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->
                    Toast.makeText(this, "식단표를 삭제하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("carte_delete", message.toString())
                    finish()

                }
                    , { thr ->
                        Toast.makeText(
                            this,
                            "식단표를 삭제하지 못했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        Log.d("carte_delete", thr.message.toString())

                    }

                ))
        }
        // 수정하기 버튼
        button_modify.setOnClickListener {
            if (selectedFileUri1 == null && selectedFileUri2 == null && selectedFileUri3 == null) {
                Toast.makeText(this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                dialog = ProgressDialog(this)
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                dialog.setMessage("식단표에 사진을 등록중입니다...")
                dialog.isIndeterminate = false
                dialog.setCancelable(false)
                dialog.max = 100
                dialog.show()

                val file1 = FileUtils.getFile(this@carte_modify, selectedFileUri1)
                val file2 = FileUtils.getFile(this@carte_modify, selectedFileUri2)
                val file3 = FileUtils.getFile(this@carte_modify, selectedFileUri3)
                val requestFile1 = ProgressRequestBody(file1, this@carte_modify)
                val requestFile2 = ProgressRequestBody(file2, this@carte_modify)
                val requestFile3 = ProgressRequestBody(file3, this@carte_modify)

                val body1 = MultipartBody.Part.createFormData("file1", file1.name, requestFile1)
                val body2 = MultipartBody.Part.createFormData("file2", file2.name, requestFile2)
                val body3 = MultipartBody.Part.createFormData("file3", file3.name, requestFile3)

                Thread(Runnable {
                    myAPI.carte_modify(
                        Common.selected_carte?.num,
                        body1,body2,body3,
                        edit_modify_menu1.text.toString(),
                        edit_modify_menu2.text.toString(),
                        edit_modify_menu3.text.toString(),
                        Common.selected_carte?.writer_id,
                        Common.selected_carte?.writer_nickname,
                        Common.selected_carte?.kindergarten,
                        Common.selected_carte?.classname,
                        Common.selected_carte?.carte_time,
                        Common.selected_carte?.carte_write_time

                    )
                        .enqueue(object : retrofit2.Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                dialog.dismiss()
                                Toast.makeText(this@carte_modify, t!!.message, Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }

                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@carte_modify,
                                    "사진을 수정하였습니다!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                var intent = Intent(this@carte_modify, Carte::class.java)
                                intent.putExtra(
                                    "kindergarten",
                                    Common.selected_carte?.kindergarten.toString()
                                )
                                intent.putExtra(
                                    "classname",
                                    Common.selected_carte?.classname.toString()
                                )
                                intent.putExtra(
                                    "babyname",
                                    Common.selected_carte?.writer_nickname.toString()
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
                    Toast.makeText(this@carte_modify, "Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@carte_modify, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------------------------------------------------------------------------------------------\
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
                    if (selectedFileUri1 != null && !selectedFileUri1!!.path.isEmpty())
                        carte_modify_image_1.setImageURI(selectedFileUri1)
                }
            } else if (requestCode == PICK_IMAGE_REQUEST2) {
                if (data != null) {
                    selectedFileUri2 = data.data
                    if (selectedFileUri2 != null && !selectedFileUri2!!.path.isEmpty())
                        carte_modify_image_2.setImageURI(selectedFileUri2)
                }
            } else if (requestCode == PICK_IMAGE_REQUEST3) {
                if (data != null) {
                    selectedFileUri3 = data.data
                    if (selectedFileUri3 != null && !selectedFileUri3!!.path.isEmpty())
                        carte_modify_image_3.setImageURI(selectedFileUri3)
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
}
