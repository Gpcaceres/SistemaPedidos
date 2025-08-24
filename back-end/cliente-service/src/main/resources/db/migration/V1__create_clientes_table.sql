CREATE TABLE clientes (
    id UUID PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    clave VARCHAR(80) NOT NULL,
    telefono VARCHAR(40),
    creado_en TIMESTAMPTZ NOT NULL
);