package com.example.babynote.알림장

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Common.Common
import com.example.babynote.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_select_baby.*

class select_baby : Activity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_select_baby)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        val lm = LinearLayoutManager(this)
        select_baby_recyclerview.layoutManager = lm
        select_baby_recyclerview.setHasFixedSize(true)
        select_baby_list(Common.selected_baby?.baby_kindergarten, Common.selected_baby?.baby_class, "아이")


    }
    private fun select_baby_list(kindergarten: String?, classname: String?, state: String?) {
        compositeDisposable.add(myAPI.baby_list(kindergarten, classname, state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ select_baby_list ->
                select_baby_recyclerview.adapter = select_baby_recyclerviewAdapter(baseContext, select_baby_list)
            }
                , { thr ->
                    Toast.makeText(this, "Error load", Toast.LENGTH_SHORT).show()
                    Log.d("select_baby_recycler", thr.message.toString())
                }

            ))
    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}
