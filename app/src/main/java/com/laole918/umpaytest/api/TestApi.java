package com.laole918.umpaytest.api;

import com.laole918.umpaytest.model.Order;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by laole918 on 2016/3/29 0029.
 */
public interface TestApi {

    @POST("bjfeiyu/order")
    Observable<Order> order11(@Body Order request);
}
