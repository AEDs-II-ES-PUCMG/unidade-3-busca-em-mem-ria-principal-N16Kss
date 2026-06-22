

import java.util.Random;

public class Teste {

    private static final int N = 10000;
    private static final int M = 1000;

    public static void main(String[] args) {

        Lista<Integer> valores = new Lista<>();
        Random random = new Random(42);

       

        for (int i = 0; i < N; i++) {
            valores.inserir(random.nextInt(100000));
        }

        ABB<Integer, Integer> abbAleatoria = new ABB<>();
        AVL<Integer, Integer> avlAleatoria = new AVL<>();

        for (int i = 0; i < valores.tamanho(); i++) {

            Integer valor = valores.elementoNaPosicao(i);

            try {
                abbAleatoria.inserir(valor, valor);
            } catch (Exception e) {}

            try {
                avlAleatoria.inserir(valor, valor);
            } catch (Exception e) {}
        }

        executarBuscas(
                "INSERÇÃO ALEATÓRIA",
                abbAleatoria,
                avlAleatoria
        );

     

        Lista<Integer> ordenados = copiarLista(valores);
        ordenarLista(ordenados);

        ABB<Integer, Integer> abbOrdenada = new ABB<>();
        AVL<Integer, Integer> avlOrdenada = new AVL<>();

        for (int i = 0; i < ordenados.tamanho(); i++) {

            Integer valor = ordenados.elementoNaPosicao(i);

            try {
                abbOrdenada.inserir(valor, valor);
            } catch (Exception e) {}

            try {
                avlOrdenada.inserir(valor, valor);
            } catch (Exception e) {}
        }

        executarBuscas(
                "INSERÇÃO ORDENADA",
                abbOrdenada,
                avlOrdenada
        );
    }

    private static void executarBuscas(
            String titulo,
            ABB<Integer, Integer> abb,
            AVL<Integer, Integer> avl) {

        Random random = new Random(99);

        long comparacoesABB = 0;
        long comparacoesAVL = 0;

        double tempoABB = 0;
        double tempoAVL = 0;

        for (int i = 0; i < M; i++) {

            int valorBusca = random.nextInt(100000);

            try {
                abb.pesquisar(valorBusca);
            } catch (Exception e) {}

            comparacoesABB += abb.getComparacoes();
            tempoABB += abb.getTempo();

            try {
                avl.pesquisar(valorBusca);
            } catch (Exception e) {}

            comparacoesAVL += avl.getComparacoes();
            tempoAVL += avl.getTempo();
        }

        System.out.println("\n==========================");
        System.out.println(titulo);
        System.out.println("==========================");

        System.out.println("\nABB");
        System.out.println("Comparações: " + comparacoesABB);
        System.out.println("Tempo total (ns): " + tempoABB);

        System.out.println("\nAVL");
        System.out.println("Comparações: " + comparacoesAVL);
        System.out.println("Tempo total (ns): " + tempoAVL);
    }

    private static Lista<Integer> copiarLista(Lista<Integer> original) {

        Lista<Integer> copia = new Lista<>();

        for (int i = 0; i < original.tamanho(); i++) {
            copia.inserir(original.elementoNaPosicao(i));
        }

        return copia;
    }

    private static void ordenarLista(Lista<Integer> lista) {

        int tamanho = lista.tamanho();

        for (int i = 0; i < tamanho - 1; i++) {

            for (int j = 0; j < tamanho - i - 1; j++) {

                Integer a = lista.elementoNaPosicao(j);
                Integer b = lista.elementoNaPosicao(j + 1);

                if (a > b) {

                    lista.remover(j + 1);
                    lista.remover(j);

                    lista.inserir(b, j);
                    lista.inserir(a, j + 1);
                }
            }
        }
    }
}








