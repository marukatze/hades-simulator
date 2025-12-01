package main.model;

import main.utils.SoulState;

public class Soul {
    private int sourceId;
    private int id;
    private SoulState state;

    public Soul(int sourceIdid, int id){
        this.sourceId = sourceIdid;
        this.id = id;
        state = SoulState.NEW;
    }
}
