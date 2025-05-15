import java.util.Scanner;

public class Game {
    public static void RunGame() {
        Scanner myObj = new Scanner(System.in);
        boolean isGameRunning = true;
        System.out.println("Wybierz opcję.");
        System.out.println("play - Zacznij rozgrywkę!");
//        System.out.println("rules - Przeczytaj zasady!");
        System.out.println("quit - Zamknij program.");
        do {
            String option = myObj.nextLine();
            switch(option) {
                case "play":
                    System.out.println("Wybrano opcję play");
                    StartGame();
                    break;
                case "quit":
                    System.out.println("Program kończy działanie.");
                    isGameRunning = false;
                    break;
                default:
                    System.out.println("Nie ma takiej opcji, wybierz inną");
                    break;
            }

        } while(isGameRunning);
    }

    public static void StartGame() {
        Scanner gettingName = new Scanner(System.in);
        System.out.println("Imię pierwszego generała:");
        String name1 = gettingName.nextLine();
        System.out.println("Imię drugiego generała:");
        String name2 = gettingName.nextLine();
        General player1 = new General(name1);
        General player2 = new General(name2);
        System.out.println("Mamy dwóch generałów: " + name1 + " oraz " + name2 );
    }
}
