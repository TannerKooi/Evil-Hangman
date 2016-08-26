//Nischal Reddy Chandra
//2/19/2015
//CSE 143 BA
//TA:Austin Weasle
//Assignment#5
/*The program lets us play Hangman in an unfair way.Computer never discloses
the answer unless it's forced to reveal the answer and has no choice. Other than
this it normally responds to every guess of the user.*/
import java.util.*;

public class HangmanManager {
    private Set<String> wordDictionary;
    private String wordPattern;
    private SortedSet<Character> letterGuess;
    private int guessRemaining;
    
    /* pre : Throws an IllegalArgumentException if the desired length is less
             than 1 or if maxNumber guesses is less than 0
    post :   Adds all the words into the set which have the desired length
             given in the parameters.*/
    public HangmanManager(List<String> dictionary, int length, int max) {
        if(length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        wordDictionary = new TreeSet<String>();
        letterGuess = new TreeSet<Character>();
        guessRemaining = max;
        for(String word : dictionary) {
            if(word.length() == length) {
                wordDictionary.add(word);
            }
        }
        wordPattern = "-";
        for(int i = 0; i < length - 1; i++) {
            wordPattern += " -";
        }
    }
    
    /*post : Returns set of words, whose length is same that of the wordPattern length.*/
    public Set<String> words() {
        return wordDictionary;
    }
    
    /*post : Returns the number of guessesleft fo the user before the game
             ends in a loss.*/
    public int guessesLeft() {
        return guessRemaining;
    }
    
    /*post : Returns all the set of letters in sorted order that have been
             guessed by the user. */
    public SortedSet<Character> guesses() {
        return letterGuess;
    }
    
    /*pre : Throws an IllegalArgumentException if current set of words
            the computer is considering is empty.
    post :  Returns the string of the wordPattern to be displayed to
            the user showing progress into the divination of the 'correct' word*/
    public String pattern() {
        if(wordDictionary.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return wordPattern;
    }
    
    /*pre : Throws IllegalStateException if guess remaining is less than 1 or if the current
            set of words in consideration is empty. Throws IllegalArgumentException
            if the list of words is nonempty and the character being guessed
            was guessed previously.
    post :  Selects the largest word set.Changes the wordPattern based on the
            selected set.Returns the characterOccurences of the guessed letter in
            the new wordPattern.*/
    public int record(char guess) {
        if(guessRemaining < 1 || wordDictionary.isEmpty()) {
            throw new IllegalStateException();
        } else if (!wordDictionary.isEmpty() && letterGuess.contains(guess)) {
            throw new IllegalArgumentException();
        }
        Map<String, Set<String>> family = new TreeMap<String, Set<String>>();
        letterGuess.add(guess);
        for (String word : wordDictionary) {
            String key = stringPattern(word,guess);
            if (!family.containsKey(key)) {
                family.put(key, new TreeSet<String>());
            }
            family.get(key).add(word);
        }
        String storeSet = largestSet(family);
        wordDictionary = family.get(storeSet);
        int letterCount = letterOccurance(storeSet,guess);
        return letterCount;
    }
        
    /*post: Returns the wordPattern of each word. */
    private String stringPattern(String word, char guess) {
        String obtainPattern = "";
        for(int pattern = 0; pattern < word.length(); pattern++) {
            if(word.charAt(pattern) == guess ) {
                obtainPattern += guess + " ";
            } else {
                obtainPattern += "- ";
            }
        }
        obtainPattern = obtainPattern.trim();
        return obtainPattern;
    }
    
    /* post : Returns the string value of the key that maps to the largest
              set of words. */
    private String largestSet(Map<String, Set<String>> largestFamily) {
        int setSize = 0;
        String value = "";
        for(String key : largestFamily.keySet()){
            if (largestFamily.get(key).size() > setSize) {
                setSize = largestFamily.get(key).size();
                value = key;
            }
        }
        return value;
    }
    
    /* post : returns count of guess made by the user. Also if count is 0,it decreases the
              max guesses by 1*/
    public int letterOccurance(String storeSet, char guess){
        int letterCount = 0;
        for(int pattern = 0; pattern < wordPattern.length(); pattern += 2 ){
            if(storeSet.charAt(pattern) == guess){
                letterCount++;
                wordPattern = wordPattern.substring(0, pattern) + guess +
                (wordPattern.substring(pattern + 1));
            }
        }
        if(letterCount == 0){
            guessRemaining--;
        }
        return letterCount;
    }
}