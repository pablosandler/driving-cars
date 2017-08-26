package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.exception.IncorrectStatusException;
import com.mytaxi.filtering.criteria.CarEngineCriteria;
import com.mytaxi.filtering.criteria.Criteria;
import com.mytaxi.service.car.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultDriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarService carService;

    @Test
    public void whenDeselectingACarFromDriverSetCarToNull() {
        CarDo car = new CarDo("licensePlate", new ManufacturerDo("BMW"), EngineType.GAS, 1, true, 1);
        car.setSelected(true);
        DriverDO driverDo = new DriverDO("user", "pass");
        driverDo.setCar(car);
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        long driverId = 1;
        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            DriverDO driver = driverService.deselectCar(driverId);
            assertNull(driver.getCar());

            car.setSelected(false);
            verify(carService).save(car);
        } catch (EntityNotFoundException e) {
            fail();
        }
    }

    @Test
    public void whenDeselectingACarFromDriverThrowExceptionIfDriverIsNotFound() {
        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.deselectCar(1);
            fail();
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find entity with id: 1", e.getMessage());
        }
    }

    @Test
    public void whenSelectingACarForDriverThrowAnExceptionIfDriverIsNotFound() {
        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1, 1);
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find entity with id: 1", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenSelectingACarForDriverThrowAnExceptionIfCarIsNotFound() {
        DriverDO driverDo = new DriverDO("user", "pass");
        driverDo.setOnlineStatus(OnlineStatus.ONLINE);
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        try {
            when(carService.find(1L)).thenThrow(new EntityNotFoundException("Could not find car with id: 1"));

            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1, 1);
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find car with id: 1", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenSelectingACarForDriverAssignCarToDriver() {
        CarDo car = new CarDo("licensePlate", new ManufacturerDo("BMW"), EngineType.GAS, 1, true, 1);

        DriverDO driverDo = new DriverDO("user", "pass");
        driverDo.setOnlineStatus(OnlineStatus.ONLINE);
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        try {
            when(carService.find(1L)).thenReturn(car);

            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            DriverDO driver = driverService.selectCar(1, 1);

            assertEquals(car, driver.getCar());

            car.setSelected(true);
            verify(carService).save(car);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenSelectingACarForDriverThrowAnExceptionIfUserHasOfflineStatus() {
        DriverDO driverDo = new DriverDO("user", "pass");
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1, 1);
        } catch (IncorrectStatusException e) {
            assertEquals("Driver with OFFLINE status cannot select a car", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenSelectingACarForDriverThrowAnExceptionIfCarIsAlreadySelected() {
        CarDo car = new CarDo("licensePlate", new ManufacturerDo("BMW"), EngineType.GAS, 1, true, 1);
        car.setSelected(true);

        DriverDO driverDo = new DriverDO("user", "pass");
        driverDo.setOnlineStatus(OnlineStatus.ONLINE);
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        try {
            when(carService.find(1L)).thenReturn(car);

            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1, 1);
        } catch (CarAlreadyInUseException e) {
            assertEquals("Car already in use by another driver", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenFindingByCriteriaApplyItToAllDrivers() {
        List<DriverDO> drivers = new ArrayList<>();

        when(driverRepository.findAll()).thenReturn(drivers);

        DriverService driverService = new DefaultDriverService(driverRepository, carService);

        Criteria engineCriteria = mock(CarEngineCriteria.class);

        List<DriverDO> criteriaResult = new ArrayList<>();
        when(engineCriteria.meetCriteria(drivers)).thenReturn(criteriaResult);

        List<DriverDO> result = driverService.findByCriteria(engineCriteria);

        assertEquals(criteriaResult, result);
        verify(engineCriteria).meetCriteria(drivers);
    }
}