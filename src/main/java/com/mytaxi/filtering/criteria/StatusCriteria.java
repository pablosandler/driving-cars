package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;

import java.util.List;
import java.util.stream.Collectors;

public class StatusCriteria implements Criteria {

    private final OnlineStatus onlineStatus;

    public StatusCriteria(OnlineStatus status) {
        this.onlineStatus = status;
    }

    @Override
    public List<DriverDO> meetCriteria(final List<DriverDO> drivers) {
        return drivers.stream()
                .filter(d -> onlineStatus==d.getOnlineStatus())
                .collect(Collectors.toList());
    }
}
