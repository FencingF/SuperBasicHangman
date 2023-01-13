import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;

public class Main {

    protected static int guessesLeft;

    protected static final URI randomWordURI = URI.create("https://random-word-api.herokuapp.com/word");

    public static void main(String[] args) {
        String randomWord = getRandomWord(randomWordURI).replaceAll("\\[", "").replaceAll("\"", "").replaceAll("]", "").toLowerCase();
        String hiddenWord = "";
        int secretWordLength = randomWord.length();
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many guesses would you like: ");
        guessesLeft = scanner.nextInt();
        for (int i = 0; i < secretWordLength; i++) {
            hiddenWord += "_";
        }

        while(guessesLeft != 0) {
            hiddenWord = guess(randomWord, hiddenWord, guessesLeft);
            System.out.println("You have " + guessesLeft + " guesses left");
            System.out.println(hiddenWord);
            if (hiddenWord.equals(randomWord)) {
                System.out.println("You win!");
                break;
            }
        }
        System.out.println("Word was " + randomWord);
    }

    public static String guess(String secretWord, String hiddenWord, int guesses) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Guess a letter: ");
        String guess = scanner.nextLine();
        char character = guess.charAt(0);
        if (secretWord.contains(String.valueOf(character))) {
            for (int i = 0; i < secretWord.length(); i++) {
                if (secretWord.charAt(i) == character) {
                    hiddenWord = hiddenWord.substring(0, i) + character + hiddenWord.substring(i + 1);
                }
            }
        } else {
            guesses--;
            guessesLeft = guesses;
        }
        if (guesses == 0) {
            System.out.println("You lost!");
            return hiddenWord;
        }
        return hiddenWord;
    }

    public static String getRandomWord(URI uri) {

        String word = "";

        try {
            //get the joke from the api
            final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofSeconds(7)).header("Accept", "application/json").build();
            //get the response
            final HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            word = response.body();
        } catch (Exception e) {
            System.out.println(e);
        }
        return word;
    }
}