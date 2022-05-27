package it.polimi.ingsw.model.card;

import java.util.Objects;

public class AssistantCard {

    private final int value;
    private final int steps;

    public AssistantCard(int value, int steps) {
        this.value = value;
        this.steps = steps;
    }

    public int getValue() {
        return this.value;
    }

    public int getSteps() {
        return this.steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssistantCard that)) return false;
        return value == that.value && steps == that.steps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, steps);
    }

    @Override
    public String toString() {
        return "{value = " + value +
                ", steps = " + steps + "}";
    }
}
