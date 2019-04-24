package net.redwarp.tool.resizer.worker;

import com.google.gson.JsonObject;

public class ImageAsset {
    private static final String KEY_IDIOM = "idiom";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_SCALE = "scale";
    private String filename;
    private String idiom;
    private String scale;
    
    public String getFilename() {
    	return filename;
    }
    
    public String getIdiom() {
    	return idiom;
    }
    
    public String getScale() {
    	return scale;
    }
    
    public JsonObject toJsonObject() {
    	JsonObject obj = new JsonObject();
    	obj.addProperty(KEY_IDIOM, idiom);
    	obj.addProperty(KEY_FILENAME, filename);
    	obj.addProperty(KEY_SCALE, scale);
    	return obj;
    }
    
    public static ImageAsset imageAssetFromScreenDensity(ScreenDensity density, String name) {
    	if ("ios".equalsIgnoreCase(density.getOs())) {
    		ImageAsset asset = new ImageAsset();
    		asset.filename = name + (density.getScale() == 1f?"":density.getName()) + ".png";
    		asset.idiom = "universal";
    		asset.scale = density.getName().replace("@", "");
    		return asset;
    	} else {
    		return null;
    	}
    }

}
