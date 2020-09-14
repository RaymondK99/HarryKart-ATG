package se.atg.service.harrykart.service.integration;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import se.atg.service.harrykart.model.HarryKartResult;
import se.atg.service.harrykart.rest.data.HarryKartType;

import java.io.*;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HarryCartRaceServiceTest {


    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private String uri;

    @Before
    public void setup() {
        this.uri = "http://localhost:" + serverPort + "/api/play";
    }

    @Test
    public void testInput1() throws Exception {
        HarryKartType harryKartType = new HarryKartType();

        // Create HTTP request object
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/xml");
        HttpEntity<String> request = new HttpEntity<>(readResourceFile("input_0.xml"), headers);
        ResponseEntity<HarryKartResult> result = this.restTemplate.postForEntity(uri, request, HarryKartResult.class);
        HarryKartResult harryKartResult = result.getBody();

        // Verify result
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(3, harryKartResult.getRanking().size() );
        assertEquals( 1, harryKartResult.getRanking().get(0).getPosition().intValue());
        assertEquals( 2, harryKartResult.getRanking().get(1).getPosition().intValue());
        assertEquals( 3, harryKartResult.getRanking().get(2).getPosition().intValue());

        assertEquals( "TIMETOBELUCKY", harryKartResult.getRanking().get(0).getHorse());
        assertEquals( "HERCULES BOKO", harryKartResult.getRanking().get(1).getHorse());
        assertEquals( "CARGO DOOR", harryKartResult.getRanking().get(2).getHorse());

    }

    @Test
    public void testInput2() throws Exception {
        HarryKartType harryKartType = new HarryKartType();

        // Create HTTP request object
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/xml");
        HttpEntity<String> request = new HttpEntity<>(readResourceFile("input_1.xml"), headers);
        ResponseEntity<HarryKartResult> result = this.restTemplate.postForEntity(uri, request, HarryKartResult.class);
        HarryKartResult harryKartResult = result.getBody();

        // Verify result
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(3, harryKartResult.getRanking().size() );
        assertEquals( 1, harryKartResult.getRanking().get(0).getPosition().intValue());
        assertEquals( 2, harryKartResult.getRanking().get(1).getPosition().intValue());
        assertEquals( 3, harryKartResult.getRanking().get(2).getPosition().intValue());

        assertEquals( "WAIKIKI SILVIO", harryKartResult.getRanking().get(0).getHorse());
        assertEquals( "TIMETOBELUCKY", harryKartResult.getRanking().get(1).getHorse());
        assertEquals( "HERCULES BOKO", harryKartResult.getRanking().get(2).getHorse());

    }


    @Test
    public void testIllegalInput() throws Exception {
        HarryKartType harryKartType = new HarryKartType();

        // Create HTTP request object
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/xml");
        HttpEntity<String> request = new HttpEntity<>("IllegalInput", headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Unable to parse input data.", result.getBody());
    }

    @Test
    public void testIllegalXml1() throws Exception {
        // Create HTTP request object
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/xml");
        HttpEntity<String> request = new HttpEntity<>(readResourceFile("input_illegal_0.xml"), headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Unable to parse input data.", result.getBody());

    }


    /**
     * Utility method, read content of file and return as a String object
     * @param fileName
     * @return
     * @throws IOException
     */
    private String readResourceFile(String fileName) throws IOException {
        String file ="src/main/resources/" + fileName;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        String currentLine;
        while ( (currentLine = reader.readLine()) != null) {
            sb.append(currentLine).append('\n');
        }

        reader.close();
        return sb.toString();
    }



}
