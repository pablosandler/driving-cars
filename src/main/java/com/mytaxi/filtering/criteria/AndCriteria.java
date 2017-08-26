package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.DriverDO;

import java.util.List;
import java.util.Optional;

public class AndCriteria implements Criteria {

    private Optional<Criteria> criteria;
    private Optional<Criteria> otherCriteria;

    public AndCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = Optional.ofNullable(criteria);
        this.otherCriteria = Optional.ofNullable(otherCriteria);
    }

    @Override
    public List<DriverDO> meetCriteria(List<DriverDO> drivers) {
        List<DriverDO> firstCriteriaItems = criteria.map(c -> c.meetCriteria(drivers))
                .orElse(drivers);

        return otherCriteria.map(c -> c.meetCriteria(firstCriteriaItems))
                .orElse(firstCriteriaItems);
    }

}
