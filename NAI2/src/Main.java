import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        double a = Double.parseDouble(args[0]);
        List<List<String>> treningData = ReadDataFromFile(args[1]);
        List<List<String>> testData = ReadDataFromFile(args[2]);
        int dimension = treningData.get(0).size() - 1;

        String kl1 = treningData.get(0).get(dimension);
        String kl2 = kl1;
        int counter = 1;
        while (kl1.equals(kl2)) {
            kl2 = treningData.get(counter++).get(dimension);
            if (counter == treningData.size()) {
                System.out.println("There is only one class (type)");
                return;
            }
        }

        double t = Math.random() * 10 - 5;
        List<Double> w = new ArrayList<>();
        for (int i = 0; i < dimension; i++)
            w.add(Math.random() * 10 - 5);

        for (List<String> ele : treningData) {
            boolean right = isRight(ele, w, t);
            int y = getY(right);
            int d = getD(kl1, ele);
            if (d - y != 0) {
                w = traningW(w, ele, a, d, y);
                t = traningT(t, a, y, d);
            }
        }

        int correct = 0;
        for (List<String> ele : testData) {
            boolean right = isRight(ele, w, t);
            int y = getY(right);
            int d = getD(kl1, ele);
            if (d == y) correct++;
        }

        double accuracy = (double) (correct) / testData.size() * 100;
        System.out.println("accuracy = " + BigDecimal.valueOf(accuracy).setScale(2, RoundingMode.HALF_UP) + '%');

        System.out.println("Now u can pass your own values(separated by semicolon)");
        System.out.println("Number of attributes: " + dimension);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a vector: ");
            String line = scanner.next();
            line = line.replace(',', '.');
            List<String> readList = new ArrayList<>(Arrays.asList(line.split(";")));
            String klOdp;
            if (getY(isRight(readList, w, t)) == 0) klOdp = kl1;
            else klOdp = kl2;
            System.out.println("Most likely it is: " + klOdp);
        }
    }

    public static List<List<String>> ReadDataFromFile(String loc) throws FileNotFoundException {
        File trainignFile = new File(loc);
        Scanner scanner = new Scanner(trainignFile);
        List<List<String>> lists = new ArrayList<>();
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            String[] tmpTab = line.split(";");
            lists.add(new ArrayList<>(Arrays.asList(tmpTab)));
        }
        return lists;
    }

    public static boolean isRight(List<String> data, List<Double> w, double t) {
        double scalar = scalarProduct(data, w);
        return scalar - t >= 0;

    }

    public static double scalarProduct(List<String> p, List<Double> w) {
        double suma = 0;
        for (int i = 0; i < w.size(); i++) {
            suma += Double.parseDouble(p.get(i)) * w.get(i);
        }
        return suma;
    }

    public static List<Double> traningW(List<Double> w, List<String> p, double a, int d, int y) {
        for (int i = 0; i < w.size(); i++) {
            w.set(i, w.get(i) + (d - y) * a * Double.parseDouble(p.get(i)));
        }
        return w;
    }

    public static double traningT(double t, double a, int y, int d) {
        return t - (d - y) * a;
    }

    public static int getY(boolean right) {
        if (right) return 1;
        return 0;
    }

    public static int getD(String kl, List<String> p) {
        if (p.get(p.size() - 1).equals(kl)) return 0;
        return 1;
    }
}
