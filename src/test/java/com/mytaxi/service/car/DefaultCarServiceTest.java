package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.car.DefaultCarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultCarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;


    @Test
    public void whenManufacturerDoesNotExistThrowException() {
        when(manufacturerRepository.findOne(1L)).thenReturn(null);

        CarService carService = new DefaultCarService(carRepository, manufacturerRepository);

        try {
            carService.create("licensePlate", 1L, 1, 1, EngineType.DIESEL, false);
            fail();
        } catch (ConstraintsViolationException e) {
            fail();
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find manufacturer with id: 1", e.getMessage());
        }
    }

    @Test
    public void whenACarWithGivenLicensePlateAlreadyExistsThrowException() {
        when(manufacturerRepository.findOne(1L)).thenReturn(new ManufacturerDo("name"));
        CarDo car = new CarDo("licensePlate", new ManufacturerDo("name"), EngineType.DIESEL, 1, false, 1);
        when(carRepository.findByLicensePlate("licensePlate")).thenReturn(Optional.of(car));

        CarService carService = new DefaultCarService(carRepository, manufacturerRepository);

        try {
            carService.create("licensePlate", 1L, 1, 1, EngineType.DIESEL, false);
            fail();
        } catch (ConstraintsViolationException e) {
            assertEquals("A car with informed license plate already exists", e.getMessage());
        } catch (EntityNotFoundException e) {
            fail();
        }
    }

    @Test
    public void whenDataIsFoundCarIsCreated() {
        ManufacturerDo manufacturer = new ManufacturerDo("name");
        when(manufacturerRepository.findOne(1L)).thenReturn(manufacturer);
        CarDo car = new CarDo("licensePlate", manufacturer, EngineType.DIESEL, 1, false, 1);
        when(carRepository.findByLicensePlate("licensePlate")).thenReturn(Optional.empty());
        when(carRepository.save(any(CarDo.class))).thenReturn(car);

        CarService carService = new DefaultCarService(carRepository, manufacturerRepository);

        try {
            CarDo newCar = carService.create("licensePlate", 1L, 1, 1, EngineType.DIESEL, false);
            assertEquals(car.getSeatCount(), newCar.getSeatCount());
            assertEquals(null, newCar.getId());
            assertEquals(manufacturer, newCar.getManufacturer());
            assertEquals(1, newCar.getRating());
            assertEquals(EngineType.DIESEL, newCar.getEngineType());
            assertEquals("licensePlate", newCar.getLicensePlate());
            assertEquals(false, newCar.isConvertible());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenFindingACarThrowExceptionIfNotFound() {
        when(carRepository.findOne(1L)).thenReturn(null);

        CarService carService = new DefaultCarService(carRepository, manufacturerRepository);
        try {
            carService.find(1);
            fail();
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find car with id: 1", e.getMessage());
        }
    }

    @Test
    public void whenFindingACarReturnItIfFound() {
        ManufacturerDo manufacturer = new ManufacturerDo("name");
        CarDo car = new CarDo("licensePlate", manufacturer, EngineType.DIESEL, 1, false, 1);
        when(carRepository.findOne(1L)).thenReturn(car);

        CarService carService = new DefaultCarService(carRepository, manufacturerRepository);
        try {
            CarDo carResponse = carService.find(1);
            assertEquals(car, carResponse);
        } catch (EntityNotFoundException e) {
            fail();
        }
    }

}