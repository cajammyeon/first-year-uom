<?php

function is_email_password_match($pdo, $email, $password) {
    $sql = "SELECT * FROM Users WHERE Email = :email";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(":email", $email);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        return password_verify($password, $user['Pwd']);
    }

    return false;
}

function get_user_id_by_email($pdo, $email) {
    $sql = "SELECT UserID FROM Users WHERE Email = :email";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(":email", $email);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        return $user['UserID'];
    }

    return null;
}
?>