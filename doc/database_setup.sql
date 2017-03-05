-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server Version:               10.1.19-MariaDB - mariadb.org binary distribution
-- Server Betriebssystem:        Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Exportiere Datenbank Struktur für server_data
DROP DATABASE IF EXISTS `server_data`;
CREATE DATABASE IF NOT EXISTS `server_data` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `server_data`;

-- Exportiere Struktur von Tabelle server_data.match_requested
DROP TABLE IF EXISTS `match_requested`;
CREATE TABLE IF NOT EXISTS `match_requested` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` text COLLATE utf8_bin NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `board_size` int(11) NOT NULL,
  `player_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `rank` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='match_requested holds every client app that wants to match to an opponent. It contains the firebase-token, the game meta information and timestamps for possible timeout, if no match is found';

-- Daten Export vom Benutzer nicht ausgewählt
-- Exportiere Struktur von Tabelle server_data.move_nodes
DROP TABLE IF EXISTS `move_nodes`;
CREATE TABLE IF NOT EXISTS `move_nodes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameID` int(11) DEFAULT NULL,
  `parentID` int(11) DEFAULT NULL,
  `posX` int(11) DEFAULT NULL,
  `posY` int(11) DEFAULT NULL,
  `isBlacksMove` int(11) DEFAULT NULL,
  `actionType` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `comment` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `isPrisoner` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Daten Export vom Benutzer nicht ausgewählt
-- Exportiere Struktur von Tabelle server_data.running_games
DROP TABLE IF EXISTS `running_games`;
CREATE TABLE IF NOT EXISTS `running_games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT NULL,
  `tokenA` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `tokenB` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `boardSize` int(11) DEFAULT NULL,
  `prisonerA` int(11) DEFAULT NULL,
  `prisonerB` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Daten Export vom Benutzer nicht ausgewählt
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
