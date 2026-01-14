package main.model;

public class Hades {
    private final String name;
    private final Buffer buffer;

    public Hades(String name, Buffer buffer) {
        this.name = name;
        this.buffer = buffer;
    }

    public void processNextSoul() throws InterruptedException {
        int soul = buffer.take();
        judgeSoul(soul);
    }

    private void judgeSoul(int soul) {
        System.out.println("🔥 Hades " + name + " судит душу #" + soul);
    }
}

