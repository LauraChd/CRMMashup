package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;


    /*
      public interface Iface {

    public java.util.List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, java.lang.String state) throws InvalidRevenueRangeException, org.apache.thrift.TException;

    public java.util.List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, org.apache.thrift.TException;

    public void deleteLead(InternalLeadDto lead) throws LeadNotFoundException, org.apache.thrift.TException;

    public void addLead(InternalLeadDto lead) throws LeadDoesNotExistException, LeadAlreadyExistsException, InvalidLeadParameterException, org.apache.thrift.TException;

  }
     */

    public class InternalCRMServiceImpl implements InternalCRMService.Iface {

        private List<InternalLeadDto> leads = new ArrayList<>();

        @Override
        public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException{
            if(lowAnnualRevenue > highAnnualRevenue){
                throw new InvalidRevenueRangeException("Le revenu minimum est supérieur au revenu maximum", lowAnnualRevenue, highAnnualRevenue);
            }
            List<InternalLeadDto> leadsReturn = new ArrayList<>();
            for (InternalLeadDto l : leads) {
                if(l.getAnnualRevenue() >= lowAnnualRevenue && l.getAnnualRevenue() <= highAnnualRevenue && l.getState().equals(state)){
                    leadsReturn.add(l);
                }
            }
            return leadsReturn;
        }

        @Override
        public List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException{
            if(startDate > endDate){
                throw new InvalidDateException("La date de début est supérieure à la date de fin", startDate, endDate);
            }
            List<InternalLeadDto> leadsReturn = new ArrayList<>();
            for (InternalLeadDto l : leads) {
                if(l.getCreationDate() >= startDate && l.getCreationDate() <= endDate){
                    leadsReturn.add(l);
                }
            }
            return leadsReturn;
        }

        @Override
        public void deleteLead(InternalLeadDto lead) throws LeadNotFoundException, TException{
            //Vérificiation, voir avec prof ???
            if(!leads.contains(lead)){
                throw new LeadNotFoundException("Le Lead n'existe pas dans la liste", lead);
            }
            else {
                leads.remove(lead);
            }
        }

        @Override
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
                leads.add(lead);
            }
        }



    }
