package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;


import java.util.ArrayList;
import java.util.List;

public class InternalCRMServiceImpl implements InternalCRMService.Iface {

        @Override
        public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException{
            try{
                List<ILead> leadsModel = ModelImpl.getInstance().findLeads(lowAnnualRevenue, highAnnualRevenue, state);
                return Utils.InternalLeadListToInternalLeadDTOList(leadsModel);
            } catch (InvalidRevenueRangeException e){
                throw new InvalidRevenueRangeException(e.getMessage(), e.getLowAnnualRevenue(), e.getHighAnnualRevenue());
            }
        }

        @Override
        public List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException{
            try {
                List<ILead> leadsModel = ModelImpl.getInstance().findLeadsByDate(startDate, endDate);
                return Utils.InternalLeadListToInternalLeadDTOList(leadsModel);
            } catch (InvalidDateException e){
                throw new InvalidDateException(e.getMessage(), e.getStartDate(), e.getEndDate());

            }
        }

        @Override
        public void deleteLead(int ID) throws LeadNotFoundException, TException {
            try {
                ModelImpl.getInstance().deleteLead(ID);
            } catch (LeadNotFoundException e){
                throw new LeadNotFoundException(e.getMessage(), e.getID());

            }
        }

        @Override
        public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {
            try {
                return ModelImpl.getInstance().addLead(fullName, annualRevenue, phone, street, postalCode, city, country, company, state);
            } catch (LeadAlreadyExistsException e){
                throw new LeadAlreadyExistsException(e.getMessage(), e.getID());
            } catch (InvalidLeadParameterException ee){
                throw new InvalidLeadParameterException(ee.getMessage());
            }
        }

        @Override
        public List<InternalLeadDto> getLeads() throws TException {
            return Utils.InternalLeadListToInternalLeadDTOList(ModelImpl.getInstance().getAllLeads());
        }
    }
