package pl.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);
    List<User> findAllUsers();
    Optional<User> findById(Long id);
    Optional<User> getUserByEmail(String email);
    List<User> searchUsers(String firstName, String lastName, LocalDate birthdate);

    /**
     * Deletes the user with the specified ID from the system.
     *
     * <p>If a user with the given ID exists, it is removed from the database and the deleted
     * user entity is returned. If no such user exists, a {@link UserNotFoundException} is thrown.</p>
     *
     * @param userId the ID of the user to delete
     * @return the deleted {@link User} entity
     * @throws UserNotFoundException if no user with the specified ID is found
     */
    User deleteUserById(Long userId);

    /**
     * Finds all users whose email addresses contain the specified fragment, ignoring case.
     *
     * <p>This method retrieves all users from the repository and performs in-memory filtering
     * using Java Streams. Only users with non-null email addresses that contain the specified
     * fragment (case-insensitive) are included in the result.</p>
     *
     * @param fragment the email substring to search for (case-insensitive)
     * @return a list of {@link User} objects whose email contains the specified fragment
     */
    List<User> findUsersByEmailFragment(String fragment);

    /**
     * Retrieves all users whose age is greater than the specified threshold.
     *
     * <p>This method fetches all users from the repository and filters them based on their age,
     * calculated from their birthdate. Only users older than the given age threshold are included
     * in the result.</p>
     *
     * @param ageThreshold the minimum age (exclusive) users must exceed to be included
     * @return a list of {@link User} objects older than the specified age
     */
    List<User> findAllUsersOlderThan(int ageThreshold);

    /**
     * Updates an existing user with the provided data or creates a new user if the user does not exist.
     * <p>
     * If a user with the given {@code userId} exists, their details (first name, last name,
     * birthdate, and email) will be updated with values from {@code newUser}. If no such user exists,
     * a new user is created using the provided {@code newUser} object.
     * </p>
     *
     * @param userId   the ID of the user to update or create
     * @param newUser  the user data to apply to the existing user or to use for creating a new user
     * @return an {@link Optional} containing the newly created user if one was created,
     *         or the updated user if one already existed
     */
    Optional<User> updateOrCreateUser(Long userId, User newUser);
}