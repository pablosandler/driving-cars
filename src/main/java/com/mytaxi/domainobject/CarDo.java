package com.mytaxi.domainobject;

import com.mytaxi.domainvalue.EngineType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "car",
    uniqueConstraints = @UniqueConstraint(name = "uc_license_plate", columnNames = {"licensePlate"})
)
public class CarDo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated;

    @Column(nullable = false)
    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    private int seatCount;

    private boolean convertible;

    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Engine type can not be null!")
    private EngineType engineType;

    @ManyToOne(optional=false)
    @JoinColumn(name="manufacturer_ID", nullable=false, updatable=false)
    private ManufacturerDo manufacturer;

    public CarDo(String licensePlate, ManufacturerDo manufacturer, EngineType engineType, int rating, boolean convertible, int seatCount) {
        this.id=null;
        this.manufacturer = manufacturer;
        this.engineType = engineType;
        this.rating = rating;
        this.convertible = convertible;
        this.seatCount = seatCount;
        this.licensePlate = licensePlate;
        dateCreated = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getDateCreated() {
        return ZonedDateTime.from(dateCreated);
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

    public ManufacturerDo getManufacturer() {
        return manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDo carDo = (CarDo) o;
        return getSeatCount() == carDo.getSeatCount() &&
                isConvertible() == carDo.isConvertible() &&
                getRating() == carDo.getRating() &&
                Objects.equals(getId(), carDo.getId()) &&
                Objects.equals(getDateCreated(), carDo.getDateCreated()) &&
                Objects.equals(getLicensePlate(), carDo.getLicensePlate()) &&
                getEngineType() == carDo.getEngineType() &&
                Objects.equals(getManufacturer(), carDo.getManufacturer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateCreated(), getLicensePlate(), getSeatCount(), isConvertible(), getRating(), getEngineType(), getManufacturer());
    }
}
