package online.umbcraft.messymariage.data.json;

import online.umbcraft.messymariage.MessyMarriage;
import online.umbcraft.messymariage.data.PairData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JsonPairData implements PairData {

    final private String filepath;
    private JSONObject root;

    public JsonPairData(String filepath) {
        this.filepath = filepath;

        File jsonFile = new File(filepath);
        if(!jsonFile.exists()) {
            resetFile();
        }
        readPairFile();
    }

    private void resetFile() {
        try {
            URL resource = MessyMarriage.class.getResource("data/default-pairs.json");
            File defaultFile = Paths.get(resource.toURI()).toFile();
            Files.copy(defaultFile.toPath(), new File(filepath).toPath());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void readPairFile() {
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(filepath)) {
            root = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            root = new JSONObject();
            e.printStackTrace();
        }
    }

    private void writePairFile() {
        try (FileWriter file = new FileWriter(filepath)) {

            file.write(root.toJSONString());
            file.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<UUID> getPartner(UUID pair, UUID player) {
        Optional<Set<UUID>> potentialMembers = getMembers(pair);

        if(potentialMembers.isEmpty())
            return Optional.empty();

        Set<UUID> members = potentialMembers.get();

        Optional<UUID> partner =
                members.stream()
                        .filter(uuid -> uuid != player)
                        .findFirst();

        return partner;
    }

    @Override
    public Optional<Set<UUID>> getMembers(UUID pair) {
        Set<UUID> members = allPairings().get(pair);
        if(members == null)
            return Optional.empty();

        return Optional.of(members);
    }

    @Override
    public boolean pairExists(UUID pair) {
        return allPairings().containsKey(pair);
    }

    @Override
    public boolean isMarriage(UUID pair) {
        return allMarriages().contains(pair);
    }

    // TODO unimplemented because lazy and dont need it
    @Override
    public boolean isMarried(UUID player) {
        return false;
    }

    // TODO unimplemented because lazy and dont need it
    @Override
    public Optional<UUID> getMarriageID(UUID player) {
        return Optional.empty();
    }

    @Override
    public Set<UUID> allMarriages() {
        JSONArray marriages = (JSONArray) root.get("marriages");
        return (Set<UUID>) marriages
                .stream()
                .map(m -> UUID.fromString((String) m))
                .collect(Collectors.toSet());

    }

    @Override
    public Map<UUID, Set<UUID>> allPairings() {
        JSONArray pairs = (JSONArray) root.get("pairs");
        Map<UUID, Set<UUID>> toReturn = new HashMap<>();

        for (Object o : pairs) {
            JSONObject entry = (JSONObject) o;
            UUID pairID = UUID.fromString((String) entry.get("pairID"));

            JSONArray members = (JSONArray) entry.get("members");
            Set<UUID> memberSet = Set.copyOf(members);

            toReturn.put(pairID, memberSet);
        }
        return toReturn;
    }

    @Override
    public UUID pairID(UUID a, UUID b) {
        UUID pairID = PairData.generatePairID(a, b);

        if(!allPairings().containsKey(pairID)) {

            JSONArray membersArray = new JSONArray();
            membersArray.addAll(Set.of(a, b));

            JSONObject newPair = new JSONObject();
            newPair.put("pairID", pairID.toString());
            newPair.put("members", membersArray);

            JSONArray allPairs = (JSONArray) root.get("pairs");
            allPairs.add(newPair);
            writePairFile();

        }
        return pairID;
    }

    @Override
    public void marry(UUID pair) {
        JSONArray marriages = (JSONArray) root.get("marriages");
        marriages.add(pair.toString());
        writePairFile();
    }

    @Override
    public void unmarry(UUID pair) {
        JSONArray marriages = (JSONArray) root.get("marriages");
        marriages.remove(pair.toString());
        writePairFile();
    }
}
