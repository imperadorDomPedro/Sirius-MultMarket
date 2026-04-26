# guia-gurupi-api

API REST do **Guia da Cidade de Gurupi - TO**  
Backend Spring Boot 3.2 / Java 17 que alimenta o frontend React gerado pelo Bolt.

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
