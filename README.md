# guia-gurupi-api

API REST do **Guia da Cidade de Gurupi - TO**  
Backend Spring Boot 3.2 / Java 17 que alimenta o frontend React gerado pelo Bolt.
Esse front gerado pelo Bolt é para a API de consulta pública acessível a qualquer
sistema que queira consumir os dados dos produtos e serviços das empresas cadastradas
na plataforma.

# Status

Acrescentei essa sessão agora no dia 28 de abril, aniversãrio da minha ilustre cunhada Layanne Freitas. Vou batizar
a API com o apelido que dei pra ela: layozzi (de layout em React, javascript avançado). Parabéns Layanne.

Fase: Testes Unitários e teste de aceitação

Segue abaixo as tecnologias empregadas por mim nesse desafio onde pensei com a ajuda da I.A em como seria a arquitetura
da aplicação, se seria usando microsserviços, ou seria hexagonal ou clean arch (hexagonal com clean code imbutido já).
Aqui usei a estratégia DDD focada no domínio da informação aliada ao propósito da ferramenta em implementar o CRUD, porém
com R - Read (Listar ou buscar no banco) separado em uma API pública disponível para consulta e outra API privada
focada na administração da API pública. A API privada é que vai me dar retorno financeiro nesse projeto, onde terei as
atividades de cadastramento do portfólio de produtos e serviços que o empreendedor oferece.

Segue para conhecimento:

---

## Stack

| Camada       | Tecnologia                        |
|--------------|-----------------------------------|
| Backend      | Spring Boot 3.2 + Java 17         |
| Banco        | PostgreSQL 15                     |
| Migrations   | Flyway                            |
| Arquivos     | MinIO (S3-compatible)             |
| Build        | Maven                             |
| Containers   | Docker Compose                    |

---

Aqui segue o passo a passo para subir essa API pública. Resolvi deixar ela pública e deixar a parte da API privada
separada em outra versão e repositório fechado, pois tem caracteristíca estratégica de cunho comercial.
Já tentaram lançar essa ferramenta umas 3 vezes sem minha participação e não conseguiram chegar no resultado satisfatório
a longo prazo e que fosse não só um sistema, mas uma ferramenta que foca na experiência do usuário como um todo.
Ao mesmo tempo que o sistema atende o propósito dele, tira também das pessoas mais experientes (pessoas acima dos 50 e
60 anos) o medo de usar tecnologia por acher avançada ou inadequada para eles.

## Subindo o ambiente

### 1. Pré-requisitos
- Docker + Docker Compose
- Java 17
- Maven 3.9+

### 2. Infraestrutura (PostgreSQL + MinIO)

```bash
docker-compose up -d
```

Acesse o console do MinIO em: http://localhost:9001  
Login: `minioadmin` / `minioadmin`  
Crie o bucket `guiadegurupi` manualmente na primeira vez.

### 3. Rodar a API

```bash
./mvnw spring-boot:run
```

A API sobe em: http://localhost:8080/api

---

## Endpoints públicos (consumidos pelo React)

### Categorias
```
GET /api/categorias              → lista todas as categorias ativas
GET /api/categorias/{slug}       → busca categoria por slug
```

### Empresas
```
GET /api/empresas                → lista todas as empresas ativas
GET /api/empresas/destaques      → apenas empresas em destaque (home)
GET /api/empresas/{slug}         → detalhe da empresa (ex: distribuidora-martins)
GET /api/empresas/categoria/{slug} → empresas por categoria
GET /api/empresas/buscar?q=...   → busca textual
```

### Produtos
```
GET /api/produtos                → produtos em destaque (home)
GET /api/produtos/{id}           → detalhe do produto
GET /api/produtos/empresa/{id}   → produtos de uma empresa
GET /api/produtos/categoria/{slug} → produtos por categoria
GET /api/produtos/buscar?q=...   → busca textual
GET /api/produtos/sazonais       → produtos do cerrado com época definida ⭐
```

---

## Integrando com o React (Bolt)

Substitua os arquivos de dados estáticos do Bolt:

**src/data/stores.ts** → buscar de `GET /api/empresas`  
**src/data/products.ts** → buscar de `GET /api/produtos`  
**src/data/categories.ts** → buscar de `GET /api/categorias`

Exemplo de fetch no React:
```typescript
// Substitui o import estático
const [empresas, setEmpresas] = useState<Store[]>([]);

useEffect(() => {
  fetch('http://localhost:8080/api/empresas')
    .then(r => r.json())
    .then(setEmpresas);
}, []);
```

---

## Empresas parceiras no MVP

| Empresa                  | Slug                      | Plano    |
|--------------------------|---------------------------|----------|
| Distribuidora Martins    | distribuidora-martins     | PREMIUM  |
| Comafe                   | comafe                    | DESTAQUE |
| Lubrificantes Bom Preço  | lubrificantes-bom-preco   | DESTAQUE |

---

## Planos de assinatura

| Plano    | Valor/mês | Max Produtos | Destaque | Banner Home |
|----------|-----------|--------------|----------|-------------|
| VITRINE  | R$ 97     | 10           | Não      | Não         |
| DESTAQUE | R$ 197    | Ilimitado    | Sim      | Não         |
| PREMIUM  | R$ 397    | Ilimitado    | Sim      | Sim         |

---

## Categorias do Guia de Gurupi

- `produtos-cerrado` — Frutos nativos, doces artesanais, Buriti 🌿
- `servicos-automotivos` — Mecânicas, lubrificantes, pneus
- `alimentacao` — Mercados, distribuidoras, restaurantes
- `profissionais` — Advogados, médicos, contadores
- `comercio-geral` — Lojas e distribuidoras locais
- `servicos-residenciais` — Eletricistas, encanadores, marceneiros
- `educacao-cursos` — Escolas, cursos técnicos
- `saude-bem-estar` — Clínicas, farmácias, academias

