package fr.univangers.utils;

import fr.univangers.model.VirtualLeadDto;
import fr.univangers.internalcrm.thrift.InternalLeadDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * TODO
 */
public class VirtualLeadConverter {

    // TODO : régler problèmes de types et implémentations

    /**
     * Permet de convertir un VirtualLeadDto en un InternalLeadDto
     *
     * @param modelTo
     * @return
     */
    public static InternalLeadDto toInternalLeadDto(VirtualLeadDto modelTo) {
        InternalLeadDto internalLeadDto = new InternalLeadDto();

        long creationDateLong = modelTo.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        internalLeadDto.setID(parseInt(modelTo.getId()));
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
     * Permet de convertir une liste de VirtualLeadDto en une liste de InternalLeadDto
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
     * Permet de convertir un InternalLeadDto en un VirtualLeadDto
     *
     * @param internalLeadDto
     * @return
     */
    public static VirtualLeadDto toVirtualLeadDto(InternalLeadDto internalLeadDto) {
        VirtualLeadDto modelTo = new VirtualLeadDto();

        String[] nameParts = internalLeadDto.getFullName().split(",");

        LocalDate creationDate = Instant.ofEpochMilli(internalLeadDto.getCreationDate()).atZone(ZoneId.systemDefault()).toLocalDate();

        modelTo.setId(Integer.toString(internalLeadDto.getID()));
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
     * Permet de convertir une liste de InternalLeadDto en une liste de VirtualLeadDto
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

    /**
     * Permet de fusionner deux listes de VirtualLeadDto. Cette méthode est utilisée pour fusionner
     * la liste des leads de l'Internal CRM et la liste des leads du Virtual CRM
     * @param internalLeadsLs
     * @param SalesforceLeadsLs
     * @return
     */
    public static List<VirtualLeadDto> mergeInternalSalesforceLeads(List<VirtualLeadDto> internalLeadsLs, List<VirtualLeadDto> SalesforceLeadsLs) {
        List<VirtualLeadDto> mergedList =  new ArrayList<>();
        mergedList.addAll(internalLeadsLs);
        mergedList.addAll(SalesforceLeadsLs);
        return mergedList;
    }

    /**
     * Permet de récupérer le nom et le prénom à partir d'un nom complet (format "Pierre,Balzac")
     *
     * @param fullName
     * @return
     */
    public static String[] splitFullName(String fullName) {
        if (fullName == null || !fullName.contains(",")) {
            throw new IllegalArgumentException("Le nom complet doit être au format 'Nom,Prénom'");
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
