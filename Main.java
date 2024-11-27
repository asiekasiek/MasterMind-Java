import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    // Stałe
    private static final int CODE_LENGTH = 4; // Długość kodu
    private static final int MAX_DIGIT = 9; // Maksymalna cyfra w kodzie

    public static void main(String[] args) {
        // Inicjalizacja gry
        ArrayList<Integer> secretCode = generateSecretCode();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Witaj w grze Mastermind!");
        System.out.println("Komputer wylosował tajny kod składający się z " + CODE_LENGTH + " unikalnych cyfr.");
        System.out.println("Twoim zadaniem jest go odgadnąć. Po każdej próbie otrzymasz wskazówki.");

        boolean won = false;
        int attempts = 0;

        while (!won) {
            System.out.println("Podaj swój kod (" + CODE_LENGTH + " unikalnych cyfr): ");
            String userInput = scanner.nextLine();

            if (!isValidInput(userInput)) {
                System.out.println("Nieprawidłowe dane wejściowe. Upewnij się, że kod ma " + CODE_LENGTH + " unikalnych cyfr.");
                continue;
            }

            ArrayList<Integer> userGuess = convertInputToList(userInput);
            attempts++;

            // Ocena próby
            int correctPosition = countCorrectPositions(secretCode, userGuess);
            ArrayList<Integer> correctPositionDetails = getCorrectPositions(secretCode, userGuess);

            int correctDigits = countCorrectDigits(secretCode, userGuess);
            ArrayList<Integer> misplacedDetails = getMisplacedDigits(secretCode, userGuess);

            if (correctPosition == CODE_LENGTH) {
                won = true;
                System.out.println("Gratulacje! Odgadłeś kod w " + attempts + " próbach. Kod to: " + secretCode);
            } else {
                System.out.println("Trafienia na właściwych miejscach: " + correctPosition + " (Liczby: " + correctPositionDetails + ")");
                System.out.println("Trafienia na niewłaściwych miejscach: " + (correctDigits - correctPosition) + " (Liczby: " + misplacedDetails + ")");
            }
        }

        scanner.close();
    }

    // Generuje losowy kod składający się z unikalnych cyfr
    private static ArrayList<Integer> generateSecretCode() {
        ArrayList<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= MAX_DIGIT; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);
        return new ArrayList<>(digits.subList(0, CODE_LENGTH));
    }

    // Sprawdza, czy dane wejściowe są poprawne
    private static boolean isValidInput(String input) {
        if (input.length() != CODE_LENGTH) return false;
        ArrayList<Character> seen = new ArrayList<>();
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c) || seen.contains(c)) return false;
            seen.add(c);
        }
        return true;
    }

    // Konwertuje ciąg znaków na listę cyfr
    private static ArrayList<Integer> convertInputToList(String input) {
        ArrayList<Integer> list = new ArrayList<>();
        for (char c : input.toCharArray()) {
            list.add(Character.getNumericValue(c));
        }
        return list;
    }

    // Liczy trafienia na właściwych miejscach
    private static int countCorrectPositions(ArrayList<Integer> secretCode, ArrayList<Integer> userGuess) {
        int correct = 0;
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (secretCode.get(i).equals(userGuess.get(i))) {
                correct++;
            }
        }
        return correct;
    }

    // Zwraca listę cyfr trafionych na właściwych miejscach
    private static ArrayList<Integer> getCorrectPositions(ArrayList<Integer> secretCode, ArrayList<Integer> userGuess) {
        ArrayList<Integer> correct = new ArrayList<>();
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (secretCode.get(i).equals(userGuess.get(i))) {
                correct.add(secretCode.get(i));
            }
        }
        return correct;
    }

    // Liczy trafienia cyfr (bez uwzględniania pozycji)
    private static int countCorrectDigits(ArrayList<Integer> secretCode, ArrayList<Integer> userGuess) {
        int correct = 0;
        for (int digit : userGuess) {
            if (secretCode.contains(digit)) {
                correct++;
            }
        }
        return correct;
    }

    // Zwraca listę cyfr trafionych na niewłaściwych miejscach
    private static ArrayList<Integer> getMisplacedDigits(ArrayList<Integer> secretCode, ArrayList<Integer> userGuess) {
        ArrayList<Integer> misplaced = new ArrayList<>();
        ArrayList<Integer> unmatchedCode = new ArrayList<>(secretCode);
        ArrayList<Integer> unmatchedGuess = new ArrayList<>();

        // Usuń trafienia na właściwych miejscach z analizy
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (!secretCode.get(i).equals(userGuess.get(i))) {
                unmatchedGuess.add(userGuess.get(i));
            } else {
                unmatchedCode.remove(secretCode.get(i));
            }
        }

        // Znajdź cyfry na niewłaściwych miejscach
        for (int digit : unmatchedGuess) {
            if (unmatchedCode.contains(digit)) {
                misplaced.add(digit);
                unmatchedCode.remove((Integer) digit);
            }
        }

        return misplaced;
    }
}
