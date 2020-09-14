package se.atg.service.harrykart.service;


import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.atg.service.harrykart.model.HarryKartRace;
import se.atg.service.harrykart.model.HarryKartResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HarryCartRaceService {

    private static final Logger log = LoggerFactory.getLogger(HarryCartRaceService.class);

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    static class IntermediateResult {
        private final String name;
        private final BigDecimal accumulatedTime;
    }

    /**
     * Calculate positions according to each participants base speeds and power ups.
     * Use BigInteger and BigDecimals here in order no to loose precision this input data
     * comes in that format.
     *
     * @param harryKartRace
     * @return
     */
    public HarryKartResult run(HarryKartRace harryKartRace) {

        List<IntermediateResult> resultList = harryKartRace.getParticipantList().stream().map( participant -> {
            BigDecimal currentSpeed = new BigDecimal(participant.getBaseSpeed());
            // Add time for first lap
            BigDecimal accumulatedTime = BigDecimal.ONE.divide(currentSpeed);

            // Iterate through each lap for current participant
            for(BigInteger powerUp : participant.getPowerUps()) {
                // Update current speed
                currentSpeed = currentSpeed.add(new BigDecimal(powerUp));

                // Update acc time
                BigDecimal timeToCompleteLap = BigDecimal.ONE.divide(currentSpeed, MathContext.DECIMAL128);
                accumulatedTime = accumulatedTime.add(timeToCompleteLap);
            }

            return new IntermediateResult(participant.getName(), accumulatedTime);
        })
                // Sort according to time consumed to complete a race
                .sorted(Comparator.comparing(a -> a.accumulatedTime))
                .collect(Collectors.toList());

        // Create result object list
        List<HarryKartResult.PositionObject> positionObjectList =
                IntStream.range(0,resultList.size() >= 3 ? 3 : resultList.size()) // Only 3 first positions
                    .mapToObj( index -> {
                            IntermediateResult res = resultList.get(index);
                            // Create object that contains name and position
                            return new HarryKartResult.PositionObject(res.getName(), index + 1);
                        }
                ).collect(Collectors.toList());

        // Return result object
        return new HarryKartResult(positionObjectList);
    }

}
