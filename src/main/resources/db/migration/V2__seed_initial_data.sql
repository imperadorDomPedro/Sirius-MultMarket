-- V2__seed_initial_data.sql
-- Dados iniciais: planos, categorias do cerrado/Gurupi e as 3 empresas parceiras

-- ===========================
-- PLANOS
-- ===========================
INSERT INTO planos (nome, valor_mensal, max_produtos, aparece_em_destaque, banner_na_home, relatorio_visualizacoes, descricao)
VALUES
    ('VITRINE',   97.00,  10, FALSE, FALSE, FALSE, 'Logo, endereço, WhatsApp, horário e até 10 produtos'),
    ('DESTAQUE',  197.00, -1, TRUE,  FALSE, FALSE, 'Tudo do Vitrine + aparece primeiro nas buscas, catálogo ilimitado'),
    ('PREMIUM',   397.00, -1, TRUE,  TRUE,  TRUE,  'Tudo do Destaque + banner na home e relatório de visualizações');

-- ===========================
-- CATEGORIAS (nichos de Gurupi/TO)
-- ===========================
INSERT INTO categorias (slug, nome, descricao, icone, cor, bg_cor) VALUES
    ('produtos-cerrado',       'Produtos do Cerrado',     'Frutos nativos, doces artesanais e produtos do bioma cerrado', 'Leaf',       'text-green-700',  'bg-green-50'),
    ('servicos-automotivos',   'Serviços Automotivos',    'Mecânicas, lubrificantes, pneus e manutenção veicular',        'Car',        'text-slate-600',  'bg-slate-50'),
    ('alimentacao',            'Alimentação',             'Restaurantes, mercados, distribuidoras e gêneros alimentícios', 'UtensilsCrossed', 'text-orange-600', 'bg-orange-50'),
    ('profissionais',          'Profissionais',           'Advogados, médicos, contadores, engenheiros e outros',         'Briefcase',  'text-blue-600',   'bg-blue-50'),
    ('comercio-geral',         'Comércio Geral',          'Lojas, distribuidoras, atacado e varejo local',                'ShoppingBag','text-purple-600', 'bg-purple-50'),
    ('servicos-residenciais',  'Serviços Residenciais',   'Eletricistas, encanadores, pintores, marceneiros',             'Home',       'text-amber-600',  'bg-amber-50'),
    ('educacao-cursos',        'Educação e Cursos',       'Escolas, cursos técnicos, idiomas e reforço escolar',          'BookOpen',   'text-teal-600',   'bg-teal-50'),
    ('saude-bem-estar',        'Saúde e Bem-Estar',       'Clínicas, farmácias, academia, nutrição e estética',           'Heart',      'text-rose-500',   'bg-rose-50');

-- ===========================
-- EMPRESAS PARCEIRAS (MVP)
-- ===========================

-- 1. Distribuidora Martins
INSERT INTO empresas (slug, nome, tagline, descricao, nome_responsavel, whatsapp, localizacao,
    tempo_resposta, politica_entrega, politica_devolucao,
    verificado, destaque, plano_id)
VALUES (
    'distribuidora-martins',
    'Distribuidora Martins',
    'O abastecimento do sul do Tocantins',
    'Distribuidora com mais de 20 anos atendendo Gurupi e região. Trabalhamos com gêneros alimentícios, bebidas, produtos de limpeza e higiene para atacado e varejo. Entrega rápida e crédito facilitado para lojistas.',
    'Família Martins',
    '6399999-0001',
    'Gurupi, TO',
    '< 2 horas',
    'Entrega em Gurupi e região sul do TO',
    'Troca mediante nota fiscal em até 7 dias',
    TRUE, TRUE,
    (SELECT id FROM planos WHERE nome = 'PREMIUM')
);

INSERT INTO empresa_categorias (empresa_id, categoria_id)
VALUES (
    (SELECT id FROM empresas WHERE slug = 'distribuidora-martins'),
    (SELECT id FROM categorias WHERE slug = 'alimentacao')
), (
    (SELECT id FROM empresas WHERE slug = 'distribuidora-martins'),
    (SELECT id FROM categorias WHERE slug = 'comercio-geral')
);

-- 2. Comafe
INSERT INTO empresas (slug, nome, tagline, descricao, nome_responsavel, whatsapp, localizacao,
    tempo_resposta, politica_entrega, politica_devolucao,
    verificado, destaque, plano_id)
