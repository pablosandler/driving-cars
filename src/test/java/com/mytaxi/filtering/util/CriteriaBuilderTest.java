package com.mytaxi.filtering.util;

import com.mytaxi.datatransferobject.CriteriaDTO;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.InvalidCriteriaValueException;
import com.mytaxi.filtering.criteria.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
public class CriteriaBuilderTest {

    @Test
    public void checkingGeneratedStructure() {
        try {
            Criteria criteria = CriteriaBuilder.build(buildCriteriaDto());

            Criteria rootCriteria = buildExpectedCriteria();

            List<DriverDO> drivers = buildDriversList();

            assertEquals(rootCriteria.meetCriteria(drivers), criteria.meetCriteria(drivers));

        } catch (InvalidCriteriaValueException e) {
            fail();
        }
    }

    private CriteriaDTO buildCriteriaDto() {
        CriteriaDTO componentA = new CriteriaDTO(AttributeType.CAR_ENGINE, "DIESEL");
        CriteriaDTO component1 = new CriteriaDTO(null, null, CriteriaType.AND, componentA, null);

        CriteriaDTO componentC = new CriteriaDTO(AttributeType.CAR_MANUFACTURER, "3");
        CriteriaDTO componentD = new CriteriaDTO(AttributeType.ONLINE_STATUS, "OFFLINE");
        CriteriaDTO component2 = new CriteriaDTO(null, null, CriteriaType.OR, componentC, componentD);

        return new CriteriaDTO(null, null, CriteriaType.AND, component1, component2);
    }

    private Criteria buildExpectedCriteria(){
        Criteria carEngineCriteria = new CarEngineCriteria(EngineType.DIESEL);
        Criteria andCriteria = new AndCriteria(carEngineCriteria, null);

        Criteria carManufacturerCriteria = new CarManufacturerCriteria(3);
        Criteria onlineStatusCriteria = new StatusCriteria(OnlineStatus.OFFLINE);
        Criteria orCriteria = new OrCriteria(carManufacturerCriteria, onlineStatusCriteria);

        return new AndCriteria(andCriteria, orCriteria);
    }

    private List<DriverDO> buildDriversList(){
        List<DriverDO> drivers = new ArrayList<>();
        DriverDO driver1 = new DriverDO("driver1","pass1");
        CarDo car = new CarDo("111111", new ManufacturerDo(""), EngineType.DIESEL, 1, false, 1);
        driver1.setCar(car);
        drivers.add(driver1);

        DriverDO driver2 = new DriverDO("driver2","pass2");
        CarDo car2 = new CarDo("111111", new ManufacturerDo(""), EngineType.GAS, 1, false, 1);
        driver2.setCar(car2);
        drivers.add(driver2);

        DriverDO driver3 = new DriverDO("driver3","pass3");
        drivers.add(driver3);

        return drivers;
    }

    @Test
    public void whenCarEngineValueIsNotValidThrowAnException() {
        CriteriaDTO root = new CriteriaDTO(AttributeType.CAR_ENGINE, "asdasd");

        try {
            CriteriaBuilder.build(root);
            fail();
        } catch (InvalidCriteriaValueException e) {
            assertEquals("Invalid engine type", e.getMessage());
        }
    }

    @Test
    public void whenCarManufacturerIdIsNotValidThrowAnException() {
        CriteriaDTO root = new CriteriaDTO(AttributeType.CAR_MANUFACTURER, "x");

        try {
            CriteriaBuilder.build(root);
            fail();
        } catch (InvalidCriteriaValueException e) {
            assertEquals("Invalid manufacturer id", e.getMessage());
        }
    }

    @Test
    public void whenOnlineStatusIsNotValidThrowAnException() {
        CriteriaDTO root = new CriteriaDTO(AttributeType.ONLINE_STATUS, "x");

        try {
            CriteriaBuilder.build(root);
            fail();
        } catch (InvalidCriteriaValueException e) {
            assertEquals("Invalid online status", e.getMessage());
        }
    }


}