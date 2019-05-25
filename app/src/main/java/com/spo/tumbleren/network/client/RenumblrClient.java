package com.spo.tumbleren.network.client;

import com.tumblr.jumblr.JumblrClient;

public class RenumblrClient extends JumblrClient {
    private final static String CONSUMER_KEY = "WLrwCuvsg4lhzcMdelFe9J721o1dDjh2JFgUrJAO3gRuqijP5r";
    private final static String CONSUMER_SECRET = "9wOeOMiAzCEDwEGg9IOWyE57F203zUqSLvHmK9k4CYkOQPlGyn";
    private final static String TOKEN = "2Qccvs361ngpsTbSHACiyyGvltqLGjhzcDO9h8yxzgmnhvZQma";
    private final static String TOKEN_SECRET = "UY7o6icANUwaxNe7Ghu8qznrtqx7OGBofhueKO4jFpTfXsBOsp";
    private static RenumblrClient renumblrClient;

    private RenumblrClient(){
        super(CONSUMER_KEY, CONSUMER_SECRET);
        this.setToken(TOKEN, TOKEN_SECRET);
    }

    public static RenumblrClient getInstance(){
        if(renumblrClient == null){
            renumblrClient = new RenumblrClient();
        }
        return renumblrClient;
    }

    public static void clear(){
        if(renumblrClient != null){
            renumblrClient = null;
        }
    }

}
