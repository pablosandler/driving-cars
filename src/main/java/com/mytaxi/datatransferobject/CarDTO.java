package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.domainvalue.EngineType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO {

    private Long id;

    @NotNull(message = "License plate can not be null!")
    @Size(min=6, max=6)
    private String licensePlate;

    @Min(1)
    private int seatCount;

    private boolean convertible;

    @Min(1)
    @Max(5)
    private int rating;

    @NotNull(message = "A car must have an engine!")
    private EngineType engineType;

    @NotNull(message = "A car must have a manufacturer!")
    private Long manufacturer;

    private CarDTO() {}

    public CarDTO(Long id, String licensePlate, int seatCount, boolean convertible, int rating, EngineType engineType, Long manufacturer) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }

    public Long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public boolean isConvertible() {
        return convertible;
    }

    public int getRating() {
        return rating;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public Long getManufacturer(){
        return  manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDTO carDTO = (CarDTO) o;
        return getSeatCount() == carDTO.getSeatCount() &&
                isConvertible() == carDTO.isConvertible() &&
                getRating() == carDTO.getRating() &&
                Objects.equals(getId(), carDTO.getId()) &&
                Objects.equals(getLicensePlate(), carDTO.getLicensePlate()) &&
                getEngineType() == carDTO.getEngineType() &&
                Objects.equals(getManufacturer(), carDTO.getManufacturer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLicensePlate(), getSeatCount(), isConvertible(), getRating(), getEngineType(), getManufacturer());
    }
}
