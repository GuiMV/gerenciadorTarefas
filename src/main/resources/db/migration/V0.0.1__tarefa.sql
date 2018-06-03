CREATE TABLE tarefa (
    id bigint NOT NULL,
    titulo varchar(128) NOT NULL,
    descricao varchar(512),
    data_inclusao date NOT NULL,
    data_edicao date NOT NULL,
    data_exclusao date,
    data_conclusao date
);


ALTER TABLE tarefa ADD CONSTRAINT tarefa_pk PRIMARY KEY (id);