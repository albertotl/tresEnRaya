-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-06-2021 a las 05:45:24
-- Versión del servidor: 10.4.17-MariaDB
-- Versión de PHP: 8.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `tresenraya_bd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultado_partida`
--

CREATE TABLE `resultado_partida` (
  `cod` varchar(50) NOT NULL,
  `idUsuario1` varchar(50) NOT NULL,
  `idUsuario2` varchar(50) NOT NULL,
  `fichaUsuario1` varchar(5) NOT NULL,
  `fichaUsuario2` varchar(5) NOT NULL,
  `fecha` varchar(50) NOT NULL,
  `ganador` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `resultado_partida`
--

INSERT INTO `resultado_partida` (`cod`, `idUsuario1`, `idUsuario2`, `fichaUsuario1`, `fichaUsuario2`, `fecha`, `ganador`) VALUES
('1', 'a', 'q', 'O', 'X', '15-jun-AM 05:43:51 ', 'a');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tiene`
--

CREATE TABLE `tiene` (
  `cod_resultado` varchar(50) NOT NULL,
  `idUsuario` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tiene`
--

INSERT INTO `tiene` (`cod_resultado`, `idUsuario`) VALUES
('1', 'a'),
('1', 'q');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `idUsuario` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `ultimaIp` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `password`, `ultimaIp`) VALUES
('a', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', '/127.0.0.1:61065'),
('hola', '8e35c2cd3bf6641bdb0e2050b76932cbb2e6034a0ddacc1d9bea82a6ba57f7cf', '/127.0.0.1:62173'),
('q', '8e35c2cd3bf6641bdb0e2050b76932cbb2e6034a0ddacc1d9bea82a6ba57f7cf', '/127.0.0.1:61068'),
('r', '454349e422f05297191ead13e21d3db520e5abef52055e4964b82fb213f593a1', '/127.0.0.1:61815'),
('s', '043a718774c572bd8a25adbeb1bfcd5c0256ae11cecf9f9c3f925d0e52beaf89', '/127.0.0.1:64209');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `resultado_partida`
--
ALTER TABLE `resultado_partida`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `tiene`
--
ALTER TABLE `tiene`
  ADD KEY `FK_codResultado` (`cod_resultado`),
  ADD KEY `FK_idUsuario` (`idUsuario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `tiene`
--
ALTER TABLE `tiene`
  ADD CONSTRAINT `FK_codResultado` FOREIGN KEY (`cod_resultado`) REFERENCES `resultado_partida` (`cod`) ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_idUsuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
