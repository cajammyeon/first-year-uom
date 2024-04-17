<?php

if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $username = $_POST["userName"];
        $email = $_POST["email"];
        $password = $_POST["password"];
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        $passwordConf = $_POST["passwordConf"];
        $age = $_POST["age"];
        $lang = $_POST["lang"];
        $timezone = $_POST["timezone"];
        $game1 = $_POST["game1"];
        $game2 = $_POST["game2"];
        $game3 = $_POST["game3"];
        $hobby1 = $_POST["hobby1"];
        $hobby2 = $_POST["hobby2"];
        $hobby3 = $_POST["hobby3"];

        try {
        require_once 'dbh.inc.php';
        require_once 'signup_model.inc.php';
        require_once 'signup_contr.inc.php';

        //ERROR HANDLEERS
        $errors = [];

        if (is_input_empty($username, $email, $password)) {
            $errors["empty_input"] = "Fill in all fields!";
        }

        if (is_email_invalid($email)) {
            $errors["invalid_email"] = "Invalid email used!";
        }

        if (is_username_taken($pdo, $username)) {
            $errors["username_taken"] = "Username already taken!";
        }

        if (is_email_registered($pdo, $email)) {
            $errors["email_used"] = "Email already registered!";
        }

        if (!($password === $passwordConf) && !password_verify($password, $passwordConf)) { 
            $errors["pwd_unmatched"] = "Password unmatched!";
        }

        require_once 'config_session.inc.php';

        if ($errors) {
            $_SESSION["errors_signup"] = $errors;
            header("Location: ../register.php");
            die();
        }

        create_users($pdo, $hashed_password, $username, $email, $age, $lang, $timezone, $game1, $game2, $game3, $hobby1, $hobby2, $hobby3);

        header("Location: ../landing_page.php");

        $pdo = null;
        $stmt = null;

        die();

        } catch(PDOException $e) {
        die("Query failed: " . $e->getMessage());
        }
    }else {
    header("Location: ../login.php");
    die();
}