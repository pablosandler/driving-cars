package com.mytaxi.controller;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(final CarService carService)
    {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDTO> getCars() {
        return carService.find().stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException {
        return CarMapper.makeCarDTO(carService.find(carId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws EntityNotFoundException, ConstraintsViolationException {
        CarDo car = carService.create(carDTO.getLicensePlate(), carDTO.getManufacturer(), carDTO.getRating(), carDTO.getSeatCount(), carDTO.getEngineType(), carDTO.isConvertible());
        return CarMapper.makeCarDTO(car);
    }


    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable long carId) throws EntityNotFoundException {
        carService.delete(carId);
    }


    @PutMapping("/{carId}")
    public CarDTO update(@PathVariable long carId, @RequestParam int rating) throws EntityNotFoundException {
        return CarMapper.makeCarDTO(carService.update(carId, rating));
    }
}
