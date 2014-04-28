package com.meli.trainingml.util;

import java.util.HashMap;

public class MeliService {

	public static final String MELI_END_POINT ="https://api.mercadolibre.com";
	public static final String SEARCH_ITEMS_END_POINT = MELI_END_POINT + "/sites/MLA/search";
	
	public static String findProducts(HashMap<String, String> params) {
		return HttpClientCustom.request(SEARCH_ITEMS_END_POINT, HttpClientCustom.GET, params);
	}

}
