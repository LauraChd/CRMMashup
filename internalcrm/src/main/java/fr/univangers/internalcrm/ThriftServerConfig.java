
package fr.univangers.internalcrm;

import fr.univangers.internalcrm.service.InternalCRMServiceImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import fr.univangers.internalcrm.thrift.InternalCRMService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration du serveur Thrift
 */
@Configuration
public class ThriftServerConfig {
    private static final int PORT = 8090;

    /**
     * Crée le serveur Thrift
     *
     * @param processor Le processeur Thrift
     * @return Le serveur Thrift
     */
    @Bean
    public TServer thriftServer(InternalCRMService.Processor<InternalCRMServiceImpl> processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(PORT);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            new Thread(() -> {
                System.out.println("Starting internalCRM Thrift server on port " + PORT + "...");
                server.serve();
            }).start();

            return server;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start Thrift server", e);
        }
    }

    /**
     * Crée le processeur Thrift
     *
     * @param internalCRMImpl L'implémentation du service
     * @return Le processeur Thrift
     */
    @Bean
    public InternalCRMService.Processor<InternalCRMServiceImpl> processor(InternalCRMServiceImpl internalCRMImpl) {
        return new InternalCRMService.Processor<>(internalCRMImpl);
    }

    /**
     * Crée l'implémentation du service InternalCRM
     *
     * @return L'implémentation du service
     */
    @Bean
    public InternalCRMServiceImpl internalCRMImpl() {
        return new InternalCRMServiceImpl();
    }
}
