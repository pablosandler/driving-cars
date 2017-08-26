package com.mytaxi.controller;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.CriteriaDTO;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.*;
import com.mytaxi.filtering.criteria.Criteria;
import com.mytaxi.filtering.util.CriteriaBuilder;
import com.mytaxi.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @DeleteMapping("/{driverId}")
    public void deleteDriver(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }


    @PutMapping("/{driverId}")
    public void updateLocation(
        @Valid @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }


    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }

    @PostMapping("/{driverId}/car/{carId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void selectCar(@Valid @PathVariable long driverId, @PathVariable long carId)
            throws EntityNotFoundException, IncorrectStatusException, CarAlreadyInUseException {
        driverService.selectCar(driverId, carId);
    }

    @DeleteMapping("/{driverId}/car")
    public void deselectCar(@Valid @PathVariable long driverId) throws EntityNotFoundException {
        driverService.deselectCar(driverId);
    }

    @PostMapping("/filter")
    public List<DriverDTO> filterDrivers(@Valid @RequestBody CriteriaDTO clientCriteria) throws InvalidCriteriaValueException {
        Criteria criteria = CriteriaBuilder.build(clientCriteria);
        return DriverMapper.makeDriverDTOList(driverService.findByCriteria(criteria));
    }

}
