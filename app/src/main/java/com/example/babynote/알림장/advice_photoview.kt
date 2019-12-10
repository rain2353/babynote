package com.example.babynote.알림장

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_advice_photoview.*

class advice_photoview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advice_photoview)
        Picasso.get().load(Common.selected_advice?.file).into(advice_photoView)

    }
}
