package com.asdc.pawpals.service.implementation;

import com.asdc.pawpals.Enums.AppointmentStatus;
import com.asdc.pawpals.Enums.ProfileStatus;
import com.asdc.pawpals.dto.*;
import com.asdc.pawpals.exception.InvalidAppointmentId;
import com.asdc.pawpals.exception.InvalidImage;
import com.asdc.pawpals.exception.NoAppointmentExist;
import com.asdc.pawpals.exception.UserNameNotFound;
import com.asdc.pawpals.model.*;
import com.asdc.pawpals.repository.AppointmentRepository;
import com.asdc.pawpals.repository.UserRepository;
import com.asdc.pawpals.repository.VetAvailabilityRepository;
import com.asdc.pawpals.repository.VetRepository;
import com.asdc.pawpals.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VetServiceImplTest {
    @Autowired
    VetServiceImpl vetService;

    VetRepository vetRepoMock;
    UserRepository userRepoMock;
    VetAvailabilityRepository vetAvailabilityRepoMock;
    AppointmentRepository aptRepoMock;

    MailServiceImpl mailServiceMock;

    @BeforeEach
    public void setup() {
        vetRepoMock = mock(VetRepository.class);
        vetService.vetRepository = vetRepoMock;

        userRepoMock = mock(UserRepository.class);
        vetService.userRepository = userRepoMock;

        vetAvailabilityRepoMock = mock(VetAvailabilityRepository.class);
        vetService.vetAvailabilityRepository = vetAvailabilityRepoMock;

        aptRepoMock = mock(AppointmentRepository.class);
        vetService.appointmentRepository = aptRepoMock;

        mailServiceMock = mock(MailServiceImpl.class);
        vetService.mailService = mailServiceMock;

    }

    @Test
    public void objectCreated() {
        assertNotNull(vetRepoMock);
    }

    @Test
    public void registerVet_shouldReturnTrueWhenVetIsRegistered() {
        //Arrange
        VetDto vetToRegister = new VetDto();
        Vet vet = new Vet();
        when(vetRepoMock.save(any(Vet.class))).thenReturn(vet);
        when(vetRepoMock.count()).thenReturn(1L).thenReturn(2L);
        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(new User()));
        doNothing().when(mailServiceMock).sendMail(eq(""), eq(""), eq(""));

        vetToRegister.setUserName("jDoe");

        //Act
        Boolean saved = vetService.registerVet(vetToRegister);
        //Assert
        assertTrue(saved);
    }

    @Test
    public void registerVet_shouldReturnFalseWhenVetIsNotRegistered() {
        //Arrange
        VetDto vetToRegister = new VetDto();
        Vet vet = new Vet();
        when(vetRepoMock.save(any(Vet.class))).thenReturn(vet);
        when(vetRepoMock.count()).thenReturn(1L).thenReturn(1L);
        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(new User()));
        vetToRegister.setUserName("jDoe");

        //Act
        Boolean saved = vetService.registerVet(vetToRegister);

        //Assert
        assertFalse(saved);
    }

    @Test
    public void registerVet_shouldThrowUserNotFoundException() {
        //Arrange
        VetDto vetToRegister = new VetDto();
        Vet vet = new Vet();
        when(vetRepoMock.save(any(Vet.class))).thenReturn(vet);
        when(vetRepoMock.count()).thenReturn(1L).thenReturn(1L);
        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        vetToRegister.setUserName("jDoe");

        //Act + Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> vetService.registerVet(vetToRegister));

        //Assert 2
        assertEquals("Please provide a valid username", exception.getMessage());
    }

    @Test
    public void getVetAvailability_shouldReturnVetAvailability() {
        //Arrange
        String userId = "jDoe";
        List<VetAvailability> vetAvailability = new ArrayList<>();
        VetAvailability avl = new VetAvailability();
        avl.setId(1);
        avl.setDayOfWeek("Monday");
        avl.setStartTime("10:00");
        avl.setEndTime("17:00");
        vetAvailability.add(avl);
        when(vetAvailabilityRepoMock.findByVet_User_UserId(anyString())).thenReturn(vetAvailability);

        //Act
        List<VetAvailabilityDto> actual = vetService.getVetAvailability(userId);

        //Assert
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getAvailabilityId());
    }

    @Test
    public void getVetAvailability_shouldReturnVetAvailabilityForVetId() {
        //Arrange
        Long vetId = 1L;
        List<VetAvailability> vetAvailability = new ArrayList<>();
        VetAvailability avl = new VetAvailability();
        avl.setId(1);
        avl.setDayOfWeek("Monday");
        avl.setStartTime("10:00");
        avl.setEndTime("17:00");
        vetAvailability.add(avl);
        when(vetAvailabilityRepoMock.findByVetId(anyLong())).thenReturn(vetAvailability);

        //Act
        List<VetAvailabilityDto> actual = vetService.getVetAvailability(vetId);

        //Assert
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getAvailabilityId());
    }

    @Test
    public void getVetAvailabilityOnSpecificDay_shouldReturnVetAvailabilityForUserId() {
        //Arrange
        String userId = "jDoe";
        List<VetAvailability> vetAvailability = new ArrayList<>();
        VetAvailability avl = new VetAvailability();
        avl.setId(1);
        avl.setDayOfWeek("Wednesday");
        avl.setStartTime("10:00");
        avl.setEndTime("17:00");
        vetAvailability.add(avl);
        when(vetAvailabilityRepoMock.findByVet_User_UserId(anyString())).thenReturn(vetAvailability);

        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setAnimal(new Animal());
        appointment.setVet(new Vet());
        appointment.setDate("15-03-2023");
        appointment.setStartTime("10:00");
        appointment.setEndTime("10:30");
        appointment.setStatus(AppointmentStatus.CONFIRMED.getLabel());
        appointments.add(appointment);
        when(aptRepoMock.findByVet_User_UserId(anyString())).thenReturn(appointments);

        //Act
        VetAvailabilityDto availability = vetService.getVetAvailabilityOnSpecificDay(userId, "15-03-2023");

        //Assert
        assertNotNull(availability);
        assertEquals(13, availability.getSlots().size());

    }

    @Test
    public void getVetAvailabilityOnSpecificDay_shouldReturnVetAvailabilityForVetId() {
        //Arrange
        Long vetId = 1L;
        List<VetAvailability> vetAvailability = new ArrayList<>();
        VetAvailability avl = new VetAvailability();
        avl.setId(1);
        avl.setDayOfWeek("Wednesday");
        avl.setStartTime("10:00");
        avl.setEndTime("17:00");
        vetAvailability.add(avl);
        when(vetAvailabilityRepoMock.findByVetId(anyLong())).thenReturn(vetAvailability);

        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setAnimal(new Animal());
        appointment.setVet(new Vet());
        appointment.setDate("15-03-2023");
        appointment.setStartTime("10:00");
        appointment.setEndTime("10:30");
        appointment.setStatus(AppointmentStatus.CONFIRMED.getLabel());
        appointments.add(appointment);
        when(aptRepoMock.findByVetId(anyLong())).thenReturn(appointments);

        //Act
        VetAvailabilityDto availability = vetService.getVetAvailabilityOnSpecificDay(vetId, "15-03-2023");

        //Assert
        assertNotNull(availability);
        assertEquals(13, availability.getSlots().size());

    }

    @Test
    public void getVetScheduleOnSpecificDay_shouldReturnScheduleForUserId() {
        //Arrange
        String userId = "jDoe";

        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setAnimal(new Animal());
        appointment.setVet(new Vet());
        appointment.setDate("15-03-2023");
        appointment.setStartTime("10:00");
        appointment.setEndTime("10:30");
        appointment.setStatus(AppointmentStatus.CONFIRMED.getLabel());
        appointments.add(appointment);
        when(aptRepoMock.findByVet_User_UserId(anyString())).thenReturn(appointments);

        //Act
        VetScheduleDto schedule = vetService.getVetScheduleOnSpecificDay(userId, "15-03-2023");

        //Assert
        assertNotNull(schedule);
        assertEquals(1, schedule.getSlotsBooked().size());
    }


    @Test
    public void testUpdateVet_Success() throws UserNameNotFound, IOException, InvalidImage, UserNameNotFound, InvalidImage, IOException {
        VetDto vetDto = new VetDto();
        vetDto.setClinicAddress("new clinic address");
        vetDto.setFirstName("hs");
        vetDto.setLastName("dndn");
        vetDto.setLicenseNumber("dd");
        vetDto.setProfileStatus(ProfileStatus.APPROVED.getLabel());

        String id = "vet1";

        Vet vet = new Vet();
        vet.setUser(new User());
        vet.setClinicAddress("old clinic address");
        vet.setLicenseNumber("sns");
        vet.setLastName("sinh");
        vet.setFirstName("sbb");


        when(vetRepoMock.findByUser_UserId(id)).thenReturn(Optional.of(vet));
        when(vetRepoMock.saveAndFlush(any(Vet.class))).thenReturn(vet);

        vetService.updateVet(vetDto, id, null);


    }

    @Test
    public void testUpdateVet_InvalidObjectException() throws UserNameNotFound, IOException, InvalidImage {
        String id = "vet1";
        InvalidObjectException exception = assertThrows(InvalidObjectException.class, () -> vetService.updateVet(null, id, null));
        assertNotNull(exception);
    }

    @Test
    public void testUpdateVet_UserNameNotFound() throws UserNameNotFound, IOException, InvalidImage {
        String id = "vet1";

        when(vetRepoMock.findByUser_UserId(id)).thenReturn(Optional.empty());
        UserNameNotFound exception = assertThrows(UserNameNotFound.class, () -> vetService.updateVet(new VetDto(), id, null));
        assertNotNull(exception);
    }

    @Test
    public void testChangeStatus_StatusChanged() throws InvalidAppointmentId {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1);
        appointmentDto.setDate("01-01-2023");
        appointmentDto.setStartTime("10:00");
        appointmentDto.setEndTime("11:00");
        appointmentDto.setStatus(Constants.STATUS[1]);
        appointmentDto.setAnimalId(1L);
        appointmentDto.setVetUserId("123456");

        int id = 1;

        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.CONFIRMED.getLabel());
        Animal animal = new Animal();
        PetOwner owner = new PetOwner();
        User user = new User();
        user.setEmail("abc@gmail.com");
        owner.setUser(user);
        animal.setOwner(owner);

        Vet vet = new Vet();
        vet.setClinicAddress("abab");
        appointment.setAnimal(animal);
        appointment.setVet(vet);

        when(aptRepoMock.findById(id)).thenReturn(Optional.of(appointment));
        when(aptRepoMock.saveAndFlush(any(Appointment.class))).thenReturn(appointment);

        vetService.changeStatus(appointmentDto, id);


    }

    @Test
    public void testChangeStatus_StatusChangedConfirmerd() throws InvalidAppointmentId {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1);
        appointmentDto.setDate("01-01-2023");
        appointmentDto.setStartTime("10:00");
        appointmentDto.setEndTime("11:00");
        appointmentDto.setStatus(Constants.STATUS[0]);
        appointmentDto.setAnimalId(1L);
        appointmentDto.setVetUserId("123456");

        int id = 1;

        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.CONFIRMED.getLabel());
        Animal animal = new Animal();
        PetOwner owner = new PetOwner();
        User user = new User();
        user.setEmail("abc@gmail.com");
        owner.setUser(user);
        animal.setOwner(owner);

        Vet vet = new Vet();
        vet.setClinicAddress("abab");
        appointment.setAnimal(animal);
        appointment.setVet(vet);

        when(aptRepoMock.findById(id)).thenReturn(Optional.of(appointment));
        when(aptRepoMock.saveAndFlush(any(Appointment.class))).thenReturn(appointment);

        vetService.changeStatus(appointmentDto, id);


    }

    @Test
    public void testGetVetByUserId_Success() throws UserNameNotFound {
        String id = "vet1";

        Vet vet = new Vet();

        when(vetRepoMock.findByUser_UserId(id)).thenReturn(Optional.of(vet));

        vetService.getVetByUserId(id);

    }

    @Test
    public void testGetVetByUserId_Failure() throws UserNameNotFound {
        String id = "1";

        Vet vet = new Vet();

        when(vetRepoMock.findByUser_UserId(id)).thenReturn(Optional.empty());

        UserNameNotFound exception = assertThrows(UserNameNotFound.class, () -> vetService.getVetByUserId(id));
        assertNotNull(exception);
    }


    @Test
    public void testRetrieveAllVets() {
        Vet vet1 = new Vet();
        vet1.setProfileStatus(AppointmentStatus.PENDING.getLabel());
        Vet vet2 = new Vet();
        vet2.setProfileStatus(AppointmentStatus.COMPLETED.getLabel());
        when(vetRepoMock.findAll()).thenReturn(Arrays.asList(vet1, vet2));

        List<VetDto> vetDtoList = vetService.retrieveAllVets();

        assertEquals(1, vetDtoList.size());
        assertEquals(AppointmentStatus.PENDING.getLabel(), vetDtoList.get(0).getProfileStatus());
    }

    @Test
    public void testRetrieveAllPets() throws UserNameNotFound, NoAppointmentExist {
        // Arrange
        String vetId = "testVetId";
        Vet vet = new Vet();
        vet.setId(1L);
        vet.setUser(new User());
        List<Appointment> appointments = new ArrayList<>();

        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setVet(vet);
        Animal animal = new Animal();
        PetOwner petOwner = new PetOwner();
        petOwner.setUser(new User());
        animal.setOwner(petOwner);
        animal.setMedicalHistories(Arrays.asList(new MedicalHistory()));
        appointment.setAnimal(animal);
        appointments.add(appointment);
        vet.setAppointment(appointments);
        List<Vet> vets = Collections.singletonList(vet);

        // Mock
        when(vetRepoMock.findByUser_UserId(any())).thenReturn(Optional.of(vet));
        when(vetRepoMock.findAll()).thenReturn(vets);

        // Act
        List<VetAppointmentDto> vetAppointmentDtos = vetService.retrieveAllPets(vetId);

        // Assert
        assertEquals(1, vetAppointmentDtos.size());
    }
}
