Solução:

Optei por usar índices no nome do cliente e resolver o match no banco de dados com
uma busca de limit 1, dessa maneira a query irá parar de executar no primeiro match.
Acredito que essa solução irá performar bem o suficiente. Porém eu sei que a
operação ‘regexp’ não tem uma performance boa, outra maneira de resolver esse
problema seria buscar todas as regras globais usando paginação para controlar a
memória da aplicação, buscar por um match em memória (na aplicação java) para
poupar a base de dados e retornar no caso de um match, do contrário fazer o
mesmo para as regras por cliente.

Para testes eu utilizei uma base de dados h2 em memória para tonar a construção da
aplicação independente do mysql estar rodando.
