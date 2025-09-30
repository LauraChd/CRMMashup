package fr.univangers.internalcrm;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.example.internalcrm.thrift.InternalCRMService;


public class ThrifInternalCRMServiceServlet extends ThriftHttpServletTemplate {


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
