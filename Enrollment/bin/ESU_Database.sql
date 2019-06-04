-- phpMyAdmin SQL Dump
-- version 3.2.0.1
-- http://www.phpmyadmin.net
--
-- Server: localhost
-- Server version: 5.1.36
-- PHP version: 5.3.0

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Banco de Dados: `patients`
--


--
-- Estrutura da tabela `dados_usuario`
--

DROP TABLE ESU_Students_Data;

CREATE TABLE IF NOT EXISTS `ESU_Students_Data` (
  `username` varchar(8) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `birthday` varchar(10) NOT NULL,
  `sex` varchar(10) NOT NULL,
  `city` varchar(20) NOT NULL,
  `state` varchar(20) NOT NULL,
  `country` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `course` varchar(50) NOT NULL,
  `GPA` float NOT NULL,
  `credits` int NOT NULL,
  `startDate` varchar(10) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Extracting data from `ESU_Students_Data`
--


INSERT INTO `ESU_Students_Data` (`username`, `name`,`surname`,`birthday`, `sex`, `city`, `state`, `country`, `email`,`course`,`GPA`,`credits`,`startDate`) VALUES
('suman','Suman','Sth','05/11/1989', 'female', 'Newcastle', 'NSW', 'Australia', 'suman@student.easternsydney.edu.au', 'Master of ICT', 86.7, 12, '2019-02-02'),
('aldir','Aldir Jose','Borelli Junior','30/01/1988','male', 'Sao Paulo', 'SP', 'Brazil', 'aldirjr@student.easternsydney.edu.au','Master of ICT', 86.7, 16, '2019-02-02'),
('student','John','Smith', '01/06/1988', 'male', 'Sydney', 'NSW', 'Australia', 'john.smith@student.easternsydney.edu.au','Master of ICT', 65.0, 24, '2019-02-02');

-- --------------------------------------------------------
--
-- 
--

drop table ESU_History;

CREATE TABLE IF NOT EXISTS `ESU_History` (
  `unitID` int NOT NULL,
  `username` varchar(8) NOT NULL,
  `mark` float NOT NULL,
  `status` varchar(10) NOT NULL
);

INSERT INTO `ESU_History` VALUES
(19003,'student',80.0,'completed'),
(19006,'student',40.0,'failed');


drop table ESU_Enrol;

CREATE TABLE IF NOT EXISTS `ESU_Enrol` (
  `unitID` int NOT NULL,
  `groupId` int NOT NULL,
  `studentUserName` varchar(8) NOT NULL
);

INSERT INTO `ESU_Enrol` VALUES
(19001,1,'aldir'),
(19001,1,'suman'),
(19006,1,'aldir'),
(19005,1,'suman'),
(19003,1,'aldir'),
(19003,1,'suman');

drop table ESU_Unit;

CREATE TABLE IF NOT EXISTS `ESU_Unit` (
  `unitID` int NOT NULL,
  `groupId` int NOT NULL,
  `unitName` varchar(50) NOT NULL,
  `credits` int,
  `requirement` int,
  `GPAmin` float,
  `professorName` varchar(255),
  `studentsEnrolled` int,
  `location` int,
  `weekDay` varchar(10) NOT NULL,
  `hourStart` varchar(5) NOT NULL,
  `hourEnd` varchar(5) NOT NULL,
  PRIMARY KEY (`unitId`, `groupId`)
);

INSERT INTO `ESU_Unit` VALUES
(19001,1,'Programming Proficiency',16,0,75,'Prof. Lupin',24,401,'Wednesday','14:00','16:00'),
(19001,2,'Programming Proficiency',16,0,50,'Prof. Lockhart',10,402,'Wednesday','14:00','16:00'),
(19001,3,'Programming Proficiency',16,0,0,'Prof. Quirrel',10,403,'Thursday','09:00','11:00'),
(19002,1,'Network Technologies',8,0,0,'Prof. McGonagall',20,404,'Thursday','10:00','12:00'),
(19002,2,'Network Technologies',8,0,0,'Prof. Dumbledore',25,405,'Wednesday','14:00','16:00'),
(19003,1,'System Analysis',8,0,0,'Prof. Binns',3,406,'Monday','14:00','16:00'),
(19003,2,'System Analysis',8,0,0,'Prof. Flitwick',25,401,'Friday','16:00','18:00'),
(19004,1,'Software Engineer',16,19003,75,'Prof. Sprout',20,402,'Friday','16:00','18:00'),
(19004,2,'Software Engineer',16,19003,0,'Prof. Hagrid',15,403,'Friday','14:00','16:00'),
(19004,3,'Software Engineer',16,19003,50,'Prof. Hooch',20,404,'Tuesday','08:00','10:00'),
(19005,1,'Operational Systems',4,0,0,'Prof. Trelawney',7,405,'Friday','08:00','10:00'),
(19006,1,'Interficial Intelligence',8,0,0,'Prof. Slughorn',13,406,'Wednesday','16:00','18:00'),
(19007,1,'Neural Network',8,19006,60,'Prof. Moody',22,401,'Wednesday','16:00','18:00'),
(19008,1,'Database',8,019003,60,'Prof. Plank',2,402,'Friday','14:00','16:00');

DROP TABLE ESU_User;

CREATE TABLE IF NOT EXISTS `ESU_User` (
  `username` varchar(8) NOT NULL,
  `password` varchar(8) NOT NULL,
  `profile` varchar(10) NOT NULL,
  `lastlogin` date NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `usuario`
--

INSERT INTO `ESU_User` (`username`, `password`, `profile`, `lastlogin`) VALUES
('aldir', 'hendrix','student', '2009-11-19'),
('suman', 'suman','student', '2009-10-05'),
('student', 'student','student', '2009-10-05'),
('admin', 'admin','admin', '2009-11-20');


--
-- Structure of the table ESU_Student_Board
--


CREATE TABLE IF NOT EXISTS `ESU_Boards` (
  `board` varchar(8) NOT NULL,
  `title` varchar(50) NOT NULL,
  `text` varchar(255) NOT NULL,
  `publication_date`varchar(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO  `ESU_Boards` VALUES
('Student','Title of the publication 1','Student board some text.... something the students have to knoe, tests week.. i dont know.. create more then 1.. like 3 sounds good','2019-05-20'),
('Student','Title of the publication 2','Student board text 2','2019-05-23'),
('Student','Title of the publication 3','Student board text 3','2019-05-25'),
('News','Title of the publication 1','News board some text.... something the students can participate, activity.. i dont know.. create more then 1.. like 3 sounds good','2019-05-20'),
('News','Title of the publication 2','News board text 2','2019-05-22'),
('News','Title of the publication 3','News board text 3','2019-05-23');





SELECT u.unitID, u.groupId, u.unitName, u.professorName, u.credits
                		, u.weekDay, u.hourStart, u.hourEnd, u.requirement, u.GPAmin,
            		    (SELECT DISTINCT p.unitName FROM ESU_UNIT p WHERE p.unitID = u.requirement) AS requirementName
                  		FROM ESU_Unit u LEFT JOIN (SELECT unitID, groupId FROM ESU_ENROL WHERE studentUserName ="+currentUser.getUsername()+") e
                  		ON u.unitID = e.unitID AND u.groupId = e.groupId WHERE e.studentUserName IS NULL;
