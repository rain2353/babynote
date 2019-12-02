package com.example.babynote.Main

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.babynote.Api.IRecyclerOnClick
import com.example.babynote.Common.Common
import com.example.babynote.Kiz.Kiz
import com.example.babynote.R
import com.squareup.picasso.Picasso

class main_drawerAdapter(internal val context: Context, internal val kizList: List<Kiz>) :
    RecyclerView.Adapter<main_drawerAdapter.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_items, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return kizList.size
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        Picasso.get().load(kizList[p1].baby_imagepath).resize(250,250).into(p0.kiz_image)
        p0.kizname.text = kizList[p1].baby_name
        p0.kizAge.text = kizList[p1].baby_birth
        p0.kizgender.text = kizList[p1].baby_gender
        p0.kiz_kindergarten.text = kizList[p1].baby_kindergarten
        p0.kiz_class.text = kizList[p1].baby_class
        p0.setClick(object : IRecyclerOnClick{
            override fun onLongClick(view: View, Position: Int) {

            }

            override fun onClick(view: View, position: Int) {
                Common.selected_baby = kizList[position]
//                Toast.makeText(view.context,kizList[position].baby_name + "클릭"+"("+position+")",Toast.LENGTH_SHORT).show()
                val intent = Intent(view.context, MainActivity::class.java)
                intent.putExtra("num",position)
                view.context.startActivity(intent)

            }

        })
//        p0?.bind(kizList[p1], context)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        internal var kiz_image: ImageView
        internal var kizname: TextView
        internal var kizAge: TextView
        internal var kizgender: TextView
        internal var kiz_kindergarten: TextView
        internal var kiz_class: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)

        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick){
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            kiz_image = itemView.findViewById(R.id.kizPhotoImg) as ImageView
            kizname = itemView.findViewById(R.id.kizName) as TextView
            kizAge = itemView.findViewById(R.id.kizbirth) as TextView
            kizgender = itemView.findViewById(R.id.dogGenderTv) as TextView
            kiz_kindergarten = itemView.findViewById(R.id.kiz_kindergarten) as TextView
            kiz_class = itemView.findViewById(R.id.kiz_class) as TextView
            itemView.setOnClickListener(this)
        }


//        val kizoption = itemView?.findViewById<ImageButton>(R.id.kizOption)


//        fun bind(kiz: Kiz, context: Context) {
//            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
//            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
//
//            if (kiz.kizphoto != "") {
//                Glide.with(itemView.context).load(kiz.kizphoto)
//                    .apply(RequestOptions().override(200, 200))
//                    .apply(RequestOptions.centerCropTransform()).into(itemView.kizPhotoImg)
//
////                val resourceId = context.resources.getIdentifier(kiz.kizphoto, "drawable", context.packageName)
////                kizPhoto?.setImageResource(resourceId)
//            } else {
//                kiz_image?.setImageResource(R.mipmap.ic_launcher)
//            }
//            /* 나머지 TextView와 String 데이터를 연결한다. */
//            kizname?.text = kiz.kizname
//            kizAge?.text = kiz.kizbirth
//            kizgender?.text = kiz.kizgender
//            kiz_kindergarten?.text = kiz.kiz_kindergarten
//            kiz_class?.text = kiz.kiz_class
//
//        }
    }


}


