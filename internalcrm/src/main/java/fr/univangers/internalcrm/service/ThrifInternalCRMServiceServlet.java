package fr.univangers.internalcrm.service;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import fr.univangers.internalcrm.thrift.InternalCRMService;

/**
 * Servlet exposant le service InternalCRM via Thrift.
 */
public class ThrifInternalCRMServiceServlet extends ThriftHttpServletTemplate {

    /**
     * Constructeur.
     */
    public ThrifInternalCRMServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {
        return new InternalCRMService.Processor<InternalCRMService.Iface>(new InternalCRMServiceImpl());
    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

}
