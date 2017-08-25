package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.ManufacturerDo;

import java.util.Optional;

public class CarMapper {

    public static CarDTO makeCarDTO(CarDo car) {
        return Optional.ofNullable(car).map( c -> {
            Long manufacturerId =  Optional.ofNullable(car.getManufacturer())
                    .map(ManufacturerDo::getId).orElse(null);

            return new CarDTO(c.getId(),
                    c.getLicensePlate(),
                    c.getSeatCount(),
                    c.isConvertible(),
                    c.getRating(),
                    c.getEngineType(),
                    manufacturerId);
        }).orElse(null);
    }
}
