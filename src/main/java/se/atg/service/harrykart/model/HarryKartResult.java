package se.atg.service.harrykart.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarryKartResult {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static public class PositionObject {
        private String horse;
        private Integer position;
    }

    private List<PositionObject> ranking;
}
