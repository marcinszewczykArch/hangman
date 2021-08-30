import Exceptions.DataImportException;
import Exceptions.NoSuchOptionException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {
    static final String FILE_NAME = "D:\\Java\\IdeaProjects\\zadania\\Wisielec\\src\\ListOfWords.csv";
    static final int MAX_WRONG_LETTERS = 5;
    static int wrongLetters;
    Printer printer = new Printer();

    public void mainLoop(){
        Option option;

        do {
            printOptions();
            option = getOption();

            switch (option) {
                case NEW_GAME:
                    singleGame();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    Printer.printLineLn("wrong option selected. Please try again");
            }
        }while(option != Option.EXIT);
    }

    private void exit() {
        Printer.printLineLn("papa!");
    }

    private Option getOption() {
        boolean optionOk = false;
        Option option = null;

        while (!optionOk) {
            try {
                option = Option.createFromInt(getIntFromUser());
                optionOk = true;

            } catch (NoSuchOptionException e) {
                Printer.printLineLn(e.getMessage());
            } catch (InputMismatchException e) {
                Printer.printLineLn("input data is not a number. Please try again");
            }
        }
        return option;
    }

    public int getIntFromUser(){
        Scanner sc = new Scanner(System.in);
        int intFromUser = sc.nextInt();
        sc.nextLine();
        return intFromUser;
    }

    public void singleGame(){

            List<String> wordsImport = importWords();
            String word = getRandomWord(wordsImport);
            Set<Integer> correctMatches = new HashSet<>();
            wrongLetters = 0;

            while ((correctMatches.size() < word.length()) && wrongLetters < MAX_WRONG_LETTERS){
                printWord(word, correctMatches);
                checkLetter(word, userInputLetter(), correctMatches);
                checkIfWinner(correctMatches.size() == word.length(), word);
        }
    }

    private void printOptions() {
        Printer.printLineLn("select option:");
        for(Option value : Option.values()){
            Printer.printLineLn(value.toString());
        }
    }

    private static void checkIfWinner(boolean winner, String word) {
        if(winner) {
            printFullWord(word);
            Printer.printLineLn("Congratulations, you are a WINNER!\n");
        }
    }

    private static void checkIfLooser(String word) {
        if(wrongLetters>=5){
            Printer.printLineLn("you have choosen wrong letters " + wrongLetters + " times - you are looser!");
            printFullWord(word);
        }else{
            Printer.printLineLn("you have choosen wrong letters " + wrongLetters + " / 5");
        }
    }

    private static void printFullWord(String word) {
        String[] wordTable = word.split("");
        Printer.printLineLn("");
        for (int i = 0; i < wordTable.length; i++) {
            Printer.printLine(" " + wordTable[i] + " ");
        }
        Printer.printLineLn("\n");
    }

    private static void printWord(String word, Set<Integer> correctMatches) {
        String[] wordTable = word.split("");

        Printer.printLineLn("");
            for (int i = 0; i < wordTable.length; i++) {
                if(correctMatches.contains(i)){
                    Printer.printLine(" " + wordTable[i] + " ");
                }else{
                Printer.printLine(" _ ");
                }
            }
        Printer.printLineLn("\n");
    }

    private static Set<Integer> checkLetter(String word, char userInputLetter, Set<Integer> correctMatches) {
        String[] wordTable = word.split("");
        int counter = 0;

        for (int i = 0; i < wordTable.length; i++) {
            if(wordTable[i].equals(String.valueOf(userInputLetter).toUpperCase(Locale.ROOT))){
                correctMatches.add(i);
                counter++;
            }
        }
        Printer.printLine("\"" + String.valueOf(userInputLetter).toUpperCase(Locale.ROOT) + "\" appears " + counter + " time(s)\n");
        if(counter==0) {
            wrongLetters++;
            checkIfLooser(word);
        }
        return correctMatches;
    }

    private static char userInputLetter() {
        Scanner sc = new Scanner(System.in);
        Printer.printLineLn("type a letter: ");
        char letter = sc.next().charAt(0);
        return letter;
    }

    public static List<String> importWords() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            List<String> words = new ArrayList<>();
            bufferedReader.lines()
                    .forEach(p -> words.add(p));
            return words;
        } catch (FileNotFoundException e) {
            throw new DataImportException("there is no file with the name: " + FILE_NAME);
        } catch (IOException e) {
            throw new DataImportException("error while importing file with the name: " + FILE_NAME);
        }
    }

    public static String getRandomWord(List<String> words){
        double random = Math.random();
        int numberOfWordsInList = words.size();
        String randomWord = words.get((int) (random * numberOfWordsInList));
        return randomWord.toUpperCase(Locale.ROOT);
    }

        private enum Option {
            EXIT(0, "exit app"),
            NEW_GAME(1, "play new game");


            private final int value;
            private final String description;

            @Override
            public String toString() {
                return value + " - " + description;
            }

            Option(int value, String description) {
                this.value = value;
                this.description = description;
            }

            static Option createFromInt(int option) throws NoSuchOptionException {
                try {
                    return Option.values()[option];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchOptionException("No option with id " + option + ". Please try again");
                }

            }
        }

}
