package com.oscar.olimpia.Managers;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.oscar.olimpia.Modelo.ErrorData;
import com.oscar.olimpia.Modelo.SuccessData;
import com.oscar.olimpia.Utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 14/06/2017.
 */

public final class VolleyManager {

    private static ErrorData errorData = new ErrorData();
    private static SuccessData successData = new SuccessData();
    private static int statusCode;
    private static EventBus eventBus = EventBus.getDefault();

    //Objeto json
    public static JsonObjectRequest makeRequestJson(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("VolleyManager", response.toString());
                        setSuccessData(response.toString());
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyManagererro", "error");
                        setErrorData(error.getMessage());
                    }
                }

                ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                //headers.put("TOKEN", token); si usa token
                return headers;
            }
        };

        return jsonObjectRequest;

    }

    //Objeto json
    public static JsonObjectRequest makeRequestJsonPOST(String url, JSONObject jsonData){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("VolleyMangerPOST", response.toString());
                        successData.setType(Constants.successType.DATA_LOAD);
                        successData.setMessage(Constants.succesMessage.successLoadData);
                        eventBus.post(successData);
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error activity_login", "massimo");

                        errorData.setType(Constants.errorType.ERROR_DATA_LOAD);
                        errorData.setMessage(error.toString());
                        eventBus.post(errorData);
                    }
                }
        ){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        return jsonObjectRequest;

    }


    //Objeto xml o json
    public static StringRequest makeRequestString(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("VolleyManger", response.toString());
                        setSuccessData(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setErrorData(error.getMessage());
                    }
                }){

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers para xml
                //headers.put("Accept", "text/xml");
                //headers.put("Content-Type", "text/xml");

                //headers para json
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                //headers.put("TOKEN", token); si usa token
                return headers;
            }
        };
        return stringRequest;
    }

    private static void setSuccessData(String response){
        successData.setMessage(response);
        successData.setType(statusCode);
        eventBus.post(successData);
    }

    private static void setErrorData(String response){
        errorData.setMessage(response);
        errorData.setType(Constants.typeResponseWebService.RESPONSE_NOT_OK);
        eventBus.post(errorData);
    }
}
