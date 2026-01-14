package main.model;

import main.utils.SoulState;

public class Hades {

    private final String name;

    public Hades(String name) {
        this.name = name;
    }

    /**
     * Обработать переданную душу
     */
    public void process(Soul soul) {
        if (soul == null) {
            throw new IllegalArgumentException("Hades не может судить пустоту");
        }

        System.out.println(
                "🔥 Hades " + name + " судит душу " + soul.getId()
        );

        judgeSoul(soul);
    }

    private void judgeSoul(Soul soul) {
        // тут позже:
        // - время обработки
        // - исход (рай / ад / реинкарнация / лодка)
        // - генерация Event
        soul.setState(SoulState.IN_SERVICE);
    }

    public String getName() {
        return name;
    }
}

