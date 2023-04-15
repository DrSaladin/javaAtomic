import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  static AtomicInteger coolNikcnamesSmallSizeNumber = new AtomicInteger();
  static AtomicInteger coolNikcnamesMediumSizeNumber = new AtomicInteger();
  static AtomicInteger coolNikcnamesLargeSizeNumber = new AtomicInteger();
  static int smallWordSize = 3;
  static int mediumWordSize = 4;
  static int largeWordSize = 5;


  public static void main(String[] args) throws InterruptedException {
    Random random = new Random();
    String[] texts = new String[100_000];
    List<Thread> processingThreads = new ArrayList<>();


    for (int i = 0; i < texts.length; i++) {
      texts[i] = generateText("abc", 3 + random.nextInt(3));
    }

    Runnable countPalindromeNames = () -> {
      for (String text : texts) {
        isWordPalindrome(text);
      }
    };

    Runnable countWordSameLettersNumber = () -> {
      for (String text : texts) {
        isWordSameLetters(text);
      }
    };

    Runnable countCoolestNumber = () -> {
      for (String text : texts) {
        isWordOnAlphabeticalOrder(text);
      }
    };

    Thread countCoolPalindromNamesThread = new Thread(countPalindromeNames);
    Thread countCoolSameLettersNamesNumberThread = new Thread(countWordSameLettersNumber);
    Thread countCoolestNumberThread = new Thread(countCoolestNumber);

    countCoolPalindromNamesThread.start();
    countCoolSameLettersNamesNumberThread.start();
    countCoolestNumberThread.start();

    processingThreads.add(countCoolPalindromNamesThread);
    processingThreads.add(countCoolSameLettersNamesNumberThread);
    processingThreads.add(countCoolestNumberThread);

    for (Thread thread : processingThreads) {
      thread.join();
    }

    renderResults(coolNikcnamesSmallSizeNumber, coolNikcnamesMediumSizeNumber, coolNikcnamesLargeSizeNumber);
  }

  public static void renderResults(AtomicInteger palindromNumber, AtomicInteger sameLettersNumber, AtomicInteger coolestNameNumber) {
    System.out.printf("""
      Красивых слов с длиной 3: %d шт
      Красивых слов с длиной 4: %d шт
      Красивых слов с длиной 5: %d шт
    """,
      palindromNumber.get(),
      sameLettersNumber.get(),
      coolestNameNumber.get()
    );
  }

  public static void isWordOnAlphabeticalOrder(String word) {
    for (int i = 1; i < word.length(); i++) {
      if (word.charAt(i) < word.charAt(i - 1)) {
        return;
      }
    }
    countersUpdater(word, coolNikcnamesSmallSizeNumber, coolNikcnamesMediumSizeNumber, coolNikcnamesLargeSizeNumber);
  }

  public static void isWordSameLetters(String word) {
    int wordLengthCounter = 1;
    char firstLetter = word.charAt(0);

    while (wordLengthCounter == word.length()) {
      if (word.charAt(wordLengthCounter) != firstLetter) {
        return;
      }
    }

    countersUpdater(word, coolNikcnamesSmallSizeNumber, coolNikcnamesMediumSizeNumber, coolNikcnamesLargeSizeNumber);
  }

  public static void isWordPalindrome(String word) {
    int left = 0;
    int right = word.length() - 1;

    while (left < right) {
      if (word.charAt(left) != word.charAt(right)) {
        return;
      }
      left++;
      right--;
    }
    countersUpdater(word, coolNikcnamesSmallSizeNumber, coolNikcnamesMediumSizeNumber, coolNikcnamesLargeSizeNumber);
  }

  public static void countersUpdater(String word, AtomicInteger palindromNumber, AtomicInteger sameLettersNumber, AtomicInteger coolestNamesNumber) {
    if(word.length() == smallWordSize) {
      palindromNumber.set(palindromNumber.get() + 1);
    } else if (word.length() == mediumWordSize) {
      sameLettersNumber.set(sameLettersNumber.get() + 1);
    } else if (word.length() == largeWordSize) {
      coolestNamesNumber.set(coolestNamesNumber.get() + 1);
    }
  }

  public static String generateText(String letters, int length) {
    Random random = new Random();
    StringBuilder text = new StringBuilder();
    for (int i = 0; i < length; i++) {
      text.append(letters.charAt(random.nextInt(letters.length())));
    }
    return text.toString();
  }
}
