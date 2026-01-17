<?php
function getRandomUser($currentUser, $pdo) {
    // Define an age range for similar age
    $ageRange = 2;
    $minAge = $currentUser["age"] - $ageRange;
    $maxAge = $currentUser["age"] + $ageRange;

    // Prepare the SQL query to find users with at least one game or hobby in common and same timezone and similar age
    // Also exclude users that are recorded as unmatched with the current user
    $sql = "
        SELECT DISTINCT u.UserID 
        FROM Users u
        WHERE u.UserID != :currentUserId
        AND u.UserID NOT IN (
            SELECT um.user1_id FROM unmatched um WHERE um.user2_id = :currentUserId
            UNION
            SELECT um.user2_id FROM unmatched um WHERE um.user1_id = :currentUserId
        )
        AND (
            u.game1 IN (:game1, :game2, :game3) OR
            u.game2 IN (:game1, :game2, :game3) OR
            u.game3 IN (:game1, :game2, :game3) OR
            u.hobby1 IN (:hobby1, :hobby2, :hobby3) OR
            u.hobby2 IN (:hobby1, :hobby2, :hobby3) OR
            u.hobby3 IN (:hobby1, :hobby2, :hobby3)
        )
        AND u.timezone = :timezone
        AND u.age BETWEEN :minAge AND :maxAge
        ORDER BY RAND()
        LIMIT 1;
    ";

    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':currentUserId', $currentUser['UserID'], PDO::PARAM_INT);
    $stmt->bindParam(':game1', $currentUser['game1'], PDO::PARAM_STR);
    $stmt->bindParam(':game2', $currentUser['game2'], PDO::PARAM_STR);
    $stmt->bindParam(':game3', $currentUser['game3'], PDO::PARAM_STR);
    $stmt->bindParam(':hobby1', $currentUser['hobby1'], PDO::PARAM_STR);
    $stmt->bindParam(':hobby2', $currentUser['hobby2'], PDO::PARAM_STR);
    $stmt->bindParam(':hobby3', $currentUser['hobby3'], PDO::PARAM_STR);
    $stmt->bindParam(':timezone', $currentUser['timezone'], PDO::PARAM_STR);
    $stmt->bindParam(':minAge', $minAge, PDO::PARAM_INT);
    $stmt->bindParam(':maxAge', $maxAge, PDO::PARAM_INT);
    $stmt->execute();

    $matchedUser = $stmt->fetch(PDO::FETCH_ASSOC);

    return $matchedUser ? $matchedUser['UserID'] : null;
}
