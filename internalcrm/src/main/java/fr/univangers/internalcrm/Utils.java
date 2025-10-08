package fr.univangers.internalcrm;

import org.example.internalcrm.thrift.InternalLeadDto;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static InternalLeadDto InternalLeadToInternalLeadDTO(ILead modelTo)
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
    public static List<InternalLeadDto> InternalLeadListToInternalLeadDTOList(List<ILead> lsILeads)
    {
        List<InternalLeadDto> internalLeadDtoList = new ArrayList<>();
        for(ILead iLead : lsILeads){
            internalLeadDtoList.add(InternalLeadToInternalLeadDTO(iLead));
        }
        return internalLeadDtoList;
    }

    public static ILead InternalLeadDtoToILead(InternalLeadDto internalLeadDto)
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

    public static List<ILead> InternalLeadDtoListToILeadList(List<InternalLeadDto> lsInternalLeadDto)
    {
        List<ILead> iLeadList = new ArrayList<>();
        for (InternalLeadDto internalLeadDto : lsInternalLeadDto) {
            iLeadList.add(InternalLeadDtoToILead(internalLeadDto));
        }
        return iLeadList;
    }

    public static String[] splitFullName(String fullName) {
        if (fullName == null || !fullName.contains(",")) {
            throw new IllegalArgumentException("Le nom complet doit être au format 'Nom, Prénom'");
        }

        // Découpage sur la virgule
        String[] parts = fullName.split(",", 2);

        String lastName = parts[0].trim();
        String firstName = parts[1].trim();

        return new String[] { firstName, lastName };
    }
}