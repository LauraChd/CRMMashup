package fr.univangers.calculator;

import org.example.internalcrm.thrift.CalculatorService;
//import org.example.internalcrm.thrift.CalculatorService.Iface;
import org.example.internalcrm.thrift.ThriftDivisionBy0;
import org.apache.thrift.TProcessor;
//import org.calculator.thrift.CalculatorService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;


public class ThrifCalculatorServiceServlet extends fr.univangers.calculator.ThriftHttpServletTemplate {


    public ThrifCalculatorServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {
        return new CalculatorService.Processor<CalculatorService.Iface>(new CalculatorServiceImpl());
    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

}
