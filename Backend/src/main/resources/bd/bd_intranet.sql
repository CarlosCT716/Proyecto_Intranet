DROP DATABASE IF EXISTS bd_intranet;
CREATE DATABASE bd_intranet;
USE bd_intranet;

CREATE TABLE tb_rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(20) NOT NULL 
);

CREATE TABLE tb_carrera (
    id_carrera INT AUTO_INCREMENT PRIMARY KEY,
    nombre_carrera VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE tb_ciclo (
    id_ciclo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_ciclo VARCHAR(20) NOT NULL 
);

CREATE TABLE tb_estado_asistencia (
    id_estado INT PRIMARY KEY,
    descripcion VARCHAR(20) NOT NULL 
);

CREATE TABLE tb_estado_pago (
    id_estado_pago INT PRIMARY KEY,
    descripcion VARCHAR(20) NOT NULL 
);

CREATE TABLE tb_usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, 
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE, 
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    dni VARCHAR(8) NOT NULL UNIQUE,
    id_rol INT NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BIT DEFAULT 1,
    FOREIGN KEY (id_rol) REFERENCES tb_rol(id_rol)
);

CREATE TABLE tb_auditoria (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT, 
    accion VARCHAR(50) NOT NULL, 
    tabla_afectada VARCHAR(50),
    detalle_anterior TEXT,
    detalle_nuevo TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_origen VARCHAR(45),
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario)
);
CREATE TABLE tb_curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso VARCHAR(100) NOT NULL,
    creditos INT NOT NULL,
    id_carrera INT NOT NULL,
    id_ciclo INT NOT NULL,
    id_profesor INT NOT NULL,
    cupo_maximo INT DEFAULT 30,
    activo BIT default 1,
    FOREIGN KEY (id_carrera) REFERENCES tb_carrera(id_carrera),
    FOREIGN KEY (id_ciclo) REFERENCES tb_ciclo(id_ciclo),
    FOREIGN KEY (id_profesor) REFERENCES tb_usuario(id_usuario)
);

CREATE TABLE tb_aula (
	id_aula INT auto_increment PRIMARY KEY,
    activo BIT default 1,
    descripcion VARCHAR(250) NOT NULL
);

CREATE TABLE tb_horario (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_curso INT NOT NULL,
    dia_semana VARCHAR(15) NOT NULL, 
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    id_aula INT NOT NULL,
    activo BIT default 1,
    FOREIGN KEY (id_curso) REFERENCES tb_curso(id_curso),
    foreign key (id_aula) REFERENCES tb_aula(id_aula)
);

CREATE TABLE tb_sesion_clase (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_curso INT NOT NULL,
    fecha DATE NOT NULL, -- 2024-10-23
    tema_tratado VARCHAR(200),
    observaciones_docente TEXT,
    estado_sesion VARCHAR(20) DEFAULT 'PROGRAMADA', 
    FOREIGN KEY (id_curso) REFERENCES tb_curso(id_curso),
    UNIQUE(id_curso, fecha)
);

CREATE TABLE tb_matricula (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    periodo VARCHAR(10) NOT NULL, 
    fecha_matricula DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_carrera INT NOT NULL, 
    id_ciclo INT NOT NULL,
    FOREIGN KEY (id_alumno) REFERENCES tb_usuario(id_usuario),
    FOREIGN KEY (id_carrera) REFERENCES tb_carrera(id_carrera),
    FOREIGN KEY (id_ciclo) REFERENCES tb_ciclo(id_ciclo)
);

CREATE TABLE tb_detalle_matricula (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_matricula INT NOT NULL,
    id_curso INT NOT NULL,
    FOREIGN KEY (id_matricula) REFERENCES tb_matricula(id_matricula),
    FOREIGN KEY (id_curso) REFERENCES tb_curso(id_curso),
    UNIQUE(id_matricula, id_curso) 
);


CREATE TABLE tb_pago (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_matricula INT NOT NULL,
    concepto VARCHAR(100) NOT NULL, 
    monto DECIMAL(10,2) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    fecha_pago DATETIME NULL,
    id_estado_pago INT DEFAULT 1,
    FOREIGN KEY (id_matricula) REFERENCES tb_matricula(id_matricula),
    FOREIGN KEY (id_estado_pago) REFERENCES tb_estado_pago(id_estado_pago)
);

