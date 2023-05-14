package com.asdc.pawpals.controller;

import com.asdc.pawpals.Enums.AppointmentStatus;
import com.asdc.pawpals.dto.*;
import com.asdc.pawpals.exception.InvalidAppointmentId;
import com.asdc.pawpals.exception.InvalidImage;
import com.asdc.pawpals.exception.NoAppointmentExist;
import com.asdc.pawpals.exception.UserNameNotFound;
import com.asdc.pawpals.service.implementation.VetServiceImpl;
import com.asdc.pawpals.utils.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VetControllerTest {
    @Autowired
    VetController vetController;
    VetServiceImpl vetServiceMock;

    @BeforeEach
    public void setup() {
        vetServiceMock = mock(VetServiceImpl.class);
        vetController.vetService = vetServiceMock;
    }

    @Test
    public void objectCreated() {
        assertNotNull(vetController);
    }

//    @Test
//    public void shouldReturnGreeting() throws UserNameNotFound {
//        assertEquals("Hello 1", vetController.getVetById("Vet_1"));
//    }

    // @Test
    // public void shouldRegisterVet(){
    //     //Arrange
    //     VetDto vetToRegister = new VetDto();
    //     when(vetServiceMock.registerVet(any(VetDto.class))).thenReturn(true);
    //     vetToRegister.setUserName("jDoe");
    //     //Act
    //     ResponseEntity<String> response = vetController.registerVet(vetToRegister);
    //     //Assert
    //     assertEquals("jDoe", response.getBody());
    // }

    // @Test
    // public void shouldReturnInternalServerError(){
    //     //Arrange
    //     VetDto vetToRegister = new VetDto();
    //     when(vetServiceMock.registerVet(any(VetDto.class))).thenReturn(false);
    //     vetToRegister.setUserName("jDoe");
    //     //Act
    //     ResponseEntity<String> response = vetController.registerVet(vetToRegister);
    //     //Assert
    //     assertEquals(500, response.getStatusCode().value());
    // }

    // @Test
    // public void shouldReturnBadRequestWhenUserNotFound(){
    //     //Arrange
    //     VetDto vetToRegister = new VetDto();
    //     when(vetServiceMock.registerVet(any(VetDto.class))).thenThrow(new UsernameNotFoundException("Invalid Username"));
    //     vetToRegister.setUserName("jDoe");
    //     //Act
    //     ResponseEntity<String> response = vetController.registerVet(vetToRegister);
    //     //Assert
    //     assertEquals("User name provided is invalid", response.getBody());
    //     assertEquals(400, response.getStatusCode().value()); 
    // }

    // @Test
    // public void shouldReturnBadRequestWhenInvalidInput(){
    //     //Act
    //     ResponseEntity<String> response = vetController.registerVet("Invalid Input");
    //     //Assert
    //     assertEquals("Invalid input provided", response.getBody());
    //     assertEquals(400, response.getStatusCode().value()); 
    // }

    @Test
    public void shouldReturnVetAvailability() {
        //Arrange
        String userId = "jDoe";
        VetAvailabilityDto availability = new VetAvailabilityDto();
        availability.setAvailabilityId(1);
        when(vetServiceMock.getVetAvailabilityOnSpecificDay(anyString(), anyString())).thenReturn(availability);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("date", "13-03-2023");
        //Act
        ResponseEntity<VetAvailabilityDto> actual = vetController.getAvailability(userId, requestBody);

        //Assert
        assertEquals(1, actual.getBody().getAvailabilityId());
    }

    @Test
    public void shouldReturnBadResponseWhenInvalidVetAvailability() {
        //Arrange
        String userId = "jDoe";
        //Act
        ResponseEntity<VetAvailabilityDto> actual = vetController.getAvailability(userId, "Invalid RequestBody");

        //Assert
        assertEquals(400, actual.getStatusCode().value());
    }

    @Test
    public void shouldReturnBadResponseWhenInvalidUserName() {
        //Arrange
        String userId = null;
        //Act
        ResponseEntity<VetAvailabilityDto> actual = vetController.getAvailability(userId, new HashMap<>());

        //Assert
        assertEquals(400, actual.getStatusCode().value());
    }

    @Test
    public void shouldReturnBadResponseWhenUserNameEmpty() {
        //Arrange
        String userId = "";
        //Act
        ResponseEntity<VetAvailabilityDto> actual = vetController.getAvailability(userId, new HashMap<>());

        //Assert
        assertEquals(400, actual.getStatusCode().value());
    }

    @Test
    public void shouldReturnVetSchedule() {
        //Arrange
        String userId = "jDoe";
        VetScheduleDto schedule = new VetScheduleDto();
        schedule.setVetUserId("1");
        schedule.setSlotsBooked(Arrays.asList(Pair.of("10:00", "10:30")));
        when(vetServiceMock.getVetScheduleOnSpecificDay(anyString(), anyString())).thenReturn(schedule);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("date", "13-03-2023");
        //Act
        ResponseEntity<VetScheduleDto> actual = vetController.getVetSchedule(userId, requestBody);

        //Assert
        assertEquals("1", actual.getBody().getVetUserId());
        assertNotNull(actual.getBody().getSlotsBooked());
        assertEquals(1, actual.getBody().getSlotsBooked().size());
    }

    @Test
    public void shouldReturnBadResponseWhenInvalidVetScheduleReqBody() {
        //Arrange
        String userId = "jDoe";
        //Act
        ResponseEntity<VetScheduleDto> actual = vetController.getVetSchedule(userId, "Invalid RequestBody");

        //Assert
        assertEquals(400, actual.getStatusCode().value());
    }

    @Test
    public void shouldReturnBadResponseWhenInvalidUserNameForVetSchedule() {
        //Arrange
        String userId = null;
        //Act
        ResponseEntity<VetScheduleDto> actual = vetController.getVetSchedule(userId, new HashMap<>());

        //Assert
        assertEquals(400, actual.getStatusCode().value());
    }

    @Test
    public void shouldReturnBadResponseWhenUserNameEmptyForVetSchedule() {
        //Arrange
        String userId = "";
        //Act
        ResponseEntity<VetScheduleDto> actual = vetController.getVetSchedule(userId, new HashMap<>());

        //Assert
        assertEquals(400, actual.getStatusCode().value());
    }


    @Test
    public void testPostAvailabilitySuccess() throws UserNameNotFound, InvalidObjectException {
        List<VetAvailabilityDto> validRequestBody;
        validRequestBody = new ArrayList<>();
        VetAvailabilityDto vetAvailabilityDto = new VetAvailabilityDto();

        validRequestBody.add(vetAvailabilityDto);
        // Mock the service layer method to return the same list
        when(vetServiceMock.postVetAvailability(validRequestBody)).thenReturn(validRequestBody);


        // Call the method and assert the response
        ResponseEntity<ApiResponse> response = vetController.postAvailability(validRequestBody);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void registerVetBadRequest() throws InvalidImage, IOException {
        // Create a mock VetDto object and requestBody map
        VetDto vetDto = new VetDto();
        vetDto.setUserName("testvet");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vet", vetDto);

        // Mock the service layer method to return true
        when(vetServiceMock.registerVet(vetDto)).thenReturn(true);

//        // Mock the ObjectMapperWrapper to return the same VetDto object
//        when(objectMapperWrapper.convertValue(requestBody.get("vet"), VetDto.class))
//                .thenReturn(vetDto);

        // Call the method and assert the response
        byte[] fileContent = "Mock clinic photo".getBytes();
        MockMultipartFile clinicPhoto = new MockMultipartFile(
                "clinicPhoto",
                "clinicPhoto.jpg",
                "image/jpeg",
                fileContent
        );
        ResponseEntity<String> response = vetController.registerVet(requestBody, clinicPhoto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testChangeStatusSuccess() throws InvalidAppointmentId {
        AppointmentDto validRequestBody = new AppointmentDto();
        validRequestBody.setStatus(AppointmentStatus.COMPLETED.getLabel());

        // Mock the service layer method to return the same AppointmentDto
        when(vetServiceMock.changeStatus(any(), any())).thenReturn(validRequestBody);

        // Call the method and assert the response
        ResponseEntity<ApiResponse> response = vetController.changeStatus(validRequestBody, 1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ApiResponse responseBody = response.getBody();
        assertTrue(responseBody.isSuccess());
        assertFalse(responseBody.isError());
        assertEquals(validRequestBody, responseBody.getBody());
        assertEquals("Successful change status to approve", responseBody.getMessage());
    }


    @Test
    public void testGetPetsByOwnerId() throws UserNameNotFound, NoAppointmentExist {
        List<VetAppointmentDto> validRequestBody = new ArrayList<>();

        when(vetServiceMock.retrieveAllPets(any())).thenReturn(validRequestBody);
        ResponseEntity<ApiResponse> response = vetController.getVetById("vet1");
        ApiResponse responseBody = response.getBody();
        assertTrue(responseBody.isSuccess());
        assertFalse(responseBody.isError());

    }



    @Test
    public void testGetVetsByPendingStatus() {
        List<VetDto> dtos = new ArrayList<>();
        when(vetServiceMock.retrieveAllVets()).thenReturn(dtos);
        ResponseEntity<ApiResponse> response = vetController.getVetsByPendingStatus();
        ApiResponse responseBody = response.getBody();
        assertTrue(responseBody.isSuccess());
        assertFalse(responseBody.isError());
    }



    @Test
    public void testUpdateVet() throws UserNameNotFound, InvalidImage, IOException {
        VetDto vetDto=new VetDto();
        vetDto.setPhoneNo("123");
        byte[] fileContent = "Mock clinic photo".getBytes();
        MockMultipartFile clinicPhoto = new MockMultipartFile(
                "clinicPhoto",
                "clinicPhoto.jpg",
                "image/jpeg",
                fileContent
        );
        String id="vet1";
        when(vetServiceMock.updateVet(vetDto,id,clinicPhoto)).thenReturn(vetDto);
        ResponseEntity<ApiResponse> response = vetController.updateVet(vetDto,id,clinicPhoto);
        ApiResponse responseBody = response.getBody();
        assertTrue(responseBody.isSuccess());
        assertFalse(responseBody.isError());
    }

    @Test
    public void testUpdateVetStatus() throws UserNameNotFound, InvalidImage, IOException {
        VetDto vetDto=new VetDto();
        vetDto.setPhoneNo("123");
        String id="vet1";
        when(vetServiceMock.updateProfileStatus(vetDto,id)).thenReturn(vetDto);
        ResponseEntity<ApiResponse> response = vetController.updateVetStatus(vetDto,id);
        ApiResponse responseBody = response.getBody();
        assertTrue(responseBody.isSuccess());
        assertFalse(responseBody.isError());
    }
}
