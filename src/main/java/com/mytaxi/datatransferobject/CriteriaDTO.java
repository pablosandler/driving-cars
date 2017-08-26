package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.filtering.util.AttributeType;
import com.mytaxi.filtering.util.CriteriaType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CriteriaDTO {

    private AttributeType attribute;
    private String value;
    private CriteriaType criteriaType;
    private CriteriaDTO criteria;
    private CriteriaDTO otherCriteria;

    public CriteriaDTO() {}

    public CriteriaDTO(AttributeType attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public CriteriaDTO(AttributeType attribute, String value,
                       CriteriaType criteriaType,
                       CriteriaDTO criteria, CriteriaDTO otherCriteria) {
        this.attribute = attribute;
        this.value = value;
        this.criteriaType = criteriaType;
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    public AttributeType getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

    public CriteriaType getCriteriaType() {
        return criteriaType;
    }

    public CriteriaDTO getCriteria() {
        return criteria;
    }

    public CriteriaDTO getOtherCriteria() {
        return otherCriteria;
    }
}
