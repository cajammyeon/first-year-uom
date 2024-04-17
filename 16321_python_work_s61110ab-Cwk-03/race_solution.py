"""
This is a stub for the comp16321 midterm.
Do not edit or delete any lines given in this file that are marked with a "(s)".
(you can move them to different lines as long as you do not change the overall structure)

Place your code below the comments marked "#Your code here".

Each method is documented to explain what work is to be placed within it.

NOTE: You can create as many more methods as you need. However, you need to add 
self as a parameter of the new method and to call it with the prefix self.name 

EXAMPLE:

def class_table_result(self, boat_type, race_results):#(s)
    strings_value = "0601-0501-0702-0803-0904-0405-0306-0207-1008-0609-0110"
    single_boat = self.remove_highest_value(strings_value)
    return(single_boat)

def remove_highest_value(self, strings_value):
    strings_value.pop(10)
    return strings_value

"""

class Races:#(s)

    def read_results(self):#(s)
    
        """
        Read in the text file and save the races_results into a python list

        :return: A list of strings denoting each race_result
        """
        f = open('input.txt', 'rt')
        results_string = []
        
        for i in f :
            results_string.append(i.strip())
            
        return results_string
        pass#(s)

    def race_result(self, boat_type, race_number, results_string):#(s)

        """
        Query results_string which is read form the input.txt and get the result
        for the given params
        
        :param: boat_type: An integer denoting which type of boat 
        :param: race_number: An integer denoting which race
        :return: A string with the race result for the given boat_type and race_number
        """
        #Type of boat, type of race
        boattype_sorted = []
        for i in results_string :
            if int(i[0:2]) == int(boat_type) :
                boattype_sorted.append(i[5:])
        
        try :
            return boattype_sorted[int(race_number)-1]
        except IndexError :
            return ''
        pass#(s)

    def class_table_result(self, boat_type, results_string):#(s)

        """
        Output the results for a given boat_type

        :param: boat_type: An integer denoting which type of boat 
        :return: A string in the specified format as shown in the pdf
        """

        # Your code here
        # Type of Boat, Type of Race
        boattype_sorted = []
        score_dict = {'01' : 0,
                      '02' : 0,
                      '03' : 0,
                      '04' : 0,
                      '05' : 0,
                      '06' : 0,
                      '07' : 0,
                      '08' : 0,
                      '09' : 0,
                      '10' : 0}
        score_return = ''
        
        #load a certain boat type result in the list
        for i in results_string :
            if int(i[0:2]) == int(boat_type) :
                boattype_sorted.append(i[3:])

        #prepare the dictionary for the specified boat type 
        try :      
            for i in boattype_sorted :
                splitted = i.split('-')
                for j in splitted[1:] :
                    if 'xx' in j :
                        score_dict[f"{j[0:2]:02}"] += int(splitted[0])*11
                        splitted.remove(j)
                for j in splitted[1:] :
                        score_dict[f"{j[0:2]:02}"] += int(splitted[0])*splitted.index(j)
        except KeyError :
            None
            
        #find points where the teams have the same point
        draw_reference = self.find_draw(score_dict)
        #use the draw marks reference to sort the rank
        final_rank = self.sorting_hat(score_dict, draw_reference, boattype_sorted)
    
        for i in final_rank :
            score_return = score_return + str(i) + '-' + f"{final_rank.index(i)+1:02}" + '-' + f"{score_dict[i]:02}" + ', '  
        return score_return[0:-2]

        pass#(s)

    def class_table_discard_result(self, boat_type, results_string):#(s)

        """
        Output the class table discard string

        :param: boat_type: An integer denoting which type of boat 
        :return: A string in the specified format as shown in the pdf
        """
        # Your code here
        
        #read the score based on the boattype
        boattype_sorted = []
        for i in results_string :
            if int(i[0:2]) == int(boat_type) :
                boattype_sorted.append(i)
        
        score_from_rank = self.class_table_result(boat_type, results_string)
        
        score_dict = {}
        
        split_string = score_from_rank.split(', ')
        for i in split_string :
            score_dict[f"{i[0:2]:02}"] = int(i[6:])
        
        def remove_score(list_given) :
            removed_list = []
            
            for i in range(1,11) :
                
                score_list = []
                for j in list_given :
                    splitted = j[5:].split('-')
                    for k in splitted :
                        if 'xx' in k :
                            splitted.remove(k)
                    for k in splitted :
                        if k[0:2] == f"{i:02}" :
                            score_list.append(splitted.index(k)+1)
                            
                highest_score = max(score_list)
                if len(score_list) != len(list_given) :
                    removed_list.append(11)
                else :
                    removed_list.append(highest_score)
                    
            return removed_list
  
        single_point = []
        double_point = []
        score_return_removed = ''
        
        for i in boattype_sorted :
            if i[3] == '1' :
                single_point.append(i)
            elif i[3] == '2' :
                double_point.append(i)
        
        #remove single point score
        if len(single_point) <= 2 :
            single_point_removed = []
        else :
            single_point_removed = remove_score(single_point)
            
            iref=0
            while iref < len(single_point_removed) :
                score_dict[f'{iref+1:02}'] -= single_point_removed[iref]
                iref += 1
        #remove double point score
        if len(double_point) <= 2 :
            double_point_removed = []
        else :
            double_point_removed = remove_score(double_point)
            
            iref=0
            while iref < len(double_point_removed) :
                score_dict[f'{iref+1:02}'] -= double_point_removed[iref]*2
                iref += 1

        #find points where the teams have the same point
        draw_reference = self.find_draw(score_dict)
        #use the draw marks reference to sort the rank
        final_rank = self.sorting_hat(score_dict, draw_reference, boattype_sorted)
        
        for i in final_rank :
            score_return_removed = score_return_removed + str(i) + '-' + f"{final_rank.index(i)+1:02}" + '-' + f"{score_dict[i]:02}" + ', '  
        return score_return_removed[0:-2]
            
        pass#(s)

    def medal_table_result(self, results_string):#(s)

        """
        Output the class table discard string

        :param: boat_type: An integer denoting which type of boat 
        :return: A string in the specified format as shown in the pdf 
        """

        # Your code here
        final_position = ''
        
        points_total = {'01' : [0, 0, 0, 0],
                      '02' : [0, 0, 0, 0],
                      '03' : [0, 0, 0, 0],
                      '04' : [0, 0, 0, 0],
                      '05' : [0, 0, 0, 0],
                      '06' : [0, 0, 0, 0],
                      '07' : [0, 0, 0, 0],
                      '08' : [0, 0, 0, 0],
                      '09' : [0, 0, 0, 0],
                      '10' : [0, 0, 0, 0]}
        
        #update gold medal
        for i in range(1, 11) :
            results_after_discard = self.class_table_discard_result(i, results_string)
            points_total[results_after_discard[0:2]][1] += 1
        #update silver medal
        for i in range(1, 11) :
            results_after_discard = self.class_table_discard_result(i, results_string)
            points_total[results_after_discard[10:12]][2] += 1
        #update bronze medal
        for i in range(1, 11) :
            results_after_discard = self.class_table_discard_result(i, results_string)
            points_total[results_after_discard[20:22]][3] += 1
        #update points
        for i in range(1, 11):
            points_total[f'{i:02}'][0] = points_total[f'{i:02}'][1]*3 + points_total[f'{i:02}'][2]*2 + points_total[f'{i:02}'][3]
        
        sorted_points = dict(sorted(points_total.items(), key=lambda item: item[1], reverse = True))
        
        for key, value in sorted_points.items() :
            final_position = final_position + key + f'-{value[1]:02}' + f'-{value[2]:02}' + f'-{value[3]:02}' + f'-{value[0]:02}, '

        return final_position[:-2]
        pass#(s)

    def find_draw(self, data) : #
        seen_values = set()
        draw_value = []
        
        for value in data.values() :
            if value in seen_values :
                draw_value.append(value)
            else :
                seen_values.add(value)
                
        draw_value = list(dict.fromkeys(draw_value))
        return draw_value
    
    def sorting_hat(self, score_dict, draw_reference, boattype_sorted) :
    
        sorted_score = dict(sorted(score_dict.items(), key=lambda item: item[1]))
        keys = list(sorted_score.keys())
        
        tie_breaker = (boattype_sorted[len(boattype_sorted)-1]).split('-')
        for i in tie_breaker :
            tie_breaker[tie_breaker.index(i)] = i[0:2]
        tie_breaker = tie_breaker[1:]
        
        if len(draw_reference) > 0 :
            
            for i in draw_reference :
                
                draw_team = []
                ref_list = []
                
                for key, value in sorted_score.items() :
                    if value == i :
                        draw_team.append(key)
                    
                for j in keys :
                    if j in draw_team :
                        ref_index = keys.index(j)
                        break
                
                for j in draw_team :
                    keys.remove(j)
                
                ref_position = 0
                while ref_position < len(tie_breaker) :
                    if tie_breaker[ref_position] in draw_team :
                        ref_list.append(tie_breaker[ref_position])
                    ref_position += 1
                    
                ref_insert = 0
                while ref_insert < len(ref_list) :
                    keys.insert(ref_index, ref_list[ref_insert])
                    ref_index += 1
                    ref_insert += 1
                    
        return keys


if __name__ == '__main__':#(s)
    # You can place any ad-hoc testing here
    # e.g. my_instance = Races()
    # e.g. section_1 = my_instance.read_results()
    # e.g. print(section_1)
    my_instance = Races()
    results_string = my_instance.read_results()
    
    print('\nOriginal results--------------------------------------------------------------\n')
        
    for i in range(1, 11) :
        race_results2 = my_instance.class_table_result(i, results_string)
        print(f"Boat type : {i:02} : Results : {race_results2}")
        
    print("\nDiscarded results-------------------------------------------------------------\n")

    for i in range(1, 11) :
        race_results = my_instance.class_table_discard_result(i, results_string)
        print(f"Boat type : {i:02} : Results : {race_results}")

    print('\nComparison--------------------------------------------------------------------\n')
    
    for i in range(1, 11) :
        race_results = my_instance.class_table_discard_result(i, results_string)
        race_results2 = my_instance.class_table_result(i, results_string)
        
        if race_results == race_results2 :
            print(f"Race {i:02} : No change in score")
        else :
            print(f"Race {i:02} : There is change in score")
            
    print('\nMedal position----------------------------------------------------------------\n')  
    race_medal = my_instance.medal_table_result(results_string)
    print(race_medal)
    pass#(s)