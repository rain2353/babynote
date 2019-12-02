package com.example.babynote.공지사항

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo_view.*

class PhotoView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)

        Picasso.get().load(Common.selected_notice?.notice_image).into(photoView)

    }
}