VALUES (
    'comafe',
    'Comafe',
    'Seu parceiro em Gurupi e região',
    'A Comafe atua no comércio local oferecendo soluções completas para empresas e consumidores finais. Com atendimento personalizado e agilidade, somos referência no sul do Tocantins.',
    'Equipe Comafe',
    '6399999-0002',
    'Gurupi, TO',
    '< 1 hora',
    'Consultar disponibilidade para entrega',
    'Conforme política da loja',
    TRUE, TRUE,
    (SELECT id FROM planos WHERE nome = 'DESTAQUE')
);

INSERT INTO empresa_categorias (empresa_id, categoria_id)
VALUES (
    (SELECT id FROM empresas WHERE slug = 'comafe'),
    (SELECT id FROM categorias WHERE slug = 'comercio-geral')
);

-- 3. Lubrificantes Bom Preço
INSERT INTO empresas (slug, nome, tagline, descricao, nome_responsavel, whatsapp, localizacao,
    tempo_resposta, politica_entrega, politica_devolucao,
    verificado, destaque, plano_id)
VALUES (
    'lubrificantes-bom-preco',
    'Lubrificantes Bom Preço',
    'Cuide do seu veículo sem pesar no bolso',
    'Especialistas em lubrificantes, filtros, aditivos e acessórios automotivos. Atendemos frotas, mecânicas e consumidores finais com os melhores preços do sul do Tocantins. Marcas como Mobil, Castrol, Shell e Petronas.',
    'Equipe Bom Preço',
    '6399999-0003',
    'Gurupi, TO',
    '< 30 min',
    'Retirada na loja ou entrega local',
    'Produto com defeito de fábrica em até 30 dias',
    TRUE, TRUE,
    (SELECT id FROM planos WHERE nome = 'DESTAQUE')
);

INSERT INTO empresa_categorias (empresa_id, categoria_id)
VALUES (
    (SELECT id FROM empresas WHERE slug = 'lubrificantes-bom-preco'),
    (SELECT id FROM categorias WHERE slug = 'servicos-automotivos')
);

-- ===========================
-- PRODUTOS DAS 3 EMPRESAS
-- ===========================

-- Distribuidora Martins
INSERT INTO produtos (nome, descricao, preco, preco_original, estoque, destaque, empresa_id, categoria_id, tempo_entrega)
VALUES (
    'Caixa de Biscoito Cream Cracker 20un',
    'Caixa com 20 pacotes de biscoito cream cracker. Ideal para revenda em mercearias e padarias.',
    45.90, 52.00, 200, TRUE,
    (SELECT id FROM empresas WHERE slug = 'distribuidora-martins'),
    (SELECT id FROM categorias WHERE slug = 'alimentacao'),
    '1-2 dias úteis'
);

INSERT INTO produtos (nome, descricao, preco, estoque, e_servico, destaque, empresa_id, categoria_id, tempo_entrega)
VALUES (
    'Fardo Água Mineral 500ml 12un',
    'Fardo com 12 garrafas de água mineral 500ml. Produto para revenda.',
    18.50, 500, FALSE, FALSE,
    (SELECT id FROM empresas WHERE slug = 'distribuidora-martins'),
    (SELECT id FROM categorias WHERE slug = 'alimentacao'),
    '1 dia útil'
);

-- Lubrificantes Bom Preço
INSERT INTO produtos (nome, descricao, preco, preco_original, estoque, destaque, empresa_id, categoria_id, tempo_entrega)
VALUES (
    'Óleo Mobil Super 5W-30 1L',
    'Óleo lubrificante sintético Mobil Super 5W-30 para motores a gasolina e flex. Alta proteção contra desgaste.',
    42.90, 49.90, 80, TRUE,
    (SELECT id FROM empresas WHERE slug = 'lubrificantes-bom-preco'),
    (SELECT id FROM categorias WHERE slug = 'servicos-automotivos'),
    'Retirada imediata'
);

INSERT INTO produtos (nome, descricao, preco, estoque, e_servico, destaque, empresa_id, categoria_id, tempo_entrega)
VALUES (
    'Troca de Óleo Completa',
    'Serviço de troca de óleo com dreno, substituição do filtro e verificação dos níveis. Agendamento pelo WhatsApp.',
    89.90, 1, TRUE, TRUE,
    (SELECT id FROM empresas WHERE slug = 'lubrificantes-bom-preco'),
    (SELECT id FROM categorias WHERE slug = 'servicos-automotivos'),
    'Agendamento'
);
