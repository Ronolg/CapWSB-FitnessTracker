package pl.wsb.fitnesstracker.training.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wsb.fitnesstracker.training.api.Training;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TrainingDto {
    private Long id;
    private Long userId;
    private Date startTime;
    private Date endTime;
    private ActivityType activityType;
    private double distance;
    private double averageSpeed;

    public static TrainingDto fromEntity(Training training) {
        return new TrainingDto(
                training.getId(),
                training.getUser().getId(),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }
}

