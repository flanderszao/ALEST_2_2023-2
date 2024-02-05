import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppHidrogenioCalculator {

    private static int contCaculateHidrogenio=0, contCalculateElemento=0;

    public static int SimpleHash(String nome) {
        return (nome.charAt(0) - 'a') % 10; //hash simples genérico baseado na primeira letra do elemento
    }

    public static BigInteger calculateHidrogenio(Path filePath, LinkListCustom[] lista) {
        BigInteger resultado = BigInteger.ZERO;

        try (BufferedReader reader = Files.newBufferedReader(filePath, Charset.forName("UTF-8"))) {
            String line = reader.readLine(); //leitor de linhas
            while (line != null) {
                String[] auxp = line.split(" "); //guardador para leitura de linhas
                String palauxs = auxp[auxp.length - 1]; //valor final
                int hash = SimpleHash(palauxs); //onde guardar
                BigInteger aux = BigInteger.ZERO; //valor inicial

                //System.out.print("\n" + palauxs + " = "); facilitador para observar

                for (int i = 0; i < auxp.length - 3; i = i + 2) {
                    BigInteger auxb = BigInteger.ZERO; //auxiliar adicional

                    String palaux = auxp[i + 1]; //o elemento a ser referenciado

                    if (palaux.equals("->")){
                        break; //caso de borda, mas não deve aconteceer por causa do "auxp.length < 3"
                    } 

                    long paux = (long) Integer.valueOf(auxp[i]); //valor da multiplicação, o 2 em (2 ElementoA -> 1 ElementoB)
                    int hashaux = SimpleHash(palaux); //para auxiliar na procura do elemento

                    //System.out.println(paux + "*" + palaux); facilitador para observar

                    BigInteger quantidade = lista[hashaux].get(palaux); //valor do elemento, referencia o valor de ElementoA em (2 ElementoA -> ElementoB)

                    if (quantidade != null) {
                        auxb = auxb.add(quantidade.multiply(BigInteger.valueOf(paux))); //auxiliar b recebe o valor deste elemento, referencia o valor da equação (2 * ElementoA) em (2 ElementoA -> ElementoB)
                    } else {
                        calculateElemento(filePath, lista, palaux); //caso o elemento não possua valor, referencie a recursivadade do próximo método
                        contCalculateElemento++; //cont++ para chamada da função
                        quantidade = lista[hashaux].get(palaux); //valor do elemento (de novo)
                        auxb = auxb.add(quantidade.multiply(BigInteger.valueOf(paux))); //auxiliar b recebe o valor deste elemento
                    }

                    if (aux == BigInteger.ZERO) { 
                        aux = auxb; //inicializa o auxiliar
                    } else {
                        aux = aux.add(auxb); //acrescenta se a equação possui múltiplos valores (ex. (3 * EA) + (2 * EB) -> EC)
                    }
                }
                //System.out.println("= " + aux); facilitador para observar

                if (lista[hash].get(palauxs) != null) { //caso valor já exista
                    if ((aux.compareTo(lista[hash].get(palauxs))) < 0) { //se valor for menor
                        lista[hash].set(palauxs, aux); //valor é atualizado
                    }
                } else {
                    lista[hash].add(palauxs, aux); //caso contrário é apenas adicionado
                }

                resultado = aux; //atualiza o resultado a cada iteração

                line = reader.readLine(); //lê a próxima linha

                contCaculateHidrogenio++; //cont++ método Hidrogenio
            }
        } catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: %s%n", e.getMessage());
        }

        return resultado; //devolve resultado da equação total
    }

    //abaixo está uma versão recursiva do método acima, similar, mas com comentários
    //onde for pertinente notar as mudanças
    private static void calculateElemento(Path filePath, LinkListCustom[] lista, String elemento) throws IOException{
        try (BufferedReader reader = Files.newBufferedReader(filePath, Charset.forName("UTF-8"))) {
            String line = reader.readLine(); 
            while (line != null){
                String[] auxp = line.split(" ");
                String palauxs = auxp[auxp.length - 1];
                if (palauxs.equals(elemento)) { //se é o elemento procurado
                
                    int hash = SimpleHash(palauxs);
                BigInteger aux = BigInteger.ZERO;

                //System.out.print("\n" + palauxs + " = "); facilitador para observar

                for (int i = 0; i < auxp.length - 3; i = i + 2) {
                    BigInteger auxb = BigInteger.ZERO;

                    String palaux = auxp[i + 1];
                    if (palaux.equals("->"))
                        break;

                    long paux = (long) Integer.valueOf(auxp[i]);
                    int hashaux = SimpleHash(palaux);

                    //System.out.println(paux + "*" + palaux); facilitador para observar

                    BigInteger quantidade = lista[hashaux].get(palaux);

                    if (quantidade != null) {
                        auxb = auxb.add(quantidade.multiply(BigInteger.valueOf(paux)));
                    } else {
                        calculateElemento(filePath, lista, palaux); //funciona de maneira recursiva também
                        contCalculateElemento++;
                        quantidade = lista[hashaux].get(palaux);
                        auxb = auxb.add(quantidade.multiply(BigInteger.valueOf(paux)));
                    }

                    if (aux == BigInteger.ZERO) {
                        aux = auxb;
                    } else {
                        aux = aux.add(auxb);
                    }
                }
                //System.out.println("= " + aux); facilitador para observar

                lista[hash].add(palauxs, aux);
                break;
                } else {line = reader.readLine();}
            }
        }
    }

    public static void main(String[] args) throws IOException {
        LinkListCustom[] lista = new LinkListCustom[10]; //facilitador para armazenamento
        String path = "testes-t12\\casoa5.txt";
        Path path1 = Paths.get(path);

        for (int i = 0; i < 10; i++) {
            lista[i] = new LinkListCustom(); //inicializa as listas
        }

        lista[SimpleHash("hidrogenio")].add("hidrogenio", BigInteger.ONE); //o valor de hidrogênio é 1

        BigInteger hidrogenioNecessario = calculateHidrogenio(path1, lista); //chamada de método

        System.out.println("A quantidade de hidrogênio necessário é: " + hidrogenioNecessario);
        System.out.println("Chamadas de CalculateHidrogenio: "+contCaculateHidrogenio);
        System.out.println("Chamadas de CaculateElemento: "+contCalculateElemento);

            FileWriter fileWriter = new FileWriter("testes-t12\\solved"+path); //auxiliador para guardar resultados
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("A quantidade de hidrogenio necessario e: " + hidrogenioNecessario);
            printWriter.println("Chamadas de CalculateHidrogenio: "+contCaculateHidrogenio);
            printWriter.println("Chamadas de CaculateElemento: "+contCalculateElemento);
            printWriter.close();
    }
}
