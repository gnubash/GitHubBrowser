package com.antondevs.apps.githubbrowser.data.remote;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.SearchResponseEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton.
 */
public class GsonSearchResponseAdapter implements JsonDeserializer<List<UserEntry>>{

    private static final String LOGTAG = GsonSearchResponseAdapter.class.getSimpleName();

    @Override
    public List<UserEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        Log.d(LOGTAG, "deserialize");

        List<UserEntry> users = new ArrayList<>();

        if (json.isJsonArray()) {
            Log.d(LOGTAG, "JsonElement.isJsonArray");
            for (JsonElement element : json.getAsJsonArray()) {
                users.add((UserEntry) context.deserialize(element, UserEntry.class));
            }
        }
        else if (json.isJsonObject()) {
            Log.d(LOGTAG, "JsonElement.isJsonObject");
            SearchResponseEntry searchResponse = context.deserialize(json, SearchResponseEntry.class);
            users = searchResponse.getItems();
//            users.add((UserEntry) context.deserialize(json, UserEntry.class));
        }
        else {
            Log.d(LOGTAG, "JsonElement not known" + json.toString());
            throw new RuntimeException("Unknown json type");
        }

        return users;
    }
}
