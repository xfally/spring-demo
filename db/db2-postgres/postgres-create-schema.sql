DROP TABLE IF EXISTS t_color;

CREATE TABLE t_color (
  id BIGINT NOT NULL,
  name VARCHAR(30) NOT NULL,
  note VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY(id)
);

COMMENT ON COLUMN t_color.id IS '颜色ID';
COMMENT ON COLUMN t_color.name IS '颜色名';
COMMENT ON COLUMN t_color.note IS '描述';

CREATE SEQUENCE public.my_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 99999999
    CACHE 1;

alter table public.t_color alter column id set default nextval('public.my_seq');
