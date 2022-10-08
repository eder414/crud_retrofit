-- --------------------------------------------------------
-- Host:                         156.67.72.82
-- Versión del servidor:         10.5.12-MariaDB-cll-lve - MariaDB Server
-- SO del servidor:              Linux
-- HeidiSQL Versión:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para u770133628_curso_android
CREATE DATABASE IF NOT EXISTS `u770133628_curso_android` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `u770133628_curso_android`;

-- Volcando estructura para procedimiento u770133628_curso_android.P_Actualizar
DELIMITER //
CREATE PROCEDURE `P_Actualizar`(id int,
									nombre varchar(45),
																sexo int,
                                                                edad int,
                                                                _password varchar(45),
                                                                out response varchar(45))
BEGIN
	UPDATE `u770133628_curso_android`.`usuarios`
		SET
		usuario_nombre = nombre,
		`usuario_sexo` = sexo,
		`usuario_edad` = edad,
		`usuario_password` = _password
		WHERE `usuario_id` = id;
        
	SELECT COUNT(*)
			into @cantidadRegistros
			FROM `u770133628_curso_android`.`usuarios`
            WHERE  `usuario_id` = id
				AND usuario_nombre = nombre
				AND `usuario_sexo` = sexo
				AND `usuario_edad` = edad
				AND `usuario_password` = _password;
                
	if @cantidadRegistros > 0 and @cantidadRegistros is not null then
		SET response = 'OK';
	else 
		SET response = 'ERROR';
	end if;

END//
DELIMITER ;

-- Volcando estructura para procedimiento u770133628_curso_android.P_DeleteUsuario
DELIMITER //
CREATE PROCEDURE `P_DeleteUsuario`(id int,out response varchar(50))
BEGIN
	DELETE FROM `u770133628_curso_android`.`usuarios`
				WHERE usuario_id = id;
SELECT COUNT(*)
			into @cantidadRegistros
			FROM `u770133628_curso_android`.`usuarios`
            WHERE  `usuario_id` = id;
                
	if @cantidadRegistros = 0 then
		SET response = 'OK';
	else 
		SET response = 'ERROR';
	end if;
END//
DELIMITER ;

-- Volcando estructura para procedimiento u770133628_curso_android.P_InsertarUsuario
DELIMITER //
CREATE PROCEDURE `P_InsertarUsuario`(nombre varchar(45),
																sexo int,
                                                                edad int,
                                                                _password varchar(45),
                                                                out response varchar(45))
BEGIN
INSERT INTO `u770133628_curso_android`.`usuarios`
		(
		`usuario_nombre`,
		`usuario_sexo`,
		`usuario_edad`,
		`usuario_password`)
		VALUES
		(
		nombre,
		sexo,
		edad,
		_password);
	/*COMPROBAMOS INSERT*/
	SELECT LAST_INSERT_ID() into @lastId; 
	if @lastId > 0 and @lastId is not null then
		SET response = 'OK';
	else 
		SET response = 'ERROR';
	end if;
END//
DELIMITER ;

-- Volcando estructura para procedimiento u770133628_curso_android.P_ObtenerUsuario
DELIMITER //
CREATE PROCEDURE `P_ObtenerUsuario`()
BEGIN
	SELECT `usuarios`.`usuario_id`,
    `usuarios`.`usuario_nombre`,
    `usuarios`.`usuario_sexo`,
    `usuarios`.`usuario_edad`,
    `usuarios`.`usuario_password`
	FROM `u770133628_curso_android`.`usuarios`;
END//
DELIMITER ;

-- Volcando estructura para tabla u770133628_curso_android.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `usuario_id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_nombre` varchar(45) NOT NULL,
  `usuario_sexo` int(11) NOT NULL,
  `usuario_edad` int(11) NOT NULL,
  `usuario_password` varchar(45) NOT NULL,
  PRIMARY KEY (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla u770133628_curso_android.usuarios: ~9 rows (aproximadamente)
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`usuario_id`, `usuario_nombre`, `usuario_sexo`, `usuario_edad`, `usuario_password`) VALUES
	(2, 'L', 0, 45, '12'),
	(3, 'Juarez', 0, 45, '123456'),
	(5, 'marco', 0, 36, '123456'),
	(6, 'kevin', 0, 27, '123456789'),
	(7, 'Julian', 0, 25, '1234'),
	(8, 'Julian', 0, 25, '1234'),
	(9, 'mariano', 0, 30, '12345678'),
	(11, 'Ck', 0, 50, '12'),
	(13, 'Carnegro', 0, 30, '1234');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
