package com.xtree.bet.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionFb;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.OptionListFb;
import com.xtree.bet.bean.ui.OptionListPm;
import com.xtree.bet.bean.ui.OptionPm;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OptionListDeserializer implements JsonDeserializer<OptionList> {
    private Gson gson = new Gson();
    private Map<String, Class<? extends  OptionList>> sonsType = new HashMap();
    public OptionListDeserializer(){
        sonsType.put("OptionListFb", OptionListFb.class);
        sonsType.put("OptionListPm", OptionListPm.class);
    }
    @Override
    public OptionList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends OptionList> clazz = this.sonsType.get(jsonObject.get("className").getAsString());
        return gson.fromJson(json, clazz);
    }
}
