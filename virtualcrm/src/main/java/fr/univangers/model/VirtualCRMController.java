package fr.univangers.model;

import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.interfaces.VirtualCRMService;
import org.apache.thrift.TException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/virtualcrm")
public class VirtualCRMController {

    private final VirtualCRMService virtualService;

    public VirtualCRMController(VirtualCRMService virtualService) {
        this.virtualService = virtualService;
    }

    // ----------------------------------------
    // 1) FIND LEADS by revenue & state
    // ----------------------------------------
    @GetMapping("/findLeads")
    public List<VirtualLeadDto> findLeads(
            @RequestParam double lowAnnualRevenue,
            @RequestParam double highAnnualRevenue,
            @RequestParam String state
    ) throws Exception {
        return virtualService.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
    }

    // ----------------------------------------
    // 2) FIND LEADS BY DATE
    // ----------------------------------------
    @GetMapping("/findLeadsByDate")
    public List<VirtualLeadDto> findLeadsByDate(
            @RequestParam long startDate,
            @RequestParam long endDate
    ) throws Exception {
        return virtualService.findLeadsByDate(startDate, endDate);
    }

    // ----------------------------------------
    // 3) GET LEAD BY ID
    // ----------------------------------------
    @GetMapping("/lead/{id}")
    public VirtualLeadDto getLeadById(@PathVariable String id)
            throws TException {
        return virtualService.getLeadById(id);
    }

    // ----------------------------------------
    // 4) DELETE LEAD
    // ----------------------------------------
    @DeleteMapping("/lead/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLead(@PathVariable String id)
            throws Exception {
        virtualService.deleteLead(id);
    }

    // ----------------------------------------
    // 5) ADD LEAD
    // ----------------------------------------
    @PostMapping("/lead")
    @ResponseStatus(HttpStatus.CREATED)
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

        return virtualService.addLead(
                fullName, annualRevenue, phone,
                street, postalCode, city, country,
                company, state
        );
    }

    // ----------------------------------------
    // 6) GET ALL LEADS
    // ----------------------------------------
    @GetMapping("/leads")
    public List<VirtualLeadDto> getLeads() throws TException {
        return virtualService.getLeads();
    }

    // ----------------------------------------
    // 7) COUNT LEADS
    // ----------------------------------------
    @GetMapping("/countLeads")
    public int countLeads() throws TException {
        return virtualService.countLeads();
    }
}


