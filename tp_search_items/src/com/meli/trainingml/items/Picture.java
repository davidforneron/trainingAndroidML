package com.meli.trainingml.items;

public class Picture implements Comparable<Picture>{
	
	String id;
	String url;
	String secureUrl;
	String size;
	String maxSize;
	
	
	 
	public Picture(String id, String url, String secureUrl, String size,
            String maxSize) {
        super();
        this.id = id;
        this.url = url;
        this.secureUrl = secureUrl;
        this.size = size;
        this.maxSize = maxSize;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSecureUrl() {
		return secureUrl;
	}
	public void setSecureUrl(String secureUrl) {
		this.secureUrl = secureUrl;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}
	
    @Override
    public int compareTo(Picture another) {
        String[] anotherArray = another.maxSize.split("x");
        String[] thisArray = maxSize.split("x");
        Integer myres = Integer.valueOf(thisArray[0]) *  Integer.valueOf(thisArray[1]);
        Integer res = Integer.valueOf(anotherArray[0]) *  Integer.valueOf(anotherArray[1]);
        return myres.compareTo(res);
    }

}
