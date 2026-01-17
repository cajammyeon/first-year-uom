'''
The game is developed as part of requirement for BSc Computer Science by The University of Manchester
Developed by Ahmad Syahrul Azim bin Ahmad Azmi, 2023

Assets used :
1. checkmark.png as the target
2. leaderboard.txt as score keeping file
3. preferences.json as setting file
Make sure all assets are in the same file as game before running

There is a bug associated with the canvas, but won't affect gameplay (as far as I'm aware of)

F1 => boss key
F2 => cheat key (change location of target randomly)

Screen resolution used : 1920x1080
Function of each function is described in each function

Enjoy :)
'''

from tkinter import Button, Label, Canvas, PhotoImage, Entry, Tk, StringVar
from random import randint
from time import sleep
import json

def main_page() :
    '''
    Menu page with options
    1. Start game
    2. Leaderboard
    3. Quit game
    '''
    global point
    point = 0
    
    clear_screen_delay(0.1)

    #game title
    title_label = Label(root, text='Me avoiding\nresponsibility', bg=background_c, fg=foreground_c)
    title_label.configure(font=('Courier', 120))
    title_label.place(relx=0.5, anchor='center', y=350)
    
    #game label
    title2_label = Label(root, text='A minimalistic game  :P', bg=background_c, fg=foreground_c)
    title2_label.configure(font=('Courier', 24))
    title2_label.place(relx=0.5, anchor='center', y=550)
    
    #introduce buttons (Start Game, Leaderboard, Exit Game)
    Button(root, command=tutorial_page, text='Start Game', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=700)
    Button(root, command=leaderboard_show, text='Leaderboard', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=800)
    Button(root, command=setting_ingame, text='Settings', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=900)
    Button(root, command=exit_game, text='Exit game', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=1000)

def leaderboard_show():
    '''
    1. Open the file
    2. Break the content and store into a dictionary where name is the key and score is the value
    3. Show the score, where the value is ranked accordingly
    '''
    clear_screen_delay(0.2)
    
    #open the leaderboard file and convert the file to a dictionary, changing the values to integer
    f = open('leaderboard.txt', 'rt')
    leaderboard = {}
    for i in f :
        key, value = i.strip().split(',')
        leaderboard[key] = int(value)
    f.close()
    
    #sort the leaderboard based on score, in decreasing value
    sorted_leaderboard = dict(sorted(leaderboard.items(), key=lambda item: item[1], reverse=True)) 
    
    #starting location of the leaderboard, for ease of adjustment
    start_x = 850
    start_y = 200
    
    #label for score
    rank_label = Label(root, text='Score', fg=foreground_c, bg=background_c)
    rank_label.configure(font=('Courier', 24))
    rank_label.place(x=start_x+225, y=start_y-50)
    
    def set_point(range_lead) :
        #print the number
        for h in range(range_lead) :
            rank = Label(root, text=str(h+1)+'.', fg=foreground_c, bg=background_c)
            rank.configure(font=('Courier', 24))
            rank.place(x = start_x - 100, y = start_y+(h*50))
        #print the names    
        for i in range(range_lead) :
            name = Label(root, text=list(sorted_leaderboard.keys())[i], fg=foreground_c, bg=background_c)
            name.configure(font=('Courier', 24))
            name.place(x = start_x, y = start_y+(i*50))
        #print the scores    
        for j in range(range_lead) :
            key = list(sorted_leaderboard.keys())[j]
            score = Label(root, text=sorted_leaderboard[key], fg=foreground_c, bg=background_c)
            score.configure(font=('Courier', 24))
            score.place(x = start_x + 250, y = start_y+(j*50))
    
    if len(sorted_leaderboard) < 11 :
        range_lead = len(sorted_leaderboard)
        set_point(range_lead)
    else :
        range_lead = 10
        set_point(range_lead)
        
    #back to main menu button   
    Button(root, command=main_page, text='Main menu', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=900)
               
def exit_game():
    '''
    Just exit game :P
    '''
    root.destroy()
 
def end_game() :
    '''
    Function after the player is touched by a "responsibility"
    The player should be able to enter their name and it will be saved in a text document
    
    name_var = name variable, declared for the text box
    '''
    clear_screen_delay(0.5)
    
    global name_var
    
    #end label
    end_label = Label(root, text=':(', bg=background_c, fg=foreground_c)
    end_label.configure(font=('Courier', 250))
    end_label.place(relx=0.5, anchor='center', y=350)
    
    #score label
    score_label = Label(root, text='Score : ' + str(point), bg=background_c, fg=foreground_c)
    score_label.configure(font=('Courier', 30))
    score_label.place(relx=0.5, anchor='center', y=600)
    
    #ask for name
    name_var = StringVar()
    Entry(root, textvariable=name_var, bg=foreground_c).place(relx=0.5, anchor='center', y = 700)
    
    #exit page button
    Button(root, command=save_score, text='Bye bye', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=800)
    
