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
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionFb;
import com.xtree.bet.bean.ui.OptionPm;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OptionDeserializer implements JsonDeserializer<Option> {
    private Gson gson = new Gson();
    private Map<String, Class<? extends  Option>> sonsType = new HashMap();
    public OptionDeserializer(){
        sonsType.put("OptionFb", OptionFb.class);
        sonsType.put("OptionPm", OptionPm.class);
    }
    @Override
    public Option deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends Option> clazz = this.sonsType.get(jsonObject.get("className").getAsString());
        return gson.fromJson(json, clazz);
    }
}
