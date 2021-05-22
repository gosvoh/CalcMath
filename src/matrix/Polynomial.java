package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Polynomial {
    Node first;
    Node second;

    Polynomial(final String path) throws FileNotFoundException {
        readFromFile(path);
    }

    public Polynomial(Node first, Node second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Вывести указанный полином на экран.
     *
     * @param node полином для вывода
     */
    static void print(Node node) {
        while (node != null) {
            System.out.print(node.value + " ");
            node = node.next;
        }
        System.out.println();
    }

    /**
     * Метод сложения полиномов.
     *
     * @param first  первый слагаемый полином
     * @param second второй слогаемый полином
     *
     * @return результат сложения полиномов в виде ещё одного полинома
     */
    static Node add(Node first, Node second) {
        Node result = null, working = null;
        while (first != null || second != null) {
            boolean pickfirst = false;
            boolean haveBoth = (first != null && second != null);
            Node tmpNode;

            if (haveBoth && first.power == second.power) {
                tmpNode = new Node(first.value + second.value, first.power);
                first = first.next;
                second = second.next;
            } else {
                pickfirst = first != null && (second == null || first.power > second.power);

                if (pickfirst) {
                    tmpNode = new Node(first.value, first.power);
                    first = first.next;
                } else {
                    tmpNode = new Node(second.value, second.power);
                    second = second.next;
                }
            }

            if (working == null) {
                result = tmpNode;
                working = result;
            } else {
                working.next = tmpNode;
                working = tmpNode;
            }
        }

        return result;
    }

    /**
     * Метод умножения полиномов.
     *
     * @param first  первый умножаемый полином
     * @param second второй умножаемый полином
     *
     * @return результат умножения полиномов в виде ещё одного полинома
     */
    static Node mul(Node first, Node second) {
        Node tmpNode = new Node();
        Node result = tmpNode;

        while (first != null) {
            while (second != null) {
                double newValue = first.value * second.value;
                int newPower = first.power + second.power;
                Position pos = findPosition(result, newPower);
                //TODO Придумать как реализовать, сейчас соображалка не работает

                second = second.next;
            }

            first = first.next;
        }

        return result;
    }

    /**
     * Метод умножения полинома на число.
     *
     * @param value   число, на которое нужно умножить полином
     * @param polinom умножаемый полином
     *
     * @return результат умножения полинома и числа в виде ещё одного полинома
     */
    static Node mul(double value, Node polinom) {
        return mul(new Node(value, 0), polinom);
    }

    static Position findPosition(Node polinom, int power) {
        Node before = null, current = null;
        while (polinom != null && polinom.next != null && polinom.next.power >= power) {
            if (polinom.next.power == power) {
                before = polinom;
                current = polinom.next;
                break;
            } else before = polinom;
            polinom = polinom.next;
        }

        return new Position(before, current);
    }

    Node findPower(int power) {
        Node node = first;
        if (node.power == power) return node;
        if (node.next != null) node = node.next;
        else return null;
        return findPower(power);
    }

    /**
     * Прочитать полиномы из файла
     *
     * @param path путь до файла
     *
     * @throws FileNotFoundException если невозможно прочитать файл
     */
    private void readFromFile(final String path) throws FileNotFoundException {
        File inFile = new File(path);
        Scanner scanner = new Scanner(inFile);
        Pattern pattern = Pattern.compile("[ \t]+");
        first = readPolynomial(pattern.split(scanner.nextLine()));
        second = readPolynomial(pattern.split(scanner.nextLine()));
    }

    /**
     * Прочитать полином из файла и занести в объект класса {@link Node}.
     *
     * @param strings полином в виде строки
     *
     * @return заполненный полином в виде связного списка
     */
    private Node readPolynomial(String[] strings) {
        Node head = new Node();
        Node node = head;
        for (int i = strings.length - 1, j = 0; i >= 0 && j < strings.length; i--, j++) {
            head.value = Double.parseDouble(strings[j].replace(',', '.'));
            head.power = i;
            head.next = new Node();
            Node old = head;
            head = head.next;
            head.prev = old;
        }
        head = head.prev;
        head.next = null;
        return node;
    }

    static class Position {
        Node before;
        Node current;

        public Position(Node before, Node current) {
            this.before = before;
            this.current = current;
        }
    }

    static class Node {
        double value;
        int    power;
        Node   next;
        Node   prev;

        Node() {
        }

        Node(double value, int power) {
            this.value = value;
            this.power = power;
        }
    }
}
