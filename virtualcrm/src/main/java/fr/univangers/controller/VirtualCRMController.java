package fr.univangers.controller;

import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.implementations.VirtualCRMServiceFactory;
import fr.univangers.service.interfaces.IVirtualCRMService;
import org.apache.thrift.TException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Contrôleur REST pour l'API VirtualCRM
 */
@RestController
public class VirtualCRMController implements IVirtualCRMService {

    // ----------------------------------------
    // 1) FIND LEADS by revenue & state
    // ----------------------------------------
    /**
     * Trouve des leads par revenu
     *
     * @param lowAnnualRevenue  Revenu minimum
     * @param highAnnualRevenue Revenu maximum
     * @param state             État
     * @return Liste des leads trouvés
     * @throws Exception En cas d'erreur
     */
    @GetMapping("/findLeads")
    public List<VirtualLeadDto> findLeads(
            @RequestParam double lowAnnualRevenue,
            @RequestParam double highAnnualRevenue,
            @RequestParam String state) throws Exception {
        return VirtualCRMServiceFactory.getInstance().findLeads(lowAnnualRevenue, highAnnualRevenue, state);
    }

    // ----------------------------------------
    // 2) FIND LEADS BY DATE
    // ----------------------------------------
    /**
     * Trouve des leads par date
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Liste des leads trouvés
     * @throws Exception En cas d'erreur
     */
    @GetMapping("/findLeadsByDate")
    public List<VirtualLeadDto> findLeadsByDate(
            @RequestParam long startDate,
            @RequestParam long endDate) throws Exception {
        return VirtualCRMServiceFactory.getInstance().findLeadsByDate(startDate, endDate);
    }

    // ----------------------------------------
    // 3) GET LEAD BY ID
    // ----------------------------------------
    /**
     * Récupère un lead par son ID
     *
     * @param id ID du lead
     * @return Le lead trouvé
     * @throws TException  En cas d'erreur Thrift
     * @throws IOException En cas d'erreur d'entrée/sortie
     */
    @GetMapping("/leads/{id}")
    public VirtualLeadDto getLeadById(@PathVariable String id)
            throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().getLeadById(id);
    }

    // ----------------------------------------
    // 4) DELETE LEAD
    // ----------------------------------------
    /**
     * Supprime un lead
     *
     * @param id ID du lead à supprimer
     * @return Toujours false (pourquoi ?)
     * @throws Exception En cas d'erreur
     */
    @DeleteMapping("/leads/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteLead(@PathVariable String id)
            throws Exception {
        VirtualCRMServiceFactory.getInstance().deleteLead(id);
        return false;
    }

    // ----------------------------------------
    // 5) ADD LEAD
    // ----------------------------------------
    /**
     * Ajoute un lead
     *
     * @param fullName      Nom complet
     * @param annualRevenue Revenu annuel
     * @param phone         Téléphone
     * @param street        Rue
     * @param postalCode    Code postal
     * @param city          Ville
     * @param country       Pays
     * @param company       Entreprise
     * @param state         État
     * @return L'ID du lead ajouté
     * @throws Exception En cas d'erreur
     */
    @PostMapping("/addLead")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody // TODELETE ??
    public int addLead(
            @RequestParam String fullName,
            @RequestParam double annualRevenue,
            @RequestParam String phone,
            @RequestParam String street,
            @RequestParam String postalCode,
            @RequestParam String city,
            @RequestParam String country,
            @RequestParam String company,
            @RequestParam String state) throws Exception {

        return VirtualCRMServiceFactory.getInstance().addLead(
                fullName, annualRevenue, phone,
                street, postalCode, city, country,
                company, state);
    }

    // ----------------------------------------
    // 6) GET ALL LEADS
    // ----------------------------------------
    /**
     * Récupère tous les leads
     *
     * @return Liste de tous les leads
     * @throws TException  En cas d'erreur Thrift
     * @throws IOException En cas d'erreur d'entrée/sortie
     */
    @GetMapping("/leads")
    public List<VirtualLeadDto> getLeads() throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().getLeads();
    }

    // ----------------------------------------
    // 7) COUNT LEADS
    // ----------------------------------------
    /**
     * Compte le nombre de leads
     *
     * @return Le nombre de leads
     * @throws TException  En cas d'erreur Thrift
     * @throws IOException En cas d'erreur d'entrée/sortie
     */
    @GetMapping("/countLeads")
    public int countLeads() throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().countLeads();
    }

    /**
     * Fusionne les leads
     *
     * @return Résultat de la fusion
     * @throws TException  En cas d'erreur Thrift
     * @throws IOException En cas d'erreur d'entrée/sortie
     */
    @GetMapping("/merge")
    public String merge() throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().merge();
    }

}
