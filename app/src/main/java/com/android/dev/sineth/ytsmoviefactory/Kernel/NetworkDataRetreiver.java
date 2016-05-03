package com.android.dev.sineth.ytsmoviefactory.Kernel;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.dev.sineth.ytsmoviefactory.Network.VolleySingleton;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

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
