# Processos para Contribuição no Projeto

Este documento detalha os passos e regras para contribuições no projeto, garantindo organização e qualidade no desenvolvimento.

## 1. Tarefas no ClickUp

* Todas as contribuições devem estar vinculadas a uma tarefa criada no **ClickUp**.
* Link para acesso ao ClickUp: **﻿**[Location Overview](https://app.clickup.com/9011819415/v/o/s/90112966955)﻿.
* Antes de iniciar qualquer atividade, verifique ou crie uma tarefa correspondente no ClickUp.

## 2. Regras para Branches

* Não é permitido realizar commits diretamente na branch `main`
* Todas as alterações devem passar pela branch `develop`  antes de serem promovidas para `main`.

### 2.1. Nomenclatura de Branches

* Toda branch criada para resolver ou adicionar uma nova funcionalidade deve incluir no início de seu nome o identificador do projeto, seguido do identificador da tarefa no ClickUp.
  * Padrão: `itelectric-iddatarefa`

### 2.2. Fluxo de Trabalho

1. Crie uma branch a partir de `main`, seguindo o padrão de nomenclatura acima:
   * `itelectric-iddatarefa`
2. Desenvolva e teste as alterações na sua branch.
3. Submeta um \*\*Pull Request\*\* (PR) da sua branch para a branch `develop`.
4. A branch `develop` é onde as alterações são testadas e aprovadas.

## 3. Regras para Commits

* Cada commit deve seguir o seguinte padrão: `<span>tipo: mensagem</span>`
* **Tipos permitidos:**
  * `docs`: Para alterações relacionadas à documentação.
  * `fix`: Para correção de bugs.
  * `feat`: Para adição de uma nova funcionalidade.
  * `ci/cd`: Para alterações no pipeline de integração/entrega contínua.

### 3.1. Exemplo de Commit:

* `feat: adiciona endpoint para criação de usuários`
* `fix: corrige bug no login`
* `docs: atualiza README com instruções`

## 4. Pull Request para main

* Somente após as alterações serem testadas e aprovadas na branch `develop`, você pode abrir um **Pull Request** para a branch `main`.
* Essa etapa é necessária para preparar a nova versão de produção.

## 5. Boas Práticas

* Certifique-se de que todo o código esteja devidamente testado antes de submetê-lo.
* Garanta que sua branch está atualizada com a última versão da branch `main`  antes de abrir um Pull Request.
* Siga as convenções de codificação do projeto para manter a consistência do código.

## 6. Resumo do Fluxo de Contribuição

1. Crie ou atribua uma tarefa no ClickUp.
2. Crie uma branch baseada em `main`.
3. Desenvolva e teste as alterações na sua branch.
4. Submeta um Pull Request para a branch `develop`.
5. Após a aprovação e os testes na branch `develop`, submeta um Pull Request para a branch `main`.
6. A nova versão é implantada em produção.
