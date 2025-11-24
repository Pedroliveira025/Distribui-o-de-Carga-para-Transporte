class Carga
{
    private int id; // identificador único
    private int tipo; // tipo da carga (9 = medicamentos, 9 = vacinas, etc.)
    private int urgência; // 1 = baixa, 2 = média, 3 = alta
    private int peso; // peso em quilogramas
    private String descrição; // breve descrição
    private int prioridade; // valor calculado

    public Carga(int id, int tipo, int urgência, int peso, String descrição)
    {
        this.id = id;
        this.tipo = tipo;
        this.urgência = urgência;
        this.peso = peso;
        this.calcularPrioridade();
    }

    public int calcularPrioridade()
    {
        return this.prioridade = (urgência * 10) + (peso * 2) + (tipo * 5);
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public int getTipo()
    {
        return this.tipo;
    }
    
    public int getUrgência()
    {
        return this.urgência;
    }
    
    public int getPeso()
    {
        return this.peso;
    }
    
    public int getPrioridade()
    {
        return this.prioridade;
    }
}

//Linhas inválidas devem ser ignoradas ou relatadas (ex.: urgência fora do intervalo
//1–3, peso negativo, tipo inexistente).
// resolver isso com exceptions!
//


class HeapMáxima
{
    private Carga[] heap; // vetor 1-indexado
    private int quantidade; // número de elementos armazenados
    private int capacidade; // tamanho máximo atual do vetor

    public HeapMáxima(int quantidadeInicial) // cria o heap
    {
        this.quantidade = 0;
        this.capacidade = quantidadeInicial;
        this.heap = new Carga[this.capacidade];
    }

    public void inserir(Carga novaCarga) // insere e ajusta a posição
    {
        this.heap[this.quantidade] = novaCarga;
        
        int novaCargaIndex = this.quantidade;
        
        int paiIndex = getPaiIndex(novaCargaIndex);
        
        while (éMaisPrioritário(novaCargaIndex, paiIndex)) {
            trocar(novaCargaIndex, paiIndex);
            
            novaCargaIndex = paiIndex;
            
            paiIndex = getPaiIndex(novaCargaIndex);
        }
        
        this.quantidade++;
    }

    public Carga removerMáximo() // remove a carga de maior prioridade
    {

    }

    public Carga consultarTopo() // retorna a carga de maior prioridade (sem remover)
    {

    }

    public int tamanho() // retorna a quantidade de elementos
    {
        return this.quantidade;
    }

    private void subir(int i) // reorganiza subindo (ajuste após inserção)
    {

    }

    private void descer(int i) // reorganiza descendo (ajuste após remoção)
    {

    }

    private void trocar(int i, int j) // troca elementos de posição
    {
        Carga cópia = heap[i];
        heap[i] = heap[j];
        heap[j] = cópia;
    }

    private void garantirCapacidade() // dobra o tamanho do vetor quando necessário
    {
        this.capacidade *= 2;
    }

    private int getPaiIndex(int nóIndex)
    {
        return (nóIndex == 0) ? -1 : (nóIndex - 1) / 2;
    }

    private int getFilhoEsquedoIndex(int nóIndex)
    {   
        int result = 2 * nóIndex + 1;
        return (result < this.capacidade) ? result : -1;
    }

    private int getFilhoDireitoIndex(int nóIndex)
    {
        int result = getFilhoEsquedoIndex(nóIndex) + 1;
        return (result < this.capacidade) ? result : -1;
    }
    
    private boolean éMaisPrioritário(int i, int j)
    {   
        if (this.heap[i].getPrioridade() > this.heap[j].getPrioridade()) {
            return true;
        } else if (this.heap[i].getPrioridade() < this.heap[j].getPrioridade()) {
            return false;
        } else {
            
            if (this.heap[i].getUrgência() > this.heap[j].getUrgência()) {
                return true;
            } else if (this.heap[i].getUrgência() < this.heap[j].getUrgência()) {
                return false;
            } else {
                
                if (this.heap[i].getPeso() > this.heap[j].getPeso()) {
                    return true;
                } else if (this.heap[i].getPeso() < this.heap[j].getPeso()) {
                    return false;
                } else {
                    
                    if (this.heap[i].getId() < this.heap[j].getId()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        
    }
}

class InterfaceDoUsuário
{
    private static int opção;

    public static void apresentarMenu()
    {
        System.out.println("1 - Carregar cargas de arquivo CSV");
        System.out.println("2 - Inserir nova carga");
        System.out.println("3 - Exibir carga de maior prioridade");
        System.out.println("4 - Remover carga de maior prioridade");
        System.out.println("5 - Exibir todas as cargas ordenadas por prioridade");
        System.out.println("6 - Sair");
    }

    public static void ação()
    {
        switch (opção)
        {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
        }

    }
}

class Main
{
    public static void main(String[] args)
    {
        while (true)
        {
            InterfaceDoUsuário.apresentarMenu();
            InterfaceDoUsuário.ação();
        }
    }

    // instânciar a heap máxima
    // fazer a Interface do Usuário dar comando as heap máxima
}


// primeira coisa: fazer ficar funcionar
// segunda coisa: aplicar as exceptions
// terceira coisa: aplicar os outros conceitos estudados na disciplina
