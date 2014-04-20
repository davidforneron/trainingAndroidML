package com.meli.trainingml;

import java.util.HashMap;

import com.meli.trainingml.util.HttpClientCustom;

public class MeliService {
												//https://api.mercadolibre.com/sites/MLA/search/
	public static final String MELI_END_POINT ="https://api.mercadolibre.com";
	public static final String SEARCH_ITEMS_END_POINT = MELI_END_POINT + "/sites/MLA/search?q=";
	
	public static String findProducts(HashMap<String, String> params) {
		String product = params.get("product"); 
		return HttpClientCustom.request(SEARCH_ITEMS_END_POINT + product, HttpClientCustom.GET, params);
	}

}
