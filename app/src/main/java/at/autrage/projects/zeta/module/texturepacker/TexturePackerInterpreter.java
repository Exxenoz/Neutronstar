package at.autrage.projects.zeta.module.texturepacker;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.autrage.projects.zeta.module.Logger;

public class TexturePackerInterpreter {
	private static TexturePackerInterpreter instance;

	private JSONParser jsonparser;

	private TexturePackerInterpreter(){
		jsonparser = new JSONParser();
	}

	public static TexturePackerInterpreter getInstance(){
		if(instance == null) {
			instance = new TexturePackerInterpreter();
		}

		return instance;
	}

	public TexturePackerAtlas parseAtlas(String textureAtlasJSONFile, AssetManager am){
		BufferedReader reader = null;
		JSONObject temporaryRawAtlas;

		try {
			reader = new BufferedReader(new InputStreamReader(am.open(textureAtlasJSONFile, AssetManager.ACCESS_BUFFER)));
			temporaryRawAtlas = (JSONObject) jsonparser.parse(reader);
		} catch (IOException | ParseException e) {
			Logger.E("Could not read atlas json file \"" + textureAtlasJSONFile + "\": " + e);
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					Logger.E("Could not close reader for atlas json file \"" + textureAtlasJSONFile + "\": " + e);
				}
			}
		}

		JSONObject meta = (JSONObject)temporaryRawAtlas.get("meta");

		return new TexturePackerAtlas((String) meta.get("image"),
				(int)((JSONObject)meta.get("size")).get("w"),
				(int)((JSONObject)meta.get("size")).get("h"),
				parseFrameList((JSONArray) temporaryRawAtlas.get("frames")));
	}

	private LinkedHashMap<String, PackedTexture> parseFrameList(JSONArray frames){
		LinkedHashMap<String, PackedTexture> packedTextures = new LinkedHashMap<>();

		for (Object frame : frames) {
			JSONObject jsonFrame = (JSONObject) frame;
			JSONObject frameproperties = (JSONObject) jsonFrame.get("frame");

			packedTextures.put((String) jsonFrame.get("fileName"), new PackedTexture((String) jsonFrame.get("fileName"),
							(int)frameproperties.get("w"),
							(int)frameproperties.get("h"),
							(int)frameproperties.get("x"),
							(int)frameproperties.get("y")));
		}

		return packedTextures;
	}
}
