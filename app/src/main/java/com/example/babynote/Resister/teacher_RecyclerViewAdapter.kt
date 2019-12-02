package com.example.babynote.Resister

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.babynote.R
import kotlinx.android.synthetic.main.item_raw.view.*


class teacher_RecyclerViewAdapter(val kindergarten_info: kindergarten_info) :
    RecyclerView.Adapter<teacher_RecyclerViewAdapter.teacher_viewholder>() {
    override fun getItemCount(): Int {
        return kindergarten_info.items.count()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): teacher_RecyclerViewAdapter.teacher_viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_item_raw, parent, false)
        return teacher_viewholder(v)
    }

    override fun onBindViewHolder(
        holder: teacher_RecyclerViewAdapter.teacher_viewholder,
        position: Int
    ) {
        holder.bindItems(kindergarten_info.items.get(position))


    }

    class teacher_viewholder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(data: teacher_Item) {


            var title = data.title.replace("<b>", " ")
            var title1 = title.replace("</b>", "")

            itemView.select_kindergarten.text = "${title1}"
            itemView.kindergarten_adress.text = "${data.roadAddress}"
            itemView.kindergarten_callnumber.text = "${data.telephone}"

            //클릭시
            itemView.setOnClickListener {
                Log.d("선택한 아이템", title1)
                Toast.makeText(itemView.context, "'${title1}'을/를 클릭했습니다.", Toast.LENGTH_LONG).show()

                val intent = Intent(itemView.context, Teacher_image_info::class.java)
                intent.putExtra("유치원이름",title1)
                itemView.context.startActivity(intent)

                //새 액티비티를 열고 웹뷰를 이용해서 상세보기 페이지를 보여 준다.
//                val webpage = Uri.parse("${data.link}")
//                val webIntent = Intent(Intent.ACTION_VIEW, webpage)
//                view.getContext().startActivity(webIntent)


            }
        }

    }
}