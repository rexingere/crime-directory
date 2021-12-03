package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
  *  CrimeInfo.java
  *  Purpose: Implement functions to call and process API calls to the FBI most wanted API.
  */

public class CrimeInfo {
    private String url;
    public String resultText;
    ImageView imgCriminal;
    int index;

    /**
     * Constructor for CrimeInfo object. Use to call and format API data
     * @param  index -> corresponds to the investigation number
     * @param  activity -> use the activity to set the layout views
     * @param  context -> use context to obtain the image reference
     */
    CrimeInfo(int index, Activity activity, Context context) {
        imgCriminal = activity.findViewById(R.id.imgCriminal);
        this.index = index;
        switch (index) {
            case 0: // cyber - polyanin
                url = "https://api.fbi.gov/@wanted-person/ec44a773c34a4da694e878a6a51d60de";
                imgCriminal.setImageDrawable(context.getResources().getDrawable(R.drawable.img_polyanin));
                break;
            case 1: // murder - smika
                url = "https://api.fbi.gov/@wanted-person/79538f13-5340-4c9d-b40f-19a311817d85";
                imgCriminal.setImageDrawable(context.getResources().getDrawable(R.drawable.img_thayne_smika));
                break;
            case 2: // fraud - arias
                url = "https://api.fbi.gov/@wanted-person/8ff4f4c1329a40c686d8f553062c2996";
                imgCriminal.setImageDrawable(context.getResources().getDrawable(R.drawable.img_frederick_arias));
                break;
            case 3: // kidnapping - boffman
                url = "https://api.fbi.gov/@wanted-person/e3b085a4a4afa244f718df5dbe8f3106";
                imgCriminal.setImageDrawable(context.getResources().getDrawable(R.drawable.img_lori_boffman));
                break;
        }
    }

    /**
     * Returns a JSONObjectRequest for the information from the FBI API call
     *
     * @param  param  the key for the value required from the request
     * @param  apiResponse  text view that will be set to the text from the parsed JSON response
     * @return JSONObjectRequest for the required parameter
     */
    public JsonObjectRequest getCrimeInfo(String param, TextView apiResponse) {
        List<String> jsonResponses = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String shortTitle = response.get(param).toString();
                    if( (param == "details") ||
                        (param == "caution") ||
                        (param == "remarks")) {

                        shortTitle = shortTitle.replace("<p>", "");
                        shortTitle = shortTitle.replace("</p>", "");
                        apiResponse.setText(shortTitle);

                    } else if (param == "subjects") {
                        JSONArray jsonArray = response.getJSONArray("subjects");
                        apiResponse.setText(jsonArray.get(0).toString());
                    } else if (param == "height") {
                        shortTitle = shortTitle + "\"";
                        apiResponse.setText(shortTitle);
                    } else {
                        shortTitle = shortTitle.replace("IGORYEVICH", "");
                        if (!(shortTitle == "null")) {
                            apiResponse.setText(shortTitle);
                        }
                    }

                    resultText = shortTitle;
                    jsonResponses.add(response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return jsonObjectRequest;
    }

    /**
     * Returns a JSONObject request for the information from the FBI API call
     *
     * @param  param  the key for the value required from the request
     * @param  apiResponse  text view that will be set to the text from the parsed JSON response
     * @return JSONObjectRequest for the required parameter
     */
    public JsonObjectRequest getCrimeInfoArray(String param, TextView apiResponse) {
        List<String> jsonResponses = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String shortTitle = response.get(param).toString();
                    apiResponse.setText(shortTitle.substring(2,shortTitle.length()-2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return jsonObjectRequest;
    }

    /**
     * Set the text fiew of the More Details sections in the CrimeInformationActivity
     * @param  t1, t2, t3, t4  -> TextViews to set the text for
     */
    public void fillTabs(TextView t1, TextView t2, TextView t3, TextView t4) {
        if (index == 0) {
            t1.setText("Details");
            t2.setText("Remarks");
            t3.setText("Charges");
            t4.setText("Subject");
        } else {
            t1.setText("Warning");
            t2.setText("Reward");
            t3.setText("Caution");
            t4.setText("Remarks");
        }
    }
}


