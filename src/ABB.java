import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V>{

	private No<K, V> raiz; // referência à raiz da árvore.
	private Comparator<K> comparador; //comparador empregado para definir "menores" e "maiores".
	private int tamanho;
	private long comparacoes;
	private long inicio;
	private long termino;
	
	/**
	 * Método auxiliar para inicialização da árvore binária de busca.
	 * 
	 * Este método define a raiz da árvore como {@code null} e seu tamanho como 0.
	 * Utiliza o comparador fornecido para definir a organização dos elementos na árvore.
	 * @param comparador o comparador para organizar os elementos da árvore.
	 */
	private void init(Comparator<K> comparador) {
		raiz = null;
		tamanho = 0;
		this.comparador = comparador;
	}

	/**
	 * Construtor da classe.
	 * O comparador padrão de ordem natural será utilizado.
	 */ 
	@SuppressWarnings("unchecked")
	public ABB() {
	    init((Comparator<K>) Comparator.naturalOrder());
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária de busca vazia.
	 *  
	 * @param comparador o comparador a ser utilizado para organizar os elementos da árvore.  
	 */
	public ABB(Comparator<K> comparador) {
	    init(comparador);
	}

    /**
     * Construtor da classe.
     * Esse construtor cria uma nova árvore binária de busca a partir de uma outra árvore binária de busca,
     * com os mesmos itens, mas usando uma nova chave.
     * @param original a árvore binária de busca original.
     * @param funcaoChave a função que irá extrair a nova chave de cada item para a nova árvore.
     */
    @SuppressWarnings("unchecked")
	public ABB(ABB<?, V> original, Function<V, K> funcaoChave) {
        ABB<K, V> nova = new ABB<>();
        nova = copiarArvore(original.raiz, funcaoChave, nova);
        this.raiz = nova.raiz;
        this.comparador = (Comparator<K>) Comparator.naturalOrder();
    }
    
    /**
     * Recursivamente, copia os elementos da árvore original para esta, num processo análogo ao caminhamento em ordem.
     * @param <T> Tipo da nova chave.
     * @param raizArvore raiz da árvore original que será copiada.
     * @param funcaoChave função extratora da nova chave para cada item da árvore.
     * @param novaArvore Nova árvore. Parâmetro usado para permitir o retorno da recursividade.
     * @return A nova árvore com os itens copiados e usando a chave indicada pela função extratora.
     */
    private <T> ABB<T, V> copiarArvore(No<?, V> raizArvore, Function<V, T> funcaoChave, ABB<T, V> novaArvore) {
    	
        if (raizArvore != null) {
    		novaArvore = copiarArvore(raizArvore.getEsquerda(), funcaoChave, novaArvore);
            V item = raizArvore.getItem();
            T chave = funcaoChave.apply(item);
    		novaArvore.inserir(chave, item);
    		novaArvore = copiarArvore(raizArvore.getDireita(), funcaoChave, novaArvore);
    	}
        return novaArvore;
    }
    
    /**
	 * Método booleano que indica se a árvore está vazia ou não.
	 * @return
	 * verdadeiro: se a raiz da árvore for null, o que significa que a árvore está vazia.
	 * falso: se a raiz da árvore não for null, o que significa que a árvore não está vazia.
	 */
	public Boolean vazia() {
	    return (this.raiz == null);
	}
    
    @Override
    /**
     * Método que encapsula a pesquisa recursiva de itens na árvore.
     * @param chave a chave do item que será pesquisado na árvore.
     * @return o valor associado à chave.
     */
	public V pesquisar(K chave) {
    	comparacoes = 0;
    	inicio = System.nanoTime();
    	V procurado = pesquisar(raiz, chave);
    	termino = System.nanoTime();
    	return procurado;
	}
    
    private V pesquisar(No<K, V> raizArvore, K procurado) {
    	
    	int comparacao;
    	
    	comparacoes++;
    	if (raizArvore == null)
    		/// Se a raiz da árvore ou sub-árvore for null, a árvore/sub-árvore está vazia e então o item não foi encontrado.
    		throw new NoSuchElementException("O item não foi localizado na árvore!");
    	
    	comparacao = comparador.compare(procurado, raizArvore.getChave());
    	
    	if (comparacao == 0)
    		/// O item procurado foi encontrado.
    		return raizArvore.getItem();
    	else if (comparacao < 0)
    		/// Se o item procurado for menor do que o item armazenado na raiz da árvore:
            /// pesquise esse item na sub-árvore esquerda.    
    		return pesquisar(raizArvore.getEsquerda(), procurado);
    	else
    		/// Se o item procurado for maior do que o item armazenado na raiz da árvore:
            /// pesquise esse item na sub-árvore direita.
    		return pesquisar(raizArvore.getDireita(), procurado);
    }
    
    @Override
    /**
     * Método que encapsula a adição recursiva de itens à árvore, associando-o à chave fornecida.
     * @param chave a chave associada ao item que será inserido na árvore.
     * @param item o item que será inserido na árvore.
     * 
     * @return o tamanho atualizado da árvore após a execução da operação de inserção.
     */
    public int inserir(K chave, V item) {
    	// TODO

		this.raiz = inserir(this.raiz, chave, item);
        tamanho++;
    	return tamanho;
    }



protected No<K, V> inserir(No<K, V> raizArvore, K chave, V item) {
    	
    	int comparacao;
    	
        /// Se a raiz da árvore ou sub-árvore for null, a árvore/sub-árvore está vazia e então um novo item é inserido.
        if (raizArvore == null)
            raizArvore = new No<>(chave, item);
        else {
        	comparacao = comparador.compare(chave, raizArvore.getChave());
        
        	if (comparacao < 0)
        		/// Se a chave do item que deverá ser inserido na árvore for menor do que 
        		/// a chave do item armazenado na raiz da árvore:
        		/// adicione esse novo item à sub-árvore esquerda; 
        		/// e atualize a referência para a sub-árvore esquerda modificada. 
        		raizArvore.setEsquerda(inserir(raizArvore.getEsquerda(), chave, item));
        	else if (comparacao > 0)
        		/// Se a chave do item que deverá ser inserido na árvore for maior do que 
        		/// a chave do item armazenado na raiz da árvore:
        		/// adicione esse novo item à sub-árvore direita; 
        		/// e atualize a referência para a sub-árvore direita modificada.
        		raizArvore.setDireita(inserir(raizArvore.getDireita(), chave, item));
        	else
        		/// A chave do item armazenado na raiz da árvore 
        		/// é igual à chave do novo item que deveria ser inserido na árvore.
        		throw new IllegalArgumentException("O item já foi inserido anteriormente na árvore.");
        }
        
        /// Retorna a raiz atualizada da árvore ou sub-árvore em que o item foi adicionado.
        return raizArvore;
    }









	@Override
	public String percorrer() {
		return caminhamentoEmOrdem();
	}
    
    public String caminhamentoEmOrdem() {
    	
    	if (vazia())
    		throw new IllegalStateException("A árvore está vazia!");
    	
    	return caminhamentoEmOrdem(raiz);
    }
    
    private String caminhamentoEmOrdem(No<K, V> raizArvore) {
    	if (raizArvore != null) {
    		String resposta = caminhamentoEmOrdem(raizArvore.getEsquerda());
    		resposta += raizArvore.getItem() + "\n";
    		resposta += caminhamentoEmOrdem(raizArvore.getDireita());
    		
    		return resposta;
    	} else {
    		return "";
    	}
    }















    @Override 
    public String toString(){
    	return percorrer();
    }


    @Override
    /**
     * Método que encapsula a remoção recursiva de um item da árvore.
     * @param chave a chave do item que deverá ser localizado e removido da árvore.
     * @return o valor associado ao item removido.
     */
    public V remover(K chave) {
    	// TODO
    	return null;
    }

    
    public Lista<V> recortar(K chaveDeOnde, K chaveAteOnde) {
    Lista<V> lista = new Lista<>();

    recortar(raiz, chaveDeOnde, chaveAteOnde, lista);

    return lista;
}



private void recortar(No<K,V> no,K chaveDeOnde,K chaveAteOnde, Lista<V> lista) {

    if(no == null)
        return;

    recortar(no.getEsquerda(), chaveDeOnde, chaveAteOnde, lista);

    if(comparador.compare(no.getChave(), chaveDeOnde) >= 0 &&
       comparador.compare(no.getChave(), chaveAteOnde) <= 0) {

        lista.inserir(no.getItem());
    }

    recortar(no.getDireita(), chaveDeOnde, chaveAteOnde, lista);
}







public V getItemRaiz() {
    if(raiz == null)
        return null;

    return raiz.getItem();
}






	@Override
	public int tamanho() {
		return tamanho;
	}
	
	@Override
	public long getComparacoes() {
		return comparacoes;
	}

	@Override
	public double getTempo() {
		return (termino - inicio) / 1_000_000;
	}
}