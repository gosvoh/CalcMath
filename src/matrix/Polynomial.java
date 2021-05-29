package matrix;

import java.util.regex.Pattern;

public class Polynomial {
    Node polynomial;

    private Polynomial(Node polynomial) {
        this.polynomial = polynomial;
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
     * Метод вычитания двух полиномов.
     *
     * @param first  полином, из которого вычитаем
     * @param second вычитаемый полином
     *
     * @return результат вычитания полиномов в виде ещё одного полинома
     */
    static Node sub(Node first, Node second) {
        return add(first, mul(-1, second));
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
        Node result = null, working = null;

        while (first != null) {
            Node secondWorking = second;
            while (secondWorking != null) {
                Node tmpNode = new Node(first.value * secondWorking.value, first.power + secondWorking.power);

                if (working == null) {
                    result = tmpNode;
                    working = result;
                } else {
                    if (tmpNode.power > working.power) {
                        Node anotherWorking = result;
                        while (anotherWorking.next.power > tmpNode.power) anotherWorking = anotherWorking.next;
                        if (tmpNode.power == anotherWorking.next.power) anotherWorking.next.value += tmpNode.value;
                        else {
                            tmpNode.next = anotherWorking.next;
                            anotherWorking.next = tmpNode;
                        }
                    }
                    if (tmpNode.power == working.power) {
                        working.value += tmpNode.value;
                    }
                    if (tmpNode.power < working.power) {
                        working.next = tmpNode;
                        working = tmpNode;
                    }
                }

                secondWorking = secondWorking.next;
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

    /**
     * Прочитать полином из файла и занести в объект класса {@link Node}.
     *
     * @param polynomial полином в виде строки
     *
     * @return заполненный полином в виде связного списка
     */
    public static Polynomial readPolynomial(String polynomial) {
        Pattern pattern = Pattern.compile("[ \t]+");
        String[] values = pattern.split(polynomial);
        Node result = null, working = null;
        for (int i = values.length - 1, j = 0; i >= 0 && j < values.length; i--, j++) {
            Node tmpNode = new Node(Double.parseDouble(values[j].replace(',', '.')), i);
            if (working == null) {
                result = tmpNode;
                working = result;
            } else {
                working.next = tmpNode;
                working = tmpNode;
            }
        }
        return new Polynomial(result);
    }

    /**
     * Добавить мономер к полиному.
     *
     * @param value значение мономера
     * @param power степень мономера
     *
     * @return изменённый полином
     */
    public Polynomial addMonomer(double value, int power) {
        polynomial = add(polynomial, new Node(value, power));

        return this;
    }

    static class Node {
        double value;
        int    power;
        Node   next;

        Node() {
        }

        Node(double value, int power) {
            this.value = value;
            this.power = power;
        }
    }
}
