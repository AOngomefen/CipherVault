package BobsCircus;

import java.util.ArrayList;

/**
 * This is a utility class that encrypts and decrypts a phrase using three
 * different approaches. 
 * 
 * The first approach is called the Vigenere Cipher.Vigenere encryption 
 * is a method of encrypting alphabetic text based on the letters of a keyword.
 * 
 * The second approach is Playfair Cipher. It encrypts two letters (a digraph) 
 * at a time instead of just one.
 * 
 * The third approach is Caesar Cipher. It is a simple replacement cypher. 
 * 
 * @author Huseyin Aygun
 * @version 8/3/2025
 */

public class CryptoManager { 

    private static final char LOWER_RANGE = ' ';
    private static final char UPPER_RANGE = '_';
    private static final int RANGE = UPPER_RANGE - LOWER_RANGE + 1;
    // Use 64-character matrix (8X8) for Playfair cipher  
    private static final String ALPHABET64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_ ";

    public static boolean isStringInBounds(String plainText) {
        for (int i = 0; i < plainText.length(); i++) {
            if (!(plainText.charAt(i) >= LOWER_RANGE && plainText.charAt(i) <= UPPER_RANGE)) {
                return false;
            }
        }
        return true;
    }

	/**
	 * Vigenere Cipher is a method of encrypting alphabetic text 
	 * based on the letters of a keyword. It works as below:
	 * 		Choose a keyword (e.g., KEY).
	 * 		Repeat the keyword to match the length of the plaintext.
	 * 		Each letter in the plaintext is shifted by the position of the 
	 * 		corresponding letter in the keyword (A = 0, B = 1, ..., Z = 25).
	 */   

    public static String vigenereEncryption(String plainText, String key) {
    	String result;
    	int plainIndex, keyIndex, encryptedIndex;
    	
    	if (isStringInBounds(plainText) == false || isStringInBounds(key) == false) {
    		return "The selected string is not in bounds, Try again.";
    	}
    	result = "";
    	for (int i = 0; i < plainText.length(); i++) {
    		plainIndex = ALPHABET64.indexOf(plainText.charAt(i));
    		keyIndex = ALPHABET64.indexOf(key.charAt(i % key.length()));
    		encryptedIndex = ((plainIndex + keyIndex) % RANGE + RANGE) % RANGE; // Handle negative keys and wrap around
    		result += ALPHABET64.charAt(encryptedIndex);
    	}
    	
    	return result;
    }

    // Vigenere Decryption
    public static String vigenereDecryption(String encryptedText, String key) {
         String result;
         int encryptedIndex, keyIndex, decryptedIndex;
         if (isStringInBounds(encryptedText) == false || isStringInBounds(key) == false) {
			 return "The selected string is not in bounds, Try again.";
		 }
         result = "";
		 for (int i = 0; i < encryptedText.length(); i++) {
			 encryptedIndex = ALPHABET64.indexOf(encryptedText.charAt(i));
			 keyIndex = ALPHABET64.indexOf(key.charAt(i % key.length()));
			 decryptedIndex = ((encryptedIndex - keyIndex) % RANGE + RANGE) % RANGE; // Handle negative keys and wrap around
			 result += ALPHABET64.charAt(decryptedIndex);
		 }
		 
    	return result;
    }


	/**
	 * Playfair Cipher encrypts two letters at a time instead of just one.
	 * It works as follows:
	 * A matrix (8X8 in our case) is built using a keyword
	 * Plaintext is split into letter pairs (e.g., ME ET YO UR).
	 * Encryption rules depend on the positions of the letters in the matrix:
	 *     Same row: replace each letter with the one to its right.
	 *     Same column: replace each with the one below.
	 *     Rectangle: replace each letter with the one in its own row but in the column of the other letter in the pair.
	 */    

    public static String playfairEncryption(String plainText, String key) {
    
    	if (isStringInBounds(plainText) == false || isStringInBounds(key) == false) {
			return "The selected string is not in bounds, Try again.";
		}
    	
		char [][] matrix = buildMatrix(key);
		String preparedText = prepareDigraphs(plainText);
		String result = "";
		
		 for (int i = 0; i < preparedText.length(); i += 2) {
			 char A = preparedText.charAt(i);
			 char B = preparedText.charAt(i + 1);
			 
			 int[] posA = findPosition(matrix, A);
			 int[] posB = findPosition(matrix, B);
			 
			 if (posA[0] == posB[0]) { // Same row
				 result += matrix[posA[0]][(posA[1] + 1) % 8];
				 result += matrix[posB[0]][(posB[1] + 1) % 8];
			 } else if (posA[1] == posB[1]) { // Same column
				 result += matrix[(posA[0] + 1) % 8][posA[1]];
				 result += matrix[(posB[0] + 1) % 8][posB[1]];
			 } else { // Rectangle
				 result += matrix[posA[0]][posB[1]];
				 result += matrix[posB[0]][posA[1]];
			 }
		 }
    	
    	
    	return result;
    }
    
