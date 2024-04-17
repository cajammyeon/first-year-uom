<?php

function login_user($pdo, $email, $password) {
    try {
        require_once 'login_model.inc.php';
        require_once 'config_session.inc.php';

        $errors = [];

        if (empty($email) || empty($password)) {
            $errors["empty_input"] = "Fill in all fields!";
        }

        if (!is_email_password_match($pdo, $email, $password)) {
            $errors["invalid_login"] = "Invalid login credentials!";
        }

        if ($errors) {
            $_SESSION["errors_login"] = $errors;
            header("Location: ../login.php");
            die();
        }

        $user_id = get_user_id_by_email($pdo, $email);

        $_SESSION["user_id"] = $user_id;
        header("Location: ../landing_page.php");
        die();

    } catch(PDOException $e) {
        die("Query failed: " . $e->getMessage());
    }
}
?>