-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 13, 2023 at 12:06 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `oocdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `equations`
--

CREATE TABLE `equations` (
  `equation_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `equation` text NOT NULL,
  `solution` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `equations`
--

INSERT INTO `equations` (`equation_id`, `user_id`, `equation`, `solution`) VALUES
(10, 5, '3*x+5*y^2', '109413'),
(11, 3, 'x+5*y', '3968'),
(14, 8, '4*y+2*x^2', '240');

-- --------------------------------------------------------

--
-- Table structure for table `tax_records`
--

CREATE TABLE `tax_records` (
  `record_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `gross_income` double NOT NULL,
  `tax_credits` double NOT NULL,
  `tax_owed` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tax_records`
--

INSERT INTO `tax_records` (`record_id`, `user_id`, `gross_income`, `tax_credits`, `tax_owed`) VALUES
(5, 5, 456, 147, 109413),
(6, 3, 478, 698, 3968),
(9, 8, 10, 10, 240);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `account_type` enum('ADMIN','REGULAR') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `first_name`, `last_name`, `account_type`) VALUES
(1, 'CCT', 'Dublin', 'CCT', 'CCT', 'ADMIN'),
(3, 'DDD', '123', 'aqwe', 'ewq', 'REGULAR'),
(5, 'qwe', '123', 'ann', 'silva', 'REGULAR'),
(8, 'ooc2023', 'ooc2023', 'OOC', '001', 'REGULAR');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `equations`
--
ALTER TABLE `equations`
  ADD PRIMARY KEY (`equation_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `tax_records`
--
ALTER TABLE `tax_records`
  ADD PRIMARY KEY (`record_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `equations`
--
ALTER TABLE `equations`
  MODIFY `equation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `tax_records`
--
ALTER TABLE `tax_records`
  MODIFY `record_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `equations`
--
ALTER TABLE `equations`
  ADD CONSTRAINT `equations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `tax_records`
--
ALTER TABLE `tax_records`
  ADD CONSTRAINT `tax_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
