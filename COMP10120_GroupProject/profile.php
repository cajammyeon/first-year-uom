<!-- 
    user will upload a picture and insert a caption, click on button and it will be posted on their wall
    fetch the name of other users and their posts, and display it
-->
<?php
session_start();
if (!isset($_SESSION['user_id'])) {
    // If the user is not logged in, redirect to the login page.
    header("Location: login.php");
    exit;
}

require_once 'includes/dbh.inc.php'; // Path to your database connection file

// Assuming 'user_id' is stored in the session upon login
// $profile_user_id = isset($_GET['userID']) ? (int)$_GET['userID'] : 0;
// $userID = $_SESSION['user_id']; // Access the session variable
// Prepare a SQL statement to fetch the user's name, bio, and profile picture
$profile_user_id = $_SESSION['user_id']; // Default to the logged-in user's ID

// If a userID is provided in the URL, override the default
if (isset($_GET['userID'])) {
    $profile_user_id = (int)$_GET['userID'];
}

$sql = "SELECT * FROM Users WHERE UserID = :user_id;"; 
$stmt = $pdo->prepare($sql);
$stmt->bindParam(':user_id', $profile_user_id, PDO::PARAM_INT);
$stmt->execute();
$userDetails = $stmt->fetch(PDO::FETCH_ASSOC);
if (!$userDetails) {
    // If no user is found or there's a fetch error, handle it here
    echo "No user found or there was an error with the fetch operation.";
    exit; // Stop script execution if no user details are found
}


// Fetch posts of the logged-in user
$sqlPosts = "SELECT post_id, description, attachment, time_posted FROM feeds WHERE user_id = :user_id ORDER BY time_posted DESC";
$stmtPosts = $pdo->prepare($sqlPosts);
$stmtPosts->bindParam(':user_id', $profile_user_id, PDO::PARAM_INT);
$stmtPosts->execute();
$userPosts = $stmtPosts->fetchAll(PDO::FETCH_ASSOC);

?>



<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="template.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

    <style>
        @import url('https://fonts.googleapis.com/css2?family=Pixelify+Sans&display=swap');

        body {
            display: grid;
            /* Change the template here as you wish but please DON'T CHANGE the mentioned dimension for top bar and side bar I'm begging, crying */
            grid-template-columns: [first] 200px [col1] calc(100vw - 400px) [col2] 200px [last]; 
            grid-template-rows: [start] 70px [row1] calc(100vh - 70px) [end];
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
            grid-column: col2 / last;
            grid-row: row1 / end;

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

        /* MIDDLE PART */
        .midContent {
            grid-column: col1 / col2;
            grid-row: row1 / end;
            align-items: center;

            display : flex;
            flex-direction: column;
            width: calc(100vw - 400px);
        }

        .leftContent {
            grid-column: first / col1;
            grid-row: row1 / end;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            width: 400px;
            font-family: 'Pixelify Sans', sans-serif;
            padding-left: 50px;
            text-align: center;
            padding-top: 70px;
        }

        /* ADD MEDIA BUTTON */
        .addMedia {
            margin-top: 40px;
            justify-self: center;

            display: flex;
            justify-content: center;
            flex-direction: row;

            width: 100%;
            padding: 50px;
        }

        .addMedia button {
            width: 20%;
            height: 40px;
        }

        .addMedia button:hover {
            background-color: #f9b3d1;
            border-color: #f9b3d1;
            color: #000;
        }

        /* MANAGE DIV FOR EVERY POST */
        .content {
            display: flex;
            flex-wrap: wrap;
            flex-direction: column;
            
            width: 500px;
            margin-top: 25px;
            margin-bottom: 25px;
            margin-left: 50px;
            padding: 10px;

            border: 3px solid #C0C0C0;
            border-radius: 20px;
        }

        .content h1 {
            height: 50px;
            font-size: 30px;
            padding-left: 10px;
            font-family: 'Pixelify Sans', sans-serif;
        }

        .content p{
            height: auto;
            font-size: 20px;
        }

        .profilePic {
            height: 50px;
            width: 50px;
            border-radius: 25px;
            border: 3px solid black;
            margin-bottom: 40px;
        }

        .postPic {
            align-self: flex-end;

            width: 500px;
            height: 500px;
            border-radius: 25px;
        }

    </style>
</head>

<body>

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

    </script>

    <div class="main-bar">
        <a href="landing_page.php">PIXEL PARTNER</a>
        <img id="menu_bar_leave" onmouseenter='showMenuHover();' onmouseleave='showMenuLeave();' onclick="showMenu();" src="menu_bar_leave.png" width="50px" height="45px" style="display : flex;">
        <img id="menu_bar_hover" onmouseenter='showMenuHover();' onmouseleave='showMenuLeave();' onclick="showMenu();" src="menu_bar_hover.png" width="50px" height="45px" style="display : none;">
        <img id="close_bar_leave" onmouseenter='hideMenuHover();' onmouseleave='hideMenuLeave();' onclick="hideMenu();" src="close_bar_leave.png" width="50px" height="45px" style="display : none;">
        <img id="close_bar_hover" onmouseenter='hideMenuHover();' onmouseleave='hideMenuLeave();' onclick="hideMenu();" src="close_bar_hover.png" width="50px" height="45px" style="display : none;">
    </div>

    <div class="side-bar" id="side-bar" style='display:none;'>
        <a href="feed.php">FEED</a><br>
        <a href="messages.php">MESSAGES</a><br>
        <a href="profile.php">PROFILE</a><br>
        <a href="settings.php">SETTINGS</a><br>
        <a href="contact.php">CONTACT</a><br>
        <a href="includes/logout.php" class="log_out_button" style="color: red;">LOG OUT</a>
    </div>

    <div class="leftContent">

    <?php if (!empty($userDetails['profilepic'])): ?>
        <img class="profilePic" id="profilePic" src="<?php echo 'ProfilePics/' . htmlspecialchars($userDetails['profilepic']); ?>" alt="Profile Picture" style="height: 200px; width: 200px; border-radius: 100px;">
    <?php else: ?>
        <!-- Display a default image if the profile picture is not set -->
        <img class="profilePic" id="profilePic" src="path_to_default_image.png" alt="Default Profile Picture" style="height: 200px; width: 200px; border-radius: 100px;">
    <?php endif; ?>    
    <h1 style="color: #f9b3d1; font-size:40px"><?php echo htmlspecialchars($userDetails['Usernames']); ?></h1><br>
    <?php
    // user bio
    if (!empty($userDetails["bio"])) {
        echo "<p style='font-size: 20px'>" . htmlspecialchars($userDetails['bio']) . "</p>";
    } else {
        echo "<p style='font-size: 20px'>No bio found</p>";
    }
    ?>
    <h1 style="font-size: 40px">Games:</h1><br>
    <p style="font-size: 20px"><?php echo htmlspecialchars($userDetails['game1']); ?></p><br>
    <p style="font-size: 20px"><?php echo htmlspecialchars($userDetails['game2']); ?></p><br>
    <p style="font-size: 20px"><?php echo htmlspecialchars($userDetails['game3']); ?></p><br>
    <h1 style="font-size: 40px">Passions:</h1><br>
    <p style="font-size: 20px"><?php echo htmlspecialchars($userDetails['hobby1']); ?></p><br>
    <p style="font-size: 20px"><?php echo htmlspecialchars($userDetails['hobby2']); ?></p><br>
    <p style="font-size: 20px"><?php echo htmlspecialchars($userDetails['hobby3']); ?></p><br>
</div>



    <div class="midContent">
    <!-- Loop through the logged-in user's posts -->
    <?php foreach ($userPosts as $post): ?>
        <div class='content'>
            <p class="postDescription"><?php echo nl2br(htmlspecialchars($post['description'])); ?></p>
            <?php if (!empty($post['attachment'])): ?>
                <img class="postPic" src="<?php echo htmlspecialchars($post['attachment']); ?>" alt="Post Image">
            <?php endif; ?>
            <p class="datePosted">Posted on: <?php echo htmlspecialchars(date('F j, Y, g:i a', strtotime($post['time_posted']))); ?></p>
        </div>
    <?php endforeach; ?>
</div>

    

</body>