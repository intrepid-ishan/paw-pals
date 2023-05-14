import React from 'react';

import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {
  Landing,
  SignIn,
  SignUp,
  Root,
  PetOwner,
  PetOwnerHome,
  Vet,
  VetHome,
  Admin,
  AdminHome,
  PetHealthAndDiseaseInfo,
  PetMedicalRecord,
  PetOwnerAllVets,
  AboutUs,
  ContactUs,
  AdminManageVets,
  AdminAllPetOwners,
  AdminAllVets,
  PetOwnerAppointments,
  PetOwnerManagePets
} from '@src/pages';

const RoutesComp = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Root />}>
          <Route path="/" element={<Landing />} />
          <Route path="signin" element={<SignIn />} />
          <Route path="signup" element={<SignUp />} />
          <Route path="about-us" element={<AboutUs />} />
          <Route path="contact-us" element={<ContactUs />} />
          <Route path="pet-owner" element={<PetOwner />}>
            <Route path="home" element={<PetOwnerHome />} />
            <Route path="manage-pets" element={<PetOwnerManagePets />} />
            <Route path="my-appointments" element={<PetOwnerAppointments />} />
            <Route path="medical-record" element={<PetMedicalRecord />} />
            <Route path="all-vets" element={<PetOwnerAllVets />} />
            <Route
              path="pet-health-and-diseases"
              element={<PetHealthAndDiseaseInfo />}
            />
          </Route>
          <Route path="vet" element={<Vet />}>
            <Route path="home" element={<VetHome />} />
          </Route>
          <Route path="admin" element={<Admin />}>
            <Route path="home" element={<AdminHome />} />
            <Route path="manage-vets" element={<AdminManageVets />} />
            <Route path="all-vets" element={<AdminAllVets />} />
            <Route path="all-pet-owners" element={<AdminAllPetOwners />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default RoutesComp;
