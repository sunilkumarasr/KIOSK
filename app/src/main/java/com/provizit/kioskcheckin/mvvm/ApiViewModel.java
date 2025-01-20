package com.provizit.kioskcheckin.mvvm;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
import com.provizit.kioskcheckin.services.Model;
import com.provizit.kioskcheckin.services.Privacypolicymodel;
import com.provizit.kioskcheckin.services.TvisitorsListModel;
import com.provizit.kioskcheckin.services.VcheckuserModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.utilities.EntryPermit.EntryPermitModel;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkPermitModel;

import org.json.JSONObject;

public class ApiViewModel extends ViewModel {

    private static final String TAG = "ApiViewModel";

    MutableLiveData<VisitorActionModel> verifylinkmobile_response = new MutableLiveData<>();

    MutableLiveData<VisitorActionModel> otpsendemailclient_response = new MutableLiveData<>();

    MutableLiveData<Model> checkinuserloginresponse = new MutableLiveData<>();

    MutableLiveData<CompanyDetailsModel> getuserLDetails_response = new MutableLiveData<>();

    MutableLiveData<GetCVisitorDetailsModel> CVisitor_response = new MutableLiveData<>();
    //EnterYourDetailActivity
    MutableLiveData<GetnationalityModel> getnationality_response = new MutableLiveData<>();

    MutableLiveData<VcheckuserModel> Vcheckusermobile_response = new MutableLiveData<>();

    MutableLiveData<VcheckuserModel> Vcheckuseremail_response = new MutableLiveData<>();

    MutableLiveData<VisitorActionModel> visitorSignup_response = new MutableLiveData<>();

    MutableLiveData<GetdocumentsModel> getdocuments_response = new MutableLiveData<>();

    //NDA_formActivity
    MutableLiveData<GetNdaActiveDetailsModel> getndafdetials_response = new MutableLiveData<>();
    //Privacypolicy
    MutableLiveData<Privacypolicymodel> Getprivacypolicymode_response = new MutableLiveData<>();

    MutableLiveData<VisitorActionModel> responseactionvisitor = new MutableLiveData<>();
    //MeetingRequestActivity
    MutableLiveData<GetpurposesModel> getpurposes_response = new MutableLiveData<>();

    MutableLiveData<GetsubhierarchysModel> Getsubhierarchys_response = new MutableLiveData<>();

    MutableLiveData<TvisitorsListModel> gettvisitors_response = new MutableLiveData<>();

    MutableLiveData<GetSearchEmployeesModel> getsearchemployees_response = new MutableLiveData<>();

    MutableLiveData<CommonModel> getslotDetails_response = new MutableLiveData<>();

    MutableLiveData<CheckinoutModel> getactioncheckinout_response = new MutableLiveData<>();

    MutableLiveData<CommoncheckuserModel> getcommoncheckuser_response = new MutableLiveData<>();

    MutableLiveData<Blocklist_Model> getcblacklistdetails_response = new MutableLiveData<>();

    MutableLiveData<BlockedvisitorrequestsModel> getblockedvisitorrequests_ModelResponse = new MutableLiveData<>();

    MutableLiveData<WorkPermitModel> getworkpermitDetails_response = new MutableLiveData<>();
    MutableLiveData<WorkPermitModel> updateworkpermita_response = new MutableLiveData<>();
    MutableLiveData<EntryPermitModel> getentrypermitdetails_response = new MutableLiveData<>();
    MutableLiveData<EntryPermitModel> materialcheckin_response = new MutableLiveData<>();

    ApiRepository apiRepository;

    public ApiViewModel() {
        apiRepository = new ApiRepository();
    }

