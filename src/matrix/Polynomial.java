package matrix;

public class Polynomial {
    private static class Node {
        double values;
        int power;

        Node() {
        }

        Node(double value, int power) {
            this.values = values;
            this.power = power;
        }

        Node next;
    }

    Node head;

    Polynomial() {
        head = new Node();
    }

    Node findPower(int power) {
        Node node = head;
        if (node.power == power) return node;
        if (node.next != null) node = node.next;
        else return null;
        return findPower(power);
    }

    void add(Node polinom1, Node polinom2) {

    }

    void mul(Node polinom1, Node polinom2) {
        if (polinom1 == null || polinom2 == null) return;
        do {

        } while (polinom1.next != null);
    }

    void mul(double value, Node polinom2) {
        mul(new Node(value, 0), head);
    }
}
