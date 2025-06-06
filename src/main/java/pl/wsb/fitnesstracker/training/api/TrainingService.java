package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.training.internal.TrainingDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Optional<Training> findById(Long trainingId);
    List<Training> findAllTrainings();
    List<Training> findTrainingsByUserId(Long userId);
    List<Training> findTrainingsAfterDate(Date date);
    List<Training> findTrainingsByActivity(ActivityType activityType);

    /**
     * Creates a new {@link Training} entry for a specified {@link User}.
     * <p>
     * This method retrieves the user based on the ID provided in the {@link TrainingDto}.
     * If the user exists, it creates a new {@code Training} entity using the details from
     * the DTO and saves it to the repository. If the user is not found, a
     * {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param trainingDto the DTO containing training details and the ID of the user to associate with
     * @return the newly created {@link Training} entity
     * @throws UserNotFoundException if no user is found with the ID specified in {@code trainingDto}
     */
    Training createTrainingForUser(TrainingDto trainingDto);

    /**
     * Updates an existing {@link Training} entry with new data provided in a {@link TrainingDto}.
     * <p>
     * This method retrieves the {@link User} and the {@link Training} based on the identifiers provided in
     * the parameters. If either the user or training is not found, it throws the corresponding exception.
     * Once found, the training is updated with new values from the DTO and persisted.
     * </p>
     *
     * @param trainingId      the ID of the training to be updated
     * @param newTrainingDto  a DTO containing updated training data and a user ID reference
     * @return the updated {@link Training} entity
     * @throws UserNotFoundException     if no user is found with the given user ID in {@code newTrainingDto}
     * @throws TrainingNotFoundException if no training is found with the given {@code trainingId}
     */
    Training updateOrCreateTrainingForUser(Long trainingId, TrainingDto newTrainingDto);
}
