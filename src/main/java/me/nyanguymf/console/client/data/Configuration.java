/**
 * Configuration.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONObject;
import me.nyanguymf.console.client.ConsoleClient;

/**
 * @author nyanguymf
 */
public class Configuration {
    private ThunderFile config;
    private Map<String, Profile> profiles;

    public Configuration(String config) {
        File configFile = new File(config);
        String path = new File(".").getAbsolutePath();

        if (!configFile.exists()) {
            InputStream input = getConfigAsStream(config);
            try {
                configFile.createNewFile();
                Files.copy(input, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Couldn't save default config.");
                ConsoleClient.exit(ConsoleClient.CONFIG_SAVE_ERROR);
            }
        }

        if (path == null) {
            JOptionPane.showMessageDialog(null, "Couldn't get path of configuration file."
                                                + "\nError code: "
                                                + ConsoleClient.CONFIG_LOAD_ERROR
                                                , null, JOptionPane.ERROR_MESSAGE);

            ConsoleClient.exit(ConsoleClient.CONFIG_LOAD_ERROR);
        }

        try {
            // TODO: wait till developer of Thunderbolt will fix regexp.
            this.config = Thunderbolt.load(config.contains(".")
                                            ? config.split("\\.")[0]
                                            : config
                                            , path);
        } catch (FileLoadException e) {
            e.printStackTrace();
            ConsoleClient.exit(ConsoleClient.CONFIG_LOAD_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            ConsoleClient.exit(ConsoleClient.CONFIG_LOAD_ERROR);
        }

        profiles = new TreeMap<>();

        JSONObject profilesObj = (JSONObject) this.config.get("profiles");

        Arrays.stream(JSONObject.getNames(profilesObj)).forEach((profile) -> {
            JSONObject profileObj = profilesObj.getJSONObject(profile);
            profiles.put(profile, new JsonProfile(profileObj, profile));
        });
    }

    public Profile getProfile(String profile) {
        return profiles.get(profile);
    }

    public void unload(String config) {
        Thunderbolt.unload(config);
    }

    private InputStream getConfigAsStream(String config) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
    }
}
