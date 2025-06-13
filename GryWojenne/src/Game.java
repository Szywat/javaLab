import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private static Secretary secretary;
    private static void showMenu() {
        System.out.println("Wybierz opcję.");
        System.out.println("play - Zacznij rozgrywkę!");
        System.out.println("continue - Kontynuuj grę z pliku zapisu!");
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
                case "continue","c":
                    System.out.println("Wybrano opcję continue\n");
                    ContinueGame(myObj);
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
        // Initialize the secretary
        secretary = new Secretary();

        // Set the secretary in the Strategy class
        Strategy.setSecretary(secretary);

        System.out.println("Imię pierwszego generała:");
        String name1 = myObj.nextLine();
        System.out.println("Imię drugiego generała:");
        String name2 = myObj.nextLine();

        General player1 = new General(name1, 100);
        General player2 = new General(name2, 100);

        System.out.println("Mamy dwóch generałów: " + name1 + " oraz " + name2 );
        System.out.println("Obaj generałowie zaczynają ze swoją armią.");
        System.out.println("Ilość żołnierzy na start: " + GameConfig.STARTING_ARMY);

        secretary.logMessage("Game started with generals: " + name1 + " and " + name2);
        secretary.logMessage("Starting army size: " + GameConfig.STARTING_ARMY);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (int i = 0; i < GameConfig.STARTING_ARMY; i++) {
            player1.getArmy().addSoldier(new Soldier(1, 1));
            player2.getArmy().addSoldier(new Soldier(1, 1));
        }

        // Log initial army status
        secretary.logArmyStatus(player1);
        secretary.logArmyStatus(player2);

        runTurns(myObj, player1, player2);
    }

    public static void ContinueGame(Scanner myObj) {
        // Initialize the secretary
        secretary = new Secretary();

        // Set the secretary in the Strategy class
        Strategy.setSecretary(secretary);

        System.out.println("Imię pierwszego generała (musi istnieć plik zapisu):");
        String name1 = myObj.nextLine();
        System.out.println("Imię drugiego generała (musi istnieć plik zapisu):");
        String name2 = myObj.nextLine();

        General player1 = new General(name1, 100);
        General player2 = new General(name2, 100);

        boolean player1Loaded = PlayerStateManager.loadPlayerState(player1);
        boolean player2Loaded = PlayerStateManager.loadPlayerState(player2);

        if (!player1Loaded || !player2Loaded) {
            System.out.println("Nie udało się wczytać stanu jednego lub obu graczy. Powrót do menu głównego.");
            return;
        }

        System.out.println("Mamy dwóch generałów: " + name1 + " oraz " + name2);
        System.out.println("Kontynuujemy grę z zapisanego stanu.");

        secretary.logMessage("Game continued with generals: " + name1 + " and " + name2);

        // Log loaded army status
        secretary.logArmyStatus(player1);
        secretary.logArmyStatus(player2);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        runTurns(myObj, player1, player2);
    }

    private static boolean checkWinningConditions(General current, General other) {
        // Check if opponent has 0 soldiers in army and less than 10 money
        if (other.getArmy().getSoldiers().isEmpty() && other.getMoney() < 10) {
            System.out.println("Generał " + other.getName() + " nie ma żołnierzy i ma mniej niż 10 pieniędzy!");
            System.out.println("Generał " + current.getName() + " wygrywa!");

            // Log the winning condition
            secretary.logGameEnd(current, other, "Przeciwnik ma 0 żołnierzy i mniej niż 10$");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("\nZakończono grę.\n");
            return true;
        }

        // Check if current player has 3 soldiers with rank 4
        long rank4Count = current.getArmy().getSoldiers().stream()
                .filter(soldier -> soldier.getRank() == 4)
                .count();

        if (rank4Count >= GameConfig.WINNING) {
            System.out.println("Generał " + current.getName() + " ma 3 żołnierzy rangi Major!");
            System.out.println("Generał " + current.getName() + " wygrywa!");

            // Log the winning condition
            secretary.logGameEnd(current, other, "Gracz ma 3 lub więcej żołnierzy z rangą Major");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("\nZakończono grę.\n");
            return true;
        }

        return false;
    }

    private static void runTurns(Scanner myObj, General player1, General player2) {
        boolean gameOngoing = true;
        General current = player1;
        General other = player2;

        while (gameOngoing) {
            // Check winning conditions
            if (checkWinningConditions(current, other)) {
                gameOngoing = false;
                continue;
            }
            System.out.println("\nTura generała: " + current.getName());
            System.out.println("Pieniądze: " + current.getMoney());
            System.out.println("Wybierz akcję:");
            System.out.println("1 - Akcja");
            System.out.println("2 - Pokaż armię");
            System.out.println("3 - Pokaż dziennik akcji");
            System.out.println("4 - Zapisz stan gracza");
            System.out.println("5 - Zakończ grę");

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
                                boolean maneuverSuccess = Strategy.performManeuver(current, selectedSoldiers);

                                if (maneuverSuccess) {
                                    // Log the maneuver
                                    secretary.logManeuver(current, selectedSoldiers);
                                }

                                try {
                                    Thread.sleep(500);

                                    // Check winning conditions after maneuver
                                    if (checkWinningConditions(current, other)) {
                                        gameOngoing = false;
                                    } else {
                                        // Switch turns automatically
                                        General temp = current;
                                        current = other;
                                        other = temp;
                                        System.out.println("Tura automatycznie przechodzi do generała " + current.getName());

                                        // Log turn change
                                        secretary.logTurnChange(current, other);
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Upewnij się, że podajesz liczby oddzielone przecinkami.");
                            }
                            break;

                        case "2":
                            System.out.println("Generał " + current.getName() + " atakauje Generała " + other.getName());

                            // Log the attack before it happens
                            secretary.logMessage("General " + current.getName() + " is attacking General " + other.getName());

                            try {
                                Thread.sleep(1000);

                                // Store money before attack to calculate how much was taken
                                int currentMoneyBefore = current.getMoney();
                                int otherMoneyBefore = other.getMoney();

                                Strategy.attackEnemy(current, other);

                                // Calculate money difference to determine victory/defeat
                                int currentMoneyAfter = current.getMoney();
                                int moneyDifference = currentMoneyAfter - currentMoneyBefore;

                                // Log the attack result
                                if (moneyDifference > 0) {
                                    // Current player won
                                    secretary.logAttack(current, other, true, moneyDifference);
                                } else if (moneyDifference < 0) {
                                    // Current player lost
                                    secretary.logAttack(current, other, false, -moneyDifference);
                                } else {
                                    // Draw
                                    secretary.logAttack(current, other, false, 0);
                                }

                                Thread.sleep(1000);

                                // Check winning conditions after attack
                                if (checkWinningConditions(current, other)) {
                                    gameOngoing = false;
                                } else {
                                    // Switch turns automatically
                                    General temp = current;
                                    current = other;
                                    other = temp;
                                    System.out.println("Tura automatycznie przechodzi do generała " + current.getName());

                                    // Log turn change
                                    secretary.logTurnChange(current, other);
                                }
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

                            // Calculate total cost before buying
                            int totalCost = 10 * rank * number;

                            // Log the purchase attempt
                            secretary.logMessage("General " + current.getName() + " is attempting to buy " + number + 
                                               " soldiers of rank " + rank + " for " + totalCost + "$");

                            // Store money before purchase
                            int moneyBefore = current.getMoney();

                            Strategy.buySoldier(current, new Rank(rank), number);

                            // Check if purchase was successful by comparing money
                            if (current.getMoney() < moneyBefore) {
                                // Log successful purchase
                                secretary.logBuySoldiers(current, rank, number, totalCost);
                            }

                            try {
                                Thread.sleep(500);

                                // Check winning conditions after buying soldiers
                                if (checkWinningConditions(current, other)) {
                                    gameOngoing = false;
                                } else {
                                    // Switch turns automatically
                                    General temp = current;
                                    current = other;
                                    other = temp;
                                    System.out.println("Tura automatycznie przechodzi do generała " + current.getName());

                                    // Log turn change
                                    secretary.logTurnChange(current, other);
                                }
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

                    // Log army status
                    secretary.logArmyStatus(current);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                case "3":
                    System.out.println("=== DZIENNIK AKCJI ===");
                    System.out.println(secretary.readLog());
                    System.out.println("Naciśnij ENTER, aby kontynuować...");
                    myObj.nextLine();
                    break;
                case "4":
                    // Save player state
                    System.out.println("Zapisywanie stanu gracza " + current.getName() + "...");
                    PlayerStateManager.savePlayerState(current);
                    System.out.println("Naciśnij ENTER, aby kontynuować...");
                    myObj.nextLine();
                    break;
                case "5":
                    System.out.println("Czy napewno chcesz się poddać?");
                    System.out.println("TAK/NIE");
                    String surrender = myObj.nextLine();
                    switch(surrender) {
                        case "TAK","tak","t","YES","yes","y":
                            gameOngoing = false;
                            System.out.println("Generał " + current.getName() + " poddał się!");
                            System.out.println("Generał " + other.getName() + " wygrywa!");

                            // Log surrender
                            secretary.logGameEnd(other, current, "Player surrender");

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
