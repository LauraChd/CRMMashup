package fr.univangers.utils;

import fr.univangers.model.VirtualLeadDto;
import org.example.internalcrm.thrift.InternalLeadDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class VirtualLeadConverter {

    // TODO : régler problèmes de types et implémentations

    /**
     * TODO
     *
     * @param modelTo
     * @return
     */
    public static InternalLeadDto toInternalLeadDto(VirtualLeadDto modelTo) {
        InternalLeadDto internalLeadDto = new InternalLeadDto();

        long creationDateLong = modelTo.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        internalLeadDto.setFullName(modelTo.getFirstName() + ',' + modelTo.getLastName());
        internalLeadDto.setCity(modelTo.getCity());
        internalLeadDto.setAnnualRevenue(modelTo.getAnnualRevenue());
        internalLeadDto.setPhone(modelTo.getPhone());
        internalLeadDto.setStreet(modelTo.getStreet());
        internalLeadDto.setPostalCode(modelTo.getPostalCode());
        internalLeadDto.setCountry(modelTo.getCountry());
        internalLeadDto.setCreationDate(creationDateLong);
        internalLeadDto.setCompany(modelTo.getCompany());
        internalLeadDto.setState(modelTo.getState());

        return internalLeadDto;
    }

    /**
     * TODO
     *
     * @param lsILeads
     * @return
     */
    public static List<InternalLeadDto> toInternalLeadDTOList(List<VirtualLeadDto> lsILeads) {
        List<InternalLeadDto> internalLeadDtoList = new ArrayList<>();
        for (VirtualLeadDto iLead : lsILeads) {
            internalLeadDtoList.add(toInternalLeadDto(iLead));
        }
        return internalLeadDtoList;
    }

    /**
     * TODO
     *
     * @param internalLeadDto
     * @return
     */
    public static VirtualLeadDto toVirtualLeadDto(InternalLeadDto internalLeadDto) {
        VirtualLeadDto modelTo = new VirtualLeadDto();

        String[] nameParts = internalLeadDto.getFullName().split(",");

        LocalDate creationDate = Instant.ofEpochMilli(internalLeadDto.getCreationDate()).atZone(ZoneId.systemDefault()).toLocalDate();

        modelTo.setFirstName(nameParts[0]);
        modelTo.setLastName(nameParts[1]);
        modelTo.setCity(internalLeadDto.getCity());
        modelTo.setAnnualRevenue(internalLeadDto.getAnnualRevenue());
        modelTo.setPhone(internalLeadDto.getPhone());
        modelTo.setStreet(internalLeadDto.getStreet());
        modelTo.setPostalCode(internalLeadDto.getPostalCode());
        modelTo.setCountry(internalLeadDto.getCountry());
        modelTo.setCreationDate(creationDate);
        modelTo.setCompany(internalLeadDto.getCompany());
        modelTo.setState(internalLeadDto.getState());

        return modelTo;
    }

    /**
     * TODO
     *
     * @param lsInternalLeadDto
     * @return
     */
    public static List<VirtualLeadDto> toVirtualLeadDtoList(List<InternalLeadDto> lsInternalLeadDto) {
        List<VirtualLeadDto> iLeadList = new ArrayList<>();
        for (InternalLeadDto internalLeadDto : lsInternalLeadDto) {
            iLeadList.add(toVirtualLeadDto(internalLeadDto));
        }
        return iLeadList;
    }

    public static List<VirtualLeadDto> mergeInternalSalesforceLeads(List<VirtualLeadDto> internalLeadsLs, List<VirtualLeadDto> SalesforceLeadsLs) {
        //TODO
        return null;
    }

    /**
     * TODO
     *
     * @param fullName
     * @return
     */
    public static String[] splitFullName(String fullName) {
        if (fullName == null || !fullName.contains(",")) {
            throw new IllegalArgumentException("Le nom complet doit être au format 'Nom, Prénom'");
        }

        // Découpage sur la virgule
        String[] parts = fullName.split(",", 2);

        String lastName = parts[0].trim();
        String firstName = parts[1].trim();

        return new String[]{firstName, lastName};
    }

    long parseDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}
