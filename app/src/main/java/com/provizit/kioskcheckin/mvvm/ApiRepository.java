package com.provizit.kioskcheckin.mvvm;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.provizit.kioskcheckin.services.BlockedvisitorrequestsModel;
import com.provizit.kioskcheckin.services.Blocklist_Model;
import com.provizit.kioskcheckin.services.CheckinoutModel;
import com.provizit.kioskcheckin.services.CommonModel;
import com.provizit.kioskcheckin.services.CommoncheckuserModel;
import com.provizit.kioskcheckin.services.CompanyDetailsModel;
import com.provizit.kioskcheckin.api.DataManger;
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
import com.provizit.kioskcheckin.utilities.EntryPermit.EntryPermitModel;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkPermitModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private static final String TAG = "ApiRepository";


    public void verifylinkmobile(VisitorActionResponse logresponse, Context context, JSONObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.verifylinkmobile(new Callback<VisitorActionModel>() {
            @Override
            public void onResponse(Call<VisitorActionModel> call, Response<VisitorActionModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());
                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VisitorActionModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }

    public void otpsendemailclient(OtpsendemailResponse logresponse, Context context, JSONObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.otpsendemailclient(new Callback<VisitorActionModel>() {
            @Override
            public void onResponse(Call<VisitorActionModel> call, Response<VisitorActionModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VisitorActionModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }

    public void checkinuserlogin(LoginResponse logresponse, Context context, JSONObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.checkinuserlogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }

    public void getuserLDetails(CompanyDetailsResponse logresponse, Context context, String type) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getuserLDetails(new Callback<CompanyDetailsModel>() {
            @Override
            public void onResponse(Call<CompanyDetailsModel> call, Response<CompanyDetailsModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<CompanyDetailsModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, type);
    }

    public void getcvisitordetails(CVisitorDetailsResponse logresponse, Context context, String comp_id, String emp_id, String id, String l_id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getcvisitordetails(new Callback<GetCVisitorDetailsModel>() {
            @Override
            public void onResponse(Call<GetCVisitorDetailsModel> call, Response<GetCVisitorDetailsModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp: " + "checksetup0");

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                    Log.e(TAG, "onResp: " + "checksetup1");
                }
            }

            @Override
            public void onFailure(Call<GetCVisitorDetailsModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
                Log.e(TAG, "onResp: " + "getcvisitorchecksetup2");
            }
        }, context, comp_id, emp_id, id, l_id);
    }
// EnterYourDetailsActivity

    public void getnationality(NationalityResponse logresponse, Context context, String doc_id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getnationality(new Callback<GetnationalityModel>() {
            @Override
            public void onResponse(Call<GetnationalityModel> call, Response<GetnationalityModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetnationalityModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, doc_id);
    }

    public void vcheckuserMobile(VcheckuserMobileResponse logresponse, Context context, String usertype, String val) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.vcheckuser(new Callback<VcheckuserModel>() {
            @Override
            public void onResponse(Call<VcheckuserModel> call, Response<VcheckuserModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VcheckuserModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, usertype, val);
    }

    public void vcheckuserEmail(VcheckuserEmailResponse logresponse, Context context, String usertype, String val) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.vcheckuser(new Callback<VcheckuserModel>() {
            @Override
            public void onResponse(Call<VcheckuserModel> call, Response<VcheckuserModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VcheckuserModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, usertype, val);
    }

    public void visitorSignup(VisitorSignupResponse logresponse, Context context, JSONObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.visitorSignup(new Callback<VisitorActionModel>() {
            @Override
            public void onResponse(Call<VisitorActionModel> call, Response<VisitorActionModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VisitorActionModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }

    public void getdocuments(SelectedId_listResponse logresponse, Context context) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getdocuments(new Callback<GetdocumentsModel>() {
            @Override
            public void onResponse(Call<GetdocumentsModel> call, Response<GetdocumentsModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetdocumentsModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context);
    }

    //NDA_FormActivity
    public void getndafdetials(NdaActiveDetailsResponse logresponse, Context context, String id, String type) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getndafdetials(new Callback<GetNdaActiveDetailsModel>() {
            @Override
            public void onResponse(Call<GetNdaActiveDetailsModel> call, Response<GetNdaActiveDetailsModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetNdaActiveDetailsModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, id, type);
    }


    //Pricicypolicy

    public void getprivacypolicydetials(PrivacypolicydetialsResponse logresponse, Context context, String id, String type) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getprivacypolicydetials(new Callback<Privacypolicymodel>() {
            @Override
            public void onResponse(Call<Privacypolicymodel> call, Response<Privacypolicymodel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Privacypolicymodel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, id, type);
    }


    public void actionvisitor(nda_formResponse logresponse, Context context, JSONObject jsonObject, GetCVisitorDetailsModel model) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.actionvisitor(new Callback<VisitorActionModel>() {
            @Override
            public void onResponse(Call<VisitorActionModel> call, Response<VisitorActionModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VisitorActionModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject, model);
    }

//MeetingRequestActivity

    public void getpurposes(getpurposesResponse logresponse, Context context) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getpurposes(new Callback<GetpurposesModel>() {
            @Override
            public void onResponse(Call<GetpurposesModel> call, Response<GetpurposesModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetpurposesModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context);
    }

    public void getsubhierarchys(GetsubhierarchysResponse logresponse, Context context, String indexid, String type) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getsubhierarchys(new Callback<GetsubhierarchysModel>() {
            @Override
            public void onResponse(Call<GetsubhierarchysModel> call, Response<GetsubhierarchysModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetsubhierarchysModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, indexid, type);
    }

    public void gettvisitors(GetTvisitorsResponse logresponse, Context context) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.gettvisitors(new Callback<TvisitorsListModel>() {
            @Override
            public void onResponse(Call<TvisitorsListModel> call, Response<TvisitorsListModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<TvisitorsListModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context);
    }

    public void getsearchemployees(SearchEmployeesResponse logresponse, Context context, String l_id, String type, String h_id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getsearchemployees(new Callback<GetSearchEmployeesModel>() {
            @Override
            public void onResponse(Call<GetSearchEmployeesModel> call, Response<GetSearchEmployeesModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetSearchEmployeesModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, l_id, type, h_id);
    }

    public void getuserslotdetails(getslotDetailsResponse logresponse, Context context, String id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getuserslotdetails(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, id);
    }

    public void actioncheckinout(getactioncheckinoutResponse logresponse, Context context, JSONObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.actioncheckinout(new Callback<CheckinoutModel>() {
            @Override
            public void onResponse(Call<CheckinoutModel> call, Response<CheckinoutModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<CheckinoutModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }

    public void commoncheckuser(getcommoncheckuserResponse logresponse, Context context, String usertype, String val) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.commoncheckuser(new Callback<CommoncheckuserModel>() {
            @Override
            public void onResponse(Call<CommoncheckuserModel> call, Response<CommoncheckuserModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<CommoncheckuserModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, usertype, val);
    }

    //Visitor Blocklist
    public void getcblacklistdetails(getblocklist_ModelResponse logresponse, Context context, String comp_id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getcblacklistdetails(new Callback<Blocklist_Model>() {
            @Override
            public void onResponse(Call<Blocklist_Model> call, Response<Blocklist_Model> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Blocklist_Model> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, comp_id);
    }

    //Visitor Blocklist
    public void getblockedvisitorrequests(getblockedvisitorrequests_ModelResponse logresponse, Context context, String comp_id, String emp_id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getblockedvisitorrequests(new Callback<BlockedvisitorrequestsModel>() {
            @Override
            public void onResponse(Call<BlockedvisitorrequestsModel> call, Response<BlockedvisitorrequestsModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<BlockedvisitorrequestsModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, comp_id, emp_id);
    }

    //work permit
    public void getworkpermitDetails(getworkpermitDetails_ModelResponse logresponse, Context context, String id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getworkpermitDetails(new Callback<WorkPermitModel>() {
            @Override
            public void onResponse(Call<WorkPermitModel> call, Response<WorkPermitModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());
                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<WorkPermitModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, id);
    }

    //work permit checkIn Status
    public void updateworkpermita(updateworkpermitaResponse logresponse, Context context, JsonObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.updateworkpermita(new Callback<WorkPermitModel>() {
            @Override
            public void onResponse(Call<WorkPermitModel> call, Response<WorkPermitModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<WorkPermitModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }

    public void getentrypermitdetails(getentrypermitdetails_ModelResponse logresponse, Context context, String id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getentrypermitdetails(new Callback<EntryPermitModel>() {
            @Override
            public void onResponse(Call<EntryPermitModel> call, Response<EntryPermitModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());
                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<EntryPermitModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, id);
    }

    public void getmeetingdetails(getmeetingdetails_ModelResponse logresponse, Context context, String id) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.getmeetingdetails(new Callback<MeetingDetailsModel>() {
            @Override
            public void onResponse(Call<MeetingDetailsModel> call, Response<MeetingDetailsModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());
                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<MeetingDetailsModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, id);
    }




    //work permit checkIn Status
    public void materialcheckin(materialcheckin_ModelResponse logresponse, Context context, JsonObject jsonObject) {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.materialcheckin(new Callback<EntryPermitModel>() {
            @Override
            public void onResponse(Call<EntryPermitModel> call, Response<EntryPermitModel> response) {
                if (response.isSuccessful()) {
                    logresponse.onResponse(response.body());

                } else {
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<EntryPermitModel> call, Throwable t) {
                logresponse.onFailure(new Throwable(t));
            }
        }, context, jsonObject);
    }


    public interface VisitorActionResponse {
        void onResponse(VisitorActionModel visitorActionModel);

        void onFailure(Throwable t);
    }

    public interface OtpsendemailResponse {
        void onResponse(VisitorActionModel visitorActionModel);

        void onFailure(Throwable t);
    }

    public interface LoginResponse {
        void onResponse(Model model);

        void onFailure(Throwable t);
    }

    public interface CompanyDetailsResponse {
        void onResponse(CompanyDetailsModel companyDetailsModel);

        void onFailure(Throwable t);
    }

    public interface CVisitorDetailsResponse {
        void onResponse(GetCVisitorDetailsModel getCVisitorDetailsModel);

        void onFailure(Throwable t);
    }

    //EnterYourDetailsActivity

    public interface NationalityResponse {
        void onResponse(GetnationalityModel getnationalityModel);

        void onFailure(Throwable t);
    }

    public interface VcheckuserMobileResponse {
        void onResponse(VcheckuserModel vcheckuserModel);

        void onFailure(Throwable t);
    }

    public interface VcheckuserEmailResponse {
        void onResponse(VcheckuserModel vcheckuserModel);

        void onFailure(Throwable t);
    }

    public interface VisitorSignupResponse {
        void onResponse(VisitorActionModel visitorActionModel);

        void onFailure(Throwable t);
    }

    public interface SelectedId_listResponse {
        void onResponse(GetdocumentsModel getdocumentsModel);

        void onFailure(Throwable t);
    }

    //NDA_FormActivity
    public interface NdaActiveDetailsResponse {
        void onResponse(GetNdaActiveDetailsModel getNdaActiveDetailsModel);

        void onFailure(Throwable t);
    }

    //Privacypolicy
    public interface PrivacypolicydetialsResponse {
        void onResponse(Privacypolicymodel privacypolicymodel);

        void onFailure(Throwable t);
    }

    public interface nda_formResponse {
        void onResponse(VisitorActionModel visitorActionModel);

        void onFailure(Throwable t);
    }
    //MeetingRequestActivity

    public interface getpurposesResponse {
        void onResponse(GetpurposesModel getpurposesModel);

        void onFailure(Throwable t);
    }

    public interface GetsubhierarchysResponse {
        void onResponse(GetsubhierarchysModel getsubhierarchysModel);

        void onFailure(Throwable t);
    }

    public interface GetTvisitorsResponse {
        void onResponse(TvisitorsListModel tvisitorsListModel);

        void onFailure(Throwable t);
    }

    public interface SearchEmployeesResponse {
        void onResponse(GetSearchEmployeesModel getSearchEmployeesModel);

        void onFailure(Throwable t);
    }

    public interface getslotDetailsResponse {
        void onResponse(CommonModel commonModel);

        void onFailure(Throwable t);
    }

    public interface getactioncheckinoutResponse {
        void onResponse(CheckinoutModel checkinoutModel);

        void onFailure(Throwable t);
    }

    public interface getcommoncheckuserResponse {
        void onResponse(CommoncheckuserModel commoncheckuserModel);

        void onFailure(Throwable t);
    }

    public interface getblocklist_ModelResponse {
        void onResponse(Blocklist_Model blocklistModel);

        void onFailure(Throwable t);
    }

    public interface getblockedvisitorrequests_ModelResponse {
        void onResponse(BlockedvisitorrequestsModel BlockedvisitorrequestsModel);

        void onFailure(Throwable t);
    }


    public interface getworkpermitDetails_ModelResponse {
        void onResponse(WorkPermitModel workPermitModel);

        void onFailure(Throwable t);
    }

    public interface updateworkpermitaResponse {
        void onResponse(WorkPermitModel workPermitModel);

        void onFailure(Throwable t);
    }

    public interface getentrypermitdetails_ModelResponse {
        void onResponse(EntryPermitModel entryPermitModel);

        void onFailure(Throwable t);
    }

    public interface materialcheckin_ModelResponse {
        void onResponse(EntryPermitModel entryPermitModel);

        void onFailure(Throwable t);
    }

    public interface getmeetingdetails_ModelResponse {
        void onResponse(MeetingDetailsModel meetingDetailsModel);

        void onFailure(Throwable t);
    }


}


