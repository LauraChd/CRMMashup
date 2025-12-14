package fr.univangers.internalcrm.utils;

import fr.univangers.internalcrm.model.ILead;
import fr.univangers.internalcrm.thrift.InternalLeadDto;
import fr.univangers.internalcrm.thrift.InvalidLeadParameterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de type pour les leads internes et les leads qui sont envoyés au VirtualCRM
 */
public class InternalLeadConverter {

    /**
     * Convertit un modèle ILead en DTO InternalLeadDto
     *
     * @param modelTo Le modèle ILead
     * @return Le DTO InternalLeadDto
     */
    public static InternalLeadDto toInternalLeadDTO(ILead modelTo) {
        InternalLeadDto internalLeadDto = new InternalLeadDto();

        internalLeadDto.setID(modelTo.getID());
        internalLeadDto.setFullName(modelTo.getFirstName() + ',' + modelTo.getLastName());
        internalLeadDto.setCity(modelTo.getCity());
        internalLeadDto.setAnnualRevenue(modelTo.getAnnualRevenue());
        internalLeadDto.setPhone(modelTo.getPhone());
        internalLeadDto.setStreet(modelTo.getStreet());
        internalLeadDto.setPostalCode(modelTo.getPostalCode());
        internalLeadDto.setCountry(modelTo.getCountry());
        internalLeadDto.setCreationDate(modelTo.getCreationDate());
        internalLeadDto.setCompany(modelTo.getCompany());
        internalLeadDto.setState(modelTo.getState());

        return internalLeadDto;
    }

    /**
     * Convertit une liste de modèles ILead en liste de DTO InternalLeadDto
     *
     * @param lsILeads Liste de modèles ILead
     * @return Liste de DTO InternalLeadDto
     */
    public static List<InternalLeadDto> toInternalLeadDTOList(List<ILead> lsILeads) {
        List<InternalLeadDto> internalLeadDtoList = new ArrayList<>();
        for (ILead iLead : lsILeads) {
            internalLeadDtoList.add(toInternalLeadDTO(iLead));
        }
        return internalLeadDtoList;
    }

    /**
     * Convertit un DTO InternalLeadDto en modèle ILead
     *
     * @param internalLeadDto Le DTO InternalLeadDto
     * @return Le modèle ILead
     */
    public static ILead toILead(InternalLeadDto internalLeadDto) {
        ILead modelTo = new ILead();

        String[] nameParts = internalLeadDto.getFullName().split(",");

        modelTo.setFirstName(nameParts[0]);
        modelTo.setLastName(nameParts[1]);
        modelTo.setCity(internalLeadDto.getCity());
        modelTo.setAnnualRevenue(internalLeadDto.getAnnualRevenue());
        modelTo.setPhone(internalLeadDto.getPhone());
        modelTo.setStreet(internalLeadDto.getStreet());
        modelTo.setPostalCode(internalLeadDto.getPostalCode());
        modelTo.setCountry(internalLeadDto.getCountry());
        modelTo.setCreationDate(internalLeadDto.getCreationDate());
        modelTo.setCompany(internalLeadDto.getCompany());
        modelTo.setState(internalLeadDto.getState());

        return modelTo;
    }

    /**
     * Convertit une liste de DTO InternalLeadDto en liste de modèles ILead
     *
     * @param lsInternalLeadDto Liste de DTO InternalLeadDto
     * @return Liste de modèles ILead
     */
    public static List<ILead> toILeadList(List<InternalLeadDto> lsInternalLeadDto) {
        List<ILead> iLeadList = new ArrayList<>();
        for (InternalLeadDto internalLeadDto : lsInternalLeadDto) {
            iLeadList.add(toILead(internalLeadDto));
        }
        return iLeadList;
    }

    /**
     * Sépare le nom complet en nom et prénom
     *
     * @param fullName Nom complet
     * @return Tableau contenant le nom et le prénom
     * @throws InvalidLeadParameterException Si le nom complet est invalide
     */
    public static String[] splitFullName(String fullName) throws InvalidLeadParameterException {
        if (fullName == null) {
            throw new InvalidLeadParameterException("Le nom complet ne peut pas être null");
        }

        String[] parts = fullName.split(",", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new InvalidLeadParameterException("Le nom complet doit être au format 'Nom,Prénom'");
        }

        return new String[] { parts[0].trim(), parts[1].trim() };
    }

}