package org.example.client;
import org.example.client.service.ClientMovieService;
import org.example.client.service.ClientMovieServiceFactory;
import org.example.client.service.dto.ClientMovieDto;
import org.example.client.service.exceptions.ClientMovieNotRemovableException;
import org.example.client.service.exceptions.ClientSaleExpirationException;
import org.example.client.service.utils.exceptions.InputValidationException;
import org.example.client.service.utils.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.TException;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
//import org.example.client.thrift.CalculatorService;
import org.example.client.service.ClientMovieService;
import org.example.internalcrm.thrift.InternalCRMService;
import org.example.internalcrm.thrift.InternalLeadDto;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        if(executeThriftClient(args))
            return;
        else executeRestClient(args);
    }


    private static boolean executeThriftClient(String[] args) {
        if (args.length < 1)
            return false;

        // 1️⃣Création du client Thrift
        InternalCRMService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

        try {
            String command = args[0];
            System.out.println(args[0]);
            switch (command) {
                case "addLead":
                    if (args.length !=10) {
                        System.out.println("Usage: addLead \"Nom, Prénom\" revenue phone street postalcode city country company state");
                        transport.close();
                        return true;
                    }

                    String fullName = args[1];
                    double revenue = Double.parseDouble(args[2]);
                    String phone = args[3];
                    String street = args[4];
                    String postalcode = args[5];
                    String city = args[6];
                    String country = args[7];
                    String company = args[8];
                    String state = args[9];

                    client.addLead(fullName, revenue, phone, street, postalcode, city, country, company, state);
                    System.out.println("Lead ajouté avec succès !");
                    transport.close();
                    return true;

                case "findLeads" :
                    if (args.length !=4) {
                        System.out.println("Usage: findLeads lowAnnualRevenue highAnnualRevenue state");
                        transport.close();
                        return true;
                    }

                    double lowAnnualRevenue = Double.parseDouble(args[1]);
                    double highAnnualRevenue = Double.parseDouble(args[2]);
                    String state2 = args[3];


                    List<InternalLeadDto> res = client.findLeads(lowAnnualRevenue, highAnnualRevenue, state2);
                    System.out.println("Leads cherchés avec succès !");
                    for(InternalLeadDto lead : res) {
                        System.out.println(lead.toString());
                    }
                    transport.close();
                    return true;
                case "findLeadsByDate":
                    if (args.length !=3) {
                        System.out.println("Usage: findLeadsByDate startDate endDate");
                        transport.close();
                        return true;
                    }

                    long startDate = Long.parseLong(args[1]);
                    long endDate = Long.parseLong(args[2]);


                    List<InternalLeadDto> res1 = client.findLeadsByDate(startDate, endDate);
                    System.out.println("Leads cherchés avec succès !");
                    for(InternalLeadDto lead : res1) {
                        System.out.println(lead.toString());
                    }
                    transport.close();
                    return true;
                case "getLeads" :

                    List<InternalLeadDto> res2 = client.getLeads();
                    System.out.println("Taille : " + res2.size());
                    System.out.println("Affichage des Leads");
                    for(InternalLeadDto lead : res2) {
                        System.out.println(lead.toString());
                    }
                    transport.close();
                    return true;

                // Tu peux ajouter findLead, deleteLead ici TODO
                default:
                    System.out.println("Commande inconnue : " + command);
                    transport.close();
                    return true;
            }

        } catch (TException e) {
            e.printStackTrace();
            return true;
        }
    }



    private static InternalCRMService.Client getClient() {

        try {
            TTransport transport = new THttpClient("http://localhost:8080/internalcrm/internalcrm");
            TProtocol protocol = new TBinaryProtocol(transport);

            return new InternalCRMService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }


    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    MovieServiceClient -a <title> <hours> <minutes> <description> <price>\n" +
                "    [remove] MovieServiceClient -r <movieId>\n" +
                "    [update] MovieServiceClient -u <movieId> <title> <hours> <minutes> <description> <price>\n" +
                "    [find]   MovieServiceClient -f <keywords>\n" +
                "    [buy]    MovieServiceClient -b <movieId> <userId> <creditCardNumber>\n" +
                "    [get]    MovieServiceClient -g <saleId>\n");
    }


    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void validateArgs(String[] args, int expectedArgs,  int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }


    public static void executeRestClient(String[] args){

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientMovieService clientMovieService = ClientMovieServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {2, 3, 5});

            // [add] MovieServiceClient -a <title> <hours> <minutes> <description> <price>

            try {
                Long movieId = clientMovieService.addMovie(new ClientMovieDto(null,
                        args[1], Short.valueOf(args[2]), Short.valueOf(args[3]),
                        args[4], Float.valueOf(args[5])));

                System.out.println("Movie " + movieId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-r".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [remove] MovieServiceClient -r <movieId>

            try {
                clientMovieService.removeMovie(Long.parseLong(args[1]));

                System.out.println("Movie with id " + args[1] +
                        " removed sucessfully");

            } catch (NumberFormatException | InstanceNotFoundException | ClientMovieNotRemovableException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-u".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[] {1, 3, 4, 6});

            // [update] MovieServiceClient -u <movieId> <title> <hours> <minutes> <description> <price>

            try {
                clientMovieService.updateMovie(new ClientMovieDto(
                        Long.valueOf(args[1]),
                        args[2], Short.valueOf(args[3]), Short.valueOf(args[4]),
                        args[5], Float.valueOf(args[6])));

                System.out.println("Movie " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-f".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [find] MovieServiceClient -f <keywords>

            try {
                List<ClientMovieDto> movies = clientMovieService.findMovies(args[1]);
                System.out.println("Found " + movies.size() +
                        " movie(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < movies.size(); i++) {
                    ClientMovieDto movieDto = movies.get(i);
                    System.out.println("Id: " + movieDto.getMovieId() +
                            ", Title: " + movieDto.getTitle() +
                            ", Runtime: " + (movieDto.getRuntimeHours() + " h - ") +
                            (movieDto.getRuntimeMinutes() + " m") +
                            ", Description: " + movieDto.getDescription() +
                            ", Price: " + movieDto.getPrice());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-b".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {1});

            // [buy] MovieServiceClient -b <movieId> <userId> <creditCardNumber>

            Long saleId;
            try {
                saleId = clientMovieService.buyMovie(Long.parseLong(args[1]),
                        args[2], args[3]);

                System.out.println("Movie " + args[1] +
                        " purchased sucessfully with sale number " +
                        saleId);

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-g".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [get] MovieServiceClient -g <saleId>

            try {
                String movieURL =
                        clientMovieService.getMovieUrl(Long.parseLong(args[1]));

                System.out.println("The URL for the sale " + args[1] +
                        " is " + movieURL);
            } catch (NumberFormatException | InstanceNotFoundException | ClientSaleExpirationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }


    }

}
