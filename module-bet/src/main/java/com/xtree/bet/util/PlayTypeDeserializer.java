package com.xtree.bet.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypeFb;
import com.xtree.bet.bean.ui.PlayTypePm;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlayTypeDeserializer implements JsonDeserializer<PlayType> {
    private Gson gson = new Gson();
    private Map<String, Class<? extends  PlayType>> sonsType = new HashMap();
    public PlayTypeDeserializer(){
        sonsType.put("PlayTypeFb", PlayTypeFb.class);
        sonsType.put("PlayTypePm", PlayTypePm.class);
    }
    @Override
    public PlayType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends PlayType> clazz = this.sonsType.get(jsonObject.get("className").getAsString());
        return gson.fromJson(json, clazz);
    }
}
