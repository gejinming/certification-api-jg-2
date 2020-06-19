package cn.jpush.api.push.model;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface PushModel extends Serializable {

    public static Gson gson = new Gson();
    public JsonElement toJSON();
    
}
