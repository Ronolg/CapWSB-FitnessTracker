package pl.wsb.fitnesstracker.training.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;

@Component
public class TrainingMapper {
    TrainingDto toDto(Training training) {
        return new TrainingDto(training.getId(),
                training.getUser().getId(),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
<<<<<<< HEAD
                training.getAverageSpeed());
    }
}
=======
                training.getAverageSpeed()
        );
    }


}
>>>>>>> db2c946bac2b51318c1c55dbb579ac00e4465c35
