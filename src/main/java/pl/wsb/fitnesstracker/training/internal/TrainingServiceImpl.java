package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import pl.wsb.fitnesstracker.training.api.TrainingService;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Training> findById(Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    @Override
    public List<Training> findAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> findTrainingsByUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    @Override
    public List<Training> findTrainingsAfterDate(Date date) {
        return trainingRepository.findByEndTimeAfter(date);
    }

    @Override
    public List<Training> findTrainingsByActivity(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    @Override
    public Training createTrainingForUser(TrainingDto trainingDto) {
        User user = userRepository.findById(trainingDto.userId())
                .orElseThrow(() -> new UserNotFoundException(trainingDto.userId()));

        return trainingRepository.save(new Training(user,
            trainingDto.startTime(),
            trainingDto.endTime(),
            trainingDto.activityType(),
            trainingDto.distance(),
            trainingDto.averageSpeed()));
    }

    @Override
    public Training updateOrCreateTrainingForUser(Long trainingId, TrainingDto newTrainingDto) {
        User user = userRepository.findById(newTrainingDto.userId())
                .orElseThrow(() -> new UserNotFoundException(newTrainingDto.userId()));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));

        training.setUser(user);
        training.setStartTime(newTrainingDto.startTime());
        training.setEndTime(newTrainingDto.endTime());
        training.setActivityType(newTrainingDto.activityType());
        training.setDistance(newTrainingDto.distance());
        training.setAverageSpeed(newTrainingDto.averageSpeed());

        return trainingRepository.save(training);
    }
}