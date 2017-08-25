package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.ManufacturerDo;
import org.springframework.data.repository.CrudRepository;

public interface ManufacturerRepository extends CrudRepository<ManufacturerDo, Long> {
}
