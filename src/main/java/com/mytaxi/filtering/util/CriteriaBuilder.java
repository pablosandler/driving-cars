package com.mytaxi.filtering.util;


import com.mytaxi.datatransferobject.CriteriaDTO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.InvalidCriteriaValueException;
import com.mytaxi.filtering.criteria.*;

public class CriteriaBuilder {

    public static Criteria build(CriteriaDTO node) throws InvalidCriteriaValueException {
        Criteria rootCriteria = null;

        if(node==null){
            return null;
        }

        if(CriteriaType.AND==node.getCriteriaType()){
            Criteria criteria1 = build(node.getCriteria());
            Criteria criteria2 = build(node.getOtherCriteria());
            rootCriteria = new AndCriteria(criteria1, criteria2);

        } else if(CriteriaType.OR==node.getCriteriaType()){
            Criteria criteria1 = build(node.getCriteria());
            Criteria criteria2 = build(node.getOtherCriteria());
            rootCriteria = new OrCriteria(criteria1, criteria2);

        } else {
            AttributeType attribute = node.getAttribute();
            String value = node.getValue();

            rootCriteria = buildCriteria(attribute, value);
        }

        return rootCriteria;
    }

    private static Criteria buildCriteria(AttributeType attribute, String value) throws InvalidCriteriaValueException {
        Criteria criteria = null;
        switch(attribute){
            case CAR_ENGINE:
                EngineType engine = getEngineType(value);
                criteria = new CarEngineCriteria(engine);
                break;

            case CAR_MANUFACTURER:
                Long manufacturerId = getManufacturerId(value);
                criteria = new CarManufacturerCriteria(manufacturerId);
                break;

            case ONLINE_STATUS:
                OnlineStatus status = getOnlineStatus(value);
                criteria =  new StatusCriteria(status);
                break;
        }

        return criteria;
    }

    private static EngineType getEngineType(String value) throws InvalidCriteriaValueException {
        EngineType engine = null;
        if(null!=value){
            try {
                engine = EngineType.valueOf(value);
            } catch(IllegalArgumentException e){
                throw new InvalidCriteriaValueException("Invalid engine type");
            }

        }
        return engine;
    }

    private static OnlineStatus getOnlineStatus(String value) throws InvalidCriteriaValueException {
        OnlineStatus status = null;
        if(null!=value){
            try {
                status = OnlineStatus.valueOf(value);
            } catch(IllegalArgumentException e){
                throw new InvalidCriteriaValueException("Invalid online status");
            }

        }
        return status;
    }


    private static long getManufacturerId(String value) throws InvalidCriteriaValueException {
        Long manufacturerId = null;
        if(null!=value){
            try{
                manufacturerId = Long.parseLong(value);
            } catch (NumberFormatException e){
                throw new InvalidCriteriaValueException("Invalid manufacturer id");
            }
        }
        return manufacturerId;
    }

}
