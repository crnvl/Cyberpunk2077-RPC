package Lib;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSON {
    
    public static String readLifePath() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject o = (JSONObject) parser.parse(new FileReader(Savestate.pathFile + "\\metadata.9.json"));

        JSONObject get = new JSONObject(o);

        JSONObject o2 = (JSONObject) get.get("Data");
        get = new JSONObject(o2);
        JSONObject o3 = (JSONObject) get.get("metadata");
        get = new JSONObject(o3);
        return get.get("lifePath").toString();
    }

    public static String readLevel() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject o = (JSONObject) parser.parse(new FileReader(Savestate.pathFile + "\\metadata.9.json"));

        JSONObject get = new JSONObject(o);

        JSONObject o2 = (JSONObject) get.get("Data");
        get = new JSONObject(o2);
        JSONObject o3 = (JSONObject) get.get("metadata");
        get = new JSONObject(o3);
        return get.get("level").toString();
    }
    
}
