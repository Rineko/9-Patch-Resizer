/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright 2013 Redwarp
 */
package net.redwarp.tool.resizer.worker;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScreenDensity {
    private static final String KEY_DENSITIES_ANDROID = "densitiesAndroid";
    private static final String KEY_DENSITIES_IOS = "densitiesiOS";
    private static final String KEY_INPUT_DENSITIES = "inputDensities";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_ENABLE_ASSETS = "exportiOSAssets";
    public static final String DENSITIES_PATHNAME = System.getProperty("user.home") + "/.resizer";
    private float scale;
    private String name;
    private String os;

    private boolean active;

    private static List<String> inputDensities = null;
    private static List<ScreenDensity> listAndroid = null;
    private static List<ScreenDensity> listiOS = null;
    private static float defaultInputDensity = 4f;
    private static boolean isSaveiOSAssetsEnabled = true;

    static {
        load();
    }

    private static void load() {
        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            InputStream preferenceStream;
            try {
            	File folder = new File(DENSITIES_PATHNAME);
            	if (!folder.exists()) {
            		folder.mkdirs();
            	}
            	File file = new File(folder, "densities.json");
                preferenceStream = new FileInputStream(file);
            } catch (Exception e) {
                preferenceStream = ScreenDensity.class.getClassLoader().getResourceAsStream("misc/densities.json");
            }
            JsonObject densitiesObject = parser.parse(new InputStreamReader(preferenceStream)).getAsJsonObject();
            JsonArray inputDensitiesArray = densitiesObject.get(KEY_INPUT_DENSITIES).getAsJsonArray();
            JsonArray densitiesAndroidArray = densitiesObject.get(KEY_DENSITIES_ANDROID).getAsJsonArray();
            JsonArray densitiesiOSArray = densitiesObject.get(KEY_DENSITIES_IOS).getAsJsonArray();

            Type inputType = new TypeToken<List<String>>() {
            }.getType();
            Type listType = new TypeToken<List<ScreenDensity>>() {
            }.getType();
            inputDensities = gson.fromJson(inputDensitiesArray, inputType);
            listAndroid = gson.fromJson(densitiesAndroidArray, listType);
            listiOS = gson.fromJson(densitiesiOSArray, listType);
            String defaultDensityName = densitiesObject.get(KEY_SOURCE).getAsString();
            defaultInputDensity = Float.parseFloat(defaultDensityName.replace("x", ""));
            isSaveiOSAssetsEnabled = densitiesObject.get(KEY_ENABLE_ASSETS).getAsBoolean();
            
        } catch (Exception e) {
        	e.printStackTrace();
            listAndroid = new ArrayList<ScreenDensity>();
            listiOS = new ArrayList<ScreenDensity>();
            //default values
            inputDensities.add("4x");
            listAndroid.add(new ScreenDensity("xxxhdpi", 4.0f, true));
            listAndroid.add(new ScreenDensity("xxhdpi", 3.0f, true));
            listAndroid.add(new ScreenDensity("xhdpi", 2.0f, true));
            listAndroid.add(new ScreenDensity("hdpi", 1.5f, true));
            listAndroid.add(new ScreenDensity("mdpi", 1.0f, true));
            listiOS.add(new ScreenDensity("@3x", 3.0f, true));
            listiOS.add(new ScreenDensity("@2x", 2.0f, true));
            listiOS.add(new ScreenDensity("@1x", 1.0f, true));
            defaultInputDensity = 4.0f;
            isSaveiOSAssetsEnabled = true;
        }
    }

    public static void save(JButton saveButton) {
        saveButton.setEnabled(false);
        JsonObject rootObject = new JsonObject();
        // Save source
        rootObject.addProperty(KEY_SOURCE, getDefaultInputDensity());

        // Save densities
        Gson gson = new Gson();
        Type listOfDensityType = new TypeToken<List<ScreenDensity>>() {
        }.getType();
        JsonElement inputDens = gson.toJsonTree(inputDensities, listOfDensityType);
        rootObject.add(KEY_INPUT_DENSITIES, inputDens);
        JsonElement densitiesAndroid = gson.toJsonTree(listAndroid, listOfDensityType);
        rootObject.add(KEY_DENSITIES_ANDROID, densitiesAndroid);
        JsonElement densitiesiOS = gson.toJsonTree(listiOS, listOfDensityType);
        rootObject.add(KEY_DENSITIES_IOS, densitiesiOS);
        
        //Assets
        rootObject.addProperty(KEY_ENABLE_ASSETS, isSaveiOSAssetsEnabled());

        SaveWorker worker = new SaveWorker(saveButton, rootObject.toString());
        worker.execute();
    }

    private ScreenDensity(String name, float density, boolean active) {
        this.scale = density;
        this.name = name;
        this.active = active;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public String getOs() {
        return this.os;
    }

    public float getScale() {
        return this.scale;
    }

    public static String[] getSupportedInputDensities() {
    	String[] s = new String[inputDensities.size()];
    	int i = 0;
    	for	(String id : inputDensities) {
    		s[i] = id;
    		i++;
    	}
        return s;
    }

    public static List<ScreenDensity> getSupportedAndroidScreenDensity() {
        return listAndroid;
    }

    public static List<ScreenDensity> getSupportediOSScreenDensity() {
        return listiOS;
    }

    public static String getDefaultInputDensity() {
        return String.format("%1.0fx", Float.valueOf(defaultInputDensity));
    }

    public static float getDefaultInputDensityAsFloat() {
        return defaultInputDensity;
    }

	public static void setDefaultInputDensity(String density) {
        defaultInputDensity = Float.parseFloat(density.replace("x", ""));
    }

    public static boolean isSaveiOSAssetsEnabled() {
        return isSaveiOSAssetsEnabled;
    }

	public static void setSaveiOSAssetsEnabled(boolean enabled) {
		isSaveiOSAssetsEnabled = enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScreenDensity) {
            return this.name.equals(((ScreenDensity) obj).getName());
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class SaveWorker extends SwingWorker<Void, Void> {
        private final String mSavePayload;
        private final JButton mButton;

        public SaveWorker(JButton saveButton, String savePayload) {
            mButton = saveButton;
            mSavePayload = savePayload;
        }



        @Override
        protected Void doInBackground() throws Exception {
            FileOutputStream fos = null;
            try {
            	File folder = new File(DENSITIES_PATHNAME);
            	if (!folder.exists()) {
            		folder.mkdirs();
            	}
            	File file = new File(folder, "densities.json");
                fos = new FileOutputStream(file);
                PrintWriter writer = new PrintWriter(fos);
                writer.write(mSavePayload);

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Couldn't save");
            }

            return null;
        }

        @Override
        protected void done() {
            if (mButton != null) {
                mButton.setEnabled(true);
            }
        }
    }
}
