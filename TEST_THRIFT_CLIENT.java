import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

import java.util.List;

/**
 * Client de test pour vérifier la communication Thrift avec InternalCRM
 */
public class TEST_THRIFT_CLIENT {
    
    private static final String INTERNALCRM_URL = "http://localhost:9090/";
    
    public static void main(String[] args) {
        System.out.println("=== Test de connexion Thrift à InternalCRM ===");
        System.out.println("URL: " + INTERNALCRM_URL);
        
        try {
            // 1. Créer le transport HTTP pour Thrift
            TTransport transport = new THttpClient(INTERNALCRM_URL);
            transport.open();
            
            System.out.println("✅ Connexion établie au serveur");
            
            // 2. Créer le protocole binaire Thrift
            TProtocol protocol = new TBinaryProtocol(transport);
            
            // 3. Créer le client Thrift
            InternalCRMService.Client client = new InternalCRMService.Client(protocol);
            
            // 4. Tester l'appel countLeads() (le plus simple)
            System.out.println("\n--- Test 1: countLeads() ---");
            try {
                int count = client.countLeads();
                System.out.println("✅ Succès ! Nombre de leads: " + count);
            } catch (TException e) {
                System.out.println("❌ Erreur: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 5. Tester l'appel getLeads()
            System.out.println("\n--- Test 2: getLeads() ---");
            try {
                List<InternalLeadDto> leads = client.getLeads();
                System.out.println("✅ Succès ! Nombre de leads récupérés: " + leads.size());
                if (leads.size() > 0) {
                    System.out.println("Premier lead: " + leads.get(0));
                }
            } catch (TException e) {
                System.out.println("❌ Erreur: " + e.getMessage());
                e.printStackTrace();
            }
            
            transport.close();
            System.out.println("\n✅ Tests terminés avec succès !");
            
        } catch (TException e) {
            System.out.println("❌ Erreur de connexion Thrift: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

