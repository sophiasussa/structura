-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS structura;
USE structura;

-- Tabelas
CREATE TABLE funcionario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(20),
    rg VARCHAR(20),
    telefone VARCHAR(25) NOT NULL,
    dataAdmissao DATE,
    salario DOUBLE
);

CREATE TABLE cliente (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(20),
    rg VARCHAR(20),
    telefone VARCHAR(25) NOT NULL
);

CREATE TABLE fornecedor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa VARCHAR(100) NOT NULL UNIQUE,
    vendedor VARCHAR(100),
    cnpj VARCHAR(20),
    ie VARCHAR(20),
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    descriProdutos VARCHAR(255)
);

CREATE TABLE material (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE unidMedida (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE modelo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produto (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    quantidadeAtual INT,
    quantidadeMinima INT,
    custoUnitario DOUBLE,
    material_id INT,
    unid_medida_id INT,
    modelo_id INT,
    CONSTRAINT fk_produto_material FOREIGN KEY (material_id) REFERENCES material(id),
    CONSTRAINT fk_produto_unid_medida FOREIGN KEY (unid_medida_id) REFERENCES unidMedida(id),
    CONSTRAINT fk_produto_modelo FOREIGN KEY (modelo_id) REFERENCES modelo(id)
);

CREATE TABLE os (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    status_os VARCHAR(20),
    entrega_os VARCHAR(20),
    endereco VARCHAR(255),
    dataa DATE,
    datap DATE,
    observacoes VARCHAR(255),
    cliente_id BIGINT NOT NULL,
    funcionario_id BIGINT,
    CONSTRAINT fk_os_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_os_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);

CREATE TABLE produto_os (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    produto_id BIGINT,
    os_id BIGINT,
    CONSTRAINT fk_produto_os_produto FOREIGN KEY (produto_id) REFERENCES produto(id),
    CONSTRAINT fk_produto_os_os FOREIGN KEY (os_id) REFERENCES os(id)
);

CREATE TABLE agenda (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(50) NOT NULL,
    descricao VARCHAR(255),
    endereco VARCHAR(255),
    dataa DATETIME,
    statuss VARCHAR(20),
    funcionario_id BIGINT,
    CONSTRAINT fk_agenda_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);

CREATE TABLE projeto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status_pr VARCHAR(20),
    data DATE,
    valor DOUBLE,
    desconto DOUBLE,
    valor_final DOUBLE,
    descricao VARCHAR(100),
    medidas VARCHAR(100),
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_projeto_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Obs: Os dados a seguir são fictícios
 
-- Exemplo de insert na tabela User / Senha para testes: 123456
INSERT INTO User (username, password) VALUES ('admin', '$2a$12$oOgqWDe7GdLGxaMylthbquMadxjFdIz.8DvRKBbGqZcma58OZdniu');

INSERT INTO funcionario (nome, cpf, rg, telefone, dataAdmissao, salario) VALUES
('Lucas Amaral Ferreira', '421.563.789-10', 'MG-15.234.678', '(31) 91234-5678', '2021-03-15', 3200.00),
('Juliana Rocha Silveira', '839.210.654-22', 'SP-32.987.654', '(11) 99876-5432', '2020-11-10', 3500.00),
('Rafael Mendes Coutinho', '650.984.123-45', 'RJ-56.123.789', '(21) 98877-1122', '2019-06-01', 3000.00),
('Marina Lopes Andrade', '745.369.852-01', 'RS-18.765.432', '(51) 91782-3344', '2022-02-28', 2800.00),
('Thiago Brito Nogueira', '564.132.879-90', 'BA-29.546.812', '(71) 99811-2244', '2018-09-17', 4000.00),
('Carla Regina Dias', '214.879.366-77', 'PE-11.223.344', '(81) 98765-9988', '2023-05-05', 3100.00),
('Eduardo Lima Vasconcelos', '999.111.333-00', 'SC-55.667.889', '(48) 98888-7777', '2021-07-20', 3300.00);

INSERT INTO cliente (nome, cpf, rg, telefone) VALUES
('Amanda Soares Lima', '321.654.987-00', 'SP-12.345.678', '(11) 91234-5678'),
('Bruno Costa Andrade', '456.789.123-11', 'RJ-87.654.321', '(21) 98876-5432'),
('Camila Ribeiro Martins', '789.123.456-22', 'MG-45.678.912', '(31) 99777-1234'),
('Diego Fernandes Rocha', '159.753.486-33', 'BA-23.456.789', '(71) 99888-1122'),
('Elaine Cristina Silva', '852.741.963-44', 'RS-34.567.890', '(51) 91717-1717'),
('Felipe Gonçalves Prado', '963.852.741-55', 'PR-56.789.012', '(41) 91212-3434'),
('Gabriela Souza Duarte', '741.258.369-66', 'PE-65.432.109', '(81) 98765-4321'),
('Henrique Melo Barros', '369.147.258-77', 'SC-76.543.210', '(48) 99999-1111'),
('Isabela Monteiro Leal', '258.369.147-88', 'CE-87.654.321', '(85) 99666-2233'),
('João Victor Almeida', '147.258.369-99', 'PA-98.765.432', '(91) 98787-3434');

INSERT INTO fornecedor (empresa, vendedor, cnpj, ie, telefone, email, descriProdutos) VALUES
('Pedra Real Ltda', 'Marcos Tavares', '12.345.678/0001-90', 'IS-1234567', '(11) 98888-1122', 'marcos@pedrareal.com', 'Mármores nobres, granitos escuros'),
('Marmoraria Sul Mineração', 'Cláudia Fonseca', '98.765.432/0001-01', 'MG-9876543', '(31) 91234-5566', 'claudia@sulmineracao.com', 'Granitos brutos e acabados'),
('Pedras Brasil Eireli', 'Rafael Moraes', '23.456.789/0001-22', 'RJ-2345678', '(21) 99887-6543', 'rafael@pedrasbrasil.com', 'Quartzitos importados, mármores brancos'),
('Marmotech Distribuidora', 'Fernanda Dias', '34.567.890/0001-33', 'SP-3456789', '(11) 91122-3344', 'fernanda@marmotech.com', 'Ferramentas e blocos de pedra'),
('Grupo Rochas do Norte', 'Bruno Cavalcante', '45.678.901/0001-44', 'AM-4567890', '(92) 98888-9090', 'bruno@rochasdonorte.com', 'Materiais ornamentais e pedras raras');

INSERT INTO unidMedida (nome) VALUES
('m²'),
('cm'),
('m'),
('unidade'),
('par');

INSERT INTO modelo (nome) VALUES
('Polido'),
('Bruto'),
('Escovado'),
('Levigado'),
('Flameado');

INSERT INTO material (nome) VALUES
('Mármore'),
('Granito'),
('Quartzo'),
('Quartzito'),
('Ônix');

INSERT INTO produto (nome, quantidadeAtual, quantidadeMinima, custoUnitario, material_id, unid_medida_id, modelo_id) VALUES
('Preto São Gabriel', 50, 10, 250.00, 2, 1, 1),
('Branco Espírito Santo', 30, 5, 300.00, 1, 1, 3),
('Verde Ubatuba', 20, 7, 180.00, 2, 1, 2),
('Azul Macaúbas', 15, 3, 500.00, 4, 1, 5),
('Crema Marfil', 25, 8, 270.00, 1, 1, 4),
('Travertino Romano', 18, 5, 350.00, 1, 1, 2),
('Branco Taj Mahal', 10, 4, 550.00, 4, 1, 3),
('Onyx Preto', 12, 3, 900.00, 5, 1, 5),
('Amarelo Ornamental', 22, 6, 230.00, 2, 1, 1),
('Rosa do Sul', 8, 2, 700.00, 4, 1, 4),
('Bege Bahia', 14, 5, 280.00, 1, 1, 2),
('Vermelho Alicante', 16, 6, 320.00, 2, 1, 3),
('Marrom Imperador', 11, 4, 360.00, 1, 1, 5),
('Cinza Corumbá', 19, 7, 210.00, 2, 1, 2),
('Cristal Branco', 9, 3, 480.00, 3, 1, 4);

INSERT INTO os (
    status_os, entrega_os, endereco, dataa, datap, observacoes, cliente_id, funcionario_id
) VALUES
('EM_ANDAMENTO', 'PRIMEIRA_ENTREGA', 'Av. das Pedras, 123, Bairro Centro', '2025-06-01', '2025-06-10', 'Instalação de bancada de mármore branco.', 1, 2),
('CONCLUIDA', 'ENTREGA_FINAL', 'Rua do Granito, 456, Bairro Jardim', '2025-05-15', '2025-05-22', 'Piso em granito preto instalado.', 2, 3),
('ABERTA', 'NAO_ENTREGUE', 'Av. Quartzo, 789, Bairro Industrial', '2025-06-05', '2025-06-15', 'Reforma de cozinha com bancada de quartzo.', 3, 4),
('CANCELADA', 'NAO_DEFINIDA', 'Rua Ônix, 101, Bairro Nobre', '2025-05-20', '2025-05-25', 'Cliente cancelou antes do início.', 4, 5),
('EM_ANDAMENTO', 'SEGUNDA_ENTREGA', 'Av. Jade, 202, Bairro Sul', '2025-06-03', '2025-06-12', 'Colocação de revestimento em ônix.', 5, 1);

INSERT INTO produto_os (produto_id, os_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 3),
(6, 3),
(7, 4),
(8, 4),
(9, 5),
(10, 5),
(11, 1),
(12, 2),
(13, 3),
(14, 4),
(15, 5);

INSERT INTO projeto (
    status_pr, data, valor, desconto, valor_final, descricao, medidas, cliente_id
) VALUES
('NOVO', '2025-06-01', 8000.00, 5.00, 7600.00, 'Bancada de mármore Carrara para cozinha', '2.5m x 0.6m', 1),
('EM_ANALISE', '2025-05-15', 12000.00, 10.00, 10800.00, 'Piso de granito preto São Gabriel', '30m²', 2),
('APROVADO', '2025-05-20', 5000.00, 0.00, 5000.00, 'Revestimento de parede em quartzo branco', '15m²', 3),
('EM_EXECUCAO', '2025-06-05', 4500.00, 7.00, 4185.00, 'Soleira em ônix para escada', '1.2m x 0.4m', 4),
('PAUSADO', '2025-05-25', 6000.00, 8.00, 5520.00, 'Mesa de jantar em granito Verde Ubatuba', '2.0m x 1.0m', 5);

INSERT INTO agenda (
    titulo, descricao, endereco, dataa, statuss, funcionario_id
) VALUES
('Reunião com cliente', 'Discutir detalhes do projeto de bancada', 'Av. das Pedras, 123', '2025-06-10 09:00:00', 'ABERTA', 1),
('Visita técnica', 'Inspeção do local para instalação', 'Rua do Granito, 456', '2025-06-12 14:00:00', 'EM_ANDAMENTO', 2),
('Entrega material', 'Entrega de mármore para obra', 'Av. Quartzo, 789', '2025-06-08 10:30:00', 'CONCLUIDA', 3),
('Treinamento equipe', 'Capacitação em segurança do trabalho', 'Sede da empresa', '2025-06-15 13:00:00', 'ABERTA', 4),
('Manutenção de equipamentos', 'Revisão das máquinas de corte', 'Oficina central', '2025-06-11 08:00:00', 'EM_ANDAMENTO', 5),
('Aprovação de orçamento', 'Reunião para aprovação do orçamento', 'Sala de reuniões', '2025-06-09 16:00:00', 'CONCLUIDA', 1),
('Entrega da pia', 'Confirmação e entrega da pia de granito', 'Rua Ônix, 101', '2025-06-14 11:00:00', 'ABERTA', 2),
('Revisão projeto', 'Avaliar ajustes no projeto de revestimento', 'Av. Jade, 202', '2025-06-13 15:00:00', 'EM_ANDAMENTO', 3),
('Consulta com fornecedor', 'Negociação de preços de materiais', 'Sede da empresa', '2025-06-07 09:00:00', 'CONCLUIDA', 4),
('Planejamento semanal', 'Planejamento das atividades da semana', 'Escritório central', '2025-06-16 08:30:00', 'ABERTA', 5);

