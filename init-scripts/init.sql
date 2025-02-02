-- Verificar e criar o banco de dados 'keycloak'
SELECT 'CREATE DATABASE keycloak'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak')
\gexec

-- Verificar e criar o banco de dados 'itelectric'
SELECT 'CREATE DATABASE itelectric'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'itelectric')
\gexec
