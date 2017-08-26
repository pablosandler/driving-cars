package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class CarManufacturerCriteriaTest {

    @Test
    public void whenCriteriaIsAppliedReturnOnlyTheItemsWhichMeetCriteria() {
        List<DriverDO> drivers = new ArrayList<>();
        DriverDO driver1 = new DriverDO("driver1","pass1");
        CarDo car = new CarDo("111111", new ManufacturerDo(1, "BMW"), EngineType.DIESEL, 1, false, 1);
        driver1.setCar(car);
        drivers.add(driver1);

        DriverDO driver2 = new DriverDO("driver2","pass2");
        CarDo car2 = new CarDo("111111", new ManufacturerDo(2, "FORD"), EngineType.GAS, 1, false, 1);
        driver2.setCar(car2);
        drivers.add(driver2);

        DriverDO driver3 = new DriverDO("driver3","pass3");
        drivers.add(driver3);

        Criteria criteria = new CarManufacturerCriteria(2);
        List<DriverDO> result = criteria.meetCriteria(drivers);

        assertTrue(result.size()==1);
        assertEquals(driver2, result.get(0));
    }

}