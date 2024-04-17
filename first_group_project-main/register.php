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
require_once 'includes/signup_view.inc.php';
?>

<!DOCTYPE html>
<head>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Pixelify+Sans&display=swap');

        body {
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
            height: 800px;
            position: absolute;
            border-radius: 35px;
            background: rgba(23, 55, 83, 0.75);
        }

        .login-bar form {
            position: absolute;
            top: 15%;
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

        .login-bar a {
            text-decoration: none;
            font-family: Arial, Helvetica, sans-serif;
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

        .gameChoice {
            display: flex;
            flex-direction: row;
            justify-content: space-around;
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
            color: #fff;
            font-family: 'Pixelify Sans', sans-serif;
        }

        .bottom-bar a:hover {
            color: #f9b3d1;
        }


    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script type="text/javascript">
    // Function to update the dropdowns, removing already selected options
    function updateDropdowns() {
        // List of all game and hobby dropdowns
        const gameDropdowns = ['game1', 'game2', 'game3'];
        const hobbyDropdowns = ['hobby1', 'hobby2', 'hobby3'];

        gameDropdowns.forEach((dropdown) => {
            // For each dropdown, clear it and then repopulate
            const selectedValues = gameDropdowns.map(d => document.getElementById(d).value);
            const options = ['pubg', 'fortnite', 'cs', 'minecraft', 'roblox', 'lol', 'cyberpunk', 'sims', 'cod', 'valorant'];
            const currentDropdown = document.getElementById(dropdown);
            const currentValue = currentDropdown.value;
            currentDropdown.innerHTML = '';

            // Add an empty option
            currentDropdown.add(new Option('Select a game', ''));

            // Add only those options that have not been selected in other dropdowns
            options.forEach((option) => {
                if (!selectedValues.includes(option) || option === currentValue) {
                    currentDropdown.add(new Option(option, option));
                }
            });

            // Restore the previously selected value if it's still valid
            if (options.includes(currentValue)) {
                currentDropdown.value = currentValue;
            }
        });

        // Do the same for hobbies
        hobbyDropdowns.forEach((dropdown) => {
            const selectedValues = hobbyDropdowns.map(d => document.getElementById(d).value);
            const options = ['socializing', 'sports', 'photography', 'reading', 'tech', 'cooking', 'travelling', 'pets', 'videoGaming', 'music'];
            const currentDropdown = document.getElementById(dropdown);
            const currentValue = currentDropdown.value;
            currentDropdown.innerHTML = '';

            currentDropdown.add(new Option('Select a hobby', ''));

            options.forEach((option) => {
                if (!selectedValues.includes(option) || option === currentValue) {
                    currentDropdown.add(new Option(option, option));
                }
            });

            if (options.includes(currentValue)) {
                currentDropdown.value = currentValue;
            }
        });
    }
</script>
</head>

<body style="background-color : #fff;">
    <div class="main-bar">
        <a href="login_page.php">PIXEL PARTNER</a>
    </div>

    <div class="login-bar">
        <h1>Find your second joystick ! </h1>
        <form action="includes/signup.inc.php" method="post" onsubmit="return updateDropdowns()">
            <input type="text" class="form-control" id="userName" aria-describedby="emailHelp" name="userName" placeholder="Username..."> <br>
            <input type="email" class="form-control" id="email" aria-describedby="emailHelp" name="email" placeholder="Email..."> <br>
            <input type="password" class="form-control" id="password" aria-describedby="emailHelp" name="password" placeholder="Password..."> <br>
            <input type="password" class="form-control" id="passwordConf" aria-describedby="emailHelp" name="passwordConf" placeholder="Confirm password..."> <br>
            
            <input type="number" class="form-control" name="age" placeholder="Age..." required><br>

            <select class="form-control" name="lang" aria-placeholder="Language..." required>
                <option value="english">English</option>
                <option value="spanish">Spanish</option>
                <option value="french">French</option>
                <option value="german">German</option>
                <option value="chinese">Chinese</option>
                <!-- Add other languages as needed -->
            </select><br>
    
            <select class="form-control" name="timezone" aria-placeholder="Timezone..." required>
                <option value="UTC-12">UTC-12</option>
                <option value="UTC-11">UTC-11</option>
                <!-- Add options for all timezones -->
                <option value="UTC+0">UTC+0</option>
                <option value="UTC+1">UTC+1</option>
                <option value="UTC+2">UTC+2</option>
                <option value="UTC+8">UTC+8</option>
                <!-- Continue listing other UTC offsets -->
            </select><br>

            <div class="gameChoice">
                <select id="game1" name="game1" onchange="updateDropdowns()" aria-placeholder="Game 1..." required></select><br> 
                <select id="game2" name="game2" onchange="updateDropdowns()" aria-placeholder="Game 2..." required></select><br>
                <select id="game3" name="game3" onchange="updateDropdowns()" aria-placeholder="Game 3..." required></select><br>
            </div><br>

            <div class="gameChoice">
                <select id="hobby1" name="hobby1" onchange="updateDropdowns()" aria-placeholder="Hobby 1..." required></select><br>
                <select id="hobby2" name="hobby2" onchange="updateDropdowns()" aria-placeholder="Hobby 2..." required></select><br>
                <select id="hobby3" name="hobby3" onchange="updateDropdowns()" aria-placeholder="Hobby 3..." required></select><br>
            </div><br>
            <button type="submit" class="btn btn-primary">Register</button>
        </form>

        <?php
        check_signup_errors();
        ?>
        <h2> Already registered ? <a href="login.php">login</a></h2>
    </div>
    
    <div class="bottom-bar">
        <a href="contact.html">CONTACT US</a>
    </div>

    <script>
        // Initialize the dropdowns when the page loads
        document.addEventListener('DOMContentLoaded', function() {
            updateDropdowns();
        }, false);
    </script>

</body>
