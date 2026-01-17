<?php
session_start();
$_SESSION["match_index"] ++;
require_once "dbh.inc.php";
$query = 'INSERT INTO unmatched (user1_id, user2_id) VALUES (:user1, :user2);';
$stmt = $pdo->prepare($query);
$stmt->bindParam(':user1', $_SESSION['user_id']);
$stmt->bindParam(':user2', $_SESSION['user2_id']);
$stmt->execute();