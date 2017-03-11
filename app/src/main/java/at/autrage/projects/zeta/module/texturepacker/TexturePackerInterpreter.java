package at.autrage.projects.zeta.module.texturepacker;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TexturePackerInterpreter {
	private static TexturePackerInterpreter instance;

	private JSONParser jsonparser;

	private TexturePackerInterpreter(){
		jsonparser = new JSONParser();
	}

	public static TexturePackerInterpreter getInstance(){
		if(instance==null){
			instance = new TexturePackerInterpreter();
		}
		return instance;
	}

	public TexturePackerAtlas parseAtlas(String path){
		FileReader fileReader;
		JSONObject temporaryRawAtlas;
		try {
			fileReader = new FileReader(path);
			temporaryRawAtlas = (JSONObject) jsonparser.parse(fileReader);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONObject meta = (JSONObject)temporaryRawAtlas.get("meta");
		TexturePackerAtlas atlas = new TexturePackerAtlas((String) meta.get("image"), parseFamesList((JSONArray) temporaryRawAtlas.get("frames")) , (Long)((JSONObject)meta.get("size")).get("w"), (Long)((JSONObject)meta.get("size")).get("h"));
		return atlas;
	}

	private LinkedHashMap<String, PackedTexture> parseFamesList(JSONArray frames){
		LinkedHashMap<String, PackedTexture> packedtextures = new LinkedHashMap<>();

		for(Object frame : frames){
			JSONObject jsonFrame = (JSONObject) frame;
			JSONObject frameproperties = (JSONObject) jsonFrame.get("frame");
			packedtextures.put((String) jsonFrame.get("filename"), new PackedTexture((String) jsonFrame.get("filename"), (Long)frameproperties.get("w"), (Long)frameproperties.get("h"), (Long)frameproperties.get("x"), (Long)frameproperties.get("y")));
		}

		return packedtextures;
	}

}
