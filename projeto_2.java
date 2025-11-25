import java.io.*;
import java.util.*;

// Exceção para cargas inválidas
class CargaInvalidaException extends Exception {
    public CargaInvalidaException(String mensagem) {
        super(mensagem);
    }
}

class Carga
{
    private int id; // identificador único
    private int tipo; // tipo da carga (9 = medicamentos, 5 = eletrônicos, 3 = roupas, etc.)
    private int urgência; // 1 = baixa, 2 = média, 3 = alta
    private int peso; // peso em quilogramas
    private String descrição; // breve descrição
    private int prioridade; // valor calculado

    public Carga(int id, int tipo, int urgência, int peso, String descrição) throws CargaInvalidaException
    {
        this.id = id;
        this.tipo = tipo;
        this.urgência = urgência;
        this.peso = peso;
        this.descrição = descrição;

        validar(); // lança CargaInvalidaException se algo estiver errado

        this.calcularPrioridade();
    }

    private void validar() throws CargaInvalidaException {
        // tipos válidos segundo a especificação / exemplos: 9, 5, 3
        Set<Integer> tiposValidos = new HashSet<>(Arrays.asList(9, 5, 3));

        if (!tiposValidos.contains(this.tipo)) {
            throw new CargaInvalidaException("Tipo inexistente: " + this.tipo);
        }
        if (this.urgência < 1 || this.urgência > 3) {
            throw new CargaInvalidaException("Urgência fora do intervalo 1–3: " + this.urgência);
        }
        if (this.peso < 0) {
            throw new CargaInvalidaException("Peso não pode ser negativo: " + this.peso);
        }
    }

    public int calcularPrioridade()
    {
        return this.prioridade = (this.urgência * 10) + (this.peso * 2) + (this.tipo * 5);
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

    @Override
    public String toString() {
        return String.format("%d | %d | %d | %d | %d | %s",
                this.id, this.tipo, this.urgência, this.peso, this.prioridade, this.descrição == null ? "" : this.descrição);
    }
}

class HeapMáxima
{
    private Carga[] heap; // vetor 0-indexado (escolhi 0-index por conveniência e para minimizar mudanças)
    private int quantidade; // número de elementos armazenados
    private int capacidade; // tamanho máximo atual do vetor

    public HeapMáxima(int capacidadeInicial) // cria o heap
    {
        if (capacidadeInicial <= 0) capacidadeInicial = 10;
        this.quantidade = 0;
        this.capacidade = capacidadeInicial;
        this.heap = new Carga[this.capacidade];
    }

    public void inserir(Carga novaCarga) // insere e ajusta a posição
    {
        garantirCapacidade();
        this.heap[this.quantidade] = novaCarga;
        subir(this.quantidade);
        this.quantidade++;
    }

    public Carga removerMáximo() // remove a carga de maior prioridade
    {
        if (this.quantidade == 0) {
            return null; // ou lançar exceção conforme preferir
        }
        
        Carga result = this.heap[0];
        
        this.heap[0] = this.heap[this.quantidade - 1];
        this.heap[this.quantidade - 1] = null;
        this.quantidade--;
        
        if (this.quantidade > 0) descer(0);
        
        return result;
    }

    public Carga consultarTopo() // retorna a carga de maior prioridade (sem remover)
    {
        if (this.quantidade == 0) return null;
        return this.heap[0];
    }

    public int tamanho() // retorna a quantidade de elementos
    {
        return this.quantidade;
    }

    private void subir(int i) // reorganiza subindo (ajuste após inserção)
    {
        int atual = i;
        int pai = getPaiIndex(atual);
        while (pai >= 0 && éMaisPrioritário(atual, pai)) {
            trocar(atual, pai);
            atual = pai;
            pai = getPaiIndex(atual);
        }
    }

