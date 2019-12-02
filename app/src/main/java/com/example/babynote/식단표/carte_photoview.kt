package com.example.babynote.식단표

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.babynote.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carte_photoview.*

class carte_photoview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carte_photoview)
        var image = intent.getStringExtra("Image")
        carte_photoView
        Picasso.get().load(image).into(carte_photoView)
    }
}
