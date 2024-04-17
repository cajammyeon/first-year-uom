<?php
session_start();
require_once 'dbh.inc.php'; // make sure this points to the correct file where $pdo is defined

// Check if the user is logged in and a POST request is made
if (isset($_SESSION['user_id']) && $_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get the user ID from the session
    $userId = $_SESSION['user_id'];
    
    // Get the profile picture filename from the POST request
    $profilePicFilename = $_POST['profilePic'];

    // Check if the filename is not empty
    if (!empty($profilePicFilename)) {
        // Prepare the SQL statement
        $sql = "UPDATE Users SET profilepic = :profilepic WHERE UserID = :userid;";
        $stmt = $pdo->prepare($sql);
        
        // Bind the parameters
        $stmt->bindParam(':profilepic', $profilePicFilename);
        $stmt->bindParam(':userid', $userId);

        // Execute the statement
        if ($stmt->execute()) {
            // Redirect to settings with success message
            header("Location: ../settings.php?update=success");
            exit();
        } else {
            // Redirect to settings with error message
            header("Location: ../settings.php?error=updatefailed");
            exit();
        }
    } else {
        // Redirect back with an error message if the profile picture is not selected
        header("Location: ../settings.php?error=noprofilepicselected");
        exit();
    }
} else {
    // Redirect to the login page if the user is not logged in
    header("Location: ../login.php");
    exit();
}
?>
