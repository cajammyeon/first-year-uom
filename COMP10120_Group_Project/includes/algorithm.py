#!/usr/bin/env python3
import mysql.connector
import sys

def calculate_score(user_row, active_user_row):
    score = 0
    if user_row[5] == active_user_row[5]:  
        score += 10
    active_timezone = active_user_row[6].upper()
    user_timezone = user_row[6].upper()
    if active_timezone == "UTC+1":
        if user_timezone == "UTC+1":
            score += 10
        elif user_timezone in ["UTC+0", "UTC+2"]:
            score += 5
        elif user_timezone == "UTC+8":
            score += 2
        elif user_timezone in ["UTC-12", "UTC-11"]:
            score += 1
    elif active_timezone == "UTC+0":
        if user_timezone == "UTC+0":
            score += 10
        elif user_timezone == "UTC+1":
            score += 7
        elif user_timezone == "UTC+2":
            score += 5
        elif user_timezone == "UTC+8":
            score += 2
        elif user_timezone in ["UTC-12", "UTC-11"]:
            score += 1
    elif active_timezone == "UTC+2":
        if user_timezone == "UTC+2":
            score += 10
        elif user_timezone == "UTC+1":
            score += 7
        elif user_timezone == "UTC+0":
            score += 5
        elif user_timezone == "UTC+8":
            score += 2
        elif user_timezone in ["UTC-12", "UTC-11"]:
            score += 1
    elif active_timezone == "UTC+8":
        if user_timezone == "UTC+8":
            score += 10
        elif user_timezone == "UTC-11":
            score += 7
        elif user_timezone in ["UTC+2", "UTC-12"]:
            score += 5
        elif user_timezone == "UTC+1":
            score += 2
        elif user_timezone == "UTC+9":
            score += 1
    elif active_timezone == "UTC-12":
        if user_timezone == "UTC-12":
            score += 10
        elif user_timezone == "UTC":
            score += 7
        elif user_timezone == "UTC+2":
            score += 5
        elif user_timezone == "UTC+1":
            score += 2
        elif user_timezone == "UTC+0":
            score += 1
    
    for i in range(7, 13):  
        if user_row[i] == active_user_row[7]: 
            score += 10
        if user_row[i] == active_user_row[8]: 
            score += 10
        if user_row[i] == active_user_row[9]: 
            score += 10
        if user_row[i] == active_user_row[10]: 
            score += 10
        if user_row[i] == active_user_row[11]: 
            score += 10
        if user_row[i] == active_user_row[12]: 
            score += 10
    return score

user_id = sys.argv[1]
print("User ID from PHP:", user_id)

connection = mysql.connector.connect(
    host="dbhost.cs.man.ac.uk",
    user="d94181xm",
    password="V5Lym20810",
    database="2023_comp1tut_z5"
)

connection.start_transaction()

try:
    cursor = connection.cursor()

    cursor.execute("SELECT * FROM Users WHERE UserID = %s", (user_id,))
    active_user_row = cursor.fetchone()

    if active_user_row:
        cursor.execute("SELECT * FROM Users")
        all_users_rows = cursor.fetchall()

        user_scores = {}
        for user_row in all_users_rows:
            score = calculate_score(user_row, active_user_row)
            user_scores[user_row[0]] = score

        sorted_users = sorted(user_scores.items(), key=lambda x: x[1], reverse=True)

        sorted_user_ids = [user_id for user_id, score in sorted_users]
        sorted_user_ids.remove(active_user_row[0])
        pair_string = ','.join(map(str, sorted_user_ids))
        cursor.execute("UPDATE Users SET pair = %s WHERE UserID = %s", (pair_string, active_user_row[0]))
        for user_id, score in sorted_users:
            update_query = "UPDATE Users SET score = %s WHERE UserID = %s"
            cursor.execute(update_query, (score, user_id))
        connection.commit()
        print("Scores and pair updated successfully.")

    else:
        print("No user found with UserID {}.".format(user_id))

except Exception as e:
    print("An error occurred:", e)
    connection.rollback()

finally:
    cursor.close()
    connection.close()
