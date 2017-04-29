package at.autrage.projects.zeta.module.texturepacker;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.autrage.projects.zeta.module.Logger;

public class TexturePackerInterpreter {
	private static TexturePackerInterpreter instance;

	private JSONParser jsonparser;
    private HashMap<String, TexturePackerAtlas> texturePackerAtlasses;

	private TexturePackerInterpreter(){
		jsonparser = new JSONParser();
        texturePackerAtlasses = new HashMap<>();
	}

	public static TexturePackerInterpreter getInstance(){
		if(instance == null) {
			instance = new TexturePackerInterpreter();
		}

		return instance;
	}

	public TexturePackerAtlas parseAtlas(String textureAtlasJSONFile, AssetManager am){
        TexturePackerAtlas cache = texturePackerAtlasses.get(textureAtlasJSONFile);
        if (cache != null) {
            return cache;
        }

		BufferedReader reader = null;
		JSONObject temporaryRawAtlas = null;

		try {
			reader = new BufferedReader(new InputStreamReader(am.open(textureAtlasJSONFile, AssetManager.ACCESS_BUFFER)));
			temporaryRawAtlas = (JSONObject) jsonparser.parse(reader);
		} catch (IOException | ParseException e) {
			Logger.E("Could not read atlas json file \"" + textureAtlasJSONFile + "\": " + e);
            texturePackerAtlasses.put(textureAtlasJSONFile, null);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					Logger.E("Could not close reader for atlas json file \"" + textureAtlasJSONFile + "\": " + e);
				}
			}
		}

		if (temporaryRawAtlas == null) {
			return null;
		}

		JSONObject meta = (JSONObject)temporaryRawAtlas.get("meta");

        cache = new TexturePackerAtlas((String) meta.get("image"),
                ((Long)((JSONObject)meta.get("size")).get("w")).intValue(),
                ((Long)((JSONObject)meta.get("size")).get("h")).intValue(),
				parseFrameList((JSONArray) temporaryRawAtlas.get("frames")));

        texturePackerAtlasses.put(textureAtlasJSONFile, cache);

        return cache;
	}

	private HashMap<String, PackedTexture> parseFrameList(JSONArray frames){
		HashMap<String, PackedTexture> packedTextures = new HashMap<>();

		for (Object frame : frames) {
			JSONObject jsonFrame = (JSONObject) frame;
			JSONObject frameproperties = (JSONObject) jsonFrame.get("frame");

			packedTextures.put((String) jsonFrame.get("filename"), new PackedTexture((String) jsonFrame.get("filename"),
                    ((Long)frameproperties.get("w")).intValue(),
                    ((Long)frameproperties.get("h")).intValue(),
                    ((Long)frameproperties.get("x")).intValue(),
                    ((Long)frameproperties.get("y")).intValue()));
		}

		return packedTextures;
	}
}
