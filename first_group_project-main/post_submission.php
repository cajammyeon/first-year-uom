<?php
session_start();
require_once 'includes/dbh.inc.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $userId = $_SESSION['user_id']; 
    $caption = $_POST['caption']; 
    $timePosted = date('Y-m-d H:i:s'); 

    // Handle the attachment upload if there's one
    $attachment = NULL;
    if (isset($_FILES['attachment']) && $_FILES['attachment']['error'] == 0) {
        // Here you should handle the file upload and get the file path
        // For example, save it to a directory and set $attachment to the file path
    }

    $sql = "INSERT INTO feeds (user_id, description, attachment, time_posted) VALUES (:userId, :description, :attachment, :timePosted)";
    $stmt = $pdo->prepare($sql);

    $stmt->bindParam(':userId', $userId);
    $stmt->bindParam(':description', $caption);
    $stmt->bindParam(':attachment', $attachment);
    $stmt->bindParam(':timePosted', $timePosted);

    if ($stmt->execute()) {
        header("Location: feed.php");
        exit;
    } else {
        die("Error: Could not post.");
    }
} else {
    header("Location: feed.php");
    exit;
}
?>
