package se.atg.service.harrykart.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.atg.service.harrykart.model.*;
import se.atg.service.harrykart.rest.data.HarryKartType;
import se.atg.service.harrykart.rest.data.LoopType;
import se.atg.service.harrykart.rest.unmarshall.HarryKartTypeUnmarshaller;
import se.atg.service.harrykart.service.HarryCartRaceService;

import javax.xml.bind.JAXBException;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Validated
public class HarryKartController {

    private static final Logger log = LoggerFactory.getLogger(HarryKartController.class);

    @Autowired
    HarryCartRaceService harryCartRaceService;

    @Autowired
    HarryKartTypeUnmarshaller harryKartTypeUnmarshaller;

    @RequestMapping(method = RequestMethod.POST, path = "/play", consumes = "application/xml", produces = "application/json")
    public ResponseEntity<HarryKartResult> playHarryKart(@RequestBody String strHarryKartType) {
        log.debug("Got request:" + strHarryKartType);

        HarryKartType harryKartType;

        try {
            // Unmarshall and validate input XML against XSD Schema
            harryKartType = harryKartTypeUnmarshaller.convertToObject(strHarryKartType);
        } catch (JAXBException exception) {
            log.warn("Failed to convert xml:" + exception.getMessage());
            log.warn("input xml:" + strHarryKartType);
            throw new IllegalArgumentException("Illegal input data.");
        }

        // Call service layer
        HarryKartResult resultList = harryCartRaceService.run(convert(harryKartType));

        return ResponseEntity.ok(resultList);
    }

    /**
     * Convert XML object to internal model
     *
     * @param harryKartType
     * @return
     */
    private HarryKartRace convert(HarryKartType harryKartType) {

        HarryKartRace harryKartRace = HarryKartRace.builder().numberOfLoops(harryKartType.getNumberOfLoops()).build();
        // Sort list of power ups by loop number in case its not sorted..
        harryKartType.getPowerUps().getLoop().stream().sorted(Comparator.comparing(LoopType::getNumber));

        // Build list of participants
        harryKartRace.setParticipantList(
                harryKartType.getStartList().getParticipant().stream().map( (participant-> {

                    // Build a list of power ups for each participant
                    List<BigInteger> powerUpsList = harryKartType.getPowerUps().getLoop().stream().map(powerUps ->
                            powerUps.getLane().stream()
                                    .filter( p -> p.getNumber().equals(participant.getLane()))
                                    .findFirst()
                                    .map( p -> p.getValue())
                                    .get()
                    ).collect(Collectors.toList());

                    // Build participant
                    return Participant.builder()
                            .name(participant.getName())
                            .baseSpeed(participant.getBaseSpeed())
                            .lane(participant.getLane())
                            .powerUps(powerUpsList)
                            .build();
                }
        )).collect(Collectors.toList()));

        return harryKartRace;
    }
}
