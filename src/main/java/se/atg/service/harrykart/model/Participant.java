package se.atg.service.harrykart.model;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Class that represents a participant and a list of powerup for each lap.
 * Since input data contains a generic integer type we have to assume that it could be larger than
 * a native type can hold.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
public class Participant {
    final BigInteger lane;
    final BigInteger baseSpeed;
    final String name;
    final List<BigInteger> powerUps;
}
