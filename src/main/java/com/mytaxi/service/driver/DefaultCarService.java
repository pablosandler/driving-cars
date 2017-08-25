package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultCarService implements CarService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private CarRepository carRepository;
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    public DefaultCarService(CarRepository carRepository, ManufacturerRepository manufacturerRepository) {
        this.carRepository = carRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public CarDo create(String licensePlate, Long manufacturerId, int rating, int seatCount, EngineType engineType, boolean convertible) throws EntityNotFoundException, ConstraintsViolationException {
        Optional<ManufacturerDo> manufacturer = Optional.ofNullable(manufacturerRepository.findOne(manufacturerId));

        manufacturer.orElseThrow( () -> {
            LOG.warn("Could not find manufacturer with id: " + manufacturerId);
            return new EntityNotFoundException("Could not find manufacturer with id: " + manufacturerId);
        });

        Optional<CarDo> existingCar = carRepository.findByLicensePlate(licensePlate);
        if(existingCar.isPresent()){
            LOG.warn("A car with informed license plate already exists");
            throw new ConstraintsViolationException("A car with informed license plate already exists");
        }

        CarDo car = new CarDo(licensePlate, manufacturer.get(), engineType, rating, convertible, seatCount);
        try {
            return carRepository.save(car);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("Some constraints are thrown due to car creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
    }

    @Override
    public CarDo find(long id) throws EntityNotFoundException {
        Optional<CarDo> car = Optional.ofNullable(carRepository.findOne(id));
        return car.orElseThrow(() -> {
            LOG.warn("Could not find car with id: " + id);
            return new EntityNotFoundException("Could not find car with id: " + id);
        });
    }

    @Override
    public CarDo save(CarDo car) {
        return carRepository.save(car);
    }

    @Override
    public List<CarDo> find() {
        List<CarDo> result = new ArrayList<>();
        carRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public void delete(long carId) {
        carRepository.delete(carId);
    }

    @Override
    public CarDo update(long id, int rating) throws EntityNotFoundException {
        Optional<CarDo> car = Optional.ofNullable(carRepository.findOne(id));

        CarDo c = car.orElseThrow(() -> {
            LOG.warn("Could not find car with id: " + id);
            return new EntityNotFoundException("Could not find car with id: " + id);
        });

        c.setRating(rating);

        return carRepository.save(c);
    }

}
