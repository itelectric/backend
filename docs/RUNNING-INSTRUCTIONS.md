### 1. Pré-requisitos

Certifique-se de que seu ambiente de desenvolvimento atende aos seguintes requisitos:

- **Java**: Versão 23 ou superior instalada.
- **Docker**: Instale a versão mais recente do Docker em seu sistema.
- **Docker Compose**: Certifique-se de que o Docker Compose está instalado.

### 2. Clonar o Repositório

1. Clone o repositório do projeto:

   ```bash
   git clone https://github.com/itelectric/backend.git
   ```
2. Navegue até o diretório do projeto:

   ```bash
   cd path-to/itelectric/backend
   ```

### 3. Configurar Variáveis de Ambiente

1. Localize o arquivo `.docker-compose.yml` na raiz do projeto.
2. Ajuste as variáveis conforme necessário:

   ```env
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/nome_do_banco
   ```

### 4. Executando o Backend com sua IDE

1. Escolha a IDE de sua preferência (sugere-se o uso do IntelliJ IDEA).
2. Abra o projeto backend na IDE escolhida.
3. Configure o ambiente conforme necessário (garanta que as dependências foram instaladas).
4. Execute a aplicação backend diretamente pela IDE.
5. Certifique-se de que o servidor está rodando em `http://localhost:8080`.

### 5. Executando o Backend com Docker Compose

1. Certifique-se de que o Docker está instalado e configurado corretamente.
2. Navegue até o diretório raiz do projeto.
3. Execute o comando:
   ```bash
   docker-compose up -d
   ```
4. Após a execução, verifique se o backend está rodando acessando `http://localhost:8080`.
