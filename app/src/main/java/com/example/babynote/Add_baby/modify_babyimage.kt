package com.example.babynote.Add_baby

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.example.babynote.Utils.ProgressRequestBody
import com.google.gson.Gson
import com.ipaulpro.afilechooser.utils.FileUtils
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_modify_babyimage.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class modify_babyimage : Activity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    private val PICK_IMAGE_REQUEST: Int = 1001   // 사용자가 갤러리에서 사진을 선택하고 전달받는 값
    private var selectedFileUri: Uri? = null    // 사용자가 갤러리에서 선택한 이미지 URI
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 액티비티를 다이얼로그로 변경하는 설정.
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams

        setContentView(R.layout.activity_modify_babyimage)

        var gson = Gson()
        val pref1 = getSharedPreferences("babyinfo", Context.MODE_PRIVATE)
        var babyinfo = pref1.getString("babyinfo", null)
        var babyDTO = gson.fromJson(babyinfo, Kiz::class.java)

        Picasso.get().load(babyDTO.baby_imagepath).resize(300, 300).into(modify_image)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // --------------------------- 갤러리에서 이미지 가져오기 ----------------------------------
        button_image.setOnClickListener { openGallery() }
        //------------------------------------------------------------------------------------------
        button_modify_image.setOnClickListener {
            if (selectedFileUri == null) {
                Toast.makeText(this, "사진을 골라주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                dialog = ProgressDialog(this)
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                dialog.setMessage("프로필 사진을 변경중입니다...")
                dialog.isIndeterminate = false
                dialog.setCancelable(false)
                dialog.max = 100
                dialog.show()

                val file = FileUtils.getFile(this@modify_babyimage, selectedFileUri)
                val requestFile = ProgressRequestBody(file, this@modify_babyimage)

                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                Thread(Runnable {
                    myAPI.modify_baby_image(
                        body,
                        babyDTO.num,
                        babyDTO.baby_name,
                        babyDTO.baby_birth,
                        babyDTO.baby_gender,
                        babyDTO.baby_kindergarten,
                        babyDTO.baby_class,
                        babyDTO.parents_id,
                        babyDTO.state
                    )
                        .enqueue(object : retrofit2.Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@modify_babyimage,
                                    t.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                finish()
                            }

                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@modify_babyimage,
                                    "사진을 수정하였습니다!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                finish()
                            }

                        })

                }).start()
            }
        }
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
                        modify_image.setImageURI(selectedFileUri)      // 사용자가 갤러리에서 선택한 이미지를 사용자에게 보여준다.
                }
            }

        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }
}
