package com.laole918.umpaytest.api;

import com.laole918.umpaytest.model.Order11Request;
import com.laole918.umpaytest.model.Order11Response;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by laole918 on 2016/3/29 0029.
 */
public interface TestApi {

    @POST("bjfeiyu/order")
    Observable<Order11Response> order11(@Body Order11Request request);
}
