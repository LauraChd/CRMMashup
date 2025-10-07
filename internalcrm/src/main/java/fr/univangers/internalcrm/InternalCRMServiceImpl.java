package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

    public class InternalCRMServiceImpl implements InternalCRMService.Iface {

        private List<InternalLeadDto> leads = new ArrayList<>();
        private static int leadcont = 0 ;

        @Override
        public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException{
            if(lowAnnualRevenue > highAnnualRevenue){
                throw new InvalidRevenueRangeException("Le revenu minimum est supérieur au revenu maximum", lowAnnualRevenue, highAnnualRevenue);
            }
            List<InternalLeadDto> leadsReturn = new ArrayList<>(); // liste à retourner au client
            List<LeadTo> leadsModel = ModelImpl.findLeads(lowAnnualRevenue, highAnnualRevenue, state); // = BDD
            for (LeadTo l : leadsModel) {
                if(l.getAnnualRevenue() >= lowAnnualRevenue && l.getAnnualRevenue() <= highAnnualRevenue && l.getState().equals(state)){
                    leadsReturn.add(Utils.toInternalLeadDTO(l));
                }
            }
            return leadsReturn;
        }

        @Override
        public List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException{
            if(startDate > endDate){
                throw new InvalidDateException("La date de début est supérieure à la date de fin", startDate, endDate);
            }
            List<InternalLeadDto> leadsReturn = new ArrayList<>(); // liste à retourner au client
            List<LeadTo> leadsModel = ModelImpl.findLeadsByDate(startDate, endDate);
            for (LeadTo l : leadsModel) {
                if(l.getCreationDate() >= startDate && l.getCreationDate() <= endDate){
                    leadsReturn.add(Utils.toInternalLeadDTO(l));
                }
            }
            return leadsReturn;
        }

        @Override
        public void deleteLead(int ID) throws LeadNotFoundException, TException {

        }

        @Override
        public void addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadDoesNotExistException, LeadAlreadyExistsException, InvalidLeadParameterException, TException {
            InternalLeadDto lead = new InternalLeadDto();

            leadcont++;
            lead.setId(leadcont);
            lead.setFullName(fullName);
            lead.setAnnualRevenue(annualRevenue);
            lead.setPhone(phone);
            lead.setStreet(street);
            lead.setPostalCode(postalCode);
            lead.setCity(city);
            lead.setCountry(country);
            lead.setCompany(company);
            lead.setState(state);
            leads.add(lead);

        }

        /*@Override
        public void deleteLead(InternalLeadDto lead) throws LeadNotFoundException, TException{
            //Vérificiation, voir avec prof ???
            if(!leads.contains(lead)){
                throw new LeadNotFoundException("Le Lead n'existe pas dans la liste", lead);
            }
            else {
                ModelImpl.deleteLead(Utils.toLeadTo(lead));
            }
        }*/

        /*@Override
        public void addLead(InternalLeadDto lead) throws LeadDoesNotExistException, LeadAlreadyExistsException, InvalidLeadParameterException, TException{
            //Vérification ?
            if(lead.equals(null)){
                throw new LeadDoesNotExistException("Le lead n'existe pas", lead);
            }
            else if (leads.contains(lead)){
                throw new LeadAlreadyExistsException("Le lead existe déjà", lead);
            }
            //Vérification si Lead ok ??? Voir avec prof
            else {
                ModelImpl.addLead(Utils.toLeadTo(lead));

            }
        }*/

//TODO Comment le client créé un nouveau lead ?

    }
