package fr.univangers.internalcrm.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Template de servlet pour les services Thrift
 */
public class ThriftHttpServletTemplate extends HttpServlet {

    private final TProcessor processor;

    private final TProtocolFactory inProtocolFactory;

    private final TProtocolFactory outProtocolFactory;

    private final Collection<Map.Entry<String, String>> customHeaders;

    /**
     * Constructeur avec processeur et factories de protocole
     *
     * @param processor          Processeur Thrift
     * @param inProtocolFactory  Factory de protocole d'entrée
     * @param outProtocolFactory Factory de protocole de sortie
     */
    public ThriftHttpServletTemplate(TProcessor processor, TProtocolFactory inProtocolFactory,
            TProtocolFactory outProtocolFactory) {
        super();
        this.processor = processor;
        this.inProtocolFactory = inProtocolFactory;
        this.outProtocolFactory = outProtocolFactory;
        this.customHeaders = new ArrayList<Map.Entry<String, String>>();
    }

    /**
     * Constructeur avec processeur et factory de protocole unique
     *
     * @param processor       Processeur Thrift
     * @param protocolFactory Factory de protocole (entrée et sortie)
     */
    public ThriftHttpServletTemplate(TProcessor processor, TProtocolFactory protocolFactory) {
        this(processor, protocolFactory, protocolFactory);
    }

    /**
     * Traite les requêtes POST
     *
     * @param request  Requête HTTP
     * @param response Réponse HTTP
     * @throws ServletException En cas d'erreur de servlet
     * @throws IOException      En cas d'erreur d'entrée/sortie
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TTransport inTransport = null;
        TTransport outTransport = null;

        try {
            response.setContentType("application/x-thrift");

            if (null != this.customHeaders) {
                for (Map.Entry<String, String> header : this.customHeaders) {
                    response.addHeader(header.getKey(), header.getValue());
                }
            }
            InputStream in = request.getInputStream();
            OutputStream out = response.getOutputStream();

            TTransport transport = new TIOStreamTransport(in, out);
            inTransport = transport;
            outTransport = transport;

            TProtocol inProtocol = inProtocolFactory.getProtocol(inTransport);
            TProtocol outProtocol = outProtocolFactory.getProtocol(outTransport);

            processor.process(inProtocol, outProtocol);
            out.flush();
        } catch (TException te) {
            throw new ServletException(te);
        }
    }

    /**
     * Traite les requêtes GET (déléguées à POST)
     *
     * @param request  Requête HTTP
     * @param response Réponse HTTP
     * @throws ServletException En cas d'erreur de servlet
     * @throws IOException      En cas d'erreur d'entrée/sortie
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Ajoute un header personnalisé
     *
     * @param key   Clé du header
     * @param value Valeur du header
     */
    public void addCustomHeader(final String key, final String value) {
        this.customHeaders.add(new Map.Entry<String, String>() {
            public String getKey() {
                return key;
            }

            public String getValue() {
                return value;
            }

            public String setValue(String value) {
                return null;
            }
        });
    }

    /**
     * Définit les headers personnalisés
     *
     * @param headers Collection de headers
     */
    public void setCustomHeaders(Collection<Map.Entry<String, String>> headers) {
        this.customHeaders.clear();
        this.customHeaders.addAll(headers);
    }
}