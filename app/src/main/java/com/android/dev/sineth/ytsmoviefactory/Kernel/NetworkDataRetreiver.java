package com.android.dev.sineth.ytsmoviefactory.Kernel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.dev.sineth.ytsmoviefactory.Network.VolleySingleton;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.View.YTSApplication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.dev.sineth.ytsmoviefactory.Kernel.Core.token;

/**This class handles all the network based data retrieval services
 * Created by Sineth on 4/4/2016.
 */
public class NetworkDataRetreiver {

    private final RequestQueue requestQueue;
    private MaterialDialog progressBar;

    public NetworkDataRetreiver() {
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }
    public JSONObject sendJSONRequest(String url, final Context context){
        final JSONObject[] jsonObject = new JSONObject[1];
        showProgressDialog(context);
        JsonObjectRequest request=new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonObject[0] =response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorDialog(context, context.getResources().getString(R.string.errorTitle),context.getResources().getString(R.string.errorContent));
            }
        });
        requestQueue.add(request);
        return null;
    }
    public Boolean sendJSONRequestHeader(String url, final Context context){
        final Boolean[] aBoolean = new Boolean[1];
        final JSONObject[] jsonObject = new JSONObject[1];
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonObject[0] =response;
                try {
                    token= (String) response.get("token");
                    if (token!=null){
                        aBoolean[0] =true;
                        Toast.makeText(YTSApplication.getAppContext(),"Login success",Toast.LENGTH_SHORT).show();
                    }else {
                        aBoolean[0] =false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorDialog(context, context.getResources().getString(R.string.errorTitle),context.getResources().getString(R.string.errorContent)+error.toString());
            }
        });
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        //mTextView.setText("Response is: "+ response.substring(0,500));
//                        Toast.makeText(YTSApplication.getAppContext(),response.substring(0,500),Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(YTSApplication.getAppContext(),error.toString(),Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params =  super.getHeaders();
//                if(params==null)params = new HashMap<>();
//                params.put("email","sin@gmail.com");
//                params.put("password","123456789");
//                //..add other headers
//                return params;
//            }
//        };
        requestQueue.add(request);
        return aBoolean[0];
    }
    private void showProgressDialog(final Context context){

        progressBar = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog_fetch)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

    private void showErrorDialog(final Context context, String title, String content){
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(R.string.retry)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sendJSONRequest(Core.getBaseURL(),context);
                        dialog.dismiss();
                        progressBar.dismiss();
                    }
                })
                .negativeText(R.string.dismiss)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        progressBar.dismiss();
                    }
                })
                .show();
    }
}
