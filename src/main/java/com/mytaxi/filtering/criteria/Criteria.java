package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.DriverDO;

import java.util.List;

public interface Criteria {
    List<DriverDO> meetCriteria(final List<DriverDO> drivers);
}
