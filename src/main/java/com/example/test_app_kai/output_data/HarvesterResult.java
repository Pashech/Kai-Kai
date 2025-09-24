package com.example.test_app_kai.output_data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Component
public class HarvesterResult {

    private String harvestMessage;
    private Integer resultCode;
    private boolean uploadSuccess;
    private String resultFileName;
    private List<TrainTicket> tickets;
    private Long dateTimestamp;

    public HarvesterResult(String harvestMessage, Integer resultCode) {
        this.harvestMessage = harvestMessage;
        this.resultCode = resultCode;
    }

    public HarvesterResult(String harvestMessage, Integer resultCode, String resultFileName) {
        this.harvestMessage = harvestMessage;
        this.resultCode = resultCode;
        this.resultFileName = resultFileName;
    }

    public HarvesterResult(String harvestMessage, Integer resultCode, String resultFileName, List<TrainTicket> tickets, Long dateTimestamp) {
        this.harvestMessage = harvestMessage;
        this.resultCode = resultCode;
        this.resultFileName = resultFileName;
        this.tickets = tickets;
        this.dateTimestamp = dateTimestamp;
    }
}
