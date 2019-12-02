package com.example.babynote.앨범

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.babynote.Common.Common
import com.example.babynote.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_album__image.*

class album_Image : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album__image)

        Picasso.get().load(Common.selected_album?.album_image).into(album_image)
    }
}
