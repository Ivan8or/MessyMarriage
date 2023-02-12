package online.umbcraft.messymariage.data.json;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class JsonAmiabilityData implements AmiabilityData {

    final private String filepath;
    private JSONObject root;

    public JsonAmiabilityData(String filepath) {
        this.filepath = filepath;
        readAmiabilityFile(filepath);
    }

    private void readAmiabilityFile(String filepath) {
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(filepath)) {
            root = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            root = new JSONObject();
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Integer> getAmiabilityLevel(UUID pair) {

        Optional<Integer> optionalExp = getAmiabilityExp(pair);
        if(optionalExp.isEmpty())
            return Optional.empty();

        int exp = optionalExp.get();
        int level = ExpLevelConverter.toLevel(exp);
        return Optional.of(level);
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID pair) {

        JSONArray expArray = (JSONArray) root.get("amiabilityExp");
        Iterator iter = expArray.iterator();

        while(iter.hasNext()) {
            JSONObject entry = (JSONObject) iter.next();

            String uuidString = (String) entry.get("pairID");
            UUID pairID = UUID.fromString(uuidString);
            if(pairID.equals(pair)) {

                // stupid but this doesn't behave right unless it is cast to long first
                Integer pairExp = Integer.parseInt((String) entry.get("exp"));

                return Optional.of(pairExp);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setExp(UUID pair, int amount) {
        JSONArray expArray = (JSONArray) root.get("amiabilityExp");
        Iterator iter = expArray.iterator();

        while(iter.hasNext()) {
            JSONObject entry = (JSONObject) iter.next();

            String uuidString = (String) entry.get("pairID");
            UUID nextPair = UUID.fromString(uuidString);

            if(nextPair.equals(pair)) {
                entry.put("exp", amount+"");
                return;
            }
        }
        JSONObject newEntry = new JSONObject();
        newEntry.put("pairID", pair.toString());
        newEntry.put("exp", amount+"");
        expArray.add(0, newEntry);
    }

    @Override
    public Map<UUID, Integer> allExps() {
        Map<UUID, Integer> toReturn = new HashMap<>();

        JSONArray expArray = (JSONArray) root.get("amiabilityExp");
        Iterator iter = expArray.iterator();

        while(iter.hasNext()) {
            JSONObject entry = (JSONObject) iter.next();

            String uuidString = (String) entry.get("pairID");
            UUID pairID = UUID.fromString(uuidString);

            Integer pairExp = Integer.parseInt((String) entry.get("exp"));

            toReturn.put(pairID, pairExp);
        }

        return toReturn;
    }
}
