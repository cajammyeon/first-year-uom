<!-- 
    after the page is loaded, image from database (the suggestion) will pop up in the div = "swipe-element"
    assets :
        1. image
        2. name
        3. description
    pulled from other user's data
    
    id = "arrowRightHover" when clicked will see whether they're a match or not
    if match, allow texting
    if not, the div = "swipe-element" will show the next suggestion

    id = "arrowLeftHover" when clicked will update that they're not a match
    the div = "swipe-element" will show the next suggestion

    note : don't forget to implement the log out function
-->
<?php
session_start();
if (!isset($_SESSION['user_email'])) {
    echo "<script>window.location.href = 'login.php';</script>";
    exit;
}
$userEmail = htmlspecialchars($_SESSION['user_email']);

//Grab other users data from the database
require_once "./includes/dbh.inc.php";
$query = "SELECT * FROM Users WHERE UserID = :session_id;";
$stmt = $pdo->prepare($query);
$stmt->bindParam(':session_id', $_SESSION['user_id']);
$stmt->execute();
$user_results = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Call our algorithm and store the matched userid in session variable "$_SESSION['user2_id']".
// require_once "./includes/algorithm(fake).php";
// $_SESSION['user2_id'] = getRandomUser($user_results[0], $pdo);

$query = "SELECT pair FROM Users WHERE UserID = :session_user_id;";
$stmt = $pdo->prepare($query);
$stmt->bindParam(":session_user_id", $_SESSION["user_id"]);
$stmt->execute();
$matched_user_ids = $stmt->fetch(PDO::FETCH_ASSOC);

$user2_ids = explode(",", $matched_user_ids["pair"]);

$_SESSION['user2_id'] = intval($user2_ids[$_SESSION['match_index']]);
var_dump($_SESSION['user2_id']);

// The matched user's id.
$user2_id = $_SESSION['user2_id']; 
// Fetch the matched user's information
$query = "SELECT * FROM Users WHERE UserID = :user2_id";
$stmt = $pdo->prepare($query);
// Bind the parameter
$stmt->bindParam(":user2_id", $user2_id);
// Execute the query
$stmt->execute();
$user2_results = $stmt->fetchAll(PDO::FETCH_ASSOC);
$user2_results = $user2_results[0];

// // Prepare the SQL query to select the bio
// $query = "SELECT bio FROM Users WHERE UserID = :user2_id";
// $stmt = $pdo->prepare($query);
// // Bind the parameter
// $stmt->bindParam(":user2_id", $user2_id);
// // Execute the query
// $stmt->execute();

// // Fetch the result
// $user_bio = $stmt->fetch(PDO::FETCH_ASSOC);

// // Check if a bio was found


$pdo = null;
$stmt = null;

