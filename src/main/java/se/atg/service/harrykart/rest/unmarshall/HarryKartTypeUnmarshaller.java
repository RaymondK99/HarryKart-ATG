package se.atg.service.harrykart.rest.unmarshall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import se.atg.service.harrykart.rest.data.HarryKartType;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;

@Component
public class HarryKartTypeUnmarshaller {
    private static final Logger log = LoggerFactory.getLogger(HarryKartTypeUnmarshaller.class);
    private static final String xsdFile = "src/main/resources/input.xsd";

    private JAXBContext jaxbContext;
    private Unmarshaller jaxbUnmarshaller;

    @PostConstruct
    private void init() {
        try {
            // Allocate common instance for unmarshaller and jaxb context
            jaxbContext = JAXBContext.newInstance(HarryKartType.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Add schema validation
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema employeeSchema = schemaFactory.newSchema(new File(xsdFile));
            jaxbUnmarshaller.setSchema(employeeSchema);
        } catch (SAXException | JAXBException e) {
            String message = "Failed to init unmarshaller:" + e.getMessage();
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    // Only one HTTP thread can use this method since the unmarshaller is not thread safe
    public synchronized HarryKartType convertToObject(String inputXml) throws JAXBException {

        JAXBElement<HarryKartType> jaxbElement = (JAXBElement<HarryKartType>) jaxbUnmarshaller
                .unmarshal(new StreamSource(new StringReader(inputXml)),HarryKartType.class);

        HarryKartType harryKartType = jaxbElement.getValue();

        return harryKartType;
    }

}
