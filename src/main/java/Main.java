import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final String SYMBOLS = "abc";
    public static final char SYMB_1 = 'a';
    public static final char SYMB_2 = 'b';
    public static final char SYMB_3 = 'c';
    public static final int NUMBER_OF_TEXTS = 10_000;
    public static final int TEXT_LENGTH = 100_000;
    public static final int QUEUE_MAX_CAPACITY = 100;
    public static final BlockingQueue<String> QUEUE_FOR_SYMB1 = new ArrayBlockingQueue<>(QUEUE_MAX_CAPACITY);
    public static final BlockingQueue<String> QUEUE_FOR_SYMB2 = new ArrayBlockingQueue<>(QUEUE_MAX_CAPACITY);
    public static final BlockingQueue<String> QUEUE_FOR_SYMB3 = new ArrayBlockingQueue<>(QUEUE_MAX_CAPACITY);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_TEXTS; i++) {
                fillQueues(generateText(SYMBOLS, TEXT_LENGTH));
            }
        }).start();

        new Thread(() -> {
            showTextWithLongestSymb(QUEUE_FOR_SYMB1, SYMB_1);
        }).start();

        new Thread(() -> {
            showTextWithLongestSymb(QUEUE_FOR_SYMB2, SYMB_2);
        }).start();

        new Thread(() -> {
            showTextWithLongestSymb(QUEUE_FOR_SYMB3, SYMB_3);
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void fillQueues(String text) {
        try {
            QUEUE_FOR_SYMB1.put(text);
            QUEUE_FOR_SYMB2.put(text);
            QUEUE_FOR_SYMB3.put(text);
        } catch (InterruptedException ignored) {
        }
    }

    public static void showTextWithLongestSymb(BlockingQueue<String> queue, char c) {
        String textWithLongestSymb = "";
        int maxNumberOfSymb = 0;

        for (int i = 0; i < NUMBER_OF_TEXTS; i++) {
            try {
                String currentText = queue.take();
                int currentNumberOfSymb = (int) currentText.chars().filter(ch -> ch == c).count();

                if (currentNumberOfSymb > maxNumberOfSymb) {
                    maxNumberOfSymb = currentNumberOfSymb;
                    textWithLongestSymb = currentText;
                }
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("Строка, имеющая самое большое количество символов \'" +
                c + "\' (" + maxNumberOfSymb + " шт.), выглядит так: \n" +
                textWithLongestSymb.substring(0, 100) + "...");
    }
}
