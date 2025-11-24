package fr.univangers.clients;

import org.example.internalcrm.thrift.*;
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
public class InternalCRMClient implements CRMClient<Integer> {

    public static final String INTERNALCRM_URL = "http://localhost:9090/";

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
    public VirtualLeadDto getLeadById(Integer id) throws LeadNotFoundException, TException {

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

    public void deleteLead(Integer id) throws LeadNotFoundException, TException {

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            client.deleteLead(id);

            transport.close();

        } catch (LeadNotFoundException e) {
            // suppression impossible car lead inexistant
            throw e;

        }catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public Integer addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {

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
