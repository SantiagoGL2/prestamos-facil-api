INSERT INTO usuario (nombres, apellidos, email, tipo_documento_id, numero_documento,
  salario_base, password_hash, rol, fecha_creacion)
VALUES ('Analista', 'Prueba principal', 'analista.prueba@prestamosfacil.com',
  (SELECT id FROM tipo_documento WHERE codigo = 'CC'), '1000000001', 5000000,
  '$2b$10$..FNnXKKb8nVbS8hX/Pp0e7JfQR4Dyn/eg4Ge2oA8TyrLBt8bVTWi', 'ANALISTA', SYSTIMESTAMP);
