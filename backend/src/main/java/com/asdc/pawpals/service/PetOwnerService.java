package com.asdc.pawpals.service;

import com.asdc.pawpals.dto.AnimalDto;
import com.asdc.pawpals.dto.PetAppointmentsDto;
import com.asdc.pawpals.dto.PetMedicalHistoryDto;
import com.asdc.pawpals.dto.PetOwnerDto;
import com.asdc.pawpals.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PetOwnerService {
    PetOwnerDto registerPetOwner(PetOwnerDto petOwner) throws UserNameNotFound, InvalidUserDetails, UserAlreadyExist;

    List<AnimalDto> retrieveAllPets(String ownerId) throws NoPetRegisterUnderPetOwner, UserNameNotFound;


    PetOwnerDto updatePetOwner(String id, PetOwnerDto petOwnerDto, MultipartFile image) throws UserNameNotFound, InvalidPetOwnerObject, InvalidImage, IOException;

    PetOwnerDto deletePetOwner(String id) throws UserNameNotFound;


    List<PetAppointmentsDto> retrievePetsAppointments(String ownerId) throws UserNameNotFound, NoPetRegisterUnderPetOwner;

    List<PetMedicalHistoryDto> retrievePetsMedicalHistory(String ownerId) throws UserNameNotFound, NoPetRegisterUnderPetOwner;
}
