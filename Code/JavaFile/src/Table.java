import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table {
    private static int[] x = new int[8];
    private static int[] y = new int[4];
    private static ArrayList<String>[] cBe0 = new ArrayList[9];
    private static ArrayList<String>[] cBe1 = new ArrayList[9];
    private static byte[][] LEDs = new byte[256][4];

    static {
        for (int i = 0; i < cBe0.length; i++) {
            cBe0[i] = new ArrayList<>();
        }
    }

    static {
        for (int i = 0; i < cBe1.length; i++) {
            cBe1[i] = new ArrayList<>();
        }
    }

    static {
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 4; j++) {
                LEDs[i][j] = 2;
            }
        }
    }

    public static void reset() {
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 4; j++) {
                LEDs[i][j] = 2;
            }
        }
    }

    public static void handler(int numOfInputs, int numOfOutputs, String str) {
        String directInputs = "xxxxxxxx";
        byte[] inputs = HelpingMethods.extract(numOfInputs + numOfOutputs, str);
        for (int i = 0; i < numOfInputs; i++) {
            if (x[i] >= 0) {
                directInputs= HelpingMethods.addToString(directInputs, inputs[i], x[i]);
            }
        }
        ArrayList<String> all = new ArrayList<>();
        all.add(directInputs);
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < numOfInputs; i++) {
            if (x[i] < 0) {
                if (inputs[i] == 1) {
                    int a = x[i] * -1;
                    for (String c : cBe1[a]) {
                        for (String one : all) {
                            if(one != null) {
                                String t = HelpingMethods.combinePatterns(c, one);
                                if(t != null) {
                                    temp.add(t);
                                }
                            }
                        }
                    }
                    all.clear();
                    all.addAll(temp);
                    temp.clear();
                }
                if (inputs[i] == 0) {
                    int a = x[i] * -1;
                    for (String c : cBe0[a]) {
                        for (String one : all) {
                            if(one != null) {
                                String t = HelpingMethods.combinePatterns(c, one);
                                if(t != null) {
                                    temp.add(t);
                                }
                            }
                        }
                    }
                    all.clear();
                    all.addAll(temp);
                    temp.clear();
                }

            }
        }
        for (int i = 0; i < numOfOutputs; i++) {
            if (y[i] >= 0) {
                byte a = inputs[i + numOfInputs];
                for (String one : all) {
                    if(one != null) {
                        List<Integer> generateNums = HelpingMethods.generateNumbers(one);

                        for (Integer num : generateNums) {
                            LEDs[num][y[i]] = a;
                        }
                    }

                }
            }
            if (y[i] < 0) {
                if(inputs[i+numOfInputs] == 1) {
                    int a = y[i] * -1;
                    for (String one : all) {
                        cBe1[a].add(one);
                    }
                }

                if(inputs[i+numOfInputs] == 0) {
                    int a = y[i] * -1;
                    for (String one : all) {
                        cBe0[a].add(one);
                    }
                }
            }
        }
    }


    public static void setX(int i, int num) {
        Table.x[i] = num;
    }


    public static void setY(int i, int num) {
        Table.y[i] = num;
    }

    public static byte[][] getFinalTable() {
        return Table.LEDs;
    }


}
