package com.mytaxi.datatransferobject;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CarUpdateDTO {

    @Min(1)
    @Max(5)
    private int rating;


    public CarUpdateDTO() {}

    public CarUpdateDTO(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
}
