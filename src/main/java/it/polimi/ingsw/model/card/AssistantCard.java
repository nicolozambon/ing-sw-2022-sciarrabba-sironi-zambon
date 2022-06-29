package it.polimi.ingsw.model.card;

import java.util.Objects;

/**
 * A Class to represent the assistant cards (immutable)
 */
public class AssistantCard {

    /**
     * Value of the card
     */
    private final int value;

    /**
     * Number of steps that mother nature can do
     */
    private final int steps;

    /**
     * Constructor with value and steps
     * @param value the value of the card
     * @param steps the steps that mother nature can do
     */
    public AssistantCard(int value, int steps) {
        this.value = value;
        this.steps = steps;
    }

    /**
     * Returns the value of the card
     * @return the value of the card
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns the steps that mother nature can do
     * @return the steps that mother nature can do
     */
    public int getSteps() {
        return this.steps;
    }

    /**
     * Compares the card with the specified object
     * @param o the object the card is compared to
     * @return true if the two cards have the same value and steps, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssistantCard that)) return false;
        return value == that.value && steps == that.steps;
    }

    /**
     * Returns hash of the card based on value and steps
     * @return hash of the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(value, steps);
    }

}
