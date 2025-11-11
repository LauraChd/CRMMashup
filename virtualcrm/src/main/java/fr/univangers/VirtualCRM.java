package fr.univangers;

import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@SpringBootApplication
public class VirtualCRM {

    public static void main(String[] args) throws IOException, TException {

        try {
            SalesforceCRMClient sTest = new SalesforceCRMClient();


            long start = LocalDate.of(2024, 1, 1)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli();

            long end = LocalDate.of(2025, 10, 3)
                    .atTime(23, 59)
                    .atZone(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli();

            List<VirtualLeadDto> test1 = sTest.findLeadsByDate(start,end);
            List<VirtualLeadDto> test2 = sTest.findLeads(0,100000,"Virginia");
            VirtualLeadDto test3 = sTest.getLeadById("00QgL00000692vIUAQ");

            System.out.println("findLeadsByDate");
            for (VirtualLeadDto dto : test1) {
                System.out.println(dto);
            }

            System.out.println("findLeads");
            for (VirtualLeadDto dto : test2) {
                System.out.println(dto);
            }

            System.out.println("getLeadById");
            System.out.println(test3.toString());

            //sTest.addLead("Pierre,Tournesol",300000, "0712345697","3 rue Jean Zay", "82000", "Paris", "France", "Google", "Ile-de-France");
            System.out.println("Nombre de leads (countLeads) : " + sTest.countLeads());

            List<VirtualLeadDto> test4 = sTest.getLeads();
            System.out.println("getLeads");
            for(VirtualLeadDto dto : test4) {
                System.out.println(dto);
            }

            //sTest.deleteLead("00QgL000007KPW5UAO");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //SpringApplication.run(VirtualCRM.class, args);
    }
}

