package com.mytaxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDo;
import com.mytaxi.domainobject.ManufacturerDo;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
public class CarControllerTest {

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CarController(carService)).build();
    }

    @Test
    public void whenCreatingCarReturnNewObject() throws Exception {
        CarDTO carDto = new CarDTO(2L, "111111", 1, false, 1, EngineType.DIESEL, 1L);
        ManufacturerDo manufacturer = new ManufacturerDo("name");
        CarDo car = new CarDo("111111", manufacturer, EngineType.DIESEL, 1, false, 1);
        when(carService.create("111111", 1L, 1, 1, EngineType.DIESEL, false)).thenReturn(car);

        MvcResult result = this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        CarDTO carResponse = asObject(content);
        assertEquals("111111", carResponse.getLicensePlate());
        assertEquals(EngineType.DIESEL, carResponse.getEngineType());
        assertEquals(1, carResponse.getRating());
        assertEquals(1, carResponse.getSeatCount());
    }


    @Test
    public void whenCreatingCarReturnAnErrorIfLicensePlateIsNull() throws Exception {
        CarDTO carDto = new CarDTO(null, null, 1, false, 1, EngineType.DIESEL, 1L);

        this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreatingCarReturnAnErrorIfLicenseDoesNotHaveCorrectLength() throws Exception {
        CarDTO carDto = new CarDTO(null, "asd", 1, false, 1, EngineType.DIESEL, 1L);

        this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreatingCarReturnAnErrorIfSeatCountIsLowerThan() throws Exception {
        CarDTO carDto = new CarDTO(null, "asdqwe", -3, false, 1, EngineType.DIESEL, 1L);

        this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreatingCarReturnAnErrorIfEngineTypeIsNull() throws Exception {
        CarDTO carDto = new CarDTO(null, "asdqwe", 3, false, 1, null, 1L);

        this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreatingCarReturnAnErrorIfRatingIsLowerThanOne() throws Exception {
        CarDTO carDto = new CarDTO(null, "asdqwe", 3, false, 0, EngineType.DIESEL, 1L);

        this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreatingCarReturnAnErrorIfManufacturerIsNull() throws Exception {
        CarDTO carDto = new CarDTO(null, "asdqwe", 3, false, 0, EngineType.DIESEL, null);

        this.mockMvc.perform(post("/v1/cars").accept(contentType).contentType(contentType).content(asJsonString(carDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenFindingACarReturnAnErrorIfCarIsNotFound() throws Exception {
        when(carService.find(1L)).thenThrow(new EntityNotFoundException("error"));

        this.mockMvc.perform(get("/v1/cars/1").accept(contentType).contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenFindingACarReturnReturnItIfFound() throws Exception {
        CarDo car = new CarDo("license", new ManufacturerDo("name"), EngineType.DIESEL, 1, false, 1);

        when(carService.find(1L)).thenReturn(car);

        MvcResult result = this.mockMvc.perform(get("/v1/cars/1").accept(contentType).contentType(contentType))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        CarDTO carDto = asObject(content);
        assertEquals(null, carDto.getId());
        assertEquals(EngineType.DIESEL, carDto.getEngineType());
        assertEquals("license", carDto.getLicensePlate());
        assertEquals(1, carDto.getRating());
        assertEquals(1, carDto.getSeatCount());
        assertEquals(false, carDto.isConvertible());
        assertEquals(null, carDto.getManufacturer());
    }

    @Test
    public void whenFindingCarsReturnListOfFoundCars() throws Exception {
        List<CarDo> carDos = new ArrayList<>();
        CarDo car1 = new CarDo("1", new ManufacturerDo("Ford"), EngineType.DIESEL, 1, false, 1);
        carDos.add(car1);
        CarDo car2 = new CarDo("2", new ManufacturerDo("Chevrolet"), EngineType.GAS, 2, true, 2);
        carDos.add(car2);

        when(carService.find()).thenReturn(carDos);

        MvcResult result = this.mockMvc.perform(get("/v1/cars")
                .accept(contentType).contentType(contentType))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        List<CarDTO> carDtos = asListOfObjects(content);

        List<CarDTO> expectedDtos = new ArrayList<>();
        CarDTO carDto1 = new CarDTO(null, "1", 1, false, 1, EngineType.DIESEL, null);
        expectedDtos.add(carDto1);
        CarDTO carDto2 = new CarDTO(null, "2", 2, true, 2, EngineType.GAS, null);
        expectedDtos.add(carDto2);

        assertArrayEquals(expectedDtos.toArray(), carDtos.toArray());
    }

    @Test
    public void whenDeletingACarReturnOKIfSuccessful() throws Exception {

        this.mockMvc.perform(delete("/v1/cars/1").accept(contentType).contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdatingCarThrowAnErrorIfCarIsNotFound() throws Exception {
        when(carService.update(1L, 1)).thenThrow(new EntityNotFoundException("error"));

        this.mockMvc.perform(put("/v1/cars/1?rating=1").accept(contentType).contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdatingCarThrowAnErrorIfRatingIsOutOfRange() throws Exception {
        this.mockMvc.perform(put("/v1/cars/1?rating=10").accept(contentType).contentType(contentType))
                .andExpect(status().isBadRequest());
    }


    public static String asJsonString(final CarDTO car) {
        try {
            return new ObjectMapper().writeValueAsString(car);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CarDTO asObject(String car) {
        try {
            return new ObjectMapper().readValue(car, CarDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CarDTO> asListOfObjects(String cars) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cars, mapper.getTypeFactory().constructCollectionType(List.class, CarDTO.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}