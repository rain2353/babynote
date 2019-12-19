package com.example.babynote.Api

import com.example.babynote.Kiz.Kiz
import com.example.babynote.공지사항.notice_comment_list
import com.example.babynote.공지사항.notice_list
import com.example.babynote.귀가동의서.consent_comment_list
import com.example.babynote.귀가동의서.consent_list
import com.example.babynote.식단표.carte_comment_list
import com.example.babynote.식단표.carte_list
import com.example.babynote.알림장.advice_comment_list
import com.example.babynote.알림장.advice_list
import com.example.babynote.알림장.select_baby_list
import com.example.babynote.앨범.album_comment_list
import com.example.babynote.앨범.album_list
import com.example.babynote.투약의뢰서.administration_request_list
import com.example.babynote.투약의뢰서.request_comment_list
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface INodeJS {
    // 회원가입.
    @POST("register")
    @FormUrlEncoded
    fun registerUser(
        @Field("id") id: String?,
        @Field("name") name: String?,
        @Field("phonenumber") phonenumber: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("state") state: String?,
        @Field("nickname") nickname: String?
    ): Observable<String>

    // 로그인
    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("id") id: String?,
        @Field("password") password: String?
    ): Observable<String>

    // 아이디를 분실한 유저 아이디 찾기.
    @POST("find_id")
    @FormUrlEncoded
    fun find_userid(
        @Field("name") name: String?,
        @Field("phonenumber") phonenumber: String?
    ): Observable<String>

    // 비밀번호를 분실한 유저 찾기.
    @POST("find_password")
    @FormUrlEncoded
    fun find_userPassword(
        @Field("id") id: String?,
        @Field("name") name: String?,
        @Field("phonenumber") phonenumber: String?
    ): Observable<String>

    // 비밀번호 변경하기
    @POST("change_password")
    @FormUrlEncoded
    fun change_userPassword(
        @Field("id") id: String?,
        @Field("name") name: String?,
        @Field("phonenumber") phonenumber: String?,
        @Field("password") password: String?
    ): Observable<String>

    // 유저 정보.
    @POST("user")
    @FormUrlEncoded
    fun userInfo(@Field("id") id: String?): Observable<String>

    // 내정보 이름,이메일,휴대전화번호, 호칭 변경
    @POST("modify_myInfo")
    @FormUrlEncoded
    fun modify_myInfo(
        @Field("num") num : Int?,
        @Field("unique_id") unique_id: String?,
        @Field("id") id: String?,
        @Field("name") name: String?,
        @Field("phone_number") phone_number: String?,
        @Field("email") email: String?,
        @Field("encrypted_password") encrypted_password: String?,
        @Field("state") state: String?,
        @Field("nickname") nickname: String?,
        @Field("salt") salt: String?,
        @Field("created_at") created_at: String?,
        @Field("updated_at") updated_at: String?
    ): Observable<String>

    // 회원 탈퇴하기.
    @POST("membership_withdrawal")
    @FormUrlEncoded
    fun membership_withdrawal(
        @Field("num") num : Int?,
        @Field("id") id: String?
    ): Observable<String>


    // 자신이 등록한 아기 선택하기.
    @GET("mybaby/{parents_id}/{num}")
    fun mybaby(
        @Path("parents_id") parents_id: String?,
        @Path("num") num: Int?
    ): Observable<String>

    // 아기 등록하기.
    @POST("add_baby")
    @Multipart
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("babyname") babyname: String?,
        @Part("babybirth") babybirth: String?,
        @Part("babygender") babygender: String?,
        @Part("baby_kindergarten") baby_kindergarten: String?,
        @Part("baby_class") baby_class: String?,
        @Part("parents_id") parents_id: String?,
        @Part("state") state: String?
    ): Call<String>

    // 자신이 등록한 아기 리스트 불러오기
    @GET("babys/{parents_id}")
    fun getbabysList(@Path("parents_id") parents_id: String?): Observable<List<Kiz>>

    // 수정할 아이 정보 불러오기.
    @POST("select_baby")
    @FormUrlEncoded
    fun select_baby(
        @Field("num") num : Int?
    ): Observable<String>

    // 내가 등록한 아이 이름, 생일, 성별 변경
    @POST("modify_baby")
    @FormUrlEncoded
    fun modify_baby(
        @Field("num") num : Int?,
        @Field("babyname") babyname: String?,
        @Field("babybirth") babybirth: String?,
        @Field("babygender") babygender: String?,
        @Field("baby_kindergarten") baby_kindergarten: String?,
        @Field("baby_class") baby_class: String?,
        @Field("baby_imagepath") baby_imagepath: String?,
        @Field("parents_id") parents_id: String?,
        @Field("state") state: String?
    ): Observable<String>

    // 아기 프로필 사진 변경하기.
    @POST("modify_baby_image")
    @Multipart
    fun modify_baby_image(
        @Part file: MultipartBody.Part,
        @Part("num") num: Int?,
        @Part("babyname") babyname: String?,
        @Part("babybirth") babybirth: String?,
        @Part("babygender") babygender: String?,
        @Part("baby_kindergarten") baby_kindergarten: String?,
        @Part("baby_class") baby_class: String?,
        @Part("parents_id") parents_id: String?,
        @Part("state") state: String?
    ): Call<String>

    // 내가 등록한 아이 삭제하기.
    @POST("delete_baby")
    @FormUrlEncoded
    fun delete_baby(
        @Field("num") num : Int?,
        @Field("babyname") babyname: String?
    ): Observable<String>

    // 공지사항 글 작성하기
    @POST("add_notice")
    @Multipart
    fun uploadNotice(
        @Part file: MultipartBody.Part,
        @Part("notice_title") notice_title: String?,
        @Part("notice_content") notice_content: String?,
        @Part("notice_writer") notice_writer: String?,
        @Part("notice_nickname") notice_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?
    ): Call<String>

    // 공지사항 글 리스트 불러오기
    @GET("notice_list/{kindergarten}/{classname}")
    fun notice_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?
    ): Observable<List<notice_list>>

    // 공지사항 댓글 읽어오기
    @GET("notice_text_comment_read/{notice_num}")
    fun notice_text_comment_read(@Path("notice_num") notice_num: Int?): Observable<List<notice_comment_list>>

    // 공지사항 댓글 작성
    @POST("notice_text_comment_write")
    @FormUrlEncoded
    fun notice_text_comment_write(
        @Field("notice_num") notice_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?
    ): Observable<String>

    // 공지사항 글 삭제
    @POST("notice_delete")
    @FormUrlEncoded
    fun notice_text_delete(@Field("num") num: Int?): Observable<String>

    // 공지사항 글 수정
    @POST("notice_modify")
    @Multipart
    fun notice_modify(
        @Part file: MultipartBody.Part,
        @Part("notice_num") notice_num: Int?,
        @Part("notice_title") notice_title: String?,
        @Part("notice_content") notice_content: String?,
        @Part("notice_time") notice_time: String?,
        @Part("notice_writer") notice_writer: String?,
        @Part("notice_nickname") notice_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?
    ): Call<String>

    // 공지사항 댓글 수정
    @POST("notice_text_modify_comment")
    @FormUrlEncoded
    fun notice_text_modify_comment(
        @Field("num") num: Int?,
        @Field("notice_num") notice_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?,
        @Field("comment_time") comment_time: String?
    ): Observable<String>

    // 공지사항 댓글 삭제
    @POST("notice_comment_delete")
    @FormUrlEncoded
    fun notice_comment_delete(@Field("num") num: Int?): Observable<String>

    // 앨범 사진 등록하기
    @POST("add_album")
    @Multipart
    fun uploadAlbum(
        @Part file: MultipartBody.Part,
        @Part("album_writer") album_writer: String?,
        @Part("album_nickname") album_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?
    ): Call<String>

    // 앨범 사진 리스트 불러오기
    @GET("album_list/{kindergarten}/{classname}")
    fun album_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?
    ): Observable<List<album_list>>

    // 앨범 사진 삭제
    @POST("album_delete")
    @FormUrlEncoded
    fun album_delete(@Field("num") num: Int?): Observable<String>

    // 앨범 사진 수정
    @POST("album_modify")
    @Multipart
    fun album_modify(
        @Part file: MultipartBody.Part,
        @Part("num") num: Int?,
        @Part("album_time") album_time: String?,
        @Part("album_writer") album_writer: String?,
        @Part("album_nickname") album_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?
    ): Call<String>

    // 앨범 댓글 읽어오기
    @GET("album_comment_read/{album_num}")
    fun album_comment_read(@Path("album_num") album_num: Int?): Observable<List<album_comment_list>>

    // 앨범 댓글 작성
    @POST("album_comment_write")
    @FormUrlEncoded
    fun album_comment_write(
        @Field("album_num") album_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?
    ): Observable<String>

    // 앨범 댓글 수정
    @POST("album_modify_comment")
    @FormUrlEncoded
    fun album_modify_comment(
        @Field("num") num: Int?,
        @Field("album_num") album_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?,
        @Field("comment_time") comment_time: String?
    ): Observable<String>

    // 앨범 댓글 삭제
    @POST("album_comment_delete")
    @FormUrlEncoded
    fun album_comment_delete(@Field("num") num: Int?): Observable<String>

    // 식단표 작성하기
    @POST("add_carte")
    @Multipart
    fun uploadCarte(
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part,
        @Part("menu1") menu1: String?,
        @Part("menu2") menu2: String?,
        @Part("menu3") menu3: String?,
        @Part("writer_id") writer_id: String?,
        @Part("writer_nickname") writer_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?,
        @Part("carte_time") carte_time: String?
    ): Call<String>

    // 식단표 리스트 불러오기
    @GET("carte_list/{kindergarten}/{classname}")
    fun carte_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?
    ): Observable<List<carte_list>>

    // 식단표 수정
    @POST("carte_modify")
    @Multipart
    fun carte_modify(
        @Part("num") num: Int?,
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part,
        @Part("menu1") menu1: String?,
        @Part("menu2") menu2: String?,
        @Part("menu3") menu3: String?,
        @Part("writer_id") writer_id: String?,
        @Part("writer_nickname") writer_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?,
        @Part("carte_time") carte_time: String?,
        @Part("carte_write_time") carte_write_time: String?
    ): Call<String>

    // 식단표 삭제
    @POST("carte_delete")
    @FormUrlEncoded
    fun carte_delete(@Field("num") num: Int?): Observable<String>

    // 식단표 댓글 읽어오기
    @GET("carte_comment_read/{carte_num}")
    fun carte_comment_read(@Path("carte_num") carte_num: Int?): Observable<List<carte_comment_list>>

    // 식단표 댓글 작성
    @POST("carte_comment_write")
    @FormUrlEncoded
    fun carte_comment_write(
        @Field("carte_num") carte_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?
    ): Observable<String>

    // 식단표 댓글 수정
    @POST("carte_modify_comment")
    @FormUrlEncoded
    fun carte_modify_comment(
        @Field("num") num: Int?,
        @Field("carte_num") carte_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?,
        @Field("comment_time") comment_time: String?
    ): Observable<String>

    // 식단표 댓글 삭제
    @POST("carte_comment_delete")
    @FormUrlEncoded
    fun carte_comment_delete(@Field("num") num: Int?): Observable<String>

    // 투약의뢰서 작성.
    @POST("request_medicine")
    @FormUrlEncoded
    fun request_medicine(
        @Field("babyname") babyname: String?,
        @Field("request_day") request_day: String?,
        @Field("symptom") symptom: String?,
        @Field("medicine") medicine: String?,
        @Field("cc") cc: String?,
        @Field("numberoftimes") numberoftimes: String?,
        @Field("medicine_time") medicine_time: String?,
        @Field("storage") storage: String?,
        @Field("baby_comment") baby_comment: String?,
        @Field("kindergarten") kindergarten: String?,
        @Field("classname") classname: String?,
        @Field("parents_id") parents_id: String?,
        @Field("baby_image") baby_image: String?
     ): Observable<String>

    // 투약의뢰서 글 리스트  전체 불러오기 ( 선생님 )
    @GET("request_list_all/{kindergarten}/{classname}")
    fun request_list_all(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?
    ): Observable<List<administration_request_list>>

    // 투약의뢰서 글 리스트  전체 불러오기 ( 학부모 )
    @GET("request_list/{kindergarten}/{classname}/{parents_id}/{babyname}")
    fun request_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?,
        @Path("parents_id") parents_id: String?,
        @Path("babyname") babyname: String?
    ): Observable<List<administration_request_list>>

    // 투약의뢰서 글 삭제
    @POST("request_delete")
    @FormUrlEncoded
    fun request_text_delete(@Field("num") num: Int?): Observable<String>

    // 투약의뢰서 댓글 읽어오기
    @GET("request_comment_read/{request_num}")
    fun request_comment_read(@Path("request_num") request_num: Int?): Observable<List<request_comment_list>>

    // 투약의뢰서 댓글 작성
    @POST("request_comment_write")
    @FormUrlEncoded
    fun request_comment_write(
        @Field("request_num") request_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?
    ): Observable<String>

    // 투약의뢰서 댓글 수정
    @POST("request_modify_comment")
    @FormUrlEncoded
    fun request_modify_comment(
        @Field("num") num: Int?,
        @Field("request_num") request_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?,
        @Field("comment_time") comment_time: String?
    ): Observable<String>

    // 투약의뢰서 댓글 삭제
    @POST("request_comment_delete")
    @FormUrlEncoded
    fun request_comment_delete(@Field("num") num: Int?): Observable<String>

    // 귀가동의서 작성.
    @POST("write_consent")
    @FormUrlEncoded
    fun write_consent(
        @Field("baby_image") baby_image: String?,
        @Field("babyname") babyname: String?,
        @Field("consent_day") consent_day: String?,
        @Field("consent_time") consent_time: String?,
        @Field("consent_how") consent_how: String?,
        @Field("relation1") relation1: String?,
        @Field("call1") call1: String?,
        @Field("relation2") relation2: String?,
        @Field("call2") call2: String?,
        @Field("kindergarten") kindergarten: String?,
        @Field("classname") classname: String?,
        @Field("parents_id") parents_id: String?
    ): Observable<String>

    // 귀가동의서 글 리스트  전체 불러오기 ( 선생님 )
    @GET("consent_list_all/{kindergarten}/{classname}")
    fun consent_list_all(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?
    ): Observable<List<consent_list>>

    // 귀가동의서 글 리스트  전체 불러오기 ( 학부모 )
    @GET("consent_list/{kindergarten}/{classname}/{parents_id}/{babyname}")
    fun consent_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?,
        @Path("parents_id") parents_id: String?,
        @Path("babyname") babyname: String?
    ): Observable<List<consent_list>>

    // 귀가동의서 글 삭제
    @POST("consent_delete")
    @FormUrlEncoded
    fun consent_delete(@Field("num") num: Int?): Observable<String>

    // 귀가동의서 댓글 읽어오기
    @GET("consent_comment_read/{consent_num}")
    fun consent_comment_read(@Path("consent_num") consent_num: Int?): Observable<List<consent_comment_list>>

    // 귀가동의서 댓글 작성
    @POST("consent_comment_write")
    @FormUrlEncoded
    fun consent_comment_write(
        @Field("consent_num") consent_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?
    ): Observable<String>

    // 귀가동의서 댓글 수정
    @POST("consent_modify_comment")
    @FormUrlEncoded
    fun consent_modify_comment(
        @Field("num") num: Int?,
        @Field("consent_num") consent_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?,
        @Field("comment_time") comment_time: String?
    ): Observable<String>

    // 귀가동의서 댓글 삭제
    @POST("consent_comment_delete")
    @FormUrlEncoded
    fun consent_comment_delete(@Field("num") num: Int?): Observable<String>

    // 알림장 작성중에 원아 선택할수있게 리스트 불러오기
    @GET("baby_list/{kindergarten}/{classname}/{state}")
    fun baby_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?,
        @Path("state") state: String?
    ): Observable<List<select_baby_list>>

    // 알림장 작성하기.
    @POST("add_advice")
    @Multipart
    fun upload_advice(
        @Part("advice_baby") advice_baby: String?,
        @Part ("advice_content") advice_content: String?,
        @Part file: MultipartBody.Part,
        @Part("feel") feel: String?,
        @Part("health") health: String?,
        @Part("temperature") temperature: String?,
        @Part("MealorNot") MealorNot: String?,
        @Part("sleep") sleep: String?,
        @Part("poop") poop: String?,
        @Part("advice_writer") advice_writer: String?,
        @Part("advice_nickname") advice_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?,
        @Part("baby_image") baby_image : String?
    ): Call<String>

    // 알림장 글 리스트  전체 불러오기 ( 선생님 )
    @GET("all_advice_list/{kindergarten}/{classname}")
    fun all_advice_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?
    ): Observable<List<advice_list>>

    // 알림장 글 리스트  전체 불러오기 ( 학부모 )
    @GET("advice_list/{kindergarten}/{classname}/{advice_baby}")
    fun advice_list(
        @Path("kindergarten") kindergarten: String?,
        @Path("classname") classname: String?,
        @Path("advice_baby") advice_baby: String?
    ): Observable<List<advice_list>>

    // 알림장 수정하기.
    @POST("advice_modify")
    @Multipart
    fun advice_modify(
        @Part("num") num: Int?,
        @Part("advice_baby") advice_baby: String?,
        @Part ("advice_content") advice_content: String?,
        @Part file: MultipartBody.Part,
        @Part("feel") feel: String?,
        @Part("health") health: String?,
        @Part("temperature") temperature: String?,
        @Part("MealorNot") MealorNot: String?,
        @Part("sleep") sleep: String?,
        @Part("poop") poop: String?,
        @Part("advice_writer") advice_writer: String?,
        @Part("advice_nickname") advice_nickname: String?,
        @Part("kindergarten") kindergarten: String?,
        @Part("classname") classname: String?,
        @Part("advice_time") advice_time: String?,
        @Part("advice_write_time") advice_write_time: String?,
        @Part("baby_image") baby_image: String?
    ): Call<String>

    // 알림장 글 삭제
    @POST("advice_delete")
    @FormUrlEncoded
    fun advice_text_delete(@Field("num") num: Int?): Observable<String>

    // 알림장 댓글 읽어오기
    @GET("advice_comment_read/{advice_num}")
    fun advice_comment_read(@Path("advice_num") advice_num: Int?): Observable<List<advice_comment_list>>

    // 알림장 댓글 작성
    @POST("advice_comment_write")
    @FormUrlEncoded
    fun advice_comment_write(
        @Field("advice_num") advice_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?
    ): Observable<String>

    // 알림장 댓글 수정
    @POST("advice_modify_comment")
    @FormUrlEncoded
    fun advice_modify_comment(
        @Field("num") num: Int?,
        @Field("advice_num") advice_num: Int?,
        @Field("comment_writer") comment_writer: String?,
        @Field("comment_nickname") comment_nickname: String?,
        @Field("comment_content") comment_content: String?,
        @Field("comment_time") comment_time: String?
    ): Observable<String>

    // 알림장 댓글 삭제
    @POST("advice_comment_delete")
    @FormUrlEncoded
    fun advice_comment_delete(@Field("num") num: Int?): Observable<String>



}