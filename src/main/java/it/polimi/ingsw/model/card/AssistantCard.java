package it.polimi.ingsw.model.card;

import java.util.Objects;

public class AssistantCard {

    private final int id;
    private final String path;
    private final int value;
    private final int steps;

    public AssistantCard(int id, String path, int value, int steps) {
        this.id = id;
        this.path = path;
        this.value = value;
        this.steps = steps;
    }

    public int getValue() {
        return this.value;
    }

    public int getSteps() {
        return this.steps;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssistantCard)) return false;
        AssistantCard that = (AssistantCard) o;
        return value == that.value && steps == that.steps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, steps);
    }

}
