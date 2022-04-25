package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class AssistantCard implements Serializable {

    private static final long serialVersionUID = 987654321L;
    private final int id;
    private final int value;
    private final int steps;

    public AssistantCard(int id, int value, int steps) {
        this.id = id;
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
        return value == that.value && steps == that.steps && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, steps);
    }

    @Override
    public String toString() {
        return "id = " + id +
                ", value = " + value +
                ", steps = " + steps +
                '}';
    }
}
