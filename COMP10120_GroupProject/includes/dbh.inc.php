<?php
$database_host = "dbhost.cs.man.ac.uk";
$database_user = "d94181xm";
$database_pass = "V5Lym20810";
$database_name = "2023_comp1tut_z5";

try {
    $pdo = new PDO("mysql:host=$database_host;dbname=$database_name", $database_user, $database_pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Connection failed: " . $e->getMessage());
}