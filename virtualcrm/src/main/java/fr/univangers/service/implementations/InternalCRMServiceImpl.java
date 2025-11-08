package fr.univangers.service.implementations;

import fr.univangers.service.interfaces.ICRMServices;
import fr.univangers.thrift.*;
import fr.univangers.model.VirtualLeadDto;
import fr.univangers.utils.VirtualLeadConverter;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class InternalCRMServiceImpl implements ICRMServices {

    public static final String INTERNALCRM_URL = "http://localhost:8080/internalcrm/";

    /**
     * TODO
     * @param lowAnnualRevenue
     * @param highAnnualRevenue
     * @param state
     * @return
     * @throws InvalidRevenueRangeException
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException {

        List<InternalLeadDto> leadsList =  new ArrayList<>();

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadsList = client.findLeads(lowAnnualRevenue, highAnnualRevenue, state);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return VirtualLeadConverter.toVirtualLeadDtoList(leadsList);
    }

    /**
     * TODO
     * @param startDate
     * @param endDate
     * @return
     * @throws InvalidDateException
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {

        List<InternalLeadDto> leadsList =  new ArrayList<>();

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadsList = client.findLeadsByDate(startDate, endDate);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return VirtualLeadConverter.toVirtualLeadDtoList(leadsList);

    }

    /**
     * TODO
     * @param id
     * @return
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public VirtualLeadDto getLeadById(int id) throws LeadNotFoundException, TException {

        InternalLeadDto lead = new InternalLeadDto();

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            lead = client.getLeadById(id);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return VirtualLeadConverter.toVirtualLeadDto(lead);

    }

    /**
     * TODO
     * @param id
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public void deleteLead(int id) throws LeadNotFoundException, TException {

        InternalLeadDto lead;

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            lead = client.getLeadById(id);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     * @param fullName
     * @param annualRevenue
     * @param phone
     * @param street
     * @param postalCode
     * @param city
     * @param country
     * @param company
     * @param state
     * @return
     * @throws LeadAlreadyExistsException
     * @throws InvalidLeadParameterException
     * @throws TException
     */
    @Override
    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {

        int leadId = -1;

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadId = client.addLead(fullName, annualRevenue, phone, street, postalCode, city, country, company, state);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return leadId;
    }

    /**
     * TODO
     * @return
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> getLeads() throws TException {

        List<InternalLeadDto> leadsList =  new ArrayList<>();

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadsList = client.getLeads();

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return VirtualLeadConverter.toVirtualLeadDtoList(leadsList);
    }

    /**
     * TODO
     * @return
     * @throws TException
     */
    @Override
    public int countLeads() throws TException {

        int leadId = -1;

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadId = client.countLeads();

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return leadId;
    }
}
