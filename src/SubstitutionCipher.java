import java.util.*;
import java.util.stream.Collectors;


public class SubstitutionCipher {
    private List<Character> chars;
    private List<Character> key;

public SubstitutionCipher() {
        String PUNCTUATION = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        String DIGITS = "0123456789";
        String ASCII_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String allCharsString = " " + PUNCTUATION + DIGITS + ASCII_LETTERS;


        
        
        this.chars = allCharsString.chars()   
                .mapToObj(c -> (char) c)      
                .collect(Collectors.toList()); 

        
        this.key = new ArrayList<>(this.chars);
        
        
        Collections.shuffle(this.key);
    }

public SubstitutionCipher(String keyString) {
        String PUNCTUATION = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        String DIGITS = "0123456789";
        String ASCII_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String allCharsString = " " + PUNCTUATION + DIGITS + ASCII_LETTERS;

        // Re-create the original 'chars' list in the exact same order
        this.chars = allCharsString.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        // Re-create the 'key' list from the key string received from the server
        this.key = keyString.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }

// Converts the shuffled key list into a single String to be sent.

    public String getKeyString() {
        // Use a StringBuilder to efficiently build the string
        StringBuilder sb = new StringBuilder(this.key.size());
        for (Character c : this.key) {
            sb.append(c);
        }
        return sb.toString();
    }


    public String encrypt(String text) {
        
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
                int index = this.chars.indexOf(c);
                if (index == -1){ // If string isn't part of key, append as is
                    result.append(c);
                } else{
                result.append(this.key.get(index));
        }}
        return result.toString();
    }
    public String decrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = this.key.indexOf(c); 
            
            if (index == -1) {
                result.append(c);
            } else {
                result.append(this.chars.get(index)); 
            }
        }
        return result.toString();
    }
}

