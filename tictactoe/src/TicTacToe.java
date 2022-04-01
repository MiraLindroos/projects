import java.io.Console;
/**
* This is a game for Tic Tac Toe, where the player plays against the computer.
* The game is divided in three classes: TicTacToe, Play and Board.
*
*@author Mira Lindroos
*/

public class TicTacToe {
    static String p1;
    static char p1mark, p2mark, x, o;
    static char [][] board;
    static int row, column, nmbOfMarks;
    /**
    * The main method calls different methods so that the game can be played.
    */
    public static void main(String [] args) {
        Play.askPlayerDetails();
        int size = Board.getSize(1);
        nmbOfMarks = Play.marksToWin(size);
        Board br = new Board();
        char[][] board = new char [size] [size];
        board = br.board(size);
        Board.makeBoard(board, size);
        Play.makeYourMove(board, size);
    }
}
/**
* Class Play has five methods: askPlayerDetails, marksToWin, makeYourMove, computersMove and checkWinner.
*/
class Play extends TicTacToe {
    /**
    * The method askPlayerDetails asks the players name and if they want to play as 'x' or 'o'.
    * If the player gives a blank response or a character that is not 'x' or 'o', the player will be asked for the input again.
    * Depending on what the player chooses, the mark that is not chosen will be set as the mark of the computer.
    */
    public static void askPlayerDetails() {
        Console c = System.console();
        System.out.println("Player 1, what is your name?");
        p1 = c.readLine();
        System.out.println("Player 2 is the computer");
        p2mark = ' ';
        x = 'x';
        o = 'o';

        do {
            System.out.println(p1 + " do you want to be x or o?");
            String str = c.readLine();
            try {
                p1mark = str.charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("enter 'x' or 'o'");
            }
        }
        while (p1mark != 'X' && p1mark!= 'O' && p1mark != 'x' && p1mark != 'o');

        if (p1mark == x || p1mark == 'X') {
        p1mark = 'x';
        p2mark = 'o';
        } else if (p1mark == o || p1mark == 'O') {
        p1mark = 'o';
        p2mark = 'x';
        }
    }
    /**
    * The method marksToWin asks the player how many marks are needed to win, and then returns the value.
    * If the grid size is smaller than 10, the amount of needed marks is three or more, but less than the grid size.
    * If the grid size is 10 or bigger, the amount of needed marks is five or more, but less than the grid size.
    * The player will be asked for the number of marks as long as the player gives it as an int.
    *
    *@param size the size of the grid.
    *@return
    */
    public static int marksToWin(int size) {
        Console c = System.console();
        if(size < 10) {
            do {
                System.out.println("How many marks in a row is needed to win?");
                String input = c.readLine();
                try {
                    nmbOfMarks = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("enter a number, ex. 3");
                }
            } while(nmbOfMarks < 3 || nmbOfMarks > size);
            return nmbOfMarks;
        } else if(size >= 10) {
            do {
                System.out.println("How many marks in a row is needed to win?");
                String input2 = c.readLine();
                try {
                    nmbOfMarks = Integer.parseInt(input2);
                } catch (NumberFormatException e) {
                    System.out.println("enter a number, ex. 5");
                } 
            } while(nmbOfMarks < 5 || nmbOfMarks > size);
        } return nmbOfMarks;
    }
    /**
    * The method makeYourMove asks the player for the coordinates to their next move.
    * If their chosen location already has been used, the location is off limits or they have not put anything at all, it asks again.
    * After the players move, the method checks if there is a winner by calling the checkWinner method.
    * If neither of the players have won or the board is not full, it calls the method computersMove.
    *
    *@param board, @param size the size of the grid.
    */
    public static void makeYourMove(char[][]board, int size) {
        Console c = System.console();
        boolean gameOn = true;
        String input, input2;
        while(gameOn) {
            while(true) {
                do {
                    System.out.println(p1 + " which row do you want to put your mark? 0 - " + (size-1));
                    input = c.readLine();
                    try {
                        row = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("error, insert number between 0 and " + (size-1));
                    }
                } while(input.length() == 0 || row < 0 || row >= size);
                do {
                    System.out.println(p1 + " which column do you want to put your mark? 0 - " + (size-1));
                    input2 = c.readLine();
                    try {
                        column = Integer.parseInt(input2);
                    } catch (NumberFormatException e) {
                        System.out.println("error, insert number between 0 and " + (size-1));
                    }
                } while(input2.length() == 0 || column < 0 || column >= size);

                if (board[row][column] == 'x' || board[row][column] =='o') {
                    System.out.println("enter another location, the location you entered has already been used");
                } else {
                    break;
                }
            }
            board[row][column] = p1mark;
            Board.makeBoard(board, size);
            //If the winning mark is 'x' and it was the mark of the player, the player wins
            //otherwise the computer wins.
            if(checkWinner(board, size) == x) {
                if(p1mark=='x') {
                    System.out.println(p1 + (" has won! Congratulations"));
                } else {
                    System.out.println("The computer has won!");
                }
                gameOn = false;
            //If the winning mark is 'o' and it was the mark of the player, the player wins
            //otherwise the computer wins.
            } else if(checkWinner(board, size) == o) {
                if(p1mark=='o') {
                    System.out.println(p1 + (" has won! Congratulations"));
                } else {
                    System.out.println("The computer has won!");
                }
                gameOn = false;
            //It is a tie if the board is full and neither of the players have won.
            } else {
                if(Board.isBoardFull(board)) {
                    System.out.println("It is a tie!");
                    gameOn = false;
                } else {
                    gameOn = computersMove(board, size);
                }
            }
            Board.makeBoard(board, size);
        } 
    }
    /**
    * The method computersMove draws random numbers for the location of the computers mark.
    * After the location is drawn, the method checks if there is a win.
    * It returns false if there is a win or a tie, otherwise it returns true and the game continues.
    *
    *@param board, @param size the size of the grid.
    *@return
    */
    public static boolean computersMove(char[][] board, int size) {
        System.out.println("Now is the computers turn");
        System.out.println();
        //This makes a two second break so that the player has time to read that it is now the computers turn.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {

        }
        //Here we draw a random number between 0 and the size-1 given by the player.
        //The first number is for the row and the second is for the column.
        //If the drawn location already has an 'x' or an 'o' in it, it draws again.
        do {
            row = (int) (Math.random()*size);
            column = (int) (Math.random()*size);
        } while (board[row][column] == 'x' || board[row][column] =='o');
        board[row][column] = p2mark;
        
