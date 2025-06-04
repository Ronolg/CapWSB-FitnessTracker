package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {



    List<Training> findAllTrainings();

    List<Training> findTrainingsByUserId(Long userId);

    List<Training> findTrainingsAfterDate(Date date);

    List<Training> findTrainingsByActivity(ActivityType activityType);

    Optional<Training> findById(Long id);
}