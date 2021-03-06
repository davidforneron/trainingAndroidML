package com.meli.trainingml.util;

import java.util.HashMap;

public class MeliService {
    private static final String MELI_END_POINT ="https://api.mercadolibre.com";
    public static final String SEARCH_ITEMS_END_POINT = MELI_END_POINT + "/sites/MLA/search";

    public static final String ITEMS_END_POINT = MELI_END_POINT+ "/items";

    public static String findProducts(HashMap<String, String> params) {
        return HttpClientCustom.request(SEARCH_ITEMS_END_POINT, HttpClientCustom.GET, params);
    }

    public static String findItem(HashMap<String, String> params) {
        return HttpClientCustom.request(ITEMS_END_POINT, HttpClientCustom.GET, params);
    }

    public static String find(String url, HashMap<String, String> params) {
        return HttpClientCustom.request(url, HttpClientCustom.GET, params);
    }
}
