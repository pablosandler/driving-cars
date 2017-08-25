package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
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
        DriverDO driverDo = new DriverDO("user", "pass");
        driverDo.setCar(car);
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        long driverId = 1;
        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            DriverDO driver = driverService.deselectCar(driverId);
            assertNull(driver.getCar());
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
        }
    }

    @Test
    public void whenSelectingACarForDriverThrowAnExceptionIfCarIsNotFound() {
        DriverDO driverDo = new DriverDO("user", "pass");
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1, 1);
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find car with id: 1", e.getMessage());
        }
    }

    @Test
    public void whenSelectingACarForDriverAssignCarToDriver() {
        CarDo car = new CarDo("licensePlate", new ManufacturerDo("BMW"), EngineType.GAS, 1, true, 1);

        DriverDO driverDo = new DriverDO("user", "pass");
        when(driverRepository.findOne(1L)).thenReturn(driverDo);

        try {
            when(carService.find(1L)).thenReturn(car);

            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            DriverDO driver = driverService.selectCar(1, 1);

            assertEquals(car, driver.getCar());
        } catch (EntityNotFoundException e) {
            fail();
        }
    }

}