package com.mytaxi.service.car;

import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;

public interface CarService {

    CarDo create(String licensePlate, Long manufacturer, int rating, int seatCount, EngineType engineType, boolean convertible) throws EntityNotFoundException, ConstraintsViolationException;

    CarDo find(long id) throws EntityNotFoundException;

    CarDo save(CarDo car);

    List<CarDo> find();

    void delete(long carId);

    CarDo update(long carId, int rating) throws EntityNotFoundException ;
}
