package it.av.eatt.todb;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.data.Country;
import it.av.eatt.ocm.model.data.CountryRegion;
import it.av.eatt.service.CountryRegionService;
import it.av.eatt.service.CountryService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
@ContextConfiguration(locations = "classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class ImportCountry {
    @Autowired
    private CountryRegionService countryRegionService;
    @Autowired
    private CountryService countryService;
    
    //@Test
    public void runImportCountryRegion(){
        try {
            BufferedReader in = new BufferedReader(new FileReader("/home/alessandro/Prod/Data/country.txt"));
            String str;
            ArrayList<CountryRegion> countries = new ArrayList<CountryRegion>();
            in.readLine();
            while ((str = in.readLine()) != null) {
                System.out.println(str);
                CountryRegion cr= new CountryRegion();
                String[] splittedLine = str.split("\t");
                cr.setIso2(splittedLine[0]);
                cr.setIso3(splittedLine[1]);
                cr.setName(splittedLine[2]);
                cr.setRegion(splittedLine[3]);
                //countryRegionService.save(cr);
            }
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    
    @Test
    public void runImportCountry(){
        try {
            BufferedReader in = new BufferedReader(new FileReader("/home/alessandro/Prod/Data/country.txt"));
            String str;
            HashMap<String, Country> countries = new HashMap<String, Country>();
            
            in.readLine();
            while ((str = in.readLine()) != null) {
                System.out.println(str);
                Country cr= new Country();
                String[] splittedLine = str.split("\t");
                cr.setIso2(splittedLine[0]);
                cr.setIso3(splittedLine[1]);
                cr.setName(splittedLine[2]);
                countries.put(cr.getIso3(), cr);
            }
            in.close();

            for (Country c : countries.values()) {
                countryService.save(c);
                System.out.println(c.getName());
            }
            System.out.println(countries.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JackWicketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
