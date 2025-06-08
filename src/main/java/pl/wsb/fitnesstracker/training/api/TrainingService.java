package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    /**
     * Retrieves a training by its unique identifier.
     *
     * @param trainingId the ID of the training to retrieve
     * @return an {@link Optional} containing the {@link Training} if found, or empty if not found
     */
    Optional<Training> findById(Long trainingId);

    /**
     * Retrieves all training entries from the system.
     *
     * @return a list of all {@link Training} entities
     */
    List<Training> findAllTrainings();

    /**
     * Retrieves all trainings associated with a specific user.
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a list of {@link Training} entries associated with the user
     */
    List<Training> findTrainingsByUserId(Long userId);

    /**
     * Retrieves all trainings that finished after a specified date.
     *
     * @param date the date after which training sessions must have ended
     * @return a list of {@link Training} entries that ended after the given date
     */
    List<Training> findTrainingsAfterDate(Date date);

    /**
     * Retrieves all trainings filtered by activity type.
     *
     * @param activityType the type of activity (e.g., RUNNING, CYCLING)
     * @return a list of {@link Training} entries matching the specified activity type
     */
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
