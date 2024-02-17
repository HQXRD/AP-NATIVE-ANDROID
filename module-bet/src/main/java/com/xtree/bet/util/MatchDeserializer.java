package com.xtree.bet.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.MatchPm;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MatchDeserializer implements JsonDeserializer<Match> {
    private Gson gson = new Gson();
    private Map<String, Class<? extends  Match>> sonsType = new HashMap();
    public MatchDeserializer(){
        sonsType.put("MatchFb", MatchFb.class);
        sonsType.put("MatchPm", MatchPm.class);
    }
    @Override
    public Match deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends Match> clazz = this.sonsType.get(jsonObject.get("className").getAsString());
        return gson.fromJson(json, clazz);
    }
}
