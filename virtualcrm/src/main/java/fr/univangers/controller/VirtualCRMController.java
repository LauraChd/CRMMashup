package fr.univangers.controller;

import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.implementations.VirtualCRMServiceFactory;
import fr.univangers.service.interfaces.IVirtualCRMService;
import org.apache.thrift.TException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class VirtualCRMController implements IVirtualCRMService {

/*    private final VirtualCRMService VirtualCRMServiceFactory.getInstance();

    public VirtualCRMController(VirtualCRMService VirtualCRMServiceFactory.getInstance()) {
        this.VirtualCRMServiceFactory.getInstance() = VirtualCRMServiceFactory.getInstance();
    }*/

    // ----------------------------------------
    // 1) FIND LEADS by revenue & state
    // ----------------------------------------
    @GetMapping("/findLeads")
    public List<VirtualLeadDto> findLeads(
            @RequestParam double lowAnnualRevenue,
            @RequestParam double highAnnualRevenue,
            @RequestParam String state
    ) throws Exception {
        return VirtualCRMServiceFactory.getInstance().findLeads(lowAnnualRevenue, highAnnualRevenue, state);
    }

    // ----------------------------------------
    // 2) FIND LEADS BY DATE
    // ----------------------------------------
    @GetMapping("/findLeadsByDate")
    public List<VirtualLeadDto> findLeadsByDate(
            @RequestParam long startDate,
            @RequestParam long endDate
    ) throws Exception {
        return VirtualCRMServiceFactory.getInstance().findLeadsByDate(startDate, endDate);
    }

    // ----------------------------------------
    // 3) GET LEAD BY ID
    // ----------------------------------------
    @GetMapping("/leads/{id}")
    public VirtualLeadDto getLeadById(@PathVariable String id)
            throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().getLeadById(id);
    }

    // ----------------------------------------
    // 4) DELETE LEAD
    // ----------------------------------------
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
    @PostMapping("/addLead")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody //TODELETE ??
    public int addLead(
            @RequestParam String fullName,
            @RequestParam double annualRevenue,
            @RequestParam String phone,
            @RequestParam String street,
            @RequestParam String postalCode,
            @RequestParam String city,
            @RequestParam String country,
            @RequestParam String company,
            @RequestParam String state
    ) throws Exception {

        return VirtualCRMServiceFactory.getInstance().addLead(
                fullName, annualRevenue, phone,
                street, postalCode, city, country,
                company, state
        );
    }

    // ----------------------------------------
    // 6) GET ALL LEADS
    // ----------------------------------------
    @GetMapping("/leads")
    public List<VirtualLeadDto> getLeads() throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().getLeads();
    }

    // ----------------------------------------
    // 7) COUNT LEADS
    // ----------------------------------------
    @GetMapping("/countLeads")
    public int countLeads() throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().countLeads();
    }

    @GetMapping("/merge")
    public String merge() throws TException, IOException {
        return VirtualCRMServiceFactory.getInstance().merge();
    }

}


