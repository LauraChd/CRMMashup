package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;


import java.util.ArrayList;
import java.util.List;

public class InternalCRMServiceImpl implements InternalCRMService.Iface {

        private static int leadcont = 0 ;

        @Override
        public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException{
            if(lowAnnualRevenue > highAnnualRevenue){
                throw new InvalidRevenueRangeException("Le revenu minimum est supérieur au revenu maximum", lowAnnualRevenue, highAnnualRevenue);
            }
            List<ILead> leadsModel = ModelImpl.getInstance().findLeads(lowAnnualRevenue, highAnnualRevenue, state);

            return Utils.InternalLeadListToInternalLeadDTOList(leadsModel);
        }

        @Override
        public List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException{
            if(startDate > endDate){
                throw new InvalidDateException("La date de début est supérieure à la date de fin", startDate, endDate);
            }
            List<ILead> leadsModel = ModelImpl.getInstance().findLeadsByDate(startDate, endDate);

            return Utils.InternalLeadListToInternalLeadDTOList(leadsModel);
        }

        @Override
        public void deleteLead(int ID) throws LeadNotFoundException, TException {

        }

        @Override
        public void addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadDoesNotExistException, LeadAlreadyExistsException, InvalidLeadParameterException, TException {
            ModelImpl.getInstance().addLead(fullName, annualRevenue, phone, street, postalCode, city, country, company, state);
        }

        @Override
        public List<InternalLeadDto> getLeads() throws TException {
            return Utils.InternalLeadListToInternalLeadDTOList(ModelImpl.getInstance().getAllLeads());
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
