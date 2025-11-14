package fr.univangers.internalcrm.utils;

import fr.univangers.internalcrm.model.ILead;
import org.example.internalcrm.thrift.InternalLeadDto;
import org.example.internalcrm.thrift.InvalidLeadParameterException;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class InternalLeadConverter {

    /**
     * TODO
     * @param modelTo
     * @return
     */
    public static InternalLeadDto toInternalLeadDTO(ILead modelTo)
    {
        InternalLeadDto internalLeadDto = new InternalLeadDto();

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
     * TODO
     * @param lsILeads
     * @return
     */
    public static List<InternalLeadDto> toInternalLeadDTOList(List<ILead> lsILeads)
    {
        List<InternalLeadDto> internalLeadDtoList = new ArrayList<>();
        for(ILead iLead : lsILeads){
            internalLeadDtoList.add(toInternalLeadDTO(iLead));
        }
        return internalLeadDtoList;
    }

    /**
     * TODO
     * @param internalLeadDto
     * @return
     */
    public static ILead toILead(InternalLeadDto internalLeadDto)
    {
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
     * TODO
     * @param lsInternalLeadDto
     * @return
     */
    public static List<ILead> toILeadList(List<InternalLeadDto> lsInternalLeadDto)
    {
        List<ILead> iLeadList = new ArrayList<>();
        for (InternalLeadDto internalLeadDto : lsInternalLeadDto) {
            iLeadList.add(toILead(internalLeadDto));
        }
        return iLeadList;
    }

    /**
     * TODO
     * @param fullName
     * @return
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