import random, time, drawhangman, re

"""
Module that contains functions to enable the play of the hangman game.
"""
def welcome():
    """
    Prints welcoming texts and asks the user's name.

    Returns
    -------
    absolute value : `string`
    """
    print("HELLO, WELCOME TO HANGMAN", "\n")
    player_name = input("Please enter your in game name: ")
    while len(player_name) < 1:
        player_name = input("Name cannot be empty. Please enter your in game name: " )
    print()
    print("The game will randomly choose a word and you have to guess what the word is!")
    print("Your little stick figure will get closer and closer of getting hanged everytime you enter a letter that is not in the word", "\n")
    time.sleep(2)
    print(f"Now.. let's get started {player_name}!", "\n")
    return player_name

def get_word(fname):
    """
    Opens textfile called words.txt and chooses a random word from the file.

    Returns
    -------
    absolute value : `string`
    """
    f = open('words.txt', 'r')
    secret_word = random.choice(f.read().split()) #splits the words so they come as separate
    f.close()
    return secret_word

#adds '_' to dashed, which represents the hidden secret word
def display_word(secret_word):
    """
    Saves as many '_' to dashed as there is characters in the random chosen word.

    Returns
    -------
    absolute value : `list`
    """
    dashed = []
    for i in range(len(secret_word)):
        dashed.append("_")
    return "".join(dashed) #joins the dashes together without any spaces

def play(secret_word, dashed, player_name):
    """
    Starts the timer so when the user guesses the whole word correct we can see how many seconds it took.
    Asks the player for a letter as long as the input contais only one letter.
    Checks if the guessed letter is in the random word and that the letter has not been already guessed.
    If the guessed letter is in the random word, it displays the correct place for the letter in the dashed word.
    If the guessed letter is not in the word, it adds the letter to faulty_letters.

    """
    wrong_guesses = 0
    faulty_letters = ""
    all_guessed = []
    correct_guesses = 0
    start = time.time()
    while wrong_guesses < 6:
        guess = input("Please enter a letter: ").lower()
        while len(guess) > 1 or guess.isalpha() == False:
            guess = input("Please enter a letter (a, b, c etc.): ").lower()

        if guess in secret_word and guess not in all_guessed:
            word_list = list(dashed)
            check = [i for i, letter in enumerate(secret_word) if letter == guess]
            for index in check:
                word_list[index] = guess
            dashed = "".join(word_list)
            correct_guesses += 1
            all_guessed += guess
            print("Congrats! You got the letter right")
        elif guess in all_guessed:
            print("You have already guessed" , guess)
        else:
            wrong_guesses += 1
            faulty_letters += guess
            all_guessed += guess
            print(f'Incorrect, you can guess {6-(wrong_guesses)} more times wrong')
            
        #Calls print_hang to display the ASCII
        print_hang(dashed, wrong_guesses, faulty_letters, secret_word)
        #Calls check_win to see if the player has won
        if check_win(dashed) == True:
            print(f"Congrats, you won! You guessed the word, {secret_word}", '\n')
            end = time.time()
            score = int(end - start)
            print('It took you', round(score), 'seconds!', '\n')
            highscore(score, player_name, secret_word)
            break


def print_hang(dashed, wrong_guesses, faulty_letters, secret_word):
    """
    Prints the correct hangman graphic according to the amount of wrong guesses.
    """
    if wrong_guesses == 0:
        print(drawhangman.hang[0])
        print(dashed)
    elif wrong_guesses == 1:
        print(drawhangman.hang[1])
        print(dashed)
        print(f'incorrect letters: {faulty_letters}', '\n')
    elif wrong_guesses == 2:
        print(drawhangman.hang[2])
        print(dashed)
        print(f'incorrect letters: {faulty_letters}', '\n')
    elif wrong_guesses == 3:
        print(drawhangman.hang[3])
        print(dashed)
        print(f'incorrect letters: {faulty_letters}', '\n')
    elif wrong_guesses == 4:
        print(drawhangman.hang[4])
        print(dashed)
        print(f'incorrect letters: {faulty_letters}', '\n')
    elif wrong_guesses == 5:
        print(drawhangman.hang[5])
        print(dashed)
        print(f'incorrect letters: {faulty_letters}', '\n')
    elif wrong_guesses == 6:
        print(drawhangman.hang[6])
        print(dashed)
        print(f'incorrect guessed letters: {faulty_letters}', '\n')
        print(f"Unfortunately you lost, the word you were looking for was {secret_word}", '\n')

def check_win(dashed):
    """
    Checks if there is '_' marks left in dashed and returns True or False.

    Return
    ------
    absolute value : `boolean`
    """
    if '_' in dashed:
        return False
    else:
        return True

def read_highscores():
    """
    Reads the highscores text file.

    Return
    ------
    absolute value : `list`
    """
    f = open("highscore.txt", "r")
    words = {}
    for line in f:
        if (line.strip().__len__() > 0):
            word, name, score2 = line.split(',')
            name = name.strip()
            score2 = score2.strip()
            if word in words:
                words[word].append((int(score2), name))
            else:
                words[word] = [(int(score2), name)]
    f.close()
    return words

def highscore(score, player_name, secret_word):
    """
    Saves the players name and score to the text file.
    Only top three scores are saved per word.
    """
    words = read_highscores()
    if secret_word in words:
        word_list = words[secret_word]
        scores = [groups[0] for groups in words[secret_word]]
        if len(word_list) < 3:
            word_list.append((score, player_name))
        elif score <= int(max(scores)):
            highest = max(word_list)
            word_list.remove(highest)
            word_list.append((score, player_name))
    else:
        words[secret_word] = [(int(score), player_name)]

    f = open('highscore.txt', 'w')
    for word, items in words.items():
        for i in items:
            score = i[0]
            player_name = i[1]
            f.write(word + ", " + player_name + ", " + str(score) + "\n")
    f.close()
    sort_highscores(secret_word)

def sort_highscores(secret_word):
    """
    Sorts the highscores and then if player wins,
    it prints the scores of the last played word.
    """
    words = read_highscores()

    for i in words:
        words[i].sort(key = lambda i: i[0])

    print("The top three highscores for the word " + '"' + secret_word + '"' + " are:" + "\n")
    for word in words[secret_word]:
        print(word[1] + ": "+ str(word[0]) + " seconds")

def main():
    """
    Calls different functions in a particular order so that the game can be played in terminal.
    """
    player_name = welcome()
    secret_word = get_word('words.txt')
    print(drawhangman.hang[0])
    dashed = display_word(secret_word)
    print(dashed, "\n")
    play(secret_word, dashed, player_name)

if __name__ == "__main__":
    main()