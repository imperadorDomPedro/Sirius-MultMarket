-- V1__create_tables.sql
-- Guia da Cidade de Gurupi - TO
-- Schema inicial

CREATE TABLE categorias (
    id          BIGSERIAL PRIMARY KEY,
    slug        VARCHAR(80)  NOT NULL UNIQUE,
    nome        VARCHAR(100) NOT NULL,
    descricao   VARCHAR(300),
    icone       VARCHAR(50),
    cor         VARCHAR(60),
    bg_cor      VARCHAR(60),
    url_imagem  TEXT,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE planos (
    id                      BIGSERIAL PRIMARY KEY,
    nome                    VARCHAR(50)    NOT NULL UNIQUE,
    valor_mensal            NUMERIC(10,2)  NOT NULL,
    max_produtos            INTEGER        NOT NULL DEFAULT 10,
    aparece_em_destaque     BOOLEAN NOT NULL DEFAULT FALSE,
    banner_na_home          BOOLEAN NOT NULL DEFAULT FALSE,
    relatorio_visualizacoes BOOLEAN NOT NULL DEFAULT FALSE,
    descricao               VARCHAR(500)
);

CREATE TABLE empresas (
    id                  BIGSERIAL PRIMARY KEY,
    slug                VARCHAR(100) NOT NULL UNIQUE,
    nome                VARCHAR(150) NOT NULL,
    tagline             VARCHAR(200),
    descricao           TEXT,
    url_logo            TEXT,
    url_banner          TEXT,
    nome_responsavel    VARCHAR(150),
    whatsapp            VARCHAR(20),
    localizacao         VARCHAR(200),
    tempo_resposta      VARCHAR(50),
    politica_entrega    VARCHAR(300),
    politica_devolucao  VARCHAR(300),
    rating              DOUBLE PRECISION NOT NULL DEFAULT 0,
    total_avaliacoes    INTEGER          NOT NULL DEFAULT 0,
    total_seguidores    INTEGER          NOT NULL DEFAULT 0,
    verificado          BOOLEAN NOT NULL DEFAULT FALSE,
    destaque            BOOLEAN NOT NULL DEFAULT FALSE,
    ativo               BOOLEAN NOT NULL DEFAULT TRUE,
    plano_id            BIGINT REFERENCES planos(id),
    criado_em           TIMESTAMP DEFAULT NOW(),
    atualizado_em       TIMESTAMP DEFAULT NOW()
);

CREATE TABLE empresa_categorias (
    empresa_id   BIGINT NOT NULL REFERENCES empresas(id),
    categoria_id BIGINT NOT NULL REFERENCES categorias(id),
    PRIMARY KEY (empresa_id, categoria_id)
);

CREATE TABLE produtos (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(200)   NOT NULL,
    descricao        TEXT,
    preco            NUMERIC(10,2)  NOT NULL,
    preco_original   NUMERIC(10,2),
    rating           DOUBLE PRECISION NOT NULL DEFAULT 0,
    total_avaliacoes INTEGER          NOT NULL DEFAULT 0,
    estoque          INTEGER          NOT NULL DEFAULT 0,
    e_servico        BOOLEAN NOT NULL DEFAULT FALSE,
    destaque         BOOLEAN NOT NULL DEFAULT FALSE,
    ativo            BOOLEAN NOT NULL DEFAULT TRUE,
    epoca_disponivel VARCHAR(100),
    tempo_entrega    VARCHAR(100),
    empresa_id       BIGINT NOT NULL REFERENCES empresas(id),
    categoria_id     BIGINT NOT NULL REFERENCES categorias(id),
    criado_em        TIMESTAMP DEFAULT NOW()
);

CREATE TABLE produto_imagens (
    produto_id  BIGINT NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    url_imagem  TEXT   NOT NULL
);

CREATE TABLE produto_tags (
    produto_id BIGINT      NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    tag        VARCHAR(80) NOT NULL
);

CREATE TABLE produto_specs (
    produto_id BIGINT      NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    chave      VARCHAR(80) NOT NULL,
    valor      VARCHAR(200)
);

-- Índices para performance de busca
CREATE INDEX idx_empresas_slug      ON empresas(slug);
CREATE INDEX idx_empresas_destaque  ON empresas(destaque) WHERE ativo = TRUE;
CREATE INDEX idx_produtos_empresa   ON produtos(empresa_id);
CREATE INDEX idx_produtos_categoria ON produtos(categoria_id);
CREATE INDEX idx_produtos_destaque  ON produtos(destaque) WHERE ativo = TRUE;
