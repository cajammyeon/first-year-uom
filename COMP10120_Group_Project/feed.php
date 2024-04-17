<!-- 
    user will upload a picture and insert a caption, click on button and it will be posted on their wall
    fetch the name of other users and their posts, and display it
-->
<?php
session_start();
require_once 'includes/dbh.inc.php'; // Path to your database connection file

// Check if the user is logged in
if (!isset($_SESSION['user_id'])) {
    // If the user is not logged in, redirect to the login page.
    header("Location: login.php");
    exit;
}

// Fetch posts from the database
$sql = "SELECT feeds.post_id, feeds.user_id, feeds.description, feeds.attachment, feeds.time_posted, Users.Usernames FROM feeds JOIN Users ON feeds.user_id = Users.UserID ORDER BY feeds.time_posted DESC";
$stmt = $pdo->prepare($sql);
$stmt->execute();
$posts = $stmt->fetchAll(PDO::FETCH_ASSOC);

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
            
            grid-column: col2 / last;
            grid-row: row1 / end;

            justify-self: right;

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

        /* ADD MEDIA BUTTON */
        .addMedia {
            margin-top: 40px;
            justify-self: center;

            border-radius: 10px;
            border: 3px solid #C0C0C0;

            display: flex;
            justify-content: center;
            flex-direction: column;

            width: 1000px;
            margin-top: 80px;
            margin-bottom: 50px;
            margin-left: 50px;
            padding: 10px;
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
            
            width: 1000px;
            margin-top: 25px;
            margin-bottom: 25px;
            margin-left: 50px;
        }

        .metada {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
        }

        .username {
            color: black;
            font-family: 'Pixelify Sans', sans-serif;
            text-decoration: none;
            font-size: 40px;

            color: #f9b3d1;
        }

        .input_type {
            display: flex;
            justify-content: space-between;

            margin: 25px;
        }

        .formstyle {
            width: 1000px;
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

    <div class="midContent">

    <form class="formstyle" id="postForm" action="post_submission.php" method="post" enctype="multipart/form-data">
    <div class="addMedia">
        <textarea class="form-control" name="caption" id="captionArea" placeholder="Say something..."></textarea>
        <div class="input_type">
            <!-- Include an input for file upload if you want users to attach images -->
            <input type="file" name="attachment" id="attachment" />
            <br><br>
            <button type="submit" class="btn btn-primary">Post</button>
        </div>
    </div>
    

    <?php foreach ($posts as $post): ?>
        <div class='content'>
            <div class="metada">
                <!-- Display the username -->
                <h1 class="username"><?php echo htmlspecialchars($post['Usernames']); ?></h1>
                <!-- Display the time posted -->
                <p class="datePosted"><?php echo htmlspecialchars(date('F j, Y, g:i a', strtotime($post['time_posted']))); ?></p>
            </div>
            <!-- Display the post description -->
            <p class="postDescription"><?php echo nl2br(htmlspecialchars($post['description'])); ?></p>
            <!-- Check if there is an attachment and display it -->
            <?php if (!empty($post['attachment'])): ?>
                <img class="postPic" src="<?php echo htmlspecialchars($post['attachment']); ?>" alt="Post Image">
            <?php endif; ?>
        </div>
    <?php endforeach; ?>
    </div>
    </form>
    </div>
</body>
</html>