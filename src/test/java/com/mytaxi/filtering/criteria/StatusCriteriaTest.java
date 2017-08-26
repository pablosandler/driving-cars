package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class StatusCriteriaTest {

    @Test
    public void whenCriteriaIsAppliedReturnOnlyTheItemsWhichMeetCriteria() {
        List<DriverDO> drivers = new ArrayList<>();
        DriverDO driver1 = new DriverDO("driver1","pass1");
        driver1.setOnlineStatus(OnlineStatus.ONLINE);
        drivers.add(driver1);
        DriverDO driver2 = new DriverDO("driver2","pass2");
        drivers.add(driver2);

        Criteria criteria = new StatusCriteria(OnlineStatus.ONLINE);
        List<DriverDO> result = criteria.meetCriteria(drivers);

        assertTrue(result.size()==1);
        assertEquals(driver1, result.get(0));
    }

    @Test
    public void whenCriteriaIsAppliedReturnOnlyTheItemsWhichMeetCriteriaEvenWheNullIsSentAsStatus() {
        List<DriverDO> drivers = new ArrayList<>();
        DriverDO driver1 = new DriverDO("driver1","pass1");
        driver1.setOnlineStatus(OnlineStatus.ONLINE);
        drivers.add(driver1);
        DriverDO driver2 = new DriverDO("driver2","pass2");
        drivers.add(driver2);

        Criteria criteria = new StatusCriteria(null);
        List<DriverDO> result = criteria.meetCriteria(drivers);

        assertTrue(result.size()==0);
    }

}