package com.yferdin.pigeon_devis_back.business.service;

import com.yferdin.pigeon_devis_back.business.dto.CreateBusinessDTO;
import com.yferdin.pigeon_devis_back.business.exception.SiretAlreadyExistsException;
import com.yferdin.pigeon_devis_back.business.model.Business;
import com.yferdin.pigeon_devis_back.business.repository.BusinessRepository;
import com.yferdin.pigeon_devis_back.user.model.Address;
import com.yferdin.pigeon_devis_back.user.model.User;
import com.yferdin.pigeon_devis_back.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public Business createBusiness(CreateBusinessDTO createBusinessDTO, User owner) {
        // Vérifier si le SIRET existe déjà
        if (businessRepository.existsBySiret(createBusinessDTO.getSiret())) {
            throw new SiretAlreadyExistsException("Une entreprise avec ce SIRET existe déjà");
        }

        // Créer l'adresse
        Address address = new Address();
        address.setStreetNumber(createBusinessDTO.getAddress().getStreetNumber());
        address.setStreetName(createBusinessDTO.getAddress().getStreetName());
        address.setZipCode(createBusinessDTO.getAddress().getZipCode());
        address.setCity(createBusinessDTO.getAddress().getCity());
        address.setComplement(createBusinessDTO.getAddress().getComplement());
        
        address = addressRepository.save(address);

        // Créer l'entreprise
        Business business = new Business();
        business.setOwner(owner);
        business.setSiret(createBusinessDTO.getSiret());
        business.setApeCode(createBusinessDTO.getApeCode());
        business.setTaxCode(createBusinessDTO.getTaxCode());
        business.setAddress(address);

        return businessRepository.save(business);
    }
} 