def save_score() :
    '''
    Save the score into a text file to be displayed in leaderboard menu
    Comma (,) is used as delimiter in text file
    
    name = name from the text box in the end game page, taken from name_var
    leaderboard = dictionary with the names and scores
    f = placeholder for file
    '''
    name = name_var.get()                                           #get the name from the text box
    
    if point > 0 :                                                  #save the score in leaderboard file if score > 0
        if name !=  ' ' :                                            #check if the name is NOT empty string, then save
            f = open('leaderboard.txt', 'rt')
            leaderboard = {}
            for i in f :                                            #save the name and score in a dictionary
                key, value = i.strip().split(',')                   #use comma (,) as delimiter, strip the string
                leaderboard[key] = value                            
            f.close()
            leaderboard[name] = point                               #append the dictionary with name as key and score as delimiter
            f = open('leaderboard.txt', 'wt')                       #overwrite the file instead of appending as the whole file has been loaded into the dictionary
            for i in leaderboard :
                f.write(i + ',' + str(leaderboard[i]) + '\n')
            f.close()
    main_page()
        
def clear_screen_delay(delay) :
    '''
    Clear everything on screen, before loading a new page but with delay
    '''
    for widgets in root.winfo_children():
        widgets.destroy()
        sleep(delay)
     
def start_game_normal():
    '''
    Start the game
    1. Clear screen
    2. Summon the player
    3. Summen the objective (cross logo) randomly
    4. Summon the "responsibility" after every key click randomly
    '''
    clear_screen_delay(0)
    global point, canvas
    
    #read the screen width and height for ease of adjustment
    root_width = root.winfo_width()
    root_height = root.winfo_height()
    
    #create canvas to summon objects
    canvas = Canvas(root, width=root_width, height=root_height, bg=background_c)
    canvas.pack()

    #button that will proceed to end game page
    Button(root, command=end_game,text='Exit', bg=background_c, fg=foreground_c, width=40, height=4).place(x=20, y=20)
    
    #display score
    score_record = Label(root, text='Score : ' + str(point), bg=background_c, fg=foreground_c)
    score_record.configure(font=('Courier', 30))
    score_record.place(x = 20, y= 120)
    
    #create player box with edge length = 50
    player = canvas.create_rectangle(150, 400, 200, 450, fill=main_player, outline=foreground_c)
    
    def find_collision():
        '''
        1. Summon random "responsibility"
        2. Detect collision
        3. Continue game, update game and point, or end game
        '''
        global point, points_player
        
        random_x = randint(0, root_width)      #pick random x of one corner
        random_y = randint(0, root_height)     #pick random y of one corner
        canvas.create_rectangle(random_x, random_y, random_x+(25+2*point), random_y+(25+2*point), fill=foreground_c, outline=foreground_c) # top-left bottom-right pair (x1,y1, x2, y2)
        
        #read the coordinate of the player and use find_overlapping to find if there is any overlapping on canvas
        points_player = canvas.coords(player)
        coll = canvas.find_overlapping(points_player[0], points_player[1], points_player[2], points_player[3])
        #change the coll to list
        coll = list(coll)
        
        #1 represents the player, #2 represents the target while others represent the "responsibility"
        if coll == [1] :
            None
        elif coll == [1,2] :    #create a new page when the player hits the target and update point
            point += 1
            start_game_normal()
        else :                  #push to end game page when the player hits the "responsibility"
            end_game()
   
    def summon_objective() :
        '''
        Create the target at random location
        '''
        random_x = randint(0, root_width)
        random_y = randint(0, root_height)
        canvas.create_image(random_x, random_y, image=image)
    
    root.bind(move_up, lambda _: canvas.move(player, 0, -15) if points_player[1] >= 0 else None)                #move up
    root.bind(move_down, lambda _: canvas.move(player, 0, 15) if points_player[3] <= root_height else None)     #move down
    root.bind(move_left, lambda _: canvas.move(player, -15, 0) if points_player[0] >= 0 else None)              #move left
    root.bind(move_right, lambda _: canvas.move(player, 15, 0) if points_player[2] <= root_width else None)     #move right
    root.bind(boss_keyp, lambda _: boss_key())               #run boss_key()
    root.bind(cheat_keyp, lambda _: cheat_key())             #run cheat_key()
    root.bind_all('<Key>', lambda _: find_collision())       #every key press will run the find_collision()
    
    summon_objective()
    find_collision()

def cheat_key() :
    '''
    1. Clear the screen
    2. Summon new target and new "responsibility"
    '''
    start_game_normal()
    
