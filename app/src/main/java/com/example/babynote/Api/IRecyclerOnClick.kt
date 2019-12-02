package com.example.babynote.Api

import android.view.View

interface IRecyclerOnClick {
    fun onClick(view:View,position:Int)
    fun onLongClick(view: View, Position: Int)
}