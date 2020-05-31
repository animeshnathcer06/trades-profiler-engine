package com.anim.tradeprofiler.tradeproducer.loader;

import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TradeDetailsLoader {

    private static final Gson gson = new Gson();
    public TradeDetails load(String json) {
        return gson.fromJson(json, TradeDetails.class);
    }

    public List<TradeDetails> loadBulk(List<String> jsonList) {
        List<TradeDetails> tradeDetailsList = new ArrayList<>();
        for (String json : jsonList) {
            tradeDetailsList.add(gson.fromJson(json, TradeDetails.class));
        }

        return tradeDetailsList;
    }
}
