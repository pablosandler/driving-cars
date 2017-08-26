package com.mytaxi.filtering.criteria;

import com.mytaxi.domainobject.DriverDO;

import java.util.*;

public class OrCriteria implements Criteria {

    private Optional<Criteria> criteria;
    private Optional<Criteria> otherCriteria;

    public OrCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = Optional.ofNullable(criteria);
        this.otherCriteria = Optional.ofNullable(otherCriteria);
    }

    @Override
    public List<DriverDO> meetCriteria(List<DriverDO> drivers) {
        List<DriverDO> firstCriteriaItems = criteria.map(c -> c.meetCriteria(drivers))
                .orElse(drivers);

        List<DriverDO> otherCriteriaItems = otherCriteria.map(c -> c.meetCriteria(drivers))
                .orElse(drivers);

        Set<DriverDO> notDuplicatedElements = new HashSet<>();
        notDuplicatedElements.addAll(firstCriteriaItems);
        notDuplicatedElements.addAll(otherCriteriaItems);

        return new ArrayList<>(notDuplicatedElements);
    }
}