    //VisitorLoginActivity
    public void verifylinkmobile(Context context, JSONObject jsonObject) {
        apiRepository.verifylinkmobile(new ApiRepository.VisitorActionResponse() {
            @Override
            public void onResponse(VisitorActionModel visitorActionModel) {
                verifylinkmobile_response.postValue(visitorActionModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: das" + t);
            }

        }, context, jsonObject);
    }
    //login_postmethod
    //VisitorLoginActivity & AdminLoginActivity

    public void checkinuserlogin(Context context, JSONObject jsonObject) {
        apiRepository.checkinuserlogin(new ApiRepository.LoginResponse() {
            @Override
            public void onResponse(Model model) {
                checkinuserloginresponse.postValue(model);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, jsonObject);
    }

    //VisitorLoginActivity
    public void getuserLDetails(Context context, String type) {
        apiRepository.getuserLDetails(new ApiRepository.CompanyDetailsResponse() {
            @Override
            public void onResponse(CompanyDetailsModel companyDetailsModel) {
                getuserLDetails_response.postValue(companyDetailsModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, type);

    }

    //VisitorLoginActivity
    public void getcvisitordetails(Context context, String comp_id, String emp_id, String id, String l_id) {
        apiRepository.getcvisitordetails(new ApiRepository.CVisitorDetailsResponse() {
            @Override
            public void onResponse(GetCVisitorDetailsModel getCVisitorDetailsModel) {
                CVisitor_response.postValue(getCVisitorDetailsModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, comp_id, emp_id, id, l_id);

    }

    //VisitorLoginActivity
    public void otpsendemailclient(Context context, JSONObject jsonObject) {
        apiRepository.otpsendemailclient(new ApiRepository.OtpsendemailResponse() {
            @Override
            public void onResponse(VisitorActionModel visitorActionModel) {
                otpsendemailclient_response.postValue(visitorActionModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, jsonObject);
    }
    // EnterYourDetailsActivity

    public void getnationality(Context context, String doc_id) {
        apiRepository.getnationality(new ApiRepository.NationalityResponse() {
            @Override
            public void onResponse(GetnationalityModel getnationalityModel) {
                getnationality_response.postValue(getnationalityModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, doc_id);
    }

    public void vcheckuserMobile(Context context, String usertype, String val) {
        apiRepository.vcheckuserMobile(new ApiRepository.VcheckuserMobileResponse() {
            @Override
            public void onResponse(VcheckuserModel vcheckuserModel) {
                Vcheckusermobile_response.postValue(vcheckuserModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, usertype, val);
    }

    public void vcheckuserEmail(Context context, String usertype, String val) {
        apiRepository.vcheckuserEmail(new ApiRepository.VcheckuserEmailResponse() {
            @Override
            public void onResponse(VcheckuserModel vcheckuserModel) {
                Vcheckuseremail_response.postValue(vcheckuserModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, usertype, val);
    }

    public void visitorSignup(Context context, JSONObject jsonObject) {
        apiRepository.visitorSignup(new ApiRepository.VisitorSignupResponse() {
            @Override
            public void onResponse(VisitorActionModel visitorActionModel) {
                visitorSignup_response.postValue(visitorActionModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, jsonObject);
    }

    public void getdocuments(Context context) {
        apiRepository.getdocuments(new ApiRepository.SelectedId_listResponse() {
            @Override
            public void onResponse(GetdocumentsModel getdocumentsModel) {
                getdocuments_response.postValue(getdocumentsModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context);
    }

    //NDA_formActivity
    public void getndafdetials(Context context, String id, String type) {
        apiRepository.getndafdetials(new ApiRepository.NdaActiveDetailsResponse() {
            @Override
            public void onResponse(GetNdaActiveDetailsModel getNdaActiveDetailsModel) {

                getndafdetials_response.postValue(getNdaActiveDetailsModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, id, type);
    }

    //Pricacypolicy
    public void getprivacypolicydetials(Context context, String id, String type) {

        apiRepository.getprivacypolicydetials(new ApiRepository.PrivacypolicydetialsResponse() {
            @Override
            public void onResponse(Privacypolicymodel privacypolicymodel) {

                Getprivacypolicymode_response.postValue(privacypolicymodel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, id, type);
    }

    public void actionvisitor(Context context, JSONObject jsonObject, GetCVisitorDetailsModel model) {
        apiRepository.actionvisitor(new ApiRepository.nda_formResponse() {
            @Override
            public void onResponse(VisitorActionModel visitorActionModel) {

                responseactionvisitor.postValue(visitorActionModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, jsonObject, model);

    }

    //Meeting RequestActivity
    public void getpurposes(Context context) {
        apiRepository.getpurposes(new ApiRepository.getpurposesResponse() {
            @Override
            public void onResponse(GetpurposesModel getpurposesModel) {
                getpurposes_response.postValue(getpurposesModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context);
    }

    public void getsubhierarchys(Context context, String indexid, String type) {
        apiRepository.getsubhierarchys(new ApiRepository.GetsubhierarchysResponse() {
            @Override
            public void onResponse(GetsubhierarchysModel getsubhierarchysModel) {
                Getsubhierarchys_response.postValue(getsubhierarchysModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, indexid, type);
    }

    public void gettvisitors(Context context) {
        apiRepository.gettvisitors(new ApiRepository.GetTvisitorsResponse() {
            @Override
            public void onResponse(TvisitorsListModel tvisitorsListModel) {
                gettvisitors_response.postValue(tvisitorsListModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context);
    }

    public void getsearchemployees(Context context, String l_id, String type, String h_id) {
        apiRepository.getsearchemployees(new ApiRepository.SearchEmployeesResponse() {
            @Override
            public void onResponse(GetSearchEmployeesModel getSearchEmployeesModel) {
                getsearchemployees_response.postValue(getSearchEmployeesModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, l_id, type, h_id);

    }

    public void getuserslotdetails(Context context, String id) {
        apiRepository.getuserslotdetails(new ApiRepository.getslotDetailsResponse() {
            @Override
            public void onResponse(CommonModel commonModel) {
                getslotDetails_response.postValue(commonModel);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        }, context, id);

    }

    public void actioncheckinout(Context context, JSONObject jsonObject) {
        apiRepository.actioncheckinout(new ApiRepository.getactioncheckinoutResponse() {
            @Override
            public void onResponse(CheckinoutModel checkinoutModel) {
                getactioncheckinout_response.postValue(checkinoutModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, jsonObject);
    }

    public void commoncheckuser(Context context, String usertype, String val) {
        apiRepository.commoncheckuser(new ApiRepository.getcommoncheckuserResponse() {
            @Override
            public void onResponse(CommoncheckuserModel commoncheckuserModel) {
                getcommoncheckuser_response.postValue(commoncheckuserModel);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, usertype, val);
    }

    //Visitor Blocklist
    public void getcblacklistdetails(Context context, String comp_id) {
        apiRepository.getcblacklistdetails(new ApiRepository.getblocklist_ModelResponse() {
            @Override
            public void onResponse(Blocklist_Model blocklistModel) {
                getcblacklistdetails_response.postValue(blocklistModel);
                System.out.println("result_success::" + "success");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("result_fail::" + "fail");
            }
        }, context, comp_id);
    }

    //Visitor Blocklist
    public void getblockedvisitorrequests(Context context, String comp_id, String emp_id) {
        apiRepository.getblockedvisitorrequests(new ApiRepository.getblockedvisitorrequests_ModelResponse() {
            @Override
            public void onResponse(BlockedvisitorrequestsModel blockedvisitorrequestsModel) {
                getblockedvisitorrequests_ModelResponse.postValue(blockedvisitorrequestsModel);
                System.out.println("result_success::" + "success");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("result_fail::" + "fail");
            }
        }, context, comp_id, emp_id);
    }


    //Work permit Activity
    public void getworkpermitDetails(Context context, String id, String type) {
        apiRepository.getworkpermitDetails(new ApiRepository.getworkpermitDetails_ModelResponse() {
            @Override
            public void onResponse(WorkPermitModel workPermitModel) {
                getworkpermitDetails_response.postValue(workPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id, type);

    }

    //work permit checkIn Status
    public void updateworkpermita(Context context, JsonObject jsonObject) {
        apiRepository.updateworkpermita(new ApiRepository.updateworkpermitaResponse() {
            @Override
            public void onResponse(WorkPermitModel workPermitModel) {
                updateworkpermita_response.postValue(workPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, jsonObject);
    }


    public void getentrypermitdetails(Context context, String id, String type) {
        apiRepository.getentrypermitdetails(new ApiRepository.getentrypermitdetails_ModelResponse() {
            @Override
            public void onResponse(EntryPermitModel entryPermitModel) {
                getentrypermitdetails_response.postValue(entryPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id,type);

    }

    public void materialcheckin(Context context, JsonObject jsonObject) {
        apiRepository.materialcheckin(new ApiRepository.materialcheckin_ModelResponse() {
            @Override
            public void onResponse(EntryPermitModel entryPermitModel) {
                materialcheckin_response.postValue(entryPermitModel);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        }, context, jsonObject);
    }



    public LiveData<VisitorActionModel> getResponseforvisitor() {
        return verifylinkmobile_response;
    }

    public LiveData<VisitorActionModel> getResponseforotpsendemail() {
        return otpsendemailclient_response;
    }

    //Login Response
    public LiveData<Model> getResponsecheckinuserlogin() {
        return checkinuserloginresponse;
    }

    //Company Response
    public LiveData<CompanyDetailsModel> getResponseforcompany() {
        return getuserLDetails_response;
    }

    //Cvisitors Response
    public LiveData<GetCVisitorDetailsModel> getResponseforCVisitor() {
        return CVisitor_response;
    }

    //EnterYourDetailsActivity
    public LiveData<GetnationalityModel> getResponseforNationalty() {
        return getnationality_response;
    }

    public LiveData<VcheckuserModel> getResponseforVcheckuserMobile() {
        return Vcheckusermobile_response;
    }

    public LiveData<VcheckuserModel> getResponseforVcheckuserEmail() {
        return Vcheckuseremail_response;
    }

    public LiveData<VisitorActionModel> getResponseforVisitorSignup() {
        return visitorSignup_response;
    }

    public LiveData<GetdocumentsModel> getResponseforSelectedId_list() {
        return getdocuments_response;
    }

    //NDA_formActivity
    public LiveData<GetNdaActiveDetailsModel> getResponseforNdaActiveDetails() {
        return getndafdetials_response;
    }

    //Privacypolicy
    public LiveData<Privacypolicymodel> getResponseforPrivacypolicyDetails() {

        return Getprivacypolicymode_response;
    }

    public LiveData<VisitorActionModel> getResponseactionvisitor() {
        return responseactionvisitor;
    }

    //MeetingRequestActivity
    public LiveData<GetpurposesModel> getResponseforpurposes() {
        return getpurposes_response;
    }

    public LiveData<GetsubhierarchysModel> getResponsesubhierarchys() {
        return Getsubhierarchys_response;
    }

    public LiveData<TvisitorsListModel> getResponseforTvisitorsList() {
        return gettvisitors_response;
    }

    public LiveData<GetSearchEmployeesModel> getResponseforSearchEmployees() {
        return getsearchemployees_response;
    }

    public LiveData<CommonModel> getResponseforslotDetails() {
        return getslotDetails_response;
    }

    public LiveData<CheckinoutModel> getResponseforactioncheckinout() {
        return getactioncheckinout_response;
    }

    public LiveData<CommoncheckuserModel> getResponsecommoncheckuser() {
        return getcommoncheckuser_response;
    }

    public LiveData<Blocklist_Model> getResponsecblacklistdetails() {
        return getcblacklistdetails_response;
    }
    public LiveData<BlockedvisitorrequestsModel> getResponsevisitorblocklistdetails(){
        return getblockedvisitorrequests_ModelResponse;
    }

    public LiveData<WorkPermitModel> getworkpermitDetails_response(){
        return getworkpermitDetails_response;
    }

    public LiveData<WorkPermitModel> updateworkpermitaDetails_response(){
        return updateworkpermita_response;
    }

    public LiveData<EntryPermitModel> getentrypermitDetails_response(){
        return getentrypermitdetails_response;
    }

    public LiveData<EntryPermitModel> materialcheckin_response(){
        return materialcheckin_response;
    }



}