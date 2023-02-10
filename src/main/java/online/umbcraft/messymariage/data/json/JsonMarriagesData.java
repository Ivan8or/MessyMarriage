package online.umbcraft.messymariage.data.json;

import online.umbcraft.messymariage.data.MarriagesData;

import java.util.Optional;
import java.util.UUID;

public class JsonMarriagesData extends MarriagesData {

    @Override
    public boolean areMarried(UUID a, UUID b) {
        pairID(a,b);
        return false;
    }


    public static void main(String[] args) {

    }
}
