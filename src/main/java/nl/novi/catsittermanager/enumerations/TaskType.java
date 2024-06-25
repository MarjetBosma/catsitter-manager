package nl.novi.catsittermanager.enumerations;

import lombok.Getter;

@Getter
public enum TaskType {
    FOOD(10.0),
    WATER(5.0),
    LITTERBOX(15.0),
    MEDICATION(20.0),
    FURCARE(12.0),
    PLAY(8.0),
    OTHER(0.0);

    private final double price;

    TaskType(double price) {
        this.price = price;
    }

}