?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Landing Page</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Pixelify+Sans&display=swap');

        body {
            display: grid;
            grid-template-columns: [first] 15vw [col1] 400px [col2] calc(100vw - 30vw - 800px) [col3] 400px [col4] 15vw [last];
            grid-template-rows: [start] 70px [row1] 200px [row2] calc(100vh - 470px) [row3] 200px [end];
        }

        /*TOP BAR STYLIING*/
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

        /*SIDE MENU*/
        .side-bar {
            justify-self: right;
            grid-column-start: col4;
            grid-column-end: last;
            grid-row-start: row1;
            grid-row-end: end;

            display: flex;
            flex-direction: column;

            height: 100%;
            width: 200px;

            background-color: #D9D9D9;
            text-align: center;
        }

        .side-bar a {
            left: 0%;
            color: black;
            padding: 14px 16px;
            font-family: 'Pixelify Sans', sans-serif;
            text-decoration: none;
            font-size: 20px;
        }

        .side-bar a:hover {
            color: #72A1E5;
        }

        .log_out_button {
            margin-top: auto;
        }

        /*RIGHT ARROW*/
        .mouseLeft {
            justify-self: right;
            align-self: center;

            grid-column-start: col1;
            grid-column-end: col2;
            grid-row-start: row2;
            grid-row-end: row3;
        }
            

        /*SWIPE BAR*/
        .swipe {

            display: flex;
            flex-direction: column;
            align-items: center;

            background: rgba(23, 55, 83, 0.75);
            height: 500px;
            width: 400px;
            border-radius: 25px;
        }

        /*LEFT ARROW*/
        .mouseRight {
            justify-self: left;
            align-self: center;

            grid-column-start: col3;
            grid-column-end: col4;
            grid-row-start: row2;
            grid-row-end: row3;
        }

        .midContent {
            justify-self: center;
            align-self: center;

            grid-column-start: col2;
            grid-column-end: col3;
            grid-row-start: row2;
            grid-row-end: row3;

            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .matched_username {
            font-family: 'Pixelify Sans', sans-serif;
            text-decoration: none;
            font-size: 40px;
            color: white;
        }

        .welcome_message {
            justify-self: center;

            font-family: 'Pixelify Sans', sans-serif;
            font-size: 40px;
            color: black;
        }

        .profilePic {
            justify-self: center;
            align-self: center;
            border-radius: 30px;
            border: 7px solid #fff;
            margin: 25px;
            height: 310px; 
            width: 310px;
        }

        .bioDes {
            font-size: 20px;
            color: white;
        }

    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>

<?php
    // Check if the session variable for username is set and not empty
    if(isset($_SESSION['username']) && !empty($_SESSION['username'])) {
        // Use htmlspecialchars for security reasons, to prevent XSS attacks
        $username = htmlspecialchars($_SESSION['username']);
        echo "<p>Welcome " . $username . "!</p>";
    } else {
        echo "<p>Welcome Guest!</p>"; // Or redirect to login, or another appropriate action
    }
    ?>
    <script>
    
    /*Handling menu*/
    function showMenu() {
        document.getElementById("menu_bar_leave").style.display = "none";
        document.getElementById("menu_bar_hover").style.display = "none";
        document.getElementById("close_bar_leave").style.display = "flex";  
        document.getElementById("side-bar").style.display = "flex";      
    }

    function hideMenu() {
        document.getElementById("menu_bar_leave").style.display = "flex";
        document.getElementById("close_bar_hover").style.display = "none";
        document.getElementById("close_bar_leave").style.display = "none";
        document.getElementById("side-bar").style.display = "none";
    }

    /*menu color transition*/
    function showMenuHover() {
        document.getElementById("menu_bar_leave").style.display = "none";
        document.getElementById("menu_bar_hover").style.display = "flex";
        document.getElementById("close_bar_leave").style.display = "none";
        document.getElementById("close_bar_hover").style.display = "none"; 
    }

    function showMenuLeave() {
        document.getElementById("menu_bar_leave").style.display = "flex";
        document.getElementById("menu_bar_hover").style.display = "none";
        document.getElementById("close_bar_leave").style.display = "none";
        document.getElementById("close_bar_hover").style.display = "none"; 
    }

    function hideMenuHover() {
        document.getElementById("menu_bar_leave").style.display = "none";
        document.getElementById("menu_bar_hover").style.display = "none";
        document.getElementById("close_bar_leave").style.display = "none";
        document.getElementById("close_bar_hover").style.display = "flex"; 
    }

    function hideMenuLeave() {
        document.getElementById("menu_bar_leave").style.display = "none";
        document.getElementById("menu_bar_hover").style.display = "none";
        document.getElementById("close_bar_leave").style.display = "flex";
        document.getElementById("close_bar_hover").style.display = "none"; 
    }

    /*Arrow transition*/
    function arrowRightEnter() {
        document.getElementById("arrowRightLeave").style.display = 'none';
        document.getElementById("arrowRightHover").style.display = 'flex';
    }

    function arrowRightLeave() {
        document.getElementById("arrowRightLeave").style.display = 'flex';
        document.getElementById("arrowRightHover").style.display = 'none';
    }

    function arrowLeftEnter() {
        document.getElementById("arrowLeftLeave").style.display = 'none';
        document.getElementById("arrowLeftHover").style.display = 'flex';
    }

    function arrowLeftLeave() {
        document.getElementById("arrowLeftLeave").style.display = 'flex';
        document.getElementById("arrowLeftHover").style.display = 'none';
    }
    
    </script>

    <div class="main-bar">
        <a href="landing_page.php">PIXEL PARTNER</a>
        <img id="menu_bar_leave" onmouseenter='showMenuHover();' onmouseleave='showMenuLeave();' onclick="showMenu();" src="menu_bar_leave.png" width="50px" height="45px" style="display : flex;">
        <img id="menu_bar_hover" onmouseenter='showMenuHover();' onmouseleave='showMenuLeave();' onclick="showMenu();" src="menu_bar_hover.png" width="50px" height="45px" style="display : none;">
        <img id="close_bar_leave" onmouseenter='hideMenuHover();' onmouseleave='hideMenuLeave();' onclick="hideMenu();" src="close_bar_leave.png" width="50px" height="45px" style="display : none;">
        <img id="close_bar_hover" onmouseenter='hideMenuHover();' onmouseleave='hideMenuLeave();' onclick="hideMenu();" src="close_bar_hover.png" width="50px" height="45px" style="display : none;">
    </div>

    <div class="midContent">
        <h1 class="welcome_message">Welcome <?= htmlspecialchars($username) ?>!</h1>

        <div id="swipe-element" class="swipe" onclick="window.location.href='profile.php?userID=<?= $_SESSION['user2_id'] ?>';">
            
            <!-- Display the matched user's information -->
            <?php 
            $profilePicPath = "./ProfilePics/";
            $defaultProfilePic = "./ProfilePics/default_image.png";

            // diplay user's profile image
            $profilePic = !empty($user2_results["profilepic"]) ? $profilePicPath . htmlspecialchars($user2_results["profilepic"]) : $defaultProfilePic;
            echo "<img class='profilePic' id='profilePic' src='" . $profilePic . "' alt='Profile Picture'>";
            
            // this is the matched username displayed
            if (!empty($user2_results["Usernames"])) {
                echo "<h1 class='matched_username'>" . htmlspecialchars($user2_results['Usernames']) . "</h1>";
            } else {
                echo "<h1 class='matched_username'> User Not Found </h1>";
            }

            // user bio
            if (!empty($user2_results["bio"])) {
                echo "<p class='bioDes'>" . htmlspecialchars($user2_results['bio']) . "</p>";
            } else {
                echo "<p class='bioDes'>No bio found</p>";
            }
            ?>

        </div>
    </div>

        
        
        
        ?>
        <!-- <h1><?= $_SESSION['user2_id'] ?>" class="matched_username"><?= $_SESSION['user2_id'] ?></h1> -->
        
        
        <!-- Dynamically load user data here -->
        
    </div>

    <div class='mouseLeft' onmouseenter='arrowLeftEnter();' onmouseleave='arrowLeftLeave();' onclick="swipeLeft()">
        <img id='arrowLeftLeave' src='arrowLeftLeave.png'  style='display:flex;' width='100px' height='100px'>
        <img id='arrowLeftHover' src='arrowLeftHover.png'  style='display:none;' width='100px' height='100px'>
    </div>

    <div class='mouseRight' onmouseenter='arrowRightEnter();' onmouseleave='arrowRightLeave();' onclick="window.location.href='profile.php?userID=<?= $_SESSION['user2_id'] ?>';">
        <img id='arrowRightLeave' src='arrowRightLeave.png'  style='display:flex;' width='100px' height='100px'>
        <img id='arrowRightHover' src='arrowRightHover.png'  style='display:none;' width='100px' height='100px'>
    </div>

    

    <div class="side-bar" id="side-bar" style='display:none;'>
        <a href="feed.php">FEED</a><br>
        <a href="messages.php">MESSAGES</a><br>
        <a href="profile.php">PROFILE</a><br>
        <a href="settings.php">SETTINGS</a><br>
        <a href="contact.php">CONTACT</a><br>
        <a href="includes/logout.php" class="log_out_button" style="color: red;">LOG OUT</a>
    </div>

    <script>
        function swipeLeft() {
            var xhr = new XMLHttpRequest();
            xhr.open('POST', './includes/unMatched.php', true);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
            xhr.onload = function() {
                // Handle the response here
                console.log(this.responseText);
            };
            xhr.send('action=invokeFunction');
            window.location.reload();
        }

    //placeholder for algorithm

    // Example function to handle arrowRightHover click - modify as needed
    document.getElementById('arrowRightHover').addEventListener('click', function() {
        // Implement match checking logic here
        console.log('Check if they are a match');
    });

    // Example function to handle arrowLeftHover click - modify as needed
    document.getElementById('arrowLeftHover').addEventListener('click', function() {
        console.log('Load next suggestion');
    });

    </script>
</body>
</html>