<?php

declare(strict_types=1);

function get_username(object $pdo, string $username)
{
    $query = "SELECT Usernames FROM Users WHERE Usernames = :username;";
    $stmt = $pdo->prepare($query);
    $stmt->bindParam(":username", $username);
    $stmt->execute();

    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    return $result;
}

function get_email(object $pdo, string $email)
{
    $query = "SELECT Email FROM Users WHERE Email = :email;";
    $stmt = $pdo->prepare($query);
    $stmt->bindParam(":email", $email);
    $stmt->execute();

    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    return $result;
}

function set_user(object $pdo, string $password, string $username, string $email, $age, $lang, $timezone, $game1, $game2, $game3, $hobby1, $hobby2, $hobby3) {
    $query = "INSERT INTO Users (Usernames, Email, Pwd, age, lang, timezone, game1, game2, game3, hobby1, hobby2, hobby3) VALUES (:username, :email, :password, :age, :lang, :timezone, :game1, :game2, :game3, :hobby1, :hobby2, :hobby3);";
    $stmt = $pdo->prepare($query);
    $stmt->bindParam(":username", $username);
    $stmt->bindParam(":email", $email);
    $stmt->bindParam(":password", $password);
    $stmt->bindParam(":age", $age);
    $stmt->bindParam(":lang", $lang);
    $stmt->bindParam(":timezone", $timezone);
    $stmt->bindParam(":game1", $game1);
    $stmt->bindParam(":game2", $game2);
    $stmt->bindParam(":game3", $game3);
    $stmt->bindParam(":hobby1", $hobby1);
    $stmt->bindParam(":hobby2", $hobby2);
    $stmt->bindParam(":hobby3", $hobby3);
    $stmt->execute();
}