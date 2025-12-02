package fr.univangers.clients;

import fr.univangers.internalcrm.thrift.*;
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
 * Client pour le service InternalCRM via Thrift.
 */
public class InternalCRMClient implements CRMClient<Integer> {

    public static final String INTERNALCRM_URL = "http://localhost:9090/";

    /**
     * Trouve des leads par revenu.
     *
     * @param lowAnnualRevenue  Revenu minimum.
     * @param highAnnualRevenue Revenu maximum.
     * @param state             État.
     * @return Liste des leads trouvés.
     * @throws InvalidRevenueRangeException Si la plage de revenus est invalide.
     * @throws TException                   En cas d'erreur Thrift.
     */
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws InvalidRevenueRangeException, TException {

        List<InternalLeadDto> leadsList = new ArrayList<>();

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadsList = client.findLeads(lowAnnualRevenue, highAnnualRevenue, state);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
            throw new TException("Erreur de connexion à InternalCRM: " + e.getMessage(), e);
        }
        return VirtualLeadConverter.toVirtualLeadDtoList(leadsList);
    }

    /**
     * Trouve des leads par date.
     *
     * @param startDate Date de début.
     * @param endDate   Date de fin.
     * @return Liste des leads trouvés.
     * @throws InvalidDateException Si la plage de dates est invalide.
     * @throws TException           En cas d'erreur Thrift.
     */
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {

        List<InternalLeadDto> leadsList = new ArrayList<>();

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
     * Récupère un lead par son ID.
     *
     * @param id ID du lead.
     * @return Le lead trouvé.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
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
            throw new TException("Erreur de connexion à InternalCRM: " + e.getMessage(), e);
        }
        return VirtualLeadConverter.toVirtualLeadDto(lead);

    }

    /**
     * Supprime un lead.
     *
     * @param id ID du lead à supprimer.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
     */
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

        } catch (TTransportException e) {
            e.printStackTrace();
            throw new TException("Erreur de connexion à InternalCRM: " + e.getMessage(), e);
        }
    }

    /**
     * Ajoute un lead.
     *
     * @param fullName      Nom complet.
     * @param annualRevenue Revenu annuel.
     * @param phone         Téléphone.
     * @param street        Rue.
     * @param postalCode    Code postal.
     * @param city          Ville.
     * @param country       Pays.
     * @param company       Entreprise.
     * @param state         État.
     * @return L'ID du lead ajouté.
     * @throws LeadAlreadyExistsException    Si le lead existe déjà.
     * @throws InvalidLeadParameterException Si les paramètres sont invalides.
     * @throws TException                    En cas d'erreur Thrift.
     */
    public Integer addLead(String fullName, double annualRevenue, String phone, String street, String postalCode,
            String city, String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {

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
            throw new TException("Erreur de connexion à InternalCRM: " + e.getMessage(), e);
        }
        return leadId;
    }

    /**
     * Récupère tous les leads.
     *
     * @return Liste de tous les leads.
     * @throws TException En cas d'erreur Thrift.
     */
    public List<VirtualLeadDto> getLeads() throws TException {

        List<InternalLeadDto> leadsList = new ArrayList<>();

        try {

            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);

            leadsList = client.getLeads();

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
            throw new TException("Erreur de connexion à InternalCRM: " + e.getMessage(), e);
        }
        return VirtualLeadConverter.toVirtualLeadDtoList(leadsList);
    }

    /**
     * Compte le nombre de leads.
     *
     * @return Le nombre de leads.
     * @throws TException En cas d'erreur Thrift.
     */
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
            throw new TException("Erreur de connexion à InternalCRM: " + e.getMessage(), e);
        }
        return leadId;
    }
}
