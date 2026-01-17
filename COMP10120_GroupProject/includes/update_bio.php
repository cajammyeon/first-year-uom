<?php
session_start();
require_once 'dbh.inc.php'; 

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Check if the bio field is set and not empty
    if (isset($_POST['bio']) && !empty($_POST['bio'])) {
        $bio = $_POST['bio'];
        $userId = $_SESSION['user_id'];

        // Update the user's bio in the database
        $sql = "UPDATE Users SET bio = :bio WHERE UserID = :userId";
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam(':bio', $bio);
        $stmt->bindParam(':userId', $userId);

        if ($stmt->execute()) {
            // Bio updated successful
            header("Location: ../settings.php?update=success");
            exit();
        } else {
            // Error updating bio
            header("Location: ../settings.php?error=database");
            exit();
        }
    } else {
        // Bio field is empty
        header("Location: ../settings.php?error=emptybio");
        exit();
    }
} else {
    // Redirect to settings.php if accessed without form submission
    header("Location: ../settings.php");
    exit();
}
?>
