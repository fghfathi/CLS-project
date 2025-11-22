//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import javax.swing.plaf.IconUIResource;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TableMenu {
    public static void run(Scanner scanner) {
        boolean tableFlag = false;
        boolean inputFlag = false;
        boolean lineError = false;
        String command = "";
        Matcher matcher;
        Matcher matcher1;
        while (true) {
            System.out.print("> ");
            command = scanner.nextLine();
            if ((matcher = TableMenu.getCommandMatcher(command, "exit")).matches()) {
                break;
            }
            if ((matcher = TableMenu.getCommandMatcher(command, "finish")).matches()) {
                if (!tableFlag) {
                    System.out.println("The previous circuit has already finished");
                    continue;
                }
//                System.out.println("**finished**");
                inputFlag = true;
                tableFlag = false;
                HelpingMethods.javaToArduino();
                continue;
            }
            if ((matcher = TableMenu.getCommandMatcher(command, "create table with (?<numOfInputs>\\d) inputs and (?<numOfOutputs>\\d) outputs")).matches()) {
                if (!tableFlag) {
                    System.out.println("First you need to create a new circuit, use the \"new circuit\" command");
                    continue;
                }
                System.out.println("creating new table...");
                System.out.println("please enter the name of inputs and outputs");
                command = scanner.nextLine();
                if ((matcher1 = TableMenu.getCommandMatcher(command, "((in[0-7]|c[1-8]|led[0-3]| ) ?)+")).matches()) {
                    createTable(matcher, matcher1, command);
                    int numOfInputs = Integer.parseInt(matcher.group("numOfInputs"));
                    int numOfOutputs = Integer.parseInt(matcher.group("numOfOutputs"));
                    int row = (int) Math.pow(2, Integer.parseInt(matcher.group("numOfInputs")));
                    System.out.println("Please enter the table elements");
                    the:
                    for (int i = 0; i < row; i++) {
                        String str = scanner.nextLine();
                        String[] array = str.split("\\s");
                        int tedad = array.length;
                        for (String ele : array) {
                            if (!ele.equals("0") && !ele.equals("1")) {
                                System.out.println("just ZERO or ONE!!");
                                i--;
                                continue the;
                            }
                        }
                        if (tedad == numOfInputs + numOfOutputs) {
                            Table.handler(numOfInputs, numOfOutputs, str);
                        } else {
                            System.out.println("What are you doing bro??!");
                            i--;
                        }
                    }
                    System.out.println("**This table has been saved**");
                } else {
                    System.out.println("The input and output format is not correct!");
                    continue;
                }
                continue;
            }
            if ((matcher = TableMenu.getCommandMatcher(command, "input (?<input>\\d+)")).matches()) {
                if (tableFlag || !inputFlag) {
                    System.out.println("To enter the input, you need to finish importing the tables, you can use the \"finish\" command");
                    continue;
                }
                checkInput(matcher);
                continue;
            }
            if ((matcher = TableMenu.getCommandMatcher(command, "new circuit")).matches()) {
                if (tableFlag) {
                    System.out.println("You still haven't finished the previous circuit tables, you can use the \"finish\" command");
                    continue;
                }
                tableFlag = true;
                inputFlag = false;
                System.out.println("**New circuit was built**");
                Table.reset();
                continue;
            }
            System.out.println("invalid command");
        }

    }

    private static void checkInput(Matcher matcher) {
        int index = Integer.parseInt(matcher.group("input"), 2);
        byte[][] finalTable = Table.getFinalTable();
        if(index>255 || index<0) {
            System.out.println("invalid input");
            return;
        }
        byte[] led = finalTable[index];
        int counter = 0;
        for (byte leds : led) {
            if (leds == 0) {
                System.out.println("LED" + counter + " = off");
            } else if (leds == 1) {
                System.out.println("LED" + counter + " = on");
            } else if (leds == 2) {
                System.out.println("LED" + counter + " = dont care");
            }
            counter ++;
        }
    }

    private static Matcher getCommandMatcher(String input, String regex) {
        return Pattern.compile(regex).matcher(input);
    }



    private static void createTable(Matcher matcher, Matcher matcher1, String str) {
        int numOfInputs = Integer.parseInt(matcher.group("numOfInputs"));
        int numOfOutputs = Integer.parseInt(matcher.group("numOfOutputs"));
        int inputInCommand = 0;
        int outputInCommand = 0;
        String[] elements = str.trim().split("\\s+");
        for (String element : elements) {
            if (element.matches("in[0-7]")) {
                Matcher mat = TableMenu.getCommandMatcher(element, "in(?<inputIndex>[0-7])");
                mat.find();
                int inputIndex = Integer.parseInt(mat.group("inputIndex"));
                Table.setX(inputInCommand, inputIndex);
                inputInCommand ++;
            } else if (element.matches("c[1-8]")) {
                if (inputInCommand < numOfInputs) {
                    Matcher mat = TableMenu.getCommandMatcher(element, "c(?<cIndex>[1-8])");
                    mat.find();
                    int cIndex = Integer.parseInt(mat.group("cIndex"));
                    cIndex = -cIndex;
                    Table.setX(inputInCommand, cIndex);
                    inputInCommand ++;
                } else if (outputInCommand < numOfOutputs) {
                    Matcher mat = TableMenu.getCommandMatcher(element, "c(?<cIndex>[1-8])");
                    mat.find();
                    int cIndex = Integer.parseInt(mat.group("cIndex"));
                    cIndex = -cIndex;
                    Table.setY(outputInCommand, cIndex);
                    outputInCommand ++;
                }
            } else if (element.matches("led[0-3]")) {
                Matcher mat = TableMenu.getCommandMatcher(element, "led(?<ledIndex>[0-3])");
                mat.find();
                int ledIndex = Integer.parseInt(mat.group("ledIndex"));
                Table.setY(outputInCommand, ledIndex);
                outputInCommand ++;
            }
        }

    }
}