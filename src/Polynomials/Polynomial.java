package Polynomials;

import utils.MatrixUtils;

public class Polynomial {
    /** Точность для решения полинома, чтобы не делать лишнюю работу. */
    public static final double quality = 0.0000000000000000000000001f;
    /** Начало полинома (старшая степень). */
    private             Node   head;

    //region Конструкторы

    /** Стандартный конструктор, задаём голову как null. */
    public Polynomial() {
        head = null;
    }

    /**
     * Конструктор класса, где задаётся начальный моном.
     *
     * @param value Коэффициент при X.
     * @param power Степень X
     */
    public Polynomial(double value, int power) {
        head = new Node(value, power);
    }

    /**
     * Конструктор класса с копированием другого полинома. Реализуется через сумму двух полиномов.
     *
     * @param polynomial Полином для копирования.
     */
    public Polynomial(Polynomial polynomial) {
        this.sum(polynomial);
    }

    //endregion

    //region Базовые математические операции над полиномами

    /**
     * Метод сложения полиномов.
     *
     * @param polynomial Слагаемый полином
     */
    public void sum(Polynomial polynomial) {
        Node thisNode = head, otherNode = polynomial.head;
        Node result = null, working = null;
        while (thisNode != null || otherNode != null) {
            boolean pickfirst = false;
            boolean haveBoth = (thisNode != null && otherNode != null);
            Node tmpNode;

            if (haveBoth && thisNode.power == otherNode.power) {
                tmpNode = new Node(thisNode.value + otherNode.value, thisNode.power);
                thisNode = thisNode.next;
                otherNode = otherNode.next;
            } else {
                pickfirst = thisNode != null && (otherNode == null || thisNode.power > otherNode.power);

                if (pickfirst) {
                    tmpNode = new Node(thisNode.value, thisNode.power);
                    thisNode = thisNode.next;
                } else {
                    tmpNode = new Node(otherNode.value, otherNode.power);
                    otherNode = otherNode.next;
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

        head = result;
    }

    /**
     * Метод умножения полиномов.
     *
     * @param polynomial Умножаемый полином.
     */
    public void mul(Polynomial polynomial) {
        Node thisNode = head, otherNode = polynomial.head;
        Node result = null, working = null;

        while (thisNode != null) {
            Node secondWorking = otherNode;
            while (secondWorking != null) {
                Node tmpNode = new Node(thisNode.value * secondWorking.value, thisNode.power + secondWorking.power);

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

            thisNode = thisNode.next;
        }

        head = result;
    }

    /**
     * Метод умножения полинома на число.
     *
     * @param value Число, на которое нужно умножить полином
     */
    public void mul(double value) {
        if (MatrixUtils.isZero(value, quality)) head = null;
        Node node = head;
        while (node != null) {
            node.value *= value;
            node = node.next;
        }
    }

    //endregion

    //region Утилитарные методы

    /**
     * Вычисление значения полинома в точке через схему Горнера.
     *
     * @param value Точка для вычисления.
     *
     * @return Значение полинома в точке.
     */
    double horner(double value) {
        if (head == null) return 0;

        Node node = head;
        double sum = 0;
        int last_rank = node.power, i;

        for (i = last_rank; i > 0 && node != null; i--) {
            if (node.power == i) {
                sum += node.value;
                node = node.next;
            }
            sum *= value;
        }

        sum += (node == null ? 0 : node.value);

        for (; i > 0; i++) {
            sum *= value;
        }

        if (MatrixUtils.isZero(sum, quality)) sum = 0;

        return sum;
    }

    /**
     * Изменить значение свободного члена в полиноме из двух мономов.
     *
     * @param value Новое значение свободного члена.
     */
    void changeX(double value) {
        if (MatrixUtils.isZero(value, quality)) head.next = null;
        else if (head.next != null) head.next.value = value;
        else head.next = new Node(value, 0);
    }

    /** Вывод полинома на экран. */
    void print() {
        if (head == null) return;

        Node node = head, next = head.next;

        while (next != null) {
            if (Math.abs(next.value) < 0) {
                node.next = next.next;
                if (node.next == null) break;
            }
            next = next.next;
            node.print();
            node = node.next;
            System.out.print(node.value < 0 ? " - " : " + ");
        }
        node.print();
        System.out.println();
    }

    /**
     * @return True если полином пуст, false в противном случае.
     */
    boolean isEmpty() {
        return head == null;
    }

    //endregion

    /** Класс, представляющий моном. */
    private static class Node {
        /** Коэффициент при X. */
        double value;
        /** Степень X */
        int    power;
        /** Ссылка на следующий моном. */
        Node   next;

        Node(double value, int power) {
            this.value = value;
            this.power = power;
            this.next = null;
        }

        void print() {
            System.out.printf("%.6Ex^%d", Math.abs(value), power);
        }
    }
}