package com.vatrates;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import static com.jayway.restassured.RestAssured.given;

public class VatRates {
    private String vatRatesUrl;

    public VatRates(String url){
        this.vatRatesUrl=url;
    }

    /**
     * Gets vat rates from the json response and return them in a JSONArray
     * @return JSONArray
     */
    public JSONArray getVatRates(){
        Response response =
                given().contentType(ContentType.JSON).
                        when().get(this.vatRatesUrl).
                        then().statusCode(200).
                        extract().response();
        JSONObject vatRates = new JSONObject(response.getBody().asString());
        JSONArray vatRatesArray = (JSONArray) vatRates.get("rates");
        return vatRatesArray;
    }

    /**
     * Sorts the JSONArray by the provided key
     * @param vatRatesArray
     * @param key
     * @return JSONArray
     */
    public JSONArray sortVatRatesByKey(JSONArray vatRatesArray, final String key){
        JSONArray vatRatesArraySorted = new JSONArray();

        //convert JSONArray to a list of JSONObjects
        List<JSONObject> vatRatesAsList = new ArrayList<>();
        for (int i = 0; i < vatRatesArray.length(); i++) {
            vatRatesAsList.add(vatRatesArray.getJSONObject(i));
        }

        // sort the list of JSONObjects by key
        Collections.sort( vatRatesAsList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                int compare = 0;
                try
                {
                    int valA = JsonPath.read(a.toString(), key);
                    int valB = JsonPath.read(b.toString(), key);
                    compare = Integer.compare(valA, valB);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                return compare;
            }
        });

        //convert the list back to JSONArray and return it
        for (int i = 0; i < vatRatesArray.length(); i++) {
            vatRatesArraySorted.put(vatRatesAsList.get(i));
        }
        return vatRatesArraySorted;

    }
}
