package com.asdc.pawpals.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InvalidObjectException;

import com.asdc.pawpals.exception.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.asdc.pawpals.utils.ApiResponse;

@SpringBootTest
public class GlobalExceptionalHandlerTest {

    @Mock
    private ApiResponse apiResponseMock;

    @InjectMocks
    private GlobalExceptionalHandler globalExceptionHandler;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGlobalUserExceptionHandlerForUserNotFound() {
        Exception e = new UserNameNotFound("User not found");
        when(apiResponseMock.getMessage()).thenReturn("User not found");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForUserNotFound(e);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidUser() {
        Exception e = new InvalidUserDetails("User Details in Invalid");
        when(apiResponseMock.getMessage()).thenReturn("User Details in Invalid");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidUser(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("User Details in Invalid", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForUserAlreadyExist() {
        Exception e = new UserAlreadyExist("User Already Registered in the System");
        when(apiResponseMock.getMessage()).thenReturn("User Already Registered in the System");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForUserAlreadyExist(e);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User Already Registered in the System", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForPetOwnerDoesNotExists() {
        Exception e = new PetOwnerAlreadyDoesNotExists("Pet owner does not exist");
        when(apiResponseMock.getMessage()).thenReturn("Pet owner does not exist");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForPetOwnerDoesNotExists(e);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Pet owner does not exist", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidOwnerID() {
        Exception e = new InvalidOwnerID("Invalid Owner Id");
        when(apiResponseMock.getMessage()).thenReturn("Invalid Owner Id");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidOwnerID(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid Owner Id", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForNoPetRegisterUnderPetOwner() {
        Exception e = new NoPetRegisterUnderPetOwner("No Pet register...");
        when(apiResponseMock.getMessage()).thenReturn("No Pet register...");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForNoPetRegisterUnderPetOwner(e);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No Pet register...", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForIOException() {
        Exception e = new IOException("IO Exception ");
        when(apiResponseMock.getMessage()).thenReturn("IO Exception ");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForIOException(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("IO Exception ", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInValidImage() {
        Exception e = new InvalidImage("Invalid Image");
        when(apiResponseMock.getMessage()).thenReturn("Invalid Image");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInValidImage(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid Image", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidAnimalId() {
        Exception e = new InvalidAnimalId("Invalid Image");
        when(apiResponseMock.getMessage()).thenReturn("Invalid Image");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidAnimalId(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid Image", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidVetID() {
        Exception e = new InvalidVetID("Invalid VetId");
        when(apiResponseMock.getMessage()).thenReturn("Invalid VetId");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidVetID(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid VetId", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidObjectException() {
        Exception e = new InvalidObjectException("Invalid object parameters");
        when(apiResponseMock.getMessage()).thenReturn("Invalid object parameters");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidObjectException(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid object parameters", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidAnimalObject() {
        Exception e = new InvalidAnimalObject("Invalid animal object parameters");
        when(apiResponseMock.getMessage()).thenReturn("Invalid animal object parameters");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidAnimalObject(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid animal object parameters", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidPetOwnerObject() {
        Exception e = new InvalidPetOwnerObject("Invalid pet owner object parameters");
        when(apiResponseMock.getMessage()).thenReturn("Invalid pet owner object parameters");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidPetOwnerObject(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid pet owner object parameters", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForInvalidAppointmentId() {
        Exception e = new InvalidAppointmentId("Invalid Appointment Id entered");
        when(apiResponseMock.getMessage()).thenReturn("Invalid Appointment Id entered");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForInvalidAppointmentId(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid Appointment Id entered", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }

    @Test
    public void testGlobalUserExceptionHandlerForNoAppointmentExist() {
        Exception e = new NoAppointmentExist("No Appointment exist for this vet");
        when(apiResponseMock.getMessage()).thenReturn("No Appointment exist for this vet");

        ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.GlobalUserExceptionHandlerForNoAppointmentExist(e);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("No Appointment exist for this vet", responseEntity.getBody().getMessage());
        assertEquals(false, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getBody());
    }
}
