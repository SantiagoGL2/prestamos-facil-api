ALTER TABLE tipo_prestamo ADD plazo_min_meses NUMBER(4);

UPDATE tipo_prestamo SET plazo_min_meses = 6   WHERE nombre = 'Crédito de libre inversión';
UPDATE tipo_prestamo SET plazo_min_meses = 12  WHERE nombre = 'Crédito de libranza';
UPDATE tipo_prestamo SET plazo_min_meses = 12  WHERE nombre = 'Compra de cartera';
UPDATE tipo_prestamo SET plazo_min_meses = 60  WHERE nombre = 'Crédito de vivienda';
UPDATE tipo_prestamo SET plazo_min_meses = 6   WHERE nombre = 'Crédito para independientes';
UPDATE tipo_prestamo SET plazo_min_meses = 6   WHERE nombre = 'Crédito agro';
UPDATE tipo_prestamo SET plazo_min_meses = 1   WHERE nombre = 'Crédito rotativo';

ALTER TABLE tipo_prestamo MODIFY plazo_min_meses NUMBER(4) NOT NULL;
