/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.utilities;

import com.kparking.oltranz.config.AppDesc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import static java.lang.System.out;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Hp
 */
public class DataFactory {
    
    public DataFactory() {
    }
    
    public static final String objectToString(Object object){
        ObjectMapper mapper= new ObjectMapper();
        
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            out.println(AppDesc.APP_DESC+" Mapping object: "+object.getClass().getName());
            String jsonData=mapper.writeValueAsString(object);
            return jsonData;
        } catch (IOException e) {
            out.println(AppDesc.APP_DESC+" Failed to map object: "+object.getClass().getName()+"  due to Error: "+e.getLocalizedMessage());
        }
        return null;
    }
    
    public static final List<Object> stringToObjectList(Class className, String jsonString){
        ObjectMapper mapper= new ObjectMapper();
        
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, className));
        }catch(Exception e){
            out.println(AppDesc.APP_DESC+" Failed mapping string: "+ jsonString+"  due to Error: "+e.getLocalizedMessage());
        }
        return null;
    }
    
    public static final Object stringToObject(Class className, String jsonString){
        ObjectMapper mapper= new ObjectMapper();
        
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            out.println(AppDesc.APP_DESC+" Mapping string: "+ jsonString);
            Object result=mapper.readValue(jsonString,className);
            if(result != null)
                out.println(AppDesc.APP_DESC+" Mapping string Completed succesfully: "+ jsonString);
            else
                out.println(AppDesc.APP_DESC+" Mapping string Completed with null result: "+ jsonString);
            return result;
        } catch (IOException e) {
            out.println(AppDesc.APP_DESC+" Failed mapping string: "+ jsonString+"  due to Error: "+e.getLocalizedMessage());
        }
        return null;
    }
    
    public static final Object xmlStringToObject(Class className, String xmlString){
        try{
            out.println(AppDesc.APP_DESC+" Mapping string: "+ xmlString);
            XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            XMLStreamReader streamReader= xmlFactory.createXMLStreamReader(streamSource);
            
            JAXBContext jc = JAXBContext.newInstance(className);
            Unmarshaller unMarshaller = jc.createUnmarshaller();
            Object object= unMarshaller.unmarshal(streamReader);
            
            if(object != null)
                out.println(AppDesc.APP_DESC+" Mapping string Completed succesfully: "+ xmlString);
            else
                out.println(AppDesc.APP_DESC+" Mapping string Completed with null result: "+ xmlString);
            
            return object;
        }catch(JAXBException | XMLStreamException e){
            out.print(AppDesc.APP_DESC+"Error due to "+e.getLocalizedMessage());
            return null;
        }
    }
    
    public static final String objectToXmlString(Object object){
        try{
            out.println(AppDesc.APP_DESC+" Convertion of Object to XML String is starting");
            JAXBContext ctx = JAXBContext.newInstance(object.getClass());
            Marshaller msh = ctx.createMarshaller();
            msh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            msh.marshal(object, writer);
            String result = writer.toString();
            out.println(AppDesc.APP_DESC+" Mapping string Completed succesfully with: "+ result);
            return result;
        }catch(JAXBException e){
            out.print(AppDesc.APP_DESC+" Error due to "+e.getLocalizedMessage());
            return null;
        }
    }
    
    public static final String streamToString(InputStream inputStream){
        try {
            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String read;
            
            while((read=br.readLine()) != null) {
                out.println(AppDesc.APP_DESC+" decoding stream "+read);
                sb.append(read);
            }
            
            br.close();
            
            String result = sb.toString();
            out.print(AppDesc.APP_DESC+"Decoded the stream "+result);
            return result;
        } catch (IOException e) {
            out.print(AppDesc.APP_DESC+"Error converting stream due to: "+ e.getLocalizedMessage());
            return null;
        }
    }
    
    public static final String[] splitString(String input, String criteria){
        out.print(AppDesc.APP_DESC+"DataFactory splitString going to split: "+ input+" based on criteria: ["+criteria+"]");
        String[] outPut = input.split("\\"+criteria);
        out.print(AppDesc.APP_DESC+"DataFactory splitString sample result: part 0: "+outPut[0]+" part 1: "+outPut[1]);
        return outPut;
    }
    
    public static final String phoneFormat(String input){
        try{
            out.print(AppDesc.APP_DESC+"DataFactory phoneFormat going to format: "+ input);
            input = input.trim().replace(" ", "").replace("+", "");
            if(input.length() < 10){
                out.print(AppDesc.APP_DESC+"DataFactory phoneFormat failed to format: "+ input+" due to: invaliv length");
                return "Tel ntago yemewe, invalid tel, tel refuser.";
            }
            String firstPart = input.substring(0,1);
            if(firstPart.equalsIgnoreCase("07"))
                input = "25"+input;
            return input;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"DataFactory phoneFormat failed to format: "+ input+" due to:"+e.getMessage());
            return null;
        }
    }
    
    public static final long printDifference(Date startDate, Date endDate){
        
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        long elapsedTime = elapsed(startDate, endDate);
        
        out.println(AppDesc.APP_DESC+" DataFactory printDifference startDate : " + startDate);
        out.println(AppDesc.APP_DESC+" DataFactory printDifference endDate : "+ endDate);
        out.println(AppDesc.APP_DESC+" DataFactory printDifference difference in millisecond : " + different);
        out.println(AppDesc.APP_DESC+" DataFactory printDifference Elapsed minutes: " + elapsedTime);
        
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        
        long elapsedSeconds = different / secondsInMilli;
        
        System.out.printf(
                AppDesc.APP_DESC+" DataFactory printDifference %d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
        
        return elapsedTime;
    }
    
    public static final boolean numberPlateValidator(String numberPlate){
        String regexRwanda = "^RA[A-Z][0-9]{3}[A-Z]$";
        String regexPolice = "^RNP[0-9]{3}[A-Z]$";
        String regexRDF = "^RDF[0-9]{3}[A-Z]$";
        String regexGov = "^GOV[0-9]{3}[A-Z]{2}$";
        String regexGr = "^GR[0-9]{3}[A-Z]{1}$";
        String regexIT = "^IT[0-9]{3}[A-Z]{2}$";
        String regexRDC = "^CGO[0-9]{4}[A-B]{2}[0-9]{2}$";
        String regexDRC1 = "^[0-9]{3}[A-Z]{1}[0-9]{3}$";
        String otherNPlate = "^[0-9][A-Z]{2}[0-9]{3}[A-Z]$";
        
        String regexUn = "^UN[0-9]{3}[A-Z]{2}$";
        String regexBurundi = "^[A-Z][0-9]{4}$";
        String regexBurundi1 = "^[A-Z][0-9]{4}[A-Z]$";
        String regexUg = "^U[A-Z][0-9]{3}[A-Z]$";
        String regexKenya = "^K[A-Z][A-Z][0-9]{3}[A-Z]$";
        String regexMoto = "^R[A-Z][0-9]{3}[A-Z]$";
        String totalRegx = regexRwanda+"|"+regexBurundi+"|"+regexUg+"|"+regexRDC+"|"+regexMoto+"|"+regexIT+"|"+regexRDF+"|"+regexGov+"|"+regexKenya+"|"+regexPolice+"|"+regexBurundi1+"|"+regexGr+"|"+regexDRC1+"|"+regexUn+"|"+otherNPlate;
        out.print(AppDesc.APP_DESC+"DataFactory numberPlateValidator Current Regex: " + totalRegx);
        out.print(AppDesc.APP_DESC+"DataFactory numberPlateValidator Current Input: " + numberPlate);
        // Create a Pattern object
        Pattern r = Pattern.compile(totalRegx);
        // Now create matcher object.
        Matcher m = r.matcher(numberPlate);
        
        boolean isFound = Pattern.compile(totalRegx).matcher(numberPlate).find();
        if (isFound) {
            out.print(AppDesc.APP_DESC+"DataFactory numberPlateValidator Found value: " + numberPlate);
            return true;
        }
        out.print(AppDesc.APP_DESC+"DataFactory numberPlateValidator NO MATCH for: "+numberPlate);
        return false;
    }
    private static long elapsed(Date startDate, Date endDate) {
        long diffMills = endDate.getTime() - startDate.getTime();
        return (diffMills/1000)/60;
    }
    
    public static long diffMinutes(Date startDate, Date endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            startDate = format.parse(format.format(startDate));
            endDate = format.parse(format.format(endDate));
            out.print(AppDesc.APP_DESC+" AppProcessor diffInHour calculating hour difference startDate: "+format.format(startDate)+" endDate: "+format.format(endDate));
            long result = (endDate.getTime() - startDate.getTime())/ (60 * 1000);
            return result;
        } catch (ParseException e) {
            out.print(AppDesc.APP_DESC+"AppProcessor diffInHour failed to generate report due to: "+e.getMessage());
            return 1;
        }
    }
}
