package com.example.babynote.Add_baby

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_babyname.*

class modify_babyname : Activity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_modify_babyname)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        var gson = Gson()
        val pref1 = getSharedPreferences("babyinfo", Context.MODE_PRIVATE)
        var babyinfo = pref1.getString("babyinfo", null)
        var babyDTO = gson.fromJson(babyinfo, Kiz::class.java)

        editText_babyname.setText(babyDTO.baby_name)
        modify_babyname_button.setOnClickListener {
            if (!editText_babyname.text.isEmpty()) {
                compositeDisposable.add(myAPI.modify_baby(
                    babyDTO.num,
                    editText_babyname.text.toString(),
                    babyDTO.baby_birth,
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
                            "이름을 수정하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // 키보드를 내린다.
                        val imm =
                            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editText_babyname.windowToken, 0)
                        finish()

                    })
            } else {
                Toast.makeText(this, "변경하실 이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
                editText_babyname.requestFocus()
                return@setOnClickListener
            }
        }
    }
}