        if(checkWinner(board, size) == 'x') {
            if(p1mark == 'x') {
                System.out.println(p1 + (" has won! Congratulations"));
            } else {
                System.out.println("The computer has won!");
            } return false;
        } else if(checkWinner(board, size) == 'o') {
            if(p1mark == 'o') {
                System.out.println(p1 + (" has won! Congratulations"));
            } else {
                System.out.println("The computer has won!");
            } return false;
        } else {
            if(Board.isBoardFull(board)) {
                System.out.println("It is a tie!");
                return false;
            } else {
                return true;
            }
        }
    }
    /**
    * The method checkWinner checks every row, column and diagonals if there is enough 'x' or 'o' marks for the win.
    * The marks need to be in a row, on top of each other or diagoally in line in order to win.
    * If there is a win, the method returns 'x' or 'o' depending on which mark won.
    * The mark '-' is returned if nobody has won.
    * 
    *@param board, @param size the size of the grid.
    *@return
    */
    public static char checkWinner(char[][]board, int size) {
        int oInArow = 0;
        int xInArow = 0;
        //Row check.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j]=='x') {
                    xInArow++;
                } else {
                    xInArow = 0;
                }
                if (board[i][j]=='o') {
                    oInArow++;
                } else {
                    oInArow = 0;
                }
                if (xInArow >= nmbOfMarks) {
                    return 'x';
                } else if (oInArow >= nmbOfMarks) {
                    return 'o';
                }
            }
            xInArow = 0;
            oInArow = 0;
        }
        //Column check.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i]=='x') {
                    xInArow++;
                } else {
                    xInArow = 0;
                }
                if (board[j][i]=='o') {
                    oInArow++;
                } else {
                    oInArow = 0;
                }
                if (xInArow >= nmbOfMarks) {
                    return 'x';
                } else if (oInArow >= nmbOfMarks) {
                    return 'o';
                } 
            }
            xInArow = 0;
            oInArow = 0;
        }
        //Diagonals check.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 'x' || board[i][j] == 'o') {
                    //This try catch prevents index out of bounds exception.
                    try {
                        for (int a = 0; a < board.length; a++) {
                            if (board[i+a][j+a] == 'x') {
                                xInArow++;
                                if (xInArow >= nmbOfMarks) {
                                    return 'x';
                                }
                            } else {
                                xInArow = 0;
                            }
                            if (board[i+a][j+a] == 'o') {
                                oInArow++;
                                if (oInArow >= nmbOfMarks) {
                                    return 'o';
                                }
                            } else {
                                oInArow = 0;
                            }
                        }
                    } catch (Exception e) {}
                    xInArow = 0;
                    oInArow = 0;
                    //This try catch prevents index out of bounds exception.
                    try {
                        for (int a = 0; a < board.length; a++) {
                            if (board[i+a][j-a] == 'x') {
                                xInArow++;
                                if (xInArow >= nmbOfMarks) {
                                    return 'x';
                                }
                            } else {
                                xInArow = 0;
                            }
                            if (board[i+a][j-a] == 'o') {
                                oInArow++;
                                if (oInArow >= nmbOfMarks) {
                                    return 'o';
                                }
                            } else {
                                oInArow = 0;
                            }
                        }
                    } catch (Exception e) {}
                    xInArow = 0;
                    oInArow = 0;
                }
            }
        }
        //nobody has won 
        return '-';
    }
}
/**
* Class Board contains four methods about the board. The methods are: getSize, board, makeBoard and isBoardFull.
*/
class Board {
    /**
    * The method getSize asks the player how big of a grid they want, and then the method returns the size.
    * If the given size is smaller than three, it asks again.
    * The user will be asked for the size as long as the user gives it as an int.
    *
    *@param size the size of the grid.
    *@return
    */
    public static int getSize(int size) {
        Console c = System.console();
        do {
            System.out.println("give the size of the grid");
            String input = c.readLine();
            try {
                size = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("enter a number, ex. 3");
            }
        } while(size < 3);
        return size;
    }
    /**
    * The method board creates a new array with the size given by the player. It saves '-' to every spot in the array and then returns the array.
    *
    *@param size the size of the grid.
    *@return
    */
    public char[][] board(int size) {
        char[][] newboard = new char [size] [size];
        for(int i = 0; i < newboard.length; i++) {
            for(int j = 0; j < newboard[i].length; j++) {
                newboard[i][j] = '-';
            }
        }
        return newboard;
    }
    /**
    * The method makeBoard prints out the board and row numbers to help visualize. 
    * If the board size is smaller than 11, it also prints the column numbers at the top of the board.
    *
    *@param board, @param size the size of the grid.
    */
    public static void makeBoard(char[][]board, int size) {
        if(size <= 10) {
            System.out.print("column ");
            for(int i = 0; i < board[0].length; i++) {
                System.out.print(i + " ");
            } System.out.println();
        }
        for(int i = 0; i < size; i++) {
            if(size <= 100) {
                if(i < 10) {
                    System.out.print("row " + i + "  ");
                } else {
                    System.out.print("row " + i + " ");
                }
            }
            for(int j = 0; j < size; j++) {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    /**
    * The method isBoardFull checks if there is still '-' marks left in the board.
    * If the board doesn't have any '-' marks left, it returns true, and then the game ends.
    *
    *@param board
    *@return
    */
    public static boolean isBoardFull(char [][]board) {
        for (int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }
}