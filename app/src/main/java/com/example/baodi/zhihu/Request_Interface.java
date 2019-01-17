package com.example.baodi.zhihu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Swee on 2018/12/6.
 */



public interface Request_Interface {
    String ENDPOINT = "https://www.sweepersonal.cn/";

    // 获取关注问题列表
    @GET("api/v1/flow_questions/")
    Call<String> getFlowQuestionList(@Header("Authorization") String token);

    // 增加关注问题
    @POST("api/v1/flow_questions/")
    Call<String> postFlowQuestion(@Body RequestBody body, @Header("Authorization") String token);

    // 取消关注问题
    @DELETE("api/v1/flow_questions/{questionId}/")
    Call<String> cancelFlowQuestion(@Header("Authorization") String token, @Path("questionId") String questionId);

    // 获取收藏回答列表
    @GET("api/v1/favs/")
    Call<String> getFavList(@Header("Authorization") String token);

    // 对回答进行收藏
    @POST("api/v1/favs/")
    Call<String> postFav(@Body RequestBody body, @Header("Authorization") String token);

    // 取消对回答的收藏
    @DELETE("api/v1/favs/{answerId}/")
    Call<String> deleteFav(@Header("Authorization") String token, @Path("answerId") String answerId);

    // 获取点赞回答列表
    @GET("api/v1/votes/")
    Call<String> getVoteList(@Header("Authorization") String token);

    // 对回答进行点赞
    @POST("api/v1/votes/")
    Call<String> postVote(@Body RequestBody body, @Header("Authorization") String token);

    // 取消对回答的点赞
    @DELETE("api/v1/votes/{answerId}/")
    Call<String> cancelVote(@Header("Authorization") String token, @Path("answerId") String answerId);

    // 获取回答列表（这个是所有问题的回答，不建议使用）
    @GET("api/v1/answers/")
    Call<String> getAnswers();

    @GET("api/v1/answers/{answerId}/")
    Call<String> getAnswersinID(@Path("answerId") String answerId);

    // 提交一个回答
    @POST("api/v1/answers/")
    Call<String> postAnswer(@Body RequestBody body, @Header("Authorization") String token);

    // 获取问题列表（问题信息中包含对应回答列表）
    @GET("api/v1/questions/")
    Call<String> getQuestions();

    @GET("api/v1/questions/{questionId}/")
    Call<String> getQuestionsinID(@Path("questionId") String questionId);

    @GET("api/v1/users/")
    Call<String> getUser();

//    // 获取问题列表（问题信息中包含对应回答列表）
//    @GET("api/v1/questions/")
//    Call<String> getQuestions();

    // 获取搜索问题列表（问题信息中包含对应回答列表）
    @GET("api/v1/questions/")
    Call<String> getSearchQuestions(@Query("search") String keyword);

    // 提交一个新问题
    @POST("api/v1/questions/")
    Call<String> postQuestion(@Body RequestBody body, @Header("Authorization") String token);

    // 获取topic列表
    @GET("api/v1/topics/")
    Call<String> getTopic();

    // 提交一个新topoic
    @POST("api/v1/topics/")
    Call<String> postTopic(@Body RequestBody body);

//    @GET("/api_authlogin/")
//    Call<String> getAuthLogin();
//
//    @FormUrlEncoded
//    @POST("/api_authlogin/")
//    @Headers({"authority: www.sweepersonal.cn",
//            "method: POST",
//            "path: /api_authlogin/",
//            "scheme: https",
//            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
//            "accept-encoding: gzip, deflate, br",
//            "accept-language: zh-CN,zh;q=0.9",
//            "cache-control: max-age=0",
//            "content-length: 152",
//            "content-type: application/x-www-form-urlencoded",
//            "origin: https://www.sweepersonal.cn",
//            "referer: https://www.sweepersonal.cn/api_authlogin/?next=/api/v1/",
//            "upgrade-insecure-requests: 1",
//            "user-agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36"
//    })
//    Call<String> postAuthLogin(@Field("csrfmiddlewaretoken") String csrf,
//                               @Field("username") String username,
//                               @Field("password") String password,
//                               @Field("submit") String submitType,
//                               @Query("next") String next,
//                               @Header("cookie") String csrftoken);
//
//
//    @GET("api/v1/users/")
//    Call<String> getUser(@Header("cookie") String session);

    // 提交手机号获取验证码，默认验证码"5802"，在注册前调用一次
    @POST("api/v1/codes/")
    Call<String> postCode(@Body RequestBody body);

    // 用户注册
    @POST("api/v1/register/")
    Call<String> postRegister(@Body RequestBody body);

    // 用户登录，用于获取用户token，一开始调用
    @POST("api/v1/login")
    Call<String> postLogin(@Body RequestBody body);
}
