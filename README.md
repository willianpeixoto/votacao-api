# Desafio Técnico

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso, você precisa criar uma solução back-end para gerenciar essas sessões de votação.

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

- Cadastrar uma nova pauta;
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default);
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
- Contabilizar os votos e dar o resultado da votação na pauta.

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A escolha da linguagem, frameworks e bibliotecas é livre (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

O foco dessa avaliação é a comunicação entre o backend e o aplicativo mobile. Essa comunicação é feita através de mensagens no formato JSON, onde essas mensagens serão interpretadas pelo cliente para montar as telas onde o usuário vai interagir com o sistema. A aplicação cliente não faz parte da avaliação, apenas os componentes do servidor. O formato padrão dessas mensagens será detalhado no anexo 1.

### Tarefas bônus

- Tarefa Bônus 1 - Integração com sistemas externos
    - Integrar com um sistema que verifique, a partir do CPF do associado, se ele pode votar
      - GET https://user-info.herokuapp.com/users/{cpf}
      - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos;
      - Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação
        
        ```
        //GET /users/19839091069
        //200 OK
        {
            "status": "ABLE_TO_VOTE"
        }
            
        //GET /users/62289608068
        //200 OK
        {
            "status": "UNABLE_TO_VOTE"
        }
        ```
        Exemplos de retorno do serviço

### Tarefa Bônus 2 - Mensagerias e filas

- O resultado da votação precisa ser informado para o restante da plataforma, isso deve ser feito preferencialmente através de mensageria. Quando a sessão de votação fechar, poste uma mensagem com o resultado da votação.

### Tarefa Bônus 3 - Performance

- Imagine que sua aplicação possa ser usada em cenários que existam centenas de milhares de votos. Ela deve se comportar de maneira performática nesses cenários;
- Testes de performance são uma boa maneira de garantir e observar como sua aplicação se comporta.

### Tarefa Bônus 4 - Versionamento da API

○ Como você versionaria a API da sua aplicação? Que estratégia usar?

## O que será analisado

- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits

## Observações importantes

- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e deixe claro caso haja instruções especiais para execução do mesmo
- Teste bem sua aplicação, evite bugs

### Configurações utilizadas durante o desenvolvimento
- Java Developer Kit (JDK) versão 17.0.12
- Apache Maven versão 3.9.9
- MySQL 8.0.41
- RabbitMQ
- Docker
- Docker Compose

### Dicas para executar a API
- Instalar docker https://docs.docker.com/engine/install/
- Instalar docker-compose https://docs.docker.com/compose/install/
- Comando para rodar o projeto: docker-compose up -d --build
- Comando para parar o projeto: docker-compose down
- Comando para ver logs: docker logs -f votacao-api

### Links úteis
- Swagger: http://localhost:8080/swagger-ui/index.html
- Painel RabbitMQ: http://localhost:15672/#/ (user: guest / pass: guest)

### Observações
- Ao rodar a aplicação através do docker-compose deverá subir 3 containers:
  - rabbitmq (servidor de mensageria)
  - mysql-votacao (banco de dados)
  - votacao-api (API de pautas votação)

### Lista de CPFs fakes para testes
- A API externa não está funcionando, então o banco de dados da aplicação é criado com os CPFs a seguir para possibilitar os testes.

| CPF     | Permissão |
|---------|-----------|
| 14725836914 | ABLE_TO_VOTE   |
| 25836914725 | ABLE_TO_VOTE   |
| 36914725836 | ABLE_TO_VOTE   |
| 12345678901 | ABLE_TO_VOTE   |
| 23456789123 | ABLE_TO_VOTE   |
| 45678912345 | UNABLE_TO_VOTE   |
| 98765432100 | UNABLE_TO_VOTE   |