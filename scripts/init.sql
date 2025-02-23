-- Verificar e criar o banco de dados 'keycloak'
SELECT 'CREATE DATABASE keycloak' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak') \gexec

-- Verificar e criar o banco de dados 'iteletric'
SELECT 'CREATE DATABASE iteletric' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'iteletric') \gexec
