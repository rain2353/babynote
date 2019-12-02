package com.example.babynote.Common

import com.example.babynote.Api.INodeJS
import com.example.babynote.Api.RetrofitClient
import com.example.babynote.Kiz.Kiz
import com.example.babynote.공지사항.notice_comment_list
import com.example.babynote.공지사항.notice_list
import com.example.babynote.귀가동의서.consent_comment_list
import com.example.babynote.귀가동의서.consent_list
import com.example.babynote.식단표.carte_comment_list
import com.example.babynote.식단표.carte_list
import com.example.babynote.알림장.advice_list
import com.example.babynote.알림장.select_baby_list
import com.example.babynote.앨범.album_comment_list
import com.example.babynote.앨범.album_list
import com.example.babynote.투약의뢰서.administration_request_list
import com.example.babynote.투약의뢰서.request_comment_list

object Common {

    var selected_baby: Kiz? = null    // 등록한 아이들 리스트에서 선택한 아기정보.
    var selected_notice: notice_list? = null   // 공지사항 리스트에서 선택한 공지사항 글 정보
    var selected_comment: notice_comment_list? = null  // 공지사항 글에서 선택한 댓글 정보
    var selected_album: album_list? = null  // 앨범 리스트에서 선택한 앨범 글 정보
    var selected_album_comment: album_comment_list? = null // 앨범 글에서 선택한 댓글 정보
    var selected_carte: carte_list? = null  // 식단표 리스트에서 선택한 식단표 글 정보
    var selected_carte_comment: carte_comment_list? = null  // 식단표 글에서 선택한 댓글 정보
    var selected_administration_request_form: administration_request_list? = null  // 투약의뢰서 리스트에서 선택한 글 정보
    var selected_request_comment: request_comment_list? = null   // 투약의뢰서 글에서 선택한 댓글 정보
    var selected_consent_form: consent_list? = null   // 귀가의뢰서 리스트에서 선택한 귀가의뢰서 글 정보
    var selected_consent_comment: consent_comment_list? = null  // 귀가의뢰서 글에서 선택한 댓글 정보
    var selected_baby_list: select_baby_list? = null // 알림장 작성할때 선택한 원아 정보
    var selected_advice: advice_list? = null    // 알림장 리스트에서 선택한 알림장 글 정보

//    fun isConnectedToInternet(context: Context?): Boolean {
//        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (cm != null) {
//            if (Build.VERSION.SDK_INT < 23) {
//                val ni = cm.activeNetworkInfo
//                if (ni != null)
//                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI ||
//                            ni.type == ConnectivityManager.TYPE_MOBILE)
//                else {
//                    val n = cm.activeNetwork
//                    if (n != null) {
//                        val nc = cm.getNetworkCapabilities(n)
//                        return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//                    }
//                }
//            }
//        }
//        return false
//    }


    val api: INodeJS
        get() {
            val retrofit = RetrofitClient.instance
            return retrofit.create(INodeJS::class.java)
        }
}