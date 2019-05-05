package com.fzu.facheck.network.api;

import com.fzu.facheck.entity.RollCall.ClassInfo;
import com.fzu.facheck.entity.RollCall.CreateClassInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ClassServer {
    @POST("{cid}")
    Observable<CreateClassInfo> getclassserver(@Path("cid") String cid, @Body RequestBody requestBody);
    @POST("{cid}")
    Observable<ClassInfo> getclassInfo(@Path("cid") String cid, @Body RequestBody requestBody);
}
