package com.asdc.pawpals.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class InvalidAppointmentIdTest {

    @Test
    public void testInvalidAppointmentIdWithoutMessage() {
        InvalidAppointmentId ex = new InvalidAppointmentId();
        assertNull(ex.getMessage());
    }

    @Test
    public void testInvalidAppointmentIdWithMessage() {
        String errorMessage = "Invalid Appointment ID";
        InvalidAppointmentId ex = new InvalidAppointmentId(errorMessage);
        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    public void testInvalidAppointmentIdWithCause() {
        Exception cause = new Exception("Cause of Invalid Appointment ID");
        InvalidAppointmentId ex = new InvalidAppointmentId(cause);
        assertEquals(cause, ex.getCause());
    }

    @Test
    public void testInvalidAppointmentIdWithMessageAndCause() {
        String errorMessage = "Invalid Appointment ID";
        Exception cause = new Exception("Cause of Invalid Appointment ID");
        InvalidAppointmentId ex = new InvalidAppointmentId(errorMessage, cause);
        assertEquals(errorMessage, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}