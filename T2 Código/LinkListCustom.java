// LinkListCustom.java
import java.math.BigInteger;

public class LinkListCustom {
    private class Node {
        public String nome;
        public BigInteger valor;
        public Node next;

        public Node(String nome, BigInteger valor) {
            this.nome = nome;
            this.valor = valor;
            next = null;
        }
    }

    private Node head;
    private Node tail;
    
    public LinkListCustom() {
        head = null;
        tail = null;
    }

    public void add(String nome, BigInteger valor) {
        Node aux = new Node(nome, valor);
        if (head == null) {
            head = aux;
        } else {
            tail.next = aux;
        }
        tail = aux;
    }

    public void set(String nome, BigInteger valor) {
        Node aux = head;
        while (aux != null) {
            if (nome.equalsIgnoreCase(aux.nome)) {
                aux.valor = valor;
                return;
            }
            aux = aux.next;
        }
    }

    public BigInteger get(String nome) {
        Node aux = head;
        while (aux != null) {
            if (nome.equalsIgnoreCase(aux.nome)) {
                return aux.valor;
            }
            aux = aux.next;
        }
        return null;
    }
}
