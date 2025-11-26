package fr.univangers;

import org.apache.thrift.TException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Interface de communication avec le VirtualCRM (appelé par le Client)
 */
@SpringBootApplication
public class VirtualCRM {

    public static void main(String[] args) throws IOException, TException {
        SpringApplication.run(VirtualCRM.class, args);
    }
}

