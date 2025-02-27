package com.provizit.kioskcheckin.api;

import com.google.gson.JsonObject;
import com.provizit.kioskcheckin.services.BlockedvisitorrequestsModel;
import com.provizit.kioskcheckin.services.Blocklist_Model;
import com.provizit.kioskcheckin.services.CheckinoutModel;
import com.provizit.kioskcheckin.services.CommonModel;
import com.provizit.kioskcheckin.services.CommoncheckuserModel;
import com.provizit.kioskcheckin.services.CompanyDetailsModel;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.services.GetSearchEmployeesModel;
import com.provizit.kioskcheckin.services.GetdocumentsModel;
import com.provizit.kioskcheckin.services.GetnationalityModel;
import com.provizit.kioskcheckin.services.GetpurposesModel;
import com.provizit.kioskcheckin.services.GetsubhierarchysModel;
import com.provizit.kioskcheckin.services.MeetingDetailsModel;
import com.provizit.kioskcheckin.services.Model;
import com.provizit.kioskcheckin.services.Privacypolicymodel;
import com.provizit.kioskcheckin.services.TvisitorsListModel;
import com.provizit.kioskcheckin.services.VcheckuserModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.services.VisitorEmailModel;
import com.provizit.kioskcheckin.services.VisitorformDetailsModel;
import com.provizit.kioskcheckin.utilities.EntryPermit.EntryPermitModel;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkPermitModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @POST("login/checkinuserlogin")
    Call<Model> checkinuserlogin(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("useractions/verifylinkemail")
    Call<VisitorEmailModel> getverifylinkemail(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("visitor/actionvisitor")
    Call<VisitorActionModel> actionvisitor(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("outside/visitorSignup")
    Call<VisitorActionModel> visitorSignup(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);


    @POST("setup/otpsendemailclient")
    Call<VisitorActionModel> otpsendemailclient(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("useractions/verifylinkmobile")
    Call<VisitorActionModel> verifylinkmobile(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);


    @GET("masters/getuserDetails")
    Call<CompanyDetailsModel> getuserDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("masters/getuserLDetails")
    Call<CompanyDetailsModel> getuserLDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String visitor);

    @GET("company/getdocuments")
    Call<GetdocumentsModel> getdocuments(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("company/getnationality")
    Call<GetnationalityModel> getnationality(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("doc_id") String doc_id);

    @GET("company/getsearchemployees")
    Call<GetSearchEmployeesModel> getsearchemployees(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("l_id") String l_id, @Query("type") String type, @Query("h_id") String h_id);

    @GET("company/getpurposes")
    Call<GetpurposesModel> getpurposes(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("forms/visitorformdetails")
    Call<VisitorformDetailsModel> visitorformdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("masters/getsubhierarchys")
    Call<GetsubhierarchysModel> getsubhierarchys(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("indexid") String indexid, @Query("type") String type);

    @GET("forms/getndafdetials")
    Call<GetNdaActiveDetailsModel> getndafdetials(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("type") String active);

    //    getCVisitorDetails
    @GET("checkinout/getcvisitordetails")
    Call<GetCVisitorDetailsModel> getcvisitordetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id, @Query("emp_id") String emp_id, @Query("id") String id, @Query("l_id") String l_id, @Query("date") Long date, @Query("datetime") Long datetime);

    @POST("uploads/qrindex.php")
    Call<String> qrindex(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("company/gettvisitors")
    Call<TvisitorsListModel> gettvisitors(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("visitor/vcheckuser")
    Call<VcheckuserModel> vcheckuser(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("usertype") String usertype, @Query("val") String val);

    @GET("slots/getuserslotdetails")
    Call<CommonModel> getuserslotdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @POST("forms/guestentry")
    Call<VisitorActionModel> guestentry(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

// @POST("checkinout/actioncheckinout")
//    Call<VisitorActionModel> actioncheckinout(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("checkinout/actioncheckinout")
    Call<CheckinoutModel> actioncheckinout(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("useractions/commoncheckuser")
    Call<CommoncheckuserModel> commoncheckuser(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("usertype") String usertype, @Query("val") String val);

    @GET("forms/getprivacypolicydetials")
    Call<Privacypolicymodel> getprivacypolicydetials(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("type") String active);

    @GET("forms/cblacklistdetails")
    Call<Blocklist_Model> getcblacklistdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id);

    @GET("block/getblockedvisitorrequests")
    Call<BlockedvisitorrequestsModel> getblockedvisitorrequests(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id, @Query("emp_id") String emp_id);

    @GET("workpermits/getworkpermitDetails")
    Call<WorkPermitModel> getworkpermitDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @POST("workpermits/updateworkpermita")
    Call<WorkPermitModel> updateworkpermita(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("entry/getentrypermitdetails")
    Call<EntryPermitModel> getentrypermitdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @POST("entry/materialcheckin")
    Call<EntryPermitModel> materialcheckin(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("meeting/getmeetingdetails")
    Call<MeetingDetailsModel> getmeetingdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);


}

