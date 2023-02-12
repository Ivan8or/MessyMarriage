package online.umbcraft.messymariage.data.json;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonAmiabilityData implements AmiabilityData {

    final private String filepath;
    private JSONObject root;

    public JsonAmiabilityData(String filepath) {
        this.filepath = filepath;

        File jsonFile = new File(filepath);
        if(!jsonFile.exists()) {
            resetFile();
        }

        readAmiabilityFile();
    }

    private void resetFile() {
        try {
            InputStream defaultFileStream = getClass().getResourceAsStream("/data/default-amiability.json");
            Files.copy(defaultFileStream, new File(filepath).toPath());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void readAmiabilityFile() {
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(filepath)) {
            root = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            root = new JSONObject();
            e.printStackTrace();
        }
    }

    private void writeAmiabilityFile() {
        try (FileWriter file = new FileWriter(filepath)) {

            file.write(root.toJSONString());
            file.flush();

        } catch (Exception e) {
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

        for (Object o : expArray) {
            JSONObject entry = (JSONObject) o;

            String uuidString = (String) entry.get("pairID");
            UUID pairID = UUID.fromString(uuidString);
            if (pairID.equals(pair)) {

                // stupid but this doesn't behave right unless it is cast to long first
                Integer pairExp = Integer.parseInt((String) entry.get("exp"));

                return Optional.of(pairExp);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setExp(UUID pair, int amount) {
       setManyExp(Map.of(pair, amount));
    }

    @Override
    public void setManyExp(Map<UUID, Integer> toUpdate) {
        JSONArray expArray = (JSONArray) root.get("amiabilityExp");

        Set<UUID> handled = new HashSet<>(toUpdate.size());

        for (Object o : expArray) {
            JSONObject entry = (JSONObject) o;

            String uuidString = (String) entry.get("pairID");
            UUID nextPair = UUID.fromString(uuidString);

            if (toUpdate.containsKey(nextPair)) {
                entry.put("exp", toUpdate.get(nextPair) + "");
                handled.add(nextPair);
            }
        }

        for(Map.Entry<UUID, Integer> updates : toUpdate.entrySet()) {
            if(handled.contains(updates.getKey()))
                continue;

            JSONObject newEntry = new JSONObject();
            newEntry.put("pairID", updates.getKey().toString());
            newEntry.put("exp", updates.getValue() + "");
            expArray.add(newEntry);
        }
        writeAmiabilityFile();
    }

    @Override
    public Map<UUID, Integer> allExps() {
        Map<UUID, Integer> toReturn = new HashMap<>();

        JSONArray expArray = (JSONArray) root.get("amiabilityExp");

        for (Object o : expArray) {
            JSONObject entry = (JSONObject) o;

            String uuidString = (String) entry.get("pairID");
            UUID pairID = UUID.fromString(uuidString);

            Integer pairExp = Integer.parseInt((String) entry.get("exp"));

            toReturn.put(pairID, pairExp);
        }

        return toReturn;
    }
}
