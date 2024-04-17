"""
This is a stub for COMP16321 Coursework 01.
Do not edit or delete any lines given in this file that are marked with a "(s)".
(you can move them to different lines as long as you do not change the overall structure)

Place your code below the comments marked "#Your code here" and before the pass for that method.

Each method is documented to explain what work is to be placed within it.

NOTE: You can create as many more methods as you need. However, you need to add 
self as a parameter of the new method and  to call it with the prefix self.name 

"""

class Basics:#(s)
    # ---Section 1 --- #

    #(Question:a)
    def read_file(self):#(s)
        """
            Read in the text file and save the paragraph to a single string

            :return: A text file paragraph as a string
        """
        #Your code here
        text = open('text.txt', 'rt')
        paragraph_text = ''
        
        for i in text :
            paragraph_text += i

        return paragraph_text
    
    # ---Section 2 --- #

    #(Question:a)
    def length_of_file(self):#(s)
        """
            Reports the length of the paragraph including numbers and whitespace
        
            :input_text: The text file paragraph as a string
            :return: An integer length of the file
        """
        input_text = self.read_file()#(s)
        #Your code here
        
        length_input_text = len(input_text)
        return length_input_text
        pass

    #(Question:b)
    def if_apple(self):#(s)
        """
            Reports a boolean True/False if the paragraph contains the entire word "apple"

            :input_text: The text file paragraph as a string
            :return: A boolean True/false
        """
        input_text = self.read_file()#(s)
        #Your code here

        if 'apple' in input_text.lower() :
            return True
        else :
            return False
        pass

    #(Question:c)
    def if_upper_case_exists(self):#(s)
        """
            Reports a boolean True/False if the paragraph contains any number of upper case letters

            :input_text: The text file paragraph as a string
            :return: A boolean True/false
        """
        input_text = self.read_file()#(s)
        #Your code here

        upper_case = 0
        
        for i in input_text :
            if i.isupper() == True :
                upper_case += 1
        
        if upper_case > 0 :
            return True
        else :
            return False
        pass

    #(Question:d)
    def if_numbers_exist(self):#(s)
        """
            Reports a boolean True/False if the paragraph contains any number of integers

            :input_text: The text file paragraph as a string
            :return: A boolean True/false
        """
        input_text = self.read_file()#(s)
        #Your code here

        number_digit = 0
        
        for i in input_text :
            if i.isdigit() == True :
                number_digit += 1
        
        if number_digit > 0 :
            return True
        else :
            return False
        pass

    #(Question:e)
    def if_spaces_exist(self):#(s)
        """
            Reports a boolean True/False if the paragraph contains any number of blank spaces

            :input_text: The text file paragraph as a string
            :return: A boolean True/false
        """
        input_text = self.read_file()#(s)
        #Your code here

        number_spaces = 0
        
        for i in input_text :
            if i.isspace() == True :
                number_spaces += 1
        
        if number_spaces > 0 :
            return True
        else :
            return False
        pass

    #(Question:f)
    def if_first_letter_t(self):#(s)
        """
            Reports a boolean True/False if the first letter of the paragraph is a t

            :input_text: The text file paragraph as a string
            :return: A boolean True/false
        """
        input_text = self.read_file()#(s)
        #Your code here

        if input_text[0] == 't' or input_text[0] == 'T' :
            return True
        else :
            return False
        pass
    
    #(Question:g)
    def fourth_letter_seventh_word(self):#(s)
        """
            Reports the fourth letter in the seventh word of the paragraph as a string

            :input_text: The text file paragraph as a string
            :return: A string letter
        """
        input_text = self.read_file()#(s)
        #Your code here
        
        input_text_list = input_text.split()
        seventh_word = input_text_list[6]
        
        return seventh_word[3]
        pass

    # ---Section 3 --- #

    #(Question:a)
    def convert_to_lower_case(self):#(s)
        """
            Converts the paragraph to entirely lowercase with no other changes

            :input_text: The text file paragraph as a string
            :return: A string paragraph
        """
        input_text = self.read_file()#(s)
        #Your code here

        lower_input_text = input_text.lower()
        return lower_input_text
        pass

    #(Question:b)
    def reverse_paragraph(self):#(s)
        """
            Reverses the paragraph such that it can be read backwards with no other changes

            :input_text: The text file paragraph as a string
            :return: A string paragraph
        """
        input_text = self.read_file()#(s)
        #Your code here

        location = 0
        reverse_paragraph = ''
        
        for i in input_text :
            location -= 1
            reverse_paragraph += input_text[location]
            
        return reverse_paragraph
        pass

    #(Question:c)
    def duplicate_and_concatenate_paragraph(self):#(s)
        """
            Duplicate the paragraph and combine them such that they can be read twice in order with
            no other changes

            :input_text: The text file paragraph as a string
            :return: A string paragraph
        """
        input_text = self.read_file()#(s)
        #Your code here

        duplicated_input_text = input_text + input_text
        return duplicated_input_text
        pass

    #(Question:d)
    def remove_whitespace_from_paragraph(self):#(s)
        """
            Remove any whitespace from the paragraph except spaces between words and numbers with no
            other changes

            :input_text: The text file paragraph as a string
            :return: A string paragraph
        """
        input_text = self.read_file()#(s)
        #Your code here

        removed_whitespace = input_text.strip()
        return removed_whitespace
        pass

    if __name__ == '__main__':#(s)
        #You can place any ad-hoc testing here
        #i.e test = remove_whitespace_from_paragraph()
        #i.e print(test)
        pass