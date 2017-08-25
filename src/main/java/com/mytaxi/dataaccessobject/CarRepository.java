package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.CarDo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CarRepository extends CrudRepository<CarDo, Long> {

    Optional<CarDo> findByLicensePlate(String licensePlate);

}
