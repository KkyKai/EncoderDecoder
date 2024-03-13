import java.util.Hashtable;
import java.util.Random;
import java.util.Map.Entry;
import java.util.ArrayList;

//Chan Kai Yang
//JXC Assessment for Encoder + Decoder

class Encoder
{
    //Store all char in char array
    private char [] referenceTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 
    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
    '5', '6', '7', '8', '9',
    '(', ')', '*', '+', ',', '-', '.', '/'};

    //Initialise Hashtable 
    private Hashtable<Character, Integer> hashReferenceTable = new Hashtable<Character, Integer>();
    //Initialise reverse Hashtable
    private Hashtable<Integer, Character> reversehashtable = new Hashtable<Integer, Character>();

    //Initialise arraylist to hold encoded text filled with characters
    private ArrayList<Character> encodedText = new ArrayList<Character>(); 

    //Initialise arraylist to hold decoded text filled with characters
    private ArrayList<Character> decodedText = new ArrayList<Character>(); 

    //Initialise random variable 
    private Random rand = new Random();

    //Default Constructor
    public Encoder ()
    {

    }

    //Constructor to take in offset char from main if required
    public Encoder (char randomChar)
    {
        this.randomChar = randomChar;
    }

    private char randomChar;

    //Accessor method for random char
    public char getrandomChar ()
    {
        return randomChar;
    }
    
    //Mutator method for random char
    public void setRandomChar (char randomChar)
    {
        this.randomChar = randomChar;
    }

    //Get random character from referenceTable for offset value if not parsing in randomChar from Main
    private char getRandArrayLetter()
    {
        return referenceTable[rand.nextInt(referenceTable.length)];
    }

    private Hashtable <Character, Integer> buildHashTable ()
    {
        //Build Hashtable with keys as letter from referenceTable array
        for (int i = 0; i < referenceTable.length; i++) 
        {
            hashReferenceTable.put(referenceTable[i], i);
        }

        return hashReferenceTable;
    }

    private Hashtable <Integer, Character> buildReverseHashTable ()
    {
        //Build reverse Hashtable with keys as index using referenceTable array
        //As the range of values is small hence, better memory efficiency.
        for (int i = 0; i < referenceTable.length; i++) 
        {
            reversehashtable.put(i, referenceTable [i]);
        }

        /*Build reverse Hashtable with keys as index using original hashtable
        for(Entry<Character, Integer> entry : hashtable.entrySet()){
            reversehashtable.put(entry.getValue(), entry.getKey());
        } */

        return reversehashtable;
    }

   public String encode (String plainText)
   {  
        //Build Hashtable and Reverse Hashtable
        buildHashTable ();
        buildReverseHashTable ();

        //if plainText is not all UpperCase and application requires all lowercase to be encoded based on uppercase counterpart
        //String allCaps = plainText.toUpperCase();
        //char [] plainTextCharArray = allCaps.toCharArray();

        //Convert String to char array for comparison
        char [] plainTextCharArray = plainText.toCharArray();

        //Test random if not parsing in offset character in constructor
        //char randomChar = getRandArrayLetter();

        //Get offset value from key with random character in hashtable
        int offsetValue = hashReferenceTable.get(randomChar); 
        System.out.println("Offset Char " + randomChar + " with index value " + offsetValue);

        //System.out.println(hashReferenceTable);
        //System.out.println(reversehashtable);
        //System.out.println(offsetValue);

        encodedText.add(randomChar);

        for (int i = 0; i < plainTextCharArray.length; i++) 
        {
            //If character is found in hashtable (i.e. in the reference table)
            if (hashReferenceTable.containsKey(plainTextCharArray [i]))
            {
                // When shift down, and not negative so no overflow from end
                //i.e. with offset F (value 5) then H to C, index 7 to 2
                if ((hashReferenceTable.get(plainTextCharArray [i]) - offsetValue) >= 0)
                {
                    //Get encoded value by subtracting offset value from original index (hashtable value) 
                    int encodedValue = hashReferenceTable.get(plainTextCharArray [i]) - offsetValue;
                    //Get encoded letter by using reverse hashtable with key = encodedValue
                    char encodedKey = reversehashtable.get(encodedValue);

                    //Add to letter to arraylist holding encoded text
                    encodedText.add(encodedKey);

                    //System.out.println("The key for value " + encodedValue + " is " + encodedKey);
                }
                //When shift down and negative, hence there is overflow
                //i.e. with offset F (value 5) then E to /, index 4 to 43. Where 4 - 5 = -1
                else if ((hashReferenceTable.get(plainTextCharArray [i]) - offsetValue) < 0)
                {
                    //get encoded value by subtracting offset value from original index (hashtable value) then adding reference table length (44) to get encoded val
                    // 4 - 5 + 44 = 43 = '/'
                    int encodedValue = hashReferenceTable.get(plainTextCharArray [i]) - offsetValue + referenceTable.length;
                    //Get encoded letter by using reverse hashtable with key = encodedValue
                    char encodedKey = reversehashtable.get(encodedValue);

                    //Add to letter to arraylist holding encoded text
                    encodedText.add(encodedKey);

                    //System.out.println("The key for value " + encodedValue + " is " + encodedKey);
                }
            }
            // If char not in reference Table map back to original char
            //Takes care of spacing as well as ' ' is not in reference table
            else
            {
                encodedText.add(plainTextCharArray [i]);
            }

        }
        
        //Convert Arraylist of Characters to string using StringBuilder
        StringBuilder builder = new StringBuilder(encodedText.size());
        for(Character ch: encodedText)
        {
            builder.append(ch);
        }
        
        //System.out.println(builder.toString()); 

        //Return encoded word as String
        return builder.toString();

   }

   public String decode (String encodedText)
   {
        //if encodedText is not all UpperCase and application requires all lowercase to be decoded based on uppercase counterpart
        //String allCaps = encodedText.toUpperCase();
        //char [] encodedTextCharArray = allCaps.toCharArray();

        //Convert String to char array for comparison
        char [] encodedTextCharArray = encodedText.toCharArray();

        //Take first char of encodedText as offset value
        int offsetValue = hashReferenceTable.get(encodedTextCharArray [0]);
        //Get character from value using reversehashtable
        //char offsetKey = reversehashtable.get(offsetValue);
        //System.out.println("The offset key for value " + offsetValue + " is " + offsetKey);

        //loop from 1 to avoid offsetKey
        for (int i = 1; i < encodedTextCharArray.length; i++) 
        {
            //If character is found in hashtable (i.e. in the reference table)
            if (hashReferenceTable.containsKey(encodedTextCharArray [i]))
            {
                // When original value of encoded character + offset >= 44 (length of ref table) so overflow
                if ((hashReferenceTable.get(encodedTextCharArray [i]) + offsetValue) >= referenceTable.length)
                {
                    //Absolute of Encoded value + offset value - length of table = value of original value
                    int decodedValue = Math.abs (hashReferenceTable.get(encodedTextCharArray [i]) + offsetValue - referenceTable.length);
                    //Get decoded letter by using reverse hashtable with key = encodedValue
                    char decodedKey = reversehashtable.get(decodedValue);

                    //Add to letter to arraylist holding decoded text
                    decodedText.add(decodedKey);

                    //System.out.println("The key for value " + encodedValue + " is " + encodedKey);
                }
                // When original value of encoded char + offset < (length of ref table)
                else if ((hashReferenceTable.get(encodedTextCharArray [i]) + offsetValue) < referenceTable.length)
                {
                    // value of original = encoded + offset
                    int decodedValue = hashReferenceTable.get(encodedTextCharArray [i]) + offsetValue;
                    //Get decoded letter by using reverse hashtable with key = encodedValue
                    char decodedKey = reversehashtable.get(decodedValue);

                    //Add to letter to arraylist holding decoded text
                    decodedText.add(decodedKey);

                    //System.out.println("The key for value " + encodedValue + " is " + encodedKey);
                }
            }
            // If char not in reference Table map back to original char
            // Takes care of spacing as well as ' ' is not in reference table
            else
            {
                decodedText.add(encodedTextCharArray [i]);
            }
        }

        //Convert Arraylist of Characters to string using StringBuilder
        StringBuilder builder = new StringBuilder(decodedText.size());
        for(Character ch: decodedText)
        {
            builder.append(ch);
        }
        
        //System.out.println(builder.toString()); 

        //Return encoded word as String
        return builder.toString();
    }
}

class KyAssessment
{
    public static void main (String [] args)
    {
        //Call default constructor
        //Encoder e = new Encoder();

        //call created constructor parsing in Char
        Encoder e = new Encoder('F');

        //Test encoder
        String encodedWord = e.encode ("HELLO WORLD = !4");
        //String encodedWord = e.encode ("ABCDEF");
        System.out.printf("Encoded Word: %s\n", encodedWord);

        //Test decoder
        String decodedWord = e.decode(encodedWord);
        System.out.printf("Decoded Word: %s\n", decodedWord);
    }
}