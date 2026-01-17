<!DOCTYPE html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Pixelify+Sans&display=swap');

        body {
            display: grid;
            grid-template-columns: [first] 100vw [last]; 
            grid-template-rows: [start] 70px [row1] calc(100vh - 70px) [end];
        }

        .info{
            grid-row-start: row1;
            grid-row-end: end;
            grid-column-start: first;
            grid-column-end: last;

            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            width: 100%;
            height: calc(100vh - 70px);

            color: black;
        }

        .info h1 {
            font-size: 40px;
            font-family: 'Pixelify Sans', sans-serif;
            color: #f9b3d1;
        }
        
        .info p {
            font-size: 20px;
            font-family: Arial, Helvetica, sans-serif;
            color: #173753;
        }

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
    </style>

</head>

<body>
    <div class="main-bar">
        <a href="landing_page.php">PIXEL PARTNER</a>
    </div>

    <div class="info">
        <h1>Disclaimer</h1>
        <p>This project was done for module COMP10120 as a requirement for first year sudent in BSc Computer Science</p>
        <h1>Team members</h1>
        <p>Kevin</p>
        <p>Troy</p>
        <p>Jim</p>
        <p>Ellis</p>
        <p>Abdullah</p>
        <p>Arash</p>
        <p>Nishant</p>
    </div>

</body>