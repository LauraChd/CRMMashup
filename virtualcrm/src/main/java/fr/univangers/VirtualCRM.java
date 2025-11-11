package fr.univangers;

import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class VirtualCRM {

    public static void main(String[] args) throws IOException, TException {

        try {
            SalesforceCRMClient sTest = new SalesforceCRMClient();
            List<VirtualLeadDto> test = sTest.findLeads(0,100000,"Virginia");

            for (VirtualLeadDto dto : test) {
                System.out.println(dto);
            }
        } catch (Exception e) {
            e.printStackTrace(); // affiche la vraie cause
        }

        //SpringApplication.run(VirtualCRM.class, args);
    }
}

