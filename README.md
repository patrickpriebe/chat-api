**# ⚙️ NEXORA - API RESTful (Back-end)

> **Esta é a API que alimenta o sistema. [Acesse o repositório da Interface (UI) aqui](https://github.com/patrickpriebe/chat-ui)**

O NEXORA é um sistema full-stack de comunicação em tempo real. Este repositório contém a API RESTful, desenvolvida em Java 21 com Spring Boot 3 sob os princípios de Clean Code, arquitetura em camadas, orientação a domínios e rigorosa separação de responsabilidades entre Controllers, Services, Repositories, DTOs, Entities e Enums.

O projeto garante alta manutenibilidade, utilizando Spring Security com JWT para autenticação stateless, BCrypt para proteção de senhas, Bean Validation para validação declarativa e tratamento centralizado de erros (@RestControllerAdvice) no padrão RFC 9457. A comunicação e o processamento assíncrono são o coração da aplicação, orquestrados através de Apache Kafka, WebSocket, STOMP e SockJS para a entrega instantânea de mensagens.

## 📸 Telas principais do Sistema

|                     Login & Cadastro                      |                     Chat & Canais                     |
|:---------------------------------------------------------:|:-----------------------------------------------------:|
| ![Login e Cadastro](https://github.com/patrickpriebe/chat-ui/blob/main/chat-frontend/docs/screenshots/nexora1.png)      | ![Chat e Canais](https://github.com/patrickpriebe/chat-ui/blob/main/chat-frontend/docs/screenshots/nexora2.png)               |
|                    **Criação de Canal**                   |                **Notificações e Tracking**            |
| ![Criação de Canal](https://github.com/patrickpriebe/chat-ui/blob/main/chat-frontend/docs/screenshots/nexora3.png)| ![Notificações e Tracking](https://github.com/patrickpriebe/chat-ui/blob/main/chat-frontend/docs/screenshots/nexora4.png)     |


---

## 🏗️ Arquitetura, Infraestrutura e Stack

A API foi desenhada focando em performance, processamento assíncrono, segurança e integridade relacional.

*   **Java Spring Boot:** Arquitetura modular e robusta, com injeção de dependências e uso de Maven & Lombok para redução de código repetitivo (*boilerplate*).
*   **Apache Kafka & WebSockets:** Stack completa para comunicação bidirecional. O Kafka atua na publicação e consumo de eventos de forma assíncrona, enquanto o WebSocket (com STOMP & SockJS) distribui mensagens e indicadores de digitação em tempo real.
*   **Spring Security & JWT:** Autenticação sem estado no servidor (*stateless*), login próprio, controle de acesso e proteção estrita de rotas.
*   **Spring Data JPA & Hibernate:** Gerenciamento das operações de persistência e ORM integrados perfeitamente para manipulação de entidades.
*   **Supabase (PostgreSQL) & Render:** Banco de dados relacional hospedado na nuvem garantindo a integridade dos históricos de chat, combinado com hospedagem contínua (Render) da API em produção.
*   **Validação & Padronização:** Bean Validation para garantir a integridade dos dados na entrada e tratamento de erros padronizado com *Problem Details* (RFC 9457).
*   **OpenAPI & Swagger:** Documentação interativa e atualizada de todos os endpoints da aplicação.

---

## 💬 Domínios da Aplicação (Features)

1.  **Autenticação e Controle de Acesso:** Endpoints de criação de conta e login com emissão de JWT. Validação rigorosa de participação nos canais, tanto nas requisições REST quanto nas inscrições STOMP.
2.  **Gestão de Canais e Usuários:** Consulta de usuários disponíveis e criação de canais (conversas) fechados, com seleção de membros e listagem de canais restrita ao usuário autenticado.
3.  **Mensageria e Tempo Real:** Envio, persistência, carregamento de histórico e distribuição instantânea de mensagens, garantindo feedback imediato de quem está digitando.
4.  **Tratamento Centralizado de Erros:** Respostas blindadas e padronizadas para falhas de validação, acessos negados ou recursos não encontrados, melhorando o consumo pela interface.

---

Desenvolvido por: **Patrick Priebe**

Desenvolvedor de Software, apaixonado por código limpo, arquitetura back-end e interfaces que fogem do comum.

🔗 [LinkedIn](https://www.linkedin.com/in/patrickpriebe/) | 💻 [GitHub](https://github.com/patrickpriebe)**