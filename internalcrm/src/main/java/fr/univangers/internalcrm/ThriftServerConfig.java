/*
package fr.univangers.internalcrm;

import fr.univangers.internalcrm.service.InternalCRMServiceImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.example.internalcrm.thrift.InternalCRMService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThriftServerConfig {
    private static final int PORT = 8090;

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

    @Bean
    public InternalCRMService.Processor<InternalCRMServiceImpl> processor(InternalCRMServiceImpl internalCRMImpl) {
        return new InternalCRMService.Processor<>(internalCRMImpl);
    }

    @Bean
    public InternalCRMServiceImpl internalCRMImpl() {
        return new InternalCRMServiceImpl();
    }
}
*/
