package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarManufacturerCriteria implements Criteria {

    private final long manufacturerId;

    public CarManufacturerCriteria(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    @Override
    public List<DriverDO> meetCriteria(List<DriverDO> drivers) {
        return drivers.stream()
                .filter(d -> {
                    Optional<CarDo> car = Optional.ofNullable(d.getCar());
                    Optional<Long> manufacturer = car.map(CarDo::getManufacturer).map(ManufacturerDo::getId);

                    return new Long(manufacturerId).equals(manufacturer.orElse(null));
                })
                .collect(Collectors.toList());
    }
}