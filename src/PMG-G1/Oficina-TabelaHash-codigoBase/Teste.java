

import java.util.Random;
public class Teste {
    
    Lista<Integer> lista = new Lista<>();
    Lista<Integer> lista2 = new Lista<>();
    
    Random random =new Random(42);
    public void testeLista() {
        for(int i=0; i<10000; i++) {
            lista.inserir(random.nextInt(100));
        }
        System.out.println(lista);
    }

    ABB<Integer, Integer> abb = new ABB<>();
    
    
    public void testeABB() {
        for(int i=0; i<10000; i++) {
            abb.inserir(Lista.getItem(lista), Lista.getItem(lista));
        }
        System.out.println(abb);
    }








}
