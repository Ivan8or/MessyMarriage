package online.umbcraft.messymarriage.data.json;

import online.umbcraft.messymarriage.data.PairData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
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
            InputStream defaultFileStream = getClass().getResourceAsStream("/data/default-pairs.json");
            Files.copy(defaultFileStream, new File(filepath).toPath());
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
        throw new UnsupportedOperationException("not implemented");
    }

    // TODO unimplemented because lazy and dont need it
    @Override
    public Optional<UUID> getMarriageID(UUID player) {
        throw new UnsupportedOperationException("not implemented");
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
            Set<UUID> memberSet = new HashSet<>();
            for(Object member : members) {
                memberSet.add(UUID.fromString((String)member));
            }

            toReturn.put(pairID, memberSet);
        }
        return toReturn;
    }

    @Override
    public UUID pairID(UUID a, UUID b) {
        UUID pairID = PairData.generatePairID(a, b);

        if(!allPairings().containsKey(pairID)) {

            JSONArray membersArray = new JSONArray();
            membersArray.addAll(Set.of(a.toString(), b.toString()));

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
        if(isMarriage(pair))
            return;

        JSONArray marriages = (JSONArray) root.get("marriages");
        marriages.add(pair.toString());
        writePairFile();
    }

    @Override
    public void unmarry(UUID pair) {
        if(!isMarriage(pair))
            return;

        JSONArray marriages = (JSONArray) root.get("marriages");
        marriages.remove(pair.toString());
        writePairFile();
    }

    @Override
    public void setPairings(Map<UUID, Set<UUID>> pairings) {
        throw new UnsupportedOperationException("not implemented");
        // TODO implement
    }

    @Override
    public void setMarriages(Set<UUID> marriages) {
        throw new UnsupportedOperationException("not implemented");
        // TODO implement
    }
}
