package com.vatrates;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;

public class Main {
    private static String vatUrl = "http://jsonvat.com/";
    private static String KEY_NAME = "$.periods[0].rates.standard";

    public static void main(String[] args){
        VatRates vatRates = new VatRates(vatUrl);
        JSONArray vatRatesJsonArray = vatRates.getVatRates();
        JSONArray vatRatesJsonArraySorted = vatRates.sortVatRatesByKey(vatRatesJsonArray, KEY_NAME);
        System.out.println("Countries with highest Standard VAT: "+JsonPath.read(vatRatesJsonArraySorted.toString(), "$[-3:].country_code"));
        System.out.println("Countries with lowest Standard VAT: "+JsonPath.read(vatRatesJsonArraySorted.toString(), "$[:3].country_code"));
    }
}
