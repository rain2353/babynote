package com.example.babynote.Add_baby

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.babynote.R
import kotlinx.android.synthetic.main.item_raw.view.*

class modify_search_RecyclerViewAdapter(val context: Context, val homefeed: modify_babykindergarten) :
    RecyclerView.Adapter<modify_search_RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): modify_search_RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_raw, p0, false)
        return modify_search_RecyclerViewAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return homefeed.items.count()
    }

    override fun onBindViewHolder(p0: modify_search_RecyclerViewAdapter.ViewHolder, p1: Int) {
        p0.bindItems(homefeed.items.get(p1))
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(data: modify_Item) {


            var title = data.title.replace("<b>", " ")
            var title1 = title.replace("</b>", "")

            itemView.select_kindergarten.text = "${title1}"
            itemView.kindergarten_adress.text = "${data.roadAddress}"
            itemView.kindergarten_callnumber.text = "${data.telephone}"

            //클릭시
            itemView.setOnClickListener {
                Log.d("선택한 아이템", title1)
                Toast.makeText(itemView.context, "'${title1}'을/를 클릭했습니다.", Toast.LENGTH_LONG).show()

                val intent = Intent(itemView.context, modify_babyclass::class.java)
                intent.putExtra("유치원이름", title1)
                itemView.context.startActivity(intent)

            }
        }

    }
}

