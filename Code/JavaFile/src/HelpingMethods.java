import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fazecast.jSerialComm.SerialPort;

public class HelpingMethods {

    public static byte[] extract(int count, String input) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(input);
        byte[] numbers = new byte[count];
        int index = 0;
        while (matcher.find()) {
            int b = Integer.parseInt(matcher.group());
            numbers[index++] = (byte) b;
        }
        return numbers;
    }

    public static String addToString(String pattern, int number, int location) {
        char numChar = Character.forDigit(number, 10);
        int index = 7 - location;
        if (index < 0 || index >= pattern.length()) {
            return null;
        }
        char currentChar = pattern.charAt(index);
        if (currentChar == 'x' || currentChar == numChar) {
            char[] updatedPatternArray = pattern.toCharArray();
            updatedPatternArray[index] = numChar;
            return new String(updatedPatternArray);
        } else {
            return null;
        }
    }

    public static String combinePatterns(String pattern1, String pattern2) {
        if (pattern1.length() != pattern2.length()) {
            return null;
        }
        StringBuilder combinedPattern = new StringBuilder();
        for (int i = 0; i < pattern1.length(); i++) {
            char char1 = pattern1.charAt(i);
            char char2 = pattern2.charAt(i);
            if (char1 == 'x') {
                combinedPattern.append(char2);
            } else if (char2 == 'x') {
                combinedPattern.append(char1);
            } else if (char1 == char2) {
                combinedPattern.append(char1);
            } else {
                return null;
            }
        }
        return combinedPattern.toString();
    }


    public static List<Integer> generateNumbers(String pattern) {
        List<String> s = new ArrayList<>();
        List<Integer> results = new ArrayList<>();
        generateNumbersHelper(pattern, 0, "", s);
        for (String string : s) {
            results.add(binaryToDecimal(string));

        }
        return results;
    }
    private static void generateNumbersHelper(String pattern, int index, String current, List<String> results) {
        if (index == pattern.length()) {
            results.add(current);
            return;
        }
        if (pattern.charAt(index) == 'x') {
            generateNumbersHelper(pattern, index + 1, current + '0', results);
            generateNumbersHelper(pattern, index + 1, current + '1', results);
        } else {
            generateNumbersHelper(pattern, index + 1, current + pattern.charAt(index), results);
        }
    }

    public static int binaryToDecimal(String binary) {
        int decimal = 0;
        int length = binary.length();
        for (int i = 0; i < length; i++) {
            if (binary.charAt(length - 1 - i) == '1') {
                decimal += Math.pow(2, i);
            }
        }
        return decimal;
    }

    public static void javaToArduino (){
        byte[][] finalTable = Table.getFinalTable();
        StringBuilder result = new StringBuilder();

        for (byte[] row : finalTable) {
            for (byte num : row) {
                result.append(num);
            }
        }

        String finalString = result.toString();
        SerialPort port = SerialPort.getCommPort("COM5");
        port.setBaudRate(9600);

        if (port.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Failed to open the port.");
            return;
        }

        try {
            System.out.println("Are you sure you have completed the program?");
            Scanner scanner = new Scanner(System.in);
            String checkFinish = scanner.nextLine();
            if (checkFinish.equals("yes")){
                System.out.println("finished.");
            } else {
                System.out.println("we don't care!");
            }
            port.getOutputStream().write(finalString.getBytes());
            port.getOutputStream().flush();
//            System.out.println("Array sent successfully!");
//            System.out.println("Final String: " + finalString);
        } catch (Exception e) {
            System.out.println("Error sending data: " + e.getMessage());
        } finally {
            port.closePort();
        }

    }

}
