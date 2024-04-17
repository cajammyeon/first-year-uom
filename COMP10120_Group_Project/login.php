<!-- 
    id = "email" contains email from user input
    id = "password" contains password from user input

    functionality :
    - the button will submit "email" and "password" to the back end for verification
    - if verified, redirect to landing_page.php
    - if not, ask again for user input, output an error message as alert
-->
<?php
require_once 'includes/config_session.inc.php';
session_start();
if (isset($_GET['error'])) {
    echo "Error: " . $_GET['error'];
    if ($_GET['error'] == "emptyfields") {
        $errorMessage = '<div style="text-align: center; margin-top: 100px;">Make sure you have provided both an email and password</div>';

    } 
    
    else {
        $errorMessage = '<div style="text-align: center; margin-top: 100px;">The information entered was incorrect</div>';
        // Add more error cases if needed
    }
}
if (isset($errorMessage)) {
    echo '<h2 class="error">' . $errorMessage . '</h2>';
}
?>

<!DOCTYPE html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Pixelify+Sans&display=swap');
        
        .wrapperMainBody {
            display: grid;
            grid-template-columns: [first] 400px [col1] calc(100vw - 800px) [col2] 400px [last];
            grid-template-rows: [start] 70px [row1] calc(100vh -  140px) [row2] 70px [end];
        }

        /*TOP BAR STYLING*/
        .main-bar {
            grid-column-start: first;
            grid-column-end: last;
            grid-row-start: start;
            grid-row-end: row1;
            top: 0;

            display: flex;
            align-items: center;
            justify-content: space-between;
            background-color: #173753;
            position: fixed;

            width: 100vw;
            height: 70px;
        }

        .main-bar a {
            margin-left: 10px;
            color: #fff;
            font-family: 'Pixelify Sans', sans-serif;
            font-size: 30px;
            text-decoration: none;
        }

        .main-bar a:hover {
            color: #f9b3d1;
        }

        .main-bar img {
            margin-right: 10px;
        }

        /*LOGIN BAR STYLING*/
        .login-bar {

            align-self: center;
            justify-self: center;

            grid-column-start: col1;
            grid-column-end: col2;
            grid-row-start: row1;
            grid-row-end: row2;

            display: flex;
            justify-content: center;
            width: 600px;
            height: 400px;
            position: absolute;
            border-radius: 35px;
            background: rgba(23, 55, 83, 0.75);
            
        }

        .login-bar form {
            position: absolute;
            top: 30%;
            left: 10%;
            right: 10%;
        }

        .login-bar button {
            width: 100%;
            background-color: #F9B3D1;
            border-color: #F9B3D1;
            color: #000;
        }

        .login-bar h1 {
            padding-top: 5%;
            overflow: scroll;
            font-size: 30px;
            color: #fff;
            font-family: 'Pixelify Sans', sans-serif;
            text-decoration: none;
        }

        .login-bar h2 {
            position: absolute;
            font-size: 17px;
            color: #fff;
            font-family: Arial, Helvetica, sans-serif;
            text-decoration: none;
            bottom: 2.5%;
        }

        .login-bar a:link {
            color: #fff;
        }

        .login-bar a {
            text-decoration: none;
        }

        .login-bar a:hover {
            color: #F9B3D1;
        }

        .bottom-bar {
            grid-column-start: first;
            grid-column-end: last;
            grid-row-start: row2;
            grid-row-end: end;
            bottom: 0;

            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #173753;
            position: fixed;

            width: 100vw;
            height: 70px;
        }

        .bottom-bar a {
            text-decoration: none;
            font-family: 'Pixelify Sans', sans-serif;
        }

        .bottom-bar a:hover {
            color: #F9B3D1;
        }

    </style>

    <script>
    /*to prevent Firefox FOUC, this must be here*/
    let FF_FOUC_FIX;
    </script>
</head>

<body>
<div class="wrapperMainBody">

    <div class="main-bar">
        <a href="login_page.php">PIXEL PARTNER</a>
    </div>

    <div class="login-bar">
        <h1>Find your second joystick ! </h1>
        <form action="includes/login.inc.php" method= "post">
            <input type="email" class="form-control" id="email" aria-describedby="emailHelp" name="email" placeholder="Email..."> <br>
            <input type="password" class="form-control" id="password" aria-describedby="emailHelp" name="password" placeholder="Password..."> <br>
            <button type="submit" class="btn btn-primary">Log In</button>
        </form>

        <h2> Not a user ? <a href="register.php">register</a></h2>
    </div>
    
    <div class="bottom-bar">
        <a href="contact.html">CONTACT US</a>
    </div>

</div>
</body>
</html>