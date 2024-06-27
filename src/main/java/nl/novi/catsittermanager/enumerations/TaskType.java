package nl.novi.catsittermanager.enumerations;

import lombok.Getter;

@Getter
public enum TaskType {
    FOOD(4.0),
    WATER(3.0),
    LITTERBOX(6.0),
    MEDICATION(5.0),
    FURCARE(7.0),
    PLAY(4.0),
    OTHER(5.0);

    private final double price;

    TaskType(double price) {
        this.price = price;
    }

}