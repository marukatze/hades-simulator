package main.model;

import main.utils.SoulStatus;

public class Soul {

    private final String id;
    private SoulStatus state;

    public Soul(String id) {
        this.id = id;
        this.state = SoulStatus.NEW;
    }

    public String getId() {
        return id;
    }

    public SoulStatus getState() {
        return state;
    }

    public void setStatus(SoulStatus state) {
        this.state = state;
    }
}
