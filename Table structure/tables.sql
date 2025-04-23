-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tablejava
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table ` chat_subscriptions`
--

DROP TABLE IF EXISTS ` chat_subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE ` chat_subscriptions` (
  `chat_id` int NOT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`chat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` chat_subscriptions`
--

LOCK TABLES ` chat_subscriptions` WRITE;
/*!40000 ALTER TABLE ` chat_subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE ` chat_subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
  `chatid` int NOT NULL,
  `started_at` varchar(45) DEFAULT NULL,
  `stopped_at` varchar(45) DEFAULT NULL,
  `txt_file_path` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`chatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chats`
--

LOCK TABLES `chats` WRITE;
/*!40000 ALTER TABLE `chats` DISABLE KEYS */;
/*!40000 ALTER TABLE `chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `optionally: messages`
--

DROP TABLE IF EXISTS `optionally: messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `optionally: messages` (
  ` chat_id` int NOT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `timestamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (` chat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `optionally: messages`
--

LOCK TABLES `optionally: messages` WRITE;
/*!40000 ALTER TABLE `optionally: messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `optionally: messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `Username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `profile_pic_path` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-23 14:17:34