    	public static char[][] buildMatrix(String key){
    		ArrayList<Character> usedCharsList = new ArrayList<>();
    		
    		for (int i = 0; i < key.length(); i++) {
				char currentChar = key.charAt(i);
				if (!usedCharsList.contains(currentChar)) {
					usedCharsList.add(currentChar);
				}
			}	
			for (int i = 0; i < ALPHABET64.length(); i++) {
				char currentChar = ALPHABET64.charAt(i);
				if (!usedCharsList.contains(currentChar)) {
					usedCharsList.add(currentChar);
				}
			}
			char[][] matrix = new char[8][8];
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					matrix[i][j] = usedCharsList.get(i * 8 + j);
				}
			}
				
			return matrix;
    	}
		private static int[] findPosition(char[][] matrix, char c) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					if (matrix[i][j] == c) {
						return new int[]{i, j};
					}
				}
			}
			return null; // Character not found
		}
		
		private static String prepareDigraphs(String plainText) {
			String result = plainText;
			if (result.length() % 2 != 0) {
				result += 'X'; // Padding with space if there's an odd character at the end
			}
			return result;
		}
		

	// Vigenere Decryption
    public static String playfairDecryption(String encryptedText, String key) {
    	if (isStringInBounds(encryptedText) == false || isStringInBounds(key) == false) {
    		return "The selected string is not in bounds, Try again.";
    		
    	}
    	
    	char []	[] matrix = buildMatrix(key);
    	String preparedText = prepareDigraphs(encryptedText);
    	String result = "";
    	
    	for (int i = 0; i < preparedText.length(); i += 2) {
    		char A = preparedText.charAt(i);
    		char B = preparedText.charAt(i + 1);
    		
    		int[] posA = findPosition(matrix, A);
    		int[] posB = findPosition(matrix, B);
    		
    		
			if (posA[0] == posB[0]) { // Same row
				result += matrix[posA[0]][(posA[1] + 7) % 8]; // Move left
				result += matrix[posB[0]][(posB[1] + 7) % 8]; // Move left
			} else if (posA[1] == posB[1]) { // Same column
				result += matrix[(posA[0] + 7) % 8][posA[1]]; // Move up
				result += matrix[(posB[0] + 7) % 8][posB[1]]; // Move up
			} else { // Rectangle
				result += matrix[posA[0]][posB[1]];
				result += matrix[posB[0]][posA[1]];
			}
    	}
    	
    	if (result.endsWith("X") && !encryptedText.endsWith("X")) {
            result = result.substring(0, result.length() - 1);
        }
    	

		return result;
    }

    /**
     * Caesar Cipher is a simple substitution cipher that replaces each letter in a message 
     * with a letter some fixed number of positions down the alphabet. 
     * For example, with a shift of 3, 'A' would become 'D', 'B' would become 'E', and so on.
     */    
 
    public static String caesarEncryption(String plainText, int key) {
    	String result; 
    	int index, encryptedIndex;
    	
    	if (isStringInBounds(plainText) == false) {
			return "The selected string is not in bounds, Try again.";
		}
    	result = "";
    	
    	for (int i = 0; i < plainText.length(); i++) {
    		index = ALPHABET64.indexOf(plainText.charAt(i));
    		encryptedIndex = ((index + key) % RANGE + RANGE) % RANGE; // Handle negative keys and wrap around
    		result += ALPHABET64.charAt(encryptedIndex);
    	}
    	
    	return result;
    }

    // Caesar Decryption
    public static String caesarDecryption(String encryptedText, int key) {
    	String result;
    	int index, decryptedIndex;
    	
    	if (isStringInBounds(encryptedText) == false ) {
    		return "The selected string is not in bounds, Try again.";	
    	}
    	result = "";
    	
    	for (int i = 0; i < encryptedText.length(); i++){
			index = ALPHABET64.indexOf(encryptedText.charAt(i));
			decryptedIndex = ((index - key) % RANGE + RANGE) % RANGE; // Handle negative keys and wrap around
			result += ALPHABET64.charAt(decryptedIndex);
		}
  
    	
    	return result;
    }    

}
