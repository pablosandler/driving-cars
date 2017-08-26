package com.mytaxi.filtering.criteria;


import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class AndCriteriaTest {

    @Test
    public void whenCriteriaIsAppliedOnlyThoseItemsWhichHaveBothCriteriaAreReturned() {
        List<DriverDO> drivers = new ArrayList<>();
        DriverDO driver1 = new DriverDO("driver1","pass1");
        driver1.setOnlineStatus(OnlineStatus.ONLINE);
        drivers.add(driver1);
        DriverDO driver2 = new DriverDO("driver2","pass2");
        driver2.setOnlineStatus(OnlineStatus.ONLINE);
        drivers.add(driver2);

        Criteria statusCriteria = mock(StatusCriteria.class);
        Criteria engineCriteria = mock(CarEngineCriteria.class);

        when(statusCriteria.meetCriteria(anyList())).thenReturn(drivers);

        List<DriverDO> engineCriteriaResponse = new ArrayList<>();
        engineCriteriaResponse.add(driver1);
        when(engineCriteria.meetCriteria(anyList())).thenReturn(engineCriteriaResponse);

        Criteria andCriteria = new AndCriteria(statusCriteria, engineCriteria);
        List<DriverDO> result = andCriteria.meetCriteria(drivers);
        assertEquals(driver1, result.get(0));
    }

}