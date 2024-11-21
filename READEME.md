# Algoritmo ACO (Ant Colony Optimization)

Visa encontrar a melhor rota em um grafo, utilizando a cooperação de várias "formigas" que simulam o comportamento de busca de comida na natureza.

## Classe `ACO()`:

### Função `rodar()`:
#### Parâmetros Gerais:
- **maxIteracoes**: Máximo de iterações que o algoritmo pode realizar. Neste caso, é definido como 100.
- **maxSemMelhoria**: Define quantas iterações consecutivas podem ocorrer sem uma melhoria na solução atual. Esse valor é configurado como um terço de `maxIteracoes`.
- **iteracoesSemMelhoria**: Contador de quantas iterações consecutivas ocorreram sem uma melhoria significativa na solução.
- **melhorCustoGlobal**: Mantém o custo da melhor solução encontrada durante a execução.

#### Estrutura do Algoritmo
O algoritmo segue as etapas descritas abaixo:

1. **Inicialização de Itens Essenciais**
   - Criação da lista `cidadesVisitadas` para armazenar as rotas que cada formiga visitará durante a execução.
   - Inicialização das formigas e definição dos pontos de partida. As formigas são inicializadas através do método `inicializarFormigas()`, que coloca todas no mesmo ponto de partida definido pelo grafo.

2. **Construção da Solução por Cada Formiga**
   - Cada formiga escolhe uma próxima cidade para visitar usando um mecanismo de probabilidades baseado no feromônio atual (representando atratividade) e na distância para cada cidade vizinha.
   - As probabilidades são calculadas para cada cidade não visitada, levando em conta a influência dos parâmetros `alfa` (intensidade do feromônio) e `beta` (importância da distância).
   - Uma "roleta" é usada para determinar a próxima cidade a ser escolhida com base nas probabilidades acumuladas.

3. **Cálculo do Custo Guloso**
   - O método `calculaCustoGuloso()` é utilizado para obter uma estimativa inicial do custo de uma solução gulosa. Ele começa em um vértice aleatório e encontra a rota com menor custo até visitar todos os vértices, retornando ao ponto inicial. Esse custo é usado para inicializar os feromônios no grafo, tornando o ponto de partida mais eficiente para as formigas.

4. **Atualização dos Feromônios**
   - Depois que todas as formigas completam suas soluções, os feromônios são atualizados para refletir a qualidade das rotas encontradas. Rotas com melhor custo recebem mais feromônio, tornando-as mais atraentes para a próxima iteração. O método `atualizarFeromonios()` é responsável por essa etapa, somando os feromônios com base no custo das soluções encontradas pelas formigas e aplicando a taxa de evaporação.

5. **Verificação de Melhoria Global**
   - A cada iteração, é verificado se houve melhoria no custo da melhor rota em relação às iterações anteriores.
   - Se uma solução melhor é encontrada, o contador `iteracoesSemMelhoria` é zerado. Caso contrário, esse contador é incrementado.
   - Se o algoritmo atinge o limite de iterações sem melhoria, ele é interrompido.

#### Estrutura do Loop Principal
- **Loop While**: O loop principal é controlado por duas condições: o número máximo de iterações (`maxIteracoes`) e o limite de iterações consecutivas sem melhorias (`maxSemMelhoria`).
- Para cada iteração (`epochs`), as formigas tentam construir uma solução viável. Caso não haja progresso após um número de iterações, o loop é interrompido e a melhor solução é encontrada e exibida pelo método `encontrarMelhorSolucao()`, que exibe a solução final com o menor custo encontrado.

#### Observações Importantes
- A condição de parada depende do progresso da solução. Caso o custo global não seja melhorado durante `maxSemMelhoria` iterações, o algoritmo é encerrado.
- A escolha da próxima cidade é baseada em uma técnica de "roleta" que ajuda a explorar soluções diferentes, balanceando entre explorar novas rotas e explorar aquelas que parecem promissoras.

### Estrutura da Classe `Grafo`
A classe `Grafo` é responsável por modelar o problema como um conjunto de vértices e arestas, representando as cidades e as conexões entre elas.

1. **Atributos da Classe Grafo**
   - **qtdVertices**: Número total de vértices (ou cidades) no grafo.
   - **arestas**: Mapa que armazena as arestas do grafo, identificadas por uma string que concatena os vértices de origem e destino.
   - **vizinhos**: Mapa que relaciona cada vértice aos seus vizinhos imediatos, permitindo acesso rápido às cidades conectadas.
   - **mapaEnderecos**: Mapa que associa um índice a cada vértice, facilitando a tradução entre índices e nomes das cidades.

2. **Métodos da Classe Grafo**
   - **`inicializarFeromonio(double custoGuloso)`**: Inicializa o valor de feromônio para todas as arestas do grafo com base no custo da solução gulosa. Isso garante que todas as rotas tenham uma atratividade inicial proporcional ao custo guloso.
   - **`addAresta(String origem, String destino, Long custo)`**: Adiciona uma aresta ao grafo, criando uma conexão entre duas cidades com um custo especificado. Também adiciona o vértice de destino à lista de vizinhos da cidade de origem.
   - **`getCustoAresta(String origem, String destino)`**: Retorna o custo da aresta entre as cidades de origem e destino especificadas.
   - **`getFeromonioAresta(String origem, String destino)`**: Obtém o valor do feromônio depositado na aresta entre duas cidades.
   - **`getKeysArestas()`**: Retorna um conjunto contendo todas as chaves das arestas do grafo. Isso permite iterar por todas as conexões existentes.
   - **`setFeromonioAresta(String origem, String destino, double feromonio)`**: Define o valor do feromônio para uma aresta específica, atualizando a atratividade dessa rota.
   - **`getQtdVertices()`**: Retorna a quantidade total de vértices no grafo.
   - **`getEndereco(int indice)`**: Retorna o endereço associado a um índice específico.
   - **`getEnderecos()`**: Retorna uma coleção de todos os endereços dos vértices no grafo.
   - **`getVizinhos(String vertice)`**: Retorna uma lista dos vizinhos do vértice especificado, ou seja, todas as cidades conectadas diretamente a ele.

#### Observações Importantes
- A condição de parada depende do progresso da solução. Caso o custo global não seja melhorado durante `maxSemMelhoria` iterações, o algoritmo é encerrado.
- A escolha da próxima cidade é baseada em uma técnica de "roleta" que ajuda a explorar soluções diferentes, balanceando entre explorar novas rotas e explorar aquelas que parecem promissoras.



