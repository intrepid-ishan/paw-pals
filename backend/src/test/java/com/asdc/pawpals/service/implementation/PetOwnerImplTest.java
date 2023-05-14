package com.asdc.pawpals.service.implementation;

import com.asdc.pawpals.dto.AnimalDto;
import com.asdc.pawpals.dto.PetAppointmentsDto;
import com.asdc.pawpals.dto.PetMedicalHistoryDto;
import com.asdc.pawpals.dto.PetOwnerDto;
import com.asdc.pawpals.exception.*;
import com.asdc.pawpals.model.*;
import com.asdc.pawpals.repository.PetOwnerRepository;
import com.asdc.pawpals.repository.UserRepository;
import com.asdc.pawpals.utils.Constants;
import com.asdc.pawpals.utils.Transformations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PetOwnerImplTest {
    @Mock
    PetOwnerRepository petOwnerRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MailServiceImpl mailService;

    @InjectMocks
    PetOwnerImpl petOwnerImpl;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerPetOwner()  // fix
            throws UserNameNotFound, InvalidUserDetails, UserAlreadyExist {
        // Arrange
        PetOwnerDto petOwnerDto = new PetOwnerDto();
        petOwnerDto.setUserName("testUser");
        MultipartFile invalidImage = null;
        MockMultipartFile image = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[]{12});


        // Set the invalid image to the animal DTO
        petOwnerDto.setPhotoUrl(new Byte[]{12});
        petOwnerDto.setPhoneNo("1234567890");
        petOwnerDto.setAddress("Test Address");
        petOwnerDto.setPassword("password");
        petOwnerDto.setFirstName("John");
        petOwnerDto.setLastName("Doe");
        petOwnerDto.setRole("ROLE_USER");
        petOwnerDto.setEmail("john.doe@example.com");

        PetOwner owner = new PetOwner();
        User user = new User();
        user.setUserId("testUser");
        owner.setUser(user);

        when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
        when(petOwnerRepository.existsByUser_UserId("testUser")).thenReturn(false);
        when(petOwnerRepository.save(any(PetOwner.class)))
                .thenReturn(owner);
        doNothing().when(mailService).sendMail(any(), any(), any());

        // Act
        PetOwnerDto response = petOwnerImpl.registerPetOwner(petOwnerDto);

        // Assert
        assertNotNull(response);
        assertEquals(petOwnerDto.getUsername(), response.getUsername());

    }

    @Test
    public void testInvalidUserDetails() {
        // Arrange
        PetOwnerDto petOwnerDto = new PetOwnerDto();
        petOwnerDto.setUserName("testUser");

        // Act and Assert
        assertThrows(
                InvalidUserDetails.class,
                () -> petOwnerImpl.registerPetOwner(petOwnerDto)
        );
    }

    @Test
    public void testDeletePetOwnerWithValidId() throws UserNameNotFound {
        // Arrange
        String userId = "testUser";
        PetOwner petOwner = new PetOwner();
        petOwner.setId(1L);
        User user = new User();
        user.setUserId(userId);
        petOwner.setUser(user);
        when(petOwnerRepository.findByUser_UserId(userId))
                .thenReturn(Optional.of(petOwner));
        PetOwnerDto expectedResponse = Transformations.MODEL_TO_DTO_CONVERTER.petOwner(
                petOwner
        );

        // Act
        PetOwnerDto response = petOwnerImpl.deletePetOwner(userId);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getUsername(), response.getUsername());
        assertEquals(expectedResponse.getPhotoUrl(), response.getPhotoUrl());
        assertEquals(expectedResponse.getPhoneNo(), response.getPhoneNo());
        assertEquals(expectedResponse.getAddress(), response.getAddress());
    }

    @Test
    public void testDeletePetOwnerWithInvalidId() throws UserNameNotFound {
        // Arrange
        String userId = "invalidUser";
        when(petOwnerRepository.findByUser_UserId(userId))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
                UserNameNotFound.class,
                () -> petOwnerImpl.deletePetOwner(userId)
        );
    }

    @Test
    public void testRetrievePetsAppointmentsWithValidInput()
            throws UserNameNotFound, NoPetRegisterUnderPetOwner {
        // Arrange
        String ownerId = "testUser";
        PetOwner petOwner = new PetOwner();
        List<Animal> animals = new ArrayList<>();
        Animal animal1 = new Animal();
        animal1.setId(1L);
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Doe");
        vet.setLicenseNumber("123456");
        vet.setClinicAddress("123 Main St.");
        vet.setExperience(5);
        vet.setQualification("DVM");
        vet.setProfileStatus("ACTIVE");
        vet.setPhoneNo("555-1234");
        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        appointment1.setAnimal(animal1);
        appointment1.setVet(vet);
        appointment1.setDate("09/12/2021");
        appointment1.setStartTime("09:34");
        appointment1.setEndTime("23:23");
        appointment1.setStatus(Constants.STATUS[2]);
        List<Appointment> appointments = new ArrayList<>();
        animal1.setAppointment(appointments);
        animals.add(animal1);
        petOwner.setAnimals(animals);

        when(petOwnerRepository.findByUser_UserId(ownerId))
                .thenReturn(Optional.of(petOwner));

        // Act
        List<PetAppointmentsDto> petAppointmentsDtos = petOwnerImpl.retrievePetsAppointments(
                ownerId
        );

        // Assert
        assertEquals(0, petAppointmentsDtos.size());
    }

    @Test
    public void testRetrievePetsMedicalHistory() throws UserNameNotFound, NoPetRegisterUnderPetOwner {
        // Setup
        String ownerId = "123";
        PetOwner petOwner = new PetOwner();
        petOwner.setFirstName("John");
        petOwner.setLastName("Doe");
        User user = new User();
        user.setUserId(ownerId);
        petOwner.setUser(user);
        Animal animal = new Animal();
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setVet(new Vet());
        animal.setOwner(petOwner);
        animal.setMedicalHistories(List.of(medicalHistory));
        petOwner.setAnimals(List.of(animal));
        when(petOwnerRepository.findByUser_UserId(ownerId)).thenReturn(Optional.of(petOwner));

        // Execution
        List<PetMedicalHistoryDto> result = petOwnerImpl.retrievePetsMedicalHistory(ownerId);

        // Verification
        assert !result.isEmpty();
        PetMedicalHistoryDto dto = result.get(0);
        assert dto.getAnimalDto().getId() == animal.getId();
    }

    @Test
    public void testRetrieveAllPetsSuccess() throws NoPetRegisterUnderPetOwner, UserNameNotFound {
        // Mock the PetOwnerRepository to return the valid PetOwner
        PetOwner validPetOwner = new PetOwner();
        validPetOwner.setId(1L);
        validPetOwner.setFirstName("John");
        validPetOwner.setLastName("Doe");
        List<Animal> animals = new ArrayList<>();
        Animal animal = new Animal();
        animals.add(animal);
        validPetOwner.setUser(new User());
        animal.setOwner(validPetOwner);
        validPetOwner.setAnimals(animals);
        when(petOwnerRepository.findByUser_UserId(any())).thenReturn(Optional.of(validPetOwner));

        // Call the method and assert the response
        List<AnimalDto> response = petOwnerImpl.retrieveAllPets("vet1");
        assertEquals(1, response.size());


    }

    @Test(expected = UserNameNotFound.class)
    public void testRetrieveAllPetsInvalidOwnerId() throws NoPetRegisterUnderPetOwner, UserNameNotFound {
        // Mock the PetOwnerRepository to return an empty Optional
        when(petOwnerRepository.findByUser_UserId(any())).thenReturn(Optional.empty());

        // Call the method with an invalid ownerId and expect a UserNameNotFound exception to be thrown
        petOwnerImpl.retrieveAllPets("validOwnerId");
    }

    @Test
    public void testUpdatePetOwnerWithValidInput() throws Exception {
        PetOwnerDto petOwnerDto = new PetOwnerDto();
        petOwnerDto.setFirstName("John");
        petOwnerDto.setLastName("Doe");
        petOwnerDto.setAddress("123 Main St.");
        petOwnerDto.setPhoneNo("555-555-1212");

        MockMultipartFile image = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[]{12});


        PetOwner petOwner = new PetOwner();
        petOwner.setFirstName("Jane");
        petOwner.setLastName("Doe");
        petOwner.setAddress("456 Main St.");
        petOwner.setPhoneNo("555-555-1212");
        petOwner.setUser(new User());
        when(petOwnerRepository.findByUser_UserId(any())).thenReturn(Optional.of(petOwner));
        when(petOwnerRepository.saveAndFlush(any(PetOwner.class))).thenReturn(petOwner);

        PetOwnerDto updatedPetOwnerDto = petOwnerImpl.updatePetOwner("1", petOwnerDto, image);

        assertEquals(petOwnerDto.getFirstName(), updatedPetOwnerDto.getFirstName());
        assertEquals(petOwnerDto.getLastName(), updatedPetOwnerDto.getLastName());
        assertEquals(petOwnerDto.getAddress(), updatedPetOwnerDto.getAddress());
        assertEquals(petOwnerDto.getPhoneNo(), updatedPetOwnerDto.getPhoneNo());
    }

    @Test(expected = InvalidPetOwnerObject.class)
    public void testUpdatePetOwnerWithNullPetOwnerDto() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[]{12});
        petOwnerImpl.updatePetOwner("1", null, image);
    }

    @Test(expected = UserNameNotFound.class)
    public void testUpdatePetOwnerWithInvalidUserId() throws Exception {
        PetOwnerDto petOwnerDto = new PetOwnerDto();
        MockMultipartFile image = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[]{12});
        when(petOwnerRepository.findByUser_UserId(any())).thenReturn(Optional.empty());
        petOwnerImpl.updatePetOwner("1", petOwnerDto, image);
    }
}
