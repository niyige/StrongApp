package com.oyy.strong.http

import com.oyy.strong.entity.GifBean
import com.oyy.strong.entity.Subtitles
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import rx.Observable

/**
 * Created by
 * ouyangyi on 18/5/22.
 */
interface APIService {
    @Headers("Cache-Control: public, max-age=60 * 60 * 24 * 7")
    @POST("/app/gifCreate/filePath")
    fun getGifPath(@Body subtitles: Subtitles): Observable<GifBean>
}