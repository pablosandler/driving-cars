package com.mytaxi.service.driver;/*
 * Copyright 2014-2017 SignalPath, LLC - All Rights Reserved
 * Unauthorized dissemination or copying of these materials, in whole or in part and via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */

import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;

public interface CarService {

    CarDo create(String licensePlate, Long manufacturer, int rating, int seatCount, EngineType engineType, boolean convertible) throws EntityNotFoundException, ConstraintsViolationException;

    CarDo find(long id) throws EntityNotFoundException;

    List<CarDo> find();
}