def tutorial_page() :
    '''
    Tutoral page showing simple information about the game and how to play before starting the real game
    '''
    clear_screen_delay(0.2)
    
    root_width = root.winfo_width()
    root_height = root.winfo_height()
    
    canvas = Canvas(root, width=root_width, height=root_height, bg=background_c)
    canvas.pack()
    
    #start game button
    canvas.create_rectangle(50, 50, 150, 150, fill=main_player, outline=foreground_c)
    canvas.create_rectangle(50, 250, 150, 350, fill=foreground_c, outline=foreground_c)
    canvas.create_image(105, 500, image=image)
    Button(root, command=start_game_normal, text='Start Game', bg=background_c, fg=foreground_c, width=40, height=2).place(relx=0.5, anchor='center', y=900)
    
    #summon objects and explain the functions of the objects
    player_tutorial = Label(root, text='This the player, you control it by using the arrow keys on the keyboard', bg=background_c, fg=foreground_c)
    player_tutorial.configure(font=('Courier', 20))
    player_tutorial.place(x = 200, y= 80)

    responsibility_tutorial = Label(root, text='This the \"responsibility\", avoid it. More of it will spawn the more you move.', bg=background_c, fg=foreground_c)
    responsibility_tutorial.configure(font=('Courier', 20))
    responsibility_tutorial.place(x = 200, y= 290)
    
    target_tutorial = Label(root, text='This the target. Reach the target and get one point.', bg=background_c, fg=foreground_c)
    target_tutorial.configure(font=('Courier', 20))
    target_tutorial.place(x = 200, y=490)
    
    final1_tutorial = Label(root, text='There is no pause to this game as the \"responsibility\" will spawn when you press a key.', bg=background_c, fg=foreground_c, anchor='w')
    final1_tutorial.configure(font=('Courier', 20))
    final1_tutorial.place(x = 50, y=700)

    final2_tutorial = Label(root, text='Feel free to make a cup of tea anytime :).', bg=background_c, fg=foreground_c, anchor='w')
    final2_tutorial.configure(font=('Courier', 20))
    final2_tutorial.place(x = 50, y=730)
    
    final3_tutorial = Label(root, text='The responsibility will get bigger as you progress(the way life works).', bg=background_c, fg=foreground_c, anchor='w')
    final3_tutorial.configure(font=('Courier', 20))
    final3_tutorial.place(x = 50, y=760)
    
def boss_key() :
    '''
    Implementing boss key, where it will open new empty window
    '''
    window = Tk()
    window.title('Coursework 02')
    window.geometry('1920x1080')
    window.bind('<F1>', lambda _: window.destroy())
    
    ti_label = Label(window, text='Some important\nwork', fg='#8C0000')
    ti_label.configure(font=('Courier', 120))
    ti_label.place(relx=0.5, anchor='center', y=350)
    
    window.mainloop()

def setting_ingame() :
    '''
    Update keybinding for Up, Down, Left and Right based on the instructions
    '''
    global index_ref
    clear_screen_delay(0.1)
    settings_list = ["up", "down", "left", "right"]
    
    def accept_keypress(event) :
        '''
        Take the keypress and store in key, update settings and display message
        '''
        key = event.keysym
        update_settings(key, settings_list)
        display_message()
    
    def update_settings(key, settings_list) :
        '''
        Read json file and update settings based on the ref index
        '''
        with open('preferences.json', 'r') as set :
            load_set = json.load(set)
            
        if key == "Up" or key == "Down" or key == "Left" or key == "Right" :   #for arrow keys, we need <KeyPress-Up/Down/Left/Right> for some reason 
            load_set[settings_list[index_ref]] = "<KeyPress-"+key+">"
        else :                                                                 #for other keys, it is fine to just keep the letter
            load_set[settings_list[index_ref]] = key
            
        with open('preferences.json', 'w') as settings :
            json.dump(load_set, settings, indent=4)
    
    def display_message() :
        '''
        Self-explanatory
        '''
        global index_ref
        index_ref += 1
        if index_ref < len(settings_list) :
            text_label.config(text="Click any key for "+settings_list[index_ref])
        else :
            text_label.config(text="Reload game to update settings")
            Button(root, command=exit_game, text='Exit game', bg=background_c, fg=foreground_c, width=40, height=4).place(relx=0.5, anchor='center', y=1000)
    
    index_ref = 0   #used as counter for loop
    text_label = Label(root, text='Click any key for '+settings_list[index_ref], fg=foreground_c, bg=background_c)
    text_label.configure(font=('Courier', 24))
    text_label.place(x = 250, y = 250)
    
    root.bind("<Key>", accept_keypress)

#open json file and read settings
with open('preferences.json', 'r') as settings :
    load_setting = json.load(settings)
#read settings for control
background_c = load_setting["background"]
foreground_c = load_setting["foreground"]
main_player = load_setting["player"]  
move_up = load_setting["up"]
move_down = load_setting["down"]
move_left = load_setting["left"]
move_right = load_setting["right"]
boss_keyp = load_setting["boss"]
cheat_keyp = load_setting["cheat"]

#set up the variables for game mechanism
point = 0

#initiate the page
root = Tk()
root.title('Coursework 02')
root.configure(bg=background_c)
root.geometry('1920x1080')

#setup for image used as objective
original_image = PhotoImage(file='CheckMark.png')
image = original_image.subsample(original_image.width() // 100, original_image.height() // 100)

main_page()
root.mainloop()