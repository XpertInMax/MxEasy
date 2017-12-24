/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gaf.android.mxeasy.utilities;

import android.net.Uri;
import android.text.LoginFilter;
import android.util.Log;

import com.gaf.android.mxeasy.maximoobjects.PO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getName();

    final static String MAXIMO_BASE_URL =
            "https://maximo-demo76.mro.com/maximo/oslc/os/mxpo?lean=1";

    final static String PARAM_QUERY = "oslc.where";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "oslc.orderBy";
    final static String sortBy = "-ponum";

    /**
     * Builds the URL used to query Github.
     *
     * @param maximoSearchQueryParam The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String maximoSearchQueryParam) {
        // TODO (1) Fill in this method to build the proper Github query URL
        String maximoSearchQuery = null;
        if(maximoSearchQueryParam != null && !maximoSearchQueryParam.equals("")){
            maximoSearchQuery = "siteid=\"BEDFORD\" and status=\"WAPPR\" and ponum=\"" + maximoSearchQueryParam + "\"";
        }else{
            maximoSearchQuery = "siteid=\"BEDFORD\" and status=\"WAPPR\"";
        }
        Log.d(LOG_TAG, maximoSearchQuery );
        //http://totmaxfedev01.corp.gaf.com/maximo/oslc/os/mxpo?lean=1&oslc.where=siteid=%22MY%22%20and%20status=%22WAPPR%22%20and%20ponum=%22204994%22
        //http://totmaxfedev01.corp.gaf.com/maximo/oslc/os/mxpo?lean=1&oslc.where=siteid=%22MY%22%20and%20status=%22WAPPR%22&oslc.orderBy=-ponum
        //http://totmaxfedev01.corp.gaf.com/maximo/oslc/os/mxpo?lean=1&%26oslc.where=siteid%3D%2522MY%2522%2520and%2520status%3D%2522WAPPR%2522&oslc.orderBy=-location
        //https://maximo-demo76.mro.com/maximo/oslc/os/mxpo?lean=1&oslc.where=siteid=%22BEDFORD%22%20and%20status=%22WAPPR%22
        Uri builtUri = Uri.parse(MAXIMO_BASE_URL).buildUpon().appendQueryParameter(PARAM_QUERY, maximoSearchQuery).appendQueryParameter(PARAM_SORT, sortBy).build();

        URL url = createUrl(builtUri);

        return url;
    }

    public static ArrayList<PO> fetchMaximoObjects(String maximoObjects){
        ArrayList<PO> maximoObjectList = new ArrayList<>();
        URL url = null;
        String jsonMaximoObjectDetails = null;

        try {
            JSONObject baseJsonResponse = new JSONObject(maximoObjects);
            JSONArray maximoObjectArray = baseJsonResponse.getJSONArray("member");

            for (int i = 0; i < maximoObjectArray.length(); i++){
                String href =  maximoObjectArray.getJSONObject(i).getString("href").replace("http://localhost","https://maximo-demo76.mro.com");
                Log.d(LOG_TAG, href);
                url = createUrl(Uri.parse(href));

                jsonMaximoObjectDetails =  makeHttpRequest(url);
                JSONObject jsonMaximoObjectDetailsResponse = new JSONObject(jsonMaximoObjectDetails);

                PO newPO = new PO(
                        jsonMaximoObjectDetailsResponse.getString("spi:ponum"),
                        jsonMaximoObjectDetailsResponse.getString("spi:description"),
                        jsonMaximoObjectDetailsResponse.getString("spi:status"),
                        jsonMaximoObjectDetailsResponse.getString("spi:revisionnum"),
                        jsonMaximoObjectDetailsResponse.getString("spi:purchaseagent"),
                        jsonMaximoObjectDetailsResponse.getDouble("spi:totalcost"));

                Log.d(LOG_TAG, newPO.toString());
                maximoObjectList.add(newPO);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Maximo Object Size " + maximoObjectList.size());
        return maximoObjectList;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static ArrayList<PO> getResponseFromHttpUrl(URL url) throws IOException {
        String jsonMaximoRecords ;
        ArrayList<PO> maximoObjectList = new ArrayList<>();;
        try {
            jsonMaximoRecords = makeHttpRequest(url);
            Log.d(LOG_TAG, "Json String " + jsonMaximoRecords);
            maximoObjectList = fetchMaximoObjects(jsonMaximoRecords);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return maximoObjectList;
    }

    private static URL createUrl(Uri uri){
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("NetworkUtils", "URL " + uri.toString());
        return url;
    }


    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("MAXAUTH","bWF4aW1vOnRpbWVzMQ==");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response COde " + httpURLConnection.getResponseCode() );
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving Maximo JSON results");
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }
}