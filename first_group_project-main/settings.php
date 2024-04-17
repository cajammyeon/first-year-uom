<!-- 
    user will upload a picture and insert a caption, click on button and it will be posted on their wall
    fetch the name of other users and their posts, and display it
-->
<?php
session_start();
if (!isset($_SESSION['user_id'])) {
    // Redirect to login page if user is not logged in
    echo "<script>window.location.href = 'login.php';</script>";
    exit;
}
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
            padding-top: 70px;
        }

        .leftContent {
            grid-column: first / col1;
            grid-row: row1 / end;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            width: 400px;
            text-align: center;
            padding-top: 70px;
        }

        .leftContent a {
            left: 0%;
            color: black;
            padding: 14px 16px;
            font-family: 'Pixelify Sans', sans-serif;
            text-decoration: none;
            font-size: 20px;
        }

        .leftContent a:hover {
            color: #72A1E5;
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
            flex-direction: row;
            
            width: 500px;
            margin-top: 25px;
            margin-bottom: 25px;
            margin-left: 50px;
        }

        .content h1 {
            height: 50px;
            font-size: 30px;
            padding-left: 10px;
            font-family: 'Pixelify Sans', sans-serif;
        }

        .content p{
            height: 30px;
            font-size: 20px;
            padding-left: 30px;
            padding-top: 6px;
            font-family: 'Pixelify Sans', sans-serif;
        }

        .profilePic {
            height: 50px;
            width: 50px;
            border-radius: 25px;
        }

        .postPic {
            align-self: flex-end;

            width: 500px;
            height: 500px;
            border-radius: 25px;
        }

        .myButton {
            background-color: #ffffff; /* White background */
            border: 2px solid #3498db; /* Blue border */
            color: #3498db; /* Blue text color */
            padding: 10px 20px; /* Padding */
            font-size: 20px;
            font-family: 'Pixelify Sans', sans-serif;
            cursor: pointer;
            border-radius: 5px; /* Rounded corners */
            
            margin-top: 10px;
            transition: background-color 0.3s, color 0.3s; /* Smooth transition */
            outline: none;    
        }
        
        .myButton:hover {
            background-color: #3498db; /* Blue background on hover */
            color: #ffffff; /* White text color on hover */
        }

        .hidden{
            display: none;
        }

        .selectbar {
            width: 100px; /* Adjust width as needed */
            height: 6px; /* Adjust height as needed */
            background-color: #72A1E5; /* Adjust color as needed */
            border-radius: 0%;
        }

        .bioUpdate {
            display: flex;
            flex-direction: column;

            margin-bottom: 40px;
        }

        .bioUpdate h1 {
            font-family: 'Pixelify Sans', sans-serif;
            font-size: 40px;
            color: #f9b3d1;
        }

        .profPic {
            display: flex;
            flex-direction: column;

            margin-bottom: 40px;
        }

        .profPic h1 {
            font-family: 'Pixelify Sans', sans-serif;
            font-size: 40px;
            color: #f9b3d1;
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

    function toggleElement(id) {
            var element = document.getElementById(id);
            if (element) {
                if (element.style.display === "none") {
                    element.style.display = "block";
                } else {
                    element.style.display = "none";
                }
            }
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


    <div class="midContent">

        <!-- Display update success/error messages -->
        <?php
        if (isset($_GET['update']) && $_GET['update'] == 'success') {
            echo '<p style="color: green;">Profile updated successfully!</p>';
        } elseif (isset($_GET['error'])) {
            echo '<p style="color: red;">An error occurred. Please try again.</p>';
        }
        ?>

    <form action="includes/update_bio.php" method="POST">
        <div class="bioUpdate">
            <h1>Bio</h1><br>
            <textarea id="bio" name="bio" placeholder="Enter your bio here..." rows="4" cols="50"></textarea><br>
            <button type="submit" class="myButton">Update Bio</button>
        </div>
    </form>

    <form action="includes/update_profile_pic.php" method="post">
        <div class="profPic">
            <h1>Select Profile Picture</h1>
            <select name="profilePic" id="profilePic">
                <option value="turtle.png">Turtle</option>
                <option value="killer.png">Killer</option>
                <option value="pirate.png">Pirate</option>
                <option value="demon.png">Demon</option>
                <option value="officer.png">Officer</option>
                <option value="dead_pirate.png">Dead Pirate</option>
                <option value="robot.png">Robot</option>
                <option value="devil.png">Devil</option>
                <option value="oldman.png">Oldman</option>
                <option value="princess.png">Princess</option>
                <option value="dead_robot.png">Dead Robot</option>
                <option value="vampire.png">Vampire</option>
            </select>
            <button type="submit" class="myButton">Update Picture</button>
        </div>
    </form>

    </div>