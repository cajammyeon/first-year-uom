<?php
session_start(); // Start the session at the beginning
require_once 'dbh.inc.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    if (empty($email) || empty($password)) {
        header("Location: ../login.php?error=emptyfields");
        exit();
    } else {
        $sql = "SELECT * FROM Users WHERE Email = :email;";
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam(':email', $email);
        $stmt->execute();

        if ($stmt->rowCount() > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if (password_verify($password, $row['Pwd'])) {
                $_SESSION['user_id'] = $row['UserID'];
                $_SESSION['user_email'] = $row['Email'];
                $_SESSION['username'] = $row['Usernames']; 
                $_SESSION['bio'] = $row['Bio'];
                $_SESSION['profile_pic'] = $row['ProfilePicture'];
                $_SESSION['match_index'] = 0;

                // Call Python script and pass user ID as an argument
                exec("/usr/bin/python3 algorithm.py " . escapeshellarg($row['UserID']));
                
                header("Location: ../landing_page.php");
                exit();
            } else {
                header("Location: ../login.php?error=wrongpassword");
                exit();
            }
        } else {
            header("Location: ../login.php?error=nouser");
            exit();
        }
    }
} else {
    header("Location: ../login.php");
    exit();
}
?>