CREATE TABLE tb_nota (
    id_nota INT AUTO_INCREMENT PRIMARY KEY,
    id_detalle_matricula INT NOT NULL, 
    nota1 DECIMAL(4,2) DEFAULT 0,
    nota2 DECIMAL(4,2) DEFAULT 0,
    nota3 DECIMAL(4,2) DEFAULT 0,
    examen_final DECIMAL(4,2) DEFAULT 0,
    promedio_final DECIMAL(4,2) GENERATED ALWAYS AS ((nota1*0.2 + nota2*0.2 + nota3*0.2 + examen_final*0.4)) STORED,
    FOREIGN KEY (id_detalle_matricula) REFERENCES tb_detalle_matricula(id_detalle)
);

CREATE TABLE tb_asistencia (
    id_asistencia INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT NOT NULL, 
    id_alumno INT NOT NULL, 
    id_estado INT NOT NULL DEFAULT 1, 
    observacion VARCHAR(100),
    FOREIGN KEY (id_sesion) REFERENCES tb_sesion_clase(id_sesion),
    FOREIGN KEY (id_alumno) REFERENCES tb_usuario(id_usuario),
    FOREIGN KEY (id_estado) REFERENCES tb_estado_asistencia(id_estado),
    UNIQUE(id_sesion, id_alumno) 
);



CREATE TABLE tb_ia_historial (
    id_interaccion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT, 
    pregunta_usuario TEXT,
    respuesta_ia TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO tb_rol (nombre_rol) VALUES ('ROLE_ADMIN'), ('ROLE_PROFESOR'), ('ROLE_ALUMNO');

INSERT INTO tb_estado_asistencia VALUES (1, 'Presente'), (2, 'Falta'), (3, 'Tardanza'), (4, 'Justificado');
INSERT INTO tb_estado_pago VALUES (1, 'Pendiente'), (2, 'Pagado'), (3, 'Vencido');


INSERT INTO tb_carrera (nombre_carrera, descripcion) VALUES ('Computación e Informática', 'Desarrollo de software y redes');
INSERT INTO tb_ciclo (nombre_ciclo) VALUES ('I'), ('II'), ('III'), ('IV'), ('V'), ('VI');


INSERT INTO tb_usuario (username, password, nombres, apellidos, email, dni, id_rol) VALUES 
('admin', 'admin123', 'Administrador', 'Sistema', 'admin@cibertec.edu.pe', '00000001', 1),
('profesor1', 'profesor1', 'Juan', 'Perez Docente', 'juan.perez@cibertec.edu.pe', '10000001', 2),
('profesor2', '$2a$10$X/xyz...', 'Maria', 'Lopez Docente', 'maria.lopez@cibertec.edu.pe', '10000002', 2),
('alumno1', 'alumno1', 'Carlos', 'Estudiante Uno', 'carlos.uno@cibertec.edu.pe', '20000001', 3),
('alumno2', '$2a$10$X/xyz...', 'Ana', 'Estudiante Dos', 'ana.dos@cibertec.edu.pe', '20000002', 3);


INSERT INTO tb_curso (nombre_curso, creditos, id_carrera, id_ciclo, id_profesor) VALUES 
('Lenguaje de Programación I', 4, 1, 1, 2), 
('Base de Datos I', 3, 1, 1, 3);

INSERT INTO tb_aula( descripcion) VALUES	
('A001'),
('A002');

INSERT INTO tb_horario (id_curso, dia_semana, hora_inicio, hora_fin, id_aula) VALUES 
(1, 'LUNES', '08:00:00', '10:00:00', '1'),
(1, 'MIERCOLES', '08:00:00', '10:00:00', '2');


INSERT INTO tb_sesion_clase (id_curso, fecha, estado_sesion) VALUES 
(1, '2024-10-21', 'FINALIZADA'), 
(1, '2024-10-23', 'PROGRAMADA');

INSERT INTO tb_matricula (id_alumno, periodo, id_carrera, id_ciclo) VALUES 
(4, '2024-2', 1, 1), 
(5, '2024-2', 1, 1); 

INSERT INTO tb_detalle_matricula (id_matricula, id_curso) VALUES 
(1, 1), (1, 2), 
(2, 1);    

INSERT INTO tb_pago (id_matricula, concepto, monto, fecha_vencimiento, id_estado_pago) VALUES 
(1, 'Matrícula 2024-2', 300.00, '2024-08-01', 2), 
(1, 'Cuota 1', 550.00, '2024-09-01', 1);

INSERT INTO tb_nota (id_detalle_matricula) VALUES (1), (2), (3);


SELECT * FROM tb_usuario;
select * from tb_nota;
select * from tb_carrera;
select * from tb_curso;
select * from tb_matricula;
select * from tb_auditoria;

select * from tb_ia_faq;
select * from tb_ia_historial;

update tb_usuario set activo = 1 where id_usuario = 6
