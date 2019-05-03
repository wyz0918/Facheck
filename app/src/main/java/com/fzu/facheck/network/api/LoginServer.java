package com.fzu.facheck.network.api;

import com.fzu.facheck.entiy.logininfo.StateInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface LoginServer {
    @POST("{cid}")
    Observable<StateInfo> getserver(@Path("cid") String cid, @Body RequestBody requestBody);
}
