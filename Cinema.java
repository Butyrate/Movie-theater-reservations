package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {
    public static Scanner scanner = new Scanner(System.in);

    public static void showMenu(){
        // The main menu
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    public static char[][] createNewCinema() {
        // Set the parameters of the new room
        System.out.println("Enter the number of rows:");
        int len_x = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int len_y = scanner.nextInt();

        char[][] room = new char[len_x][len_y];
        for (char[] chars : room) {
            Arrays.fill(chars, 'S');
        }
        return room;
    }

    public static void showCinema(char[][] room) {
        // Display all the seats in the theater and show which ones are occupied
        System.out.println();
        System.out.println("Cinema:");
        System.out.print("  ");
        for (int i = 1; i <= room[0].length; i++) {
            System.out.print(i + " ");
        }

        System.out.println();
        for (int i = 0; i < room.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < room[i].length; j++) {
                System.out.print(room[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[] chooseSeat() {
        // Seat selection menu
        int[] seat = new int[2];
        System.out.println();

        System.out.println("Enter a row number:");
        seat[0] = scanner.nextInt() - 1;

        System.out.println("Enter a seat number in that row:");
        seat[1] = scanner.nextInt() - 1;

        return seat;
    }

    public static void reserveSeat(char[][] cinemaRoom, int[] reservedSeat){
        // Book one of the seats
        int row = reservedSeat[0];
        int seat = reservedSeat[1];
        cinemaRoom[row][seat] = 'B';
    }

    public static boolean isReserved(char[][] cinemaRoom, int[] checkingSeat){
        // Check if the seat is occupied
        int row = checkingSeat[0];
        int seat = checkingSeat[1];
        return cinemaRoom[row][seat] == 'B';
    }

    public static boolean isEnable(char[][] cinemaRoom, int[] checkingSeat){
        // Check if this place is even available in the hall
        int row = checkingSeat[0];
        int seat = checkingSeat[1];
        if (cinemaRoom.length > row && row >= 0) {
            return cinemaRoom[row].length > seat && seat >= 0;
        }
        return false;
    }

    public static int calculatePrice (char[][] cinemaRoom, int[] chosenSeat) {
        // Calculation of the final price for the selected place
        int price;
        int lastFrontSeat;
        int numRows = cinemaRoom.length;
        int numSeats = cinemaRoom[0].length;

        // If the theater has less than 60 seats
        // all seats will cost $10 each.
        if (numRows * numSeats <= 60) {
            price = 10;
        } else {

            // If there is not an odd number of seats in the hall
            if (numRows * numSeats % 2 != 0) {
                lastFrontSeat = (numRows - 1)/2;
            } else {
                lastFrontSeat = numRows/2;
            }

            // If there are more seats
            // back row seats are $8 each
            if ((chosenSeat[0] + 1)  > lastFrontSeat) {
                price = 8;
            } else price = 10;
        }

        return price;
    }

    public static int calculateTotalIncome (char[][] cinemaRoom) {
        // Calculation of the cost of all seats in the hall
        int totalIncome = 0;

        for (int i = 0; i < cinemaRoom.length; i++) {
            for (int j = 0; j < cinemaRoom[i].length; j++) {
                totalIncome += calculatePrice(cinemaRoom, new int[]{i, j});
            }
        }

        return totalIncome;
    }

    public static void showStatistics(char [][] cinemaRoom){
        // Output of statistics on tickets sold
        System.out.println();
        System.out.printf("Number of purchased tickets: %d%n", calculatePurchasedTickets(cinemaRoom));
        System.out.printf("Percentage: %.2f%%%n", calculatePercentage(cinemaRoom));
        System.out.printf("Current income: $%d%n", currentIncome(cinemaRoom));
        System.out.printf("Total income: $%d%n", calculateTotalIncome(cinemaRoom));
    }

    public static int calculatePurchasedTickets(char [][] cinemaRoom) {
        // How many times does the char 'B' appear in the table
        int totalPurchasedSeats = 0;
        for (char[] chars : cinemaRoom) {
            for (char aChar : chars) {
                if (aChar == 'B') {
                    totalPurchasedSeats += 1;
                }
            }
        }

        return totalPurchasedSeats;
    }

    public static double calculatePercentage(char[][] cinemaRoom) {
        // Calculation of how many seats in the hall are redeemed (in percentages)
        double totalPercentage;
        double totalSeats = 0;
        int totalPurchasedSeats = 0;

        for (char[] chars : cinemaRoom) {
            for (char aChar : chars) {
                if (aChar == 'B') {
                    totalPurchasedSeats += 1;
                }
                totalSeats += 1;
            }
        }

        totalPercentage = totalPurchasedSeats / totalSeats * 100;

        return totalPercentage;
    }

    public static int currentIncome(char[][] cinemaRoom) {
        // Calculation of how many tickets were sold
        int totalIncome = 0;
        for (int i = 0; i < cinemaRoom.length; i++) {
            for (int j = 0; j < cinemaRoom[i].length; j++) {
                if (cinemaRoom[i][j] == 'B') {
                    totalIncome += calculatePrice(cinemaRoom, new int[]{i, j});
                }
            }
        }
        return totalIncome;
    }

    public static void main(String[] args) {
        char [][] cinemaRoom = createNewCinema();
        int myChoice = -1;

        // Show the program menu until the user presses "Exit"
        while (myChoice != 0) {
            showMenu();
            myChoice = scanner.nextInt();

            // Checking user selection
            switch (myChoice) {
                case 0:
                    //Exit
                    break;

                case 1:
                    // Show the seats
                    showCinema(cinemaRoom);
                    break;

                case 2:
                    // Buy a ticket
                    int[] chosenSeat = chooseSeat();
                    while (true) {
                        if (isEnable(cinemaRoom, chosenSeat)) {
                            if (isReserved(cinemaRoom, chosenSeat)) {
                                System.out.println("That ticket has already been purchased!");
                                chosenSeat = chooseSeat();
                            } else {
                                reserveSeat(cinemaRoom, chosenSeat);
                                break;
                            }
                        } else {
                            System.out.println("Wrong input!");
                            chosenSeat = chooseSeat();
                        }
                    }

                    System.out.printf("Ticket price: $%d%n", calculatePrice(cinemaRoom, chosenSeat));
                    break;

                case 3:
                    // Statistics
                    showStatistics(cinemaRoom);
            }
        }
    }
}