    private void descer(int i) // reorganiza descendo (ajuste após remoção)
    {
        int atual = i;
        while (true) {
            int esquerdo = getFilhoEsquedoIndex(atual);
            int direito = getFilhoDireitoIndex(atual);
            int maior = atual;

            if (esquerdo < this.quantidade && éMaisPrioritário(esquerdo, maior)) {
                maior = esquerdo;
            }
            if (direito < this.quantidade && éMaisPrioritário(direito, maior)) {
                maior = direito;
            }

            if (maior == atual) break;

            trocar(atual, maior);
            atual = maior;
        }
    }

    private void trocar(int i, int j) // troca elementos de posição
    {
        Carga copia = heap[i];
        heap[i] = heap[j];
        heap[j] = copia;
    }

    private void garantirCapacidade() // dobra o tamanho do vetor quando necessário
    {
        if (this.quantidade < this.capacidade) return;

        int novaCapacidade = Math.max(1, this.capacidade * 2);
        Carga[] novo = new Carga[novaCapacidade];
        for (int k = 0; k < this.quantidade; k++) novo[k] = this.heap[k];
        this.heap = novo;
        this.capacidade = novaCapacidade;
    }

    private int getPaiIndex(int nóIndex)
    {
        return (nóIndex == 0) ? -1 : (nóIndex - 1) / 2;
    }

    private int getFilhoEsquedoIndex(int nóIndex)
    {   
        return 2 * nóIndex + 1;
    }

    private int getFilhoDireitoIndex(int nóIndex)
    {
        return 2 * nóIndex + 2;
    }
    
    private boolean éMaisPrioritário(int i, int j)
    {   
        // assumimos i e j válidos (0 <= i,j < quantidade)
        Carga a = this.heap[i];
        Carga b = this.heap[j];

        if (a.getPrioridade() > b.getPrioridade()) {
            return true;
        } else if (a.getPrioridade() < b.getPrioridade()) {
            return false;
        } else {
            // desempate por urgência (maior)
            if (a.getUrgência() > b.getUrgência()) return true;
            if (a.getUrgência() < b.getUrgência()) return false;

            // desempate por peso (maior)
            if (a.getPeso() > b.getPeso()) return true;
            if (a.getPeso() < b.getPeso()) return false;

            // desempate por id (menor)
            return a.getId() < b.getId();
        }
    }
    
    private void heapfy(int index)
    {
        descer(index);
    }

    // método utilitário: cria uma cópia independente do heap (para exibir ordenado sem modificar original)
    public HeapMáxima copiar()
    {
        HeapMáxima copia = new HeapMáxima(Math.max(10, this.capacidade));
        copia.quantidade = this.quantidade;
        for (int i = 0; i < this.quantidade; i++) copia.heap[i] = this.heap[i];
        return copia;
    }
}

class ArquivoCarga {
    // carrega CSV e insere no heap; relata linhas inválidas por exceptions
    public static void carregarCSV(String caminho, HeapMáxima heap) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(caminho), "UTF-8"));
        String linha;
        int numeroLinha = 0;
        List<String> relatorioErros = new ArrayList<>();

        while ((linha = br.readLine()) != null) {
            numeroLinha++;
            linha = linha.trim();
            if (linha.isEmpty()) continue;

            // ignorar possível cabeçalho (detecta se tem palavra não-numérica na primeira coluna)
            String[] partes = linha.split(",", 5);
            if (partes.length < 5) {
                relatorioErros.add("Linha " + numeroLinha + ": formato inválido (menos de 5 colunas)");
                continue;
            }

            try {
                int id = Integer.parseInt(partes[0].trim());
                int tipo = Integer.parseInt(partes[1].trim());
                int urgencia = Integer.parseInt(partes[2].trim());
                int peso = Integer.parseInt(partes[3].trim());
                String descricao = partes[4].trim();

                // caso a primeira linha seja cabeçalho textual (ex.: "ID,Tipo,..."), detectamos e pulamos
                if (numeroLinha == 1) {
                    boolean isHeader = false;
                    try {
                        Integer.parseInt(partes[0].trim());
                    } catch (NumberFormatException e) {
                        isHeader = true;
                    }
                    if (isHeader) {
                        // pular cabeçalho e continuar
                        continue;
                    }
                }

                try {
                    Carga c = new Carga(id, tipo, urgencia, peso, descricao);
                    heap.inserir(c);
                } catch (CargaInvalidaException cie) {
                    relatorioErros.add("Linha " + numeroLinha + ": " + cie.getMessage());
                }

            } catch (NumberFormatException nfe) {
                relatorioErros.add("Linha " + numeroLinha + ": valores numéricos inválidos");
            }
        }

        br.close();

        if (!relatorioErros.isEmpty()) {
            System.out.println("Relatório de linhas inválidas:");
            for (String err : relatorioErros) {
                System.out.println(" - " + err);
            }
        } else {
            System.out.println("Arquivo carregado sem erros.");
        }
    }
}

class InterfaceDoUsuário
{
    private static int opção;

    public static void apresentarMenu()
    {
        System.out.println();
        System.out.println("1 - Carregar cargas de arquivo CSV");
        System.out.println("2 - Inserir nova carga");
        System.out.println("3 - Exibir carga de maior prioridade");
        System.out.println("4 - Remover carga de maior prioridade");
        System.out.println("5 - Exibir todas as cargas ordenadas por prioridade");
        System.out.println("6 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    public static void ação(HeapMáxima heap, Scanner sc)
    {
        String entrada = sc.nextLine().trim();
        try {
            opção = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            opção = -1;
        }

        switch (opção)
        {
            case 1:
                System.out.print("Caminho do arquivo CSV: ");
                String caminho = sc.nextLine().trim();
                try {
                    ArquivoCarga.carregarCSV(caminho, heap);
                } catch (IOException ioe) {
                    System.out.println("Erro ao ler o arquivo: " + ioe.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Tipo (ex.: 9,5,3): ");
                    int tipo = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Urgência (1-baixa,2-média,3-alta): ");
                    int urg = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Peso (kg): ");
                    int peso = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Descrição: ");
                    String desc = sc.nextLine().trim();

                    Carga c = new Carga(id, tipo, urg, peso, desc);
                    heap.inserir(c);
                    System.out.println("Carga inserida com sucesso.");
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrada numérica inválida. Operação cancelada.");
                } catch (CargaInvalidaException cie) {
                    System.out.println("Carga inválida: " + cie.getMessage());
                }
                break;
            case 3:
                Carga topo = heap.consultarTopo();
                if (topo == null) {
                    System.out.println("Heap vazio.");
                } else {
                    System.out.println("ID | Tipo | Urgência | Peso | Prioridade | Descrição");
                    System.out.println("-----------------------------------------------------");
                    System.out.println(topo.toString());
                }
                break;
            case 4:
                Carga removida = heap.removerMáximo();
                if (removida == null) {
                    System.out.println("Heap vazio. Nada a remover.");
                } else {
                    System.out.println("Removida:");
                    System.out.println("ID | Tipo | Urgência | Peso | Prioridade | Descrição");
                    System.out.println("-----------------------------------------------------");
                    System.out.println(removida.toString());
                }
                break;
            case 5:
                if (heap.tamanho() == 0) {
                    System.out.println("Heap vazio.");
                } else {
                    System.out.println("Lista de cargas ordenadas (maior prioridade primeiro):");
                    System.out.println("ID | Tipo | Urgência | Peso | Prioridade | Descrição");
                    System.out.println("-----------------------------------------------------");
                    HeapMáxima copia = heap.copiar();
                    while (copia.tamanho() > 0) {
                        Carga c = copia.removerMáximo();
                        System.out.println(c.toString());
                    }
                }
                break;
            case 6:
                System.out.println("Saindo...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }

    }
}

public class Main
{
    public static void main(String[] args)
    {
        HeapMáxima heap = new HeapMáxima(10);
        Scanner sc = new Scanner(System.in, "UTF-8");

        // loop principal
        while (true)
        {
            InterfaceDoUsuário.apresentarMenu();
            InterfaceDoUsuário.ação(heap, sc);
        }
    }
}
