CREATE SEQUENCE usuario_id_seq;

CREATE TABLE usuario (
  id integer default nextval('usuario_id_seq') primary key,
  login character varying(10) not null,
  senha character varying(10) not null
);

INSERT INTO usuario (login, senha) VALUES ('login1', 'senha1');
INSERT INTO usuario (login, senha) VALUES ('login2', 'senha2');
INSERT INTO usuario (login, senha) VALUES ('login3', 'senha3');

CREATE SEQUENCE perfil_id_seq;

CREATE TABLE perfil (
  id integer default nextval('perfil_id_seq') primary key,
  nome character varying(20) not null
);

INSERT INTO perfil (nome) VALUES ('ROLE_PERFIL1');
INSERT INTO perfil (nome) VALUES ('ROLE_PERFIL2');

CREATE TABLE usuario_perfil (
  id_usuario integer references usuario(id),
  id_perfil integer references perfil(id)
);

INSERT INTO usuario_perfil (id_usuario, id_perfil) VALUES (1, 1);
INSERT INTO usuario_perfil (id_usuario, id_perfil) VALUES (2, 2);
INSERT INTO usuario_perfil (id_usuario, id_perfil) VALUES (3, 1);
INSERT INTO usuario_perfil (id_usuario, id_perfil) VALUES (3, 2);