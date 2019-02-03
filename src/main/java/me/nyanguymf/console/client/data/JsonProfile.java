/**
 * Profile.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.data;

import java.util.Map;
import java.util.TreeMap;

import io.bluecube.thunderbolt.org.json.JSONObject;

/**
 * @author nyanguymf
 *
 */
class JsonProfile implements Profile {
    private JSONObject profileObj;

    private JSONObject connectionObj;

    /**
     * server : Sever address
     * user : User's name
     * password : Connection password
     */
    private Map<String, String> connectionSettings;

    private String name;

    public JsonProfile(JSONObject profileObj, String name) {
        this.profileObj = profileObj;

        this.connectionObj = profileObj.getJSONObject("connection");

        connectionSettings = new TreeMap<>();

        String username = connectionObj.getString("username");
        String host     = connectionObj.getString("host");
        String password = connectionObj.getString("password");
        int    port     = connectionObj.getInt("port");

        connectionSettings.put("username", username);
        connectionSettings.put("host", host);
        connectionSettings.put("password", password);
        connectionSettings.put("port", String.valueOf(port));
    }

    @Override
    public String toString() {
        return profileObj.toString();
    }
    
    @Override
    public String getUserName() {
        return this.connectionSettings.get("username");
    }

    @Override
    public String getHost() {
        return this.connectionSettings.get("host");
    }

    @Override
    public String getPassword() {
        return this.connectionSettings.get("password");
    }

    @Override
    public int getPort() {
        return Integer.parseInt(this.connectionSettings.get("port"));
    }

    @Override
    public String getName() {
        return this.name;
    }
}
