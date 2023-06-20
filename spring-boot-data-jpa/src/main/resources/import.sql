/*Table clientes*/
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('John', 'Doe', 'john.doe@gmail.com', '2023-05-27','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Jane', 'Smith', 'jane.smith@gmail.com', '2023-05-28','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Mike', 'Johnson', 'mike.johnson@gmail.com', '2023-05-29','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Sarah', 'Williams', 'sarah.williams@gmail.com', '2023-05-30','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('David', 'Brown', 'david.brown@gmail.com', '2023-05-31','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Emily', 'Jones', 'emily.jones@gmail.com', '2023-06-01','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Daniel', 'Miller', 'daniel.miller@gmail.com', '2023-06-02','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Olivia', 'Davis', 'olivia.davis@gmail.com', '2023-06-03','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('James', 'Wilson', 'james.wilson@gmail.com', '2023-06-04','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Emma', 'Anderson', 'emma.anderson@gmail.com', '2023-06-05','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('William', 'Martinez', 'william.martinez@gmail.com', '2023-06-06','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Ava', 'Taylor', 'ava.taylor@gmail.com', '2023-06-07','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Benjamin', 'Thomas', 'benjamin.thomas@gmail.com', '2023-06-08','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Mia', 'Hernandez', 'mia.hernandez@gmail.com', '2023-06-09','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Alexander', 'Moore', 'alexander.moore@gmail.com', '2023-06-10','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Sofia', 'Clark', 'sofia.clark@gmail.com', '2023-06-11','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Henry', 'Lee', 'henry.lee@gmail.com', '2023-06-12','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Charlotte', 'Walker', 'charlotte.walker@gmail.com', '2023-06-13','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Joseph', 'Lopez', 'joseph.lopez@gmail.com', '2023-06-14','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Amelia', 'Green', 'amelia.green@gmail.com', '2023-06-15','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Michael', 'Adams', 'michael.adams@gmail.com', '2023-06-16','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Sophia', 'Gonzalez', 'sophia.gonzalez@gmail.com', '2023-06-17','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Daniel', 'Wilson', 'daniel.wilson@gmail.com', '2023-06-18','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Isabella', 'Hall', 'isabella.hall@gmail.com', '2023-06-19','');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Matthew', 'Young', 'matthew.young@gmail.com', '2023-06-20','');


/*Table productos*/
INSERT INTO productos(nombre, precio, create_at) VALUES ('Lavadora Samsung', 500, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Refrigerador LG', 800, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Televisor Sony', 600, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Microondas Panasonic', 200, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Aspiradora Dyson', 300, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Licuadora Oster', 80, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Horno Tostador Black & Decker', 150, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Secadora Whirlpool', 400, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Plancha Philips', 50, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Cafetera Nespresso', 150, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('iPhone 13', 1200, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Samsung Galaxy S21', 1000, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Google Pixel 6', 900, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('OnePlus 9 Pro', 800, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Xiaomi Mi 11', 700, NOW());

/*
Tabla usuarios
CREATE TABLE `db_springboot`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);
  */

  /*
  Tabla authorities
  CREATE TABLE `db_springboot`.`authorities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id_authority_unique` (`user_id` ASC, `authority` ASC) VISIBLE,
  CONSTRAINT `fk_authorities_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `db_springboot`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

  */