package se.atg.service.harrykart.model;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class HarryKartRace {
    BigInteger numberOfLoops;
    List<Participant> participantList;
}
