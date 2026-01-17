<?php

declare(strict_types=1);

function is_input_empty(string $username, string $password, string $email) {
    if (empty($username) || empty($password) || empty($email)) {
        return true;
    } else {
        return false;
    }
}

function is_email_invalid(string $email) {
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        return true;
    } else {
        return false;
    }
}

function is_username_taken(object $pdo, string $username) {
    if (get_username($pdo, $username)) {
        return true;
    } else {
        return false;
    }
}

function is_email_registered(object $pdo, string $email) {
    if (get_email($pdo, $email)) {
        return true;
    } else {
        return false;
    }
}

function create_users(object $pdo, string $password, string $username, string $email, $age, $lang, $timezone, $game1, $game2, $game3, $hobby1, $hobby2, $hobby3) {
    set_user($pdo, $password, $username, $email, $age, $lang, $timezone, $game1, $game2, $game3, $hobby1, $hobby2, $hobby3);
}