import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private static void showMenu() {
        System.out.println("Wybierz opcję.");
        System.out.println("play - Zacznij rozgrywkę!");
        System.out.println("rules - Przeczytaj zasady!");
        System.out.println("quit - Zamknij program.");
    }

    private static void showRules() {
        System.out.println("""
                Każdy z generałów (graczy) dowodzi swoją armią.
                Celem gry jest pokonanie przeciwnika poprzez wyszkolenie trzech żołnierzy do rangi Major
                lub doprowadzenie przeciwnika do bankructwa. Generałowie mogą wykonywać akcje, tj.
                
                Manewr zwiadowczy - wyszkala wybranych żołnierzy podnosząc ich doświadczenie o 1pkt.
                Atak na wroga - wygrany przejmuje 10% banku przeciwnika i podnosi doświadczenie swoich żołnierzy o 1pkt,
                natomiast żołnierze armii przegranego tracą po 1pkt doświadczenia.
                Zakup żołnierzy - zakup i wyszkolenie żołnierzy. Koszt zależy od ilości jak i rangi nabytych żołnierzy
                Żołnierze pokonani, których doświadczenie spada do 0pkt giną.""");
    }

    public static void RunGame() {
        Scanner myObj = new Scanner(System.in);
        boolean isGameRunning = true;
        showMenu();
        do {
            String option = myObj.nextLine().trim().toLowerCase();
            switch(option) {
                case "play","p":
                    System.out.println("Wybrano opcję play\n");
                    StartGame(myObj);
                    showMenu();
                    break;
                case "rules","r":
                    showRules();
                    System.out.println("Kliknij ENTER aby wrócić do Menu\n");
                    myObj.nextLine();
                    showMenu();
                    break;
                case "quit", "q":
                    System.out.println("Program kończy działanie.\n");
                    isGameRunning = false;
                    break;
                default:
                    System.out.println("Nieznana opcja.\n");
                    showMenu();
                    break;
            }
        } while(isGameRunning);
    }

    public static void StartGame(Scanner myObj) {
        System.out.println("Imię pierwszego generała:");
        String name1 = myObj.nextLine();
        System.out.println("Imię drugiego generała:");
        String name2 = myObj.nextLine();

        General player1 = new General(name1, 100);
        General player2 = new General(name2, 100);

        System.out.println("Mamy dwóch generałów: " + name1 + " oraz " + name2 );
        System.out.println("Obaj generałowie zaczynają ze swoją armią.");
        System.out.println("Ilość żołnierzy na start: " + GameConfig.STARTING_ARMY);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (int i = 0; i < GameConfig.STARTING_ARMY; i++) {
            player1.getArmy().addSoldier(new Soldier(1, 1));
            player2.getArmy().addSoldier(new Soldier(1, 1));
        }

        runTurns(myObj, player1, player2);

    }

    private static void runTurns(Scanner myObj, General player1, General player2) {
        boolean gameOngoing = true;
        General current = player1;
        General other = player2;

        while (gameOngoing) {
            System.out.println("\nTura generała: " + current.getName());
            System.out.println("Pieniądze: " + current.getMoney());
            System.out.println("Wybierz akcję:");
            System.out.println("1 - Akcja");
            System.out.println("2 - Pokaż armię");
            System.out.println("3 - Zakończ turę");
            System.out.println("4 - Zakończ grę");

            String action = myObj.nextLine();

            switch (action) {
                case "1":

                    System.out.println("Wybierz akcję:");
                    System.out.println("1 - Wykonaj manewr zwiadowczy");
                    System.out.println("2 - Zaatakuj wroga");
                    System.out.println("3 - Kup żołnierzy");
                    System.out.println("4 - Wyjdź");
                    String chooseAction = myObj.nextLine();
                    switch (chooseAction) {
                        case "1":
                            System.out.println(current.getArmy()); // Wyświetl listę żołnierzy z indeksami
                            System.out.println("Podaj kolejno numery żołnierzy, których chcesz wysłać na zwiady (oddzielone przecinkami):");

                            String chooseSoldiers = myObj.nextLine();

                            try {
                                // Podziel wprowadzone dane na indeksy
                                General finalCurrent = current;
                                List<Soldier> selectedSoldiers = Arrays.stream(chooseSoldiers.split(","))
                                        .map(String::trim) // Usuń ewentualne spacje
                                        .mapToInt(Integer::parseInt) // Konwertuj na liczby
                                        .filter(index -> index > 0 && index <= finalCurrent.getArmy().getSoldiers().size()) // Filtruj poprawne indeksy (1-based)
                                        .map(index -> index - 1) // Konwertuj na indeksy 0-based
                                        .mapToObj(index -> finalCurrent.getArmy().getSoldiers().get(index)) // Pobierz odpowiadających żołnierzy
                                        .collect(Collectors.toList()); // Zbierz ich w listę

                                if (selectedSoldiers.isEmpty()) {
                                    System.out.println("Nie wybrano żadnych poprawnych żołnierzy! Wybierz ponownie.");
                                    break; // Powrót do menu
                                }

                                // Przekaż generała (current) i listę żołnierzy do performManeuver
                                Strategy.performManeuver(current, selectedSoldiers);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Upewnij się, że podajesz liczby oddzielone przecinkami.");
                            }
                            break;

                        case "2":
                            System.out.println("Generał " + current.getName() + " atakauje Generała " + other.getName());
                            try {
                                Thread.sleep(1000);
                                Strategy.attackEnemy(current, other);
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            break;
                        case "3":
                            boolean validRank = false;
                            int rank = 0;

                            while (!validRank) {
                                try {
                                    System.out.println("Wybierz rangę (1-4):");
                                    rank = Integer.parseInt(myObj.nextLine());

                                    if (rank < 1 || rank > 4) {
                                        System.out.println("Nieprawidłowa ranga! Wprowadź liczbę z zakresu 1-4.");
                                    } else {
                                        validRank = true;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Podaj poprawną liczbę!");
                                }
                            }

                            System.out.println("Ilu żołnierzy chcesz kupić?");
                            int number = Integer.parseInt(myObj.nextLine());
                            Strategy.buySoldier(current, new Rank(rank), number);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            break;
                        case "4":
                            break;
                    }
                    break;


                case "2":
                    System.out.println("Armia generała " + current.getName() + ":");
                    System.out.println(current.getArmy());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                case "3":
                    General temp = current;
                    current = other;
                    other = temp;
                    break;
                case "4":
                    System.out.println("Czy napewno chcesz się poddać?");
                    System.out.println("TAK/NIE");
                    String surrender = myObj.nextLine();
                    switch(surrender) {
                        case "TAK","tak","t","YES","yes","y":
                            gameOngoing = false;
                            System.out.println("Generał " + current.getName() + " poddał się!");
                            System.out.println("Generał " + other.getName() + " wygrywa!");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("\nZakończono grę.\n");
                            break;
                        case "NIE","nie","n","NO","no":
                            break;
                        default:
                            System.out.println("Wybrano inną opcję.");
                            System.out.println("Anulowano.");
                            break;
                    }

                default:
                    System.out.println("Nieznana akcja. Wybierz ponownie.");
            }
        }
    }
}
