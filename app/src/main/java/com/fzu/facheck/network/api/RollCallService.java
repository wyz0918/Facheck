package com.fzu.facheck.network.api;

import com.fzu.facheck.entity.RollCall.RollCallInfo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @date: 2019/4/25
 * @author: wyz
 * @version:
 * @description: 点名相关的api
 */
public interface RollCallService {

    /**
     * 点名班级信息
     */
    @POST("/{cid}")
    Observable<RollCallInfo> getRollCallInfoById(@Path("cid") String cid, @Body RequestBody requestBody);

}
