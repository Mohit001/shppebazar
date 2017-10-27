-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 27, 2017 at 01:53 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shopebazar`
--

-- --------------------------------------------------------

--
-- Table structure for table `brand_master`
--

CREATE TABLE `brand_master` (
  `brand_id` int(11) NOT NULL,
  `brand_name` varchar(255) DEFAULT NULL,
  `brand_description` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `brand_master`
--

INSERT INTO `brand_master` (`brand_id`, `brand_name`, `brand_description`, `is_enable`, `create_date`, `user_id`) VALUES
(1, 'F2', '', 1, '2017-09-25 07:37:29', 1),
(2, 'Karaoke', '', 1, '2017-09-25 07:37:41', 1),
(3, 'Samsung', '', 1, '2017-09-25 07:37:56', 1),
(4, 'Skullcandy', '', 1, '2017-09-25 07:38:10', 1),
(5, 'Solar Camping', '', 1, '2017-09-25 07:38:25', 1),
(6, 'ShineCom ', '', 1, '2017-09-25 07:38:40', 1),
(7, 'Enlarge Screen', '', 1, '2017-09-25 08:01:05', 1);

-- --------------------------------------------------------

--
-- Table structure for table `category_master`
--

CREATE TABLE `category_master` (
  `cat_id` int(11) NOT NULL,
  `cat_name` varchar(255) DEFAULT NULL,
  `cat_description` varchar(1000) DEFAULT NULL,
  `cat_image` varchar(500) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `user_id` int(6) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `category_master`
--

INSERT INTO `category_master` (`cat_id`, `cat_name`, `cat_description`, `cat_image`, `is_enable`, `user_id`, `create_date`, `parent_id`) VALUES
(1, 'Electronic', 'Electronic Device\r\n', '1_Elec.jpg', 1, 1, '2017-09-25 07:36:54', 0),
(2, 'Mobile Accessories', '', '1_mcaa.jpg', 1, 1, '2017-09-25 07:39:27', 1),
(3, 'Microphone ', '', '1_microphone.jpg', 0, 1, '2017-09-25 07:42:01', 1),
(4, 'Torch', '', '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell (1).jpg', 1, 1, '2017-09-25 07:53:47', 1),
(5, 'Headphones', '', '1_headphn.jpg', 0, 1, '2017-09-25 07:56:33', 1);

-- --------------------------------------------------------

--
-- Table structure for table `contact_master`
--

CREATE TABLE `contact_master` (
  `contact_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `massge` blob,
  `create_date` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `contact_master`
--

INSERT INTO `contact_master` (`contact_id`, `name`, `email`, `subject`, `massge`, `create_date`, `user_id`) VALUES
(1, 'Pinank', 'pinank1510@gmail.com', 'Test', 0x6b6a6473766b6a6273, '2017-09-25 13:52:41', 1),
(2, 'Pinank', 'pinank1510@gmail.com', 'Test', 0x6b6a6473766b6a6273, '2017-09-25 14:20:39', 1),
(3, 'Pinank', 'pinank1510@gmail.com', 'Test', 0x6b6a6473766b6a6273, '2017-09-25 14:21:10', 1),
(4, 'Pinank', 'pinank1510@gmail.com', 'Test', 0x6b6a6473766b6a6273, '2017-09-25 14:21:51', 1),
(5, 'Pinank', 'pinank1510@gmail.com', 'Test', 0x6b6a6473766b6a6273, '2017-09-25 14:22:45', 1),
(6, 'Pinank', 'pinank1510@gmail.com', 'Test', 0x6b6a6473766b6a6273, '2017-09-25 14:24:02', 1),
(7, 'nlk', 'pinank1510@gmail.com', 'nkln', 0x6d766d64766c, '2017-09-26 10:53:28', 0),
(8, 'nlk', 'pinank1510@gmail.com', 'nkln', 0x6d766d64766c, '2017-09-26 10:54:19', 0),
(9, 'nlk', 'pinank1510@gmail.com', 'nkln', 0x6d766d64766c, '2017-09-26 10:56:14', 0),
(10, NULL, NULL, NULL, NULL, '2017-09-26 10:56:35', 0);

-- --------------------------------------------------------

--
-- Table structure for table `gst_master`
--

CREATE TABLE `gst_master` (
  `gst_id` int(11) NOT NULL,
  `gst_name` varchar(255) DEFAULT NULL,
  `gst_type` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `user_id` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_details`
--

CREATE TABLE `invoice_details` (
  `invoice_details_id` int(11) NOT NULL,
  `invoice_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_code` varchar(255) DEFAULT NULL,
  `product_description` varchar(2500) DEFAULT NULL,
  `product_price` varchar(255) DEFAULT NULL,
  `product_cat_id` varchar(255) DEFAULT NULL,
  `product_brand_id` varchar(255) DEFAULT NULL,
  `product_gst_type` varchar(255) DEFAULT NULL,
  `product_gst` varchar(255) DEFAULT NULL,
  `product_discount_price` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `product_image_name` varchar(255) DEFAULT NULL,
  `product_category_name` varchar(255) DEFAULT NULL,
  `product_brand_name` varchar(255) DEFAULT NULL,
  `product_qty` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `invoice_details`
--

INSERT INTO `invoice_details` (`invoice_details_id`, `invoice_id`, `product_id`, `product_name`, `product_code`, `product_description`, `product_price`, `product_cat_id`, `product_brand_id`, `product_gst_type`, `product_gst`, `product_discount_price`, `create_date`, `product_image_name`, `product_category_name`, `product_brand_name`, `product_qty`) VALUES
(1, 1, 2, '3D Mobile Screen Magnifier', 'Es-123', '&#9679;	Compatible with all iphones, Samsung phones, HTC phones, LG, Nokia phones, Blackberry phones, Sony phones, phone, ZTE phones, Motorola phones, Google phones and other brands of mobile phones.\r\n&#9679;	Magnification, it make each mobile phone has large screen clear movies like Reduce eye fatigue of movies and television brought on mobile phone, and stinging eyes and tears phenomenon.\r\n&#9679;	This is the New Mobile Phone Screen Magnifier Bracket and Enlarge Stand, which can 3x~5x magnify your phone screen so as to reduce your eyes'' fatigue during watching videos on the phone.\r\n&#9679;	Mobile phone adapter use skid material, prevent slip, fall. Folding design, small volume, convenient carrying on business travel.\r\n&#9679;	How to use it: Please avoid using in direct sunlight or strong light, avoid light cause is not clear, it will display better in the dark light environment.', '150', '2', '7', '%', '10', '0', '2017-09-30 19:34:47', '1_Foldable-3-Times-font-b-Mobile-b-font-font-b-Phone-b-font-font-b-Screen.jpg', 'Mobile Accessories', 'Enlarge Screen', 7),
(2, 1, 4, 'Black Selfie Stick', 'b-12', '&#9679;	Enjoy multi-angle/group photo-taking with this portable selfie stick\r\n&#9679;	UNIVERSAL COMPATIBILITY: Compatible with almost all smartphones\r\n&#9679;	EASY TO USE: 1. Plug in the cord to your phone, 2. press the button to take photos.\r\n&#9679;	PREMIUM ALUMINIUM DESIGN : it comes with premium Silicon handel bar, Portable, Flexible, Easy to use\r\n&#9679;	Super Expendable: Extendable stick as long as 90cm. Adjustable ball head and thumb screw of monopod locks for multiple angle shooting with 180 degree position\r\n', '140', '2', '1', '%', '10', '0', '2017-09-30 19:34:48', '1_1295646_AB_01_FB.EPS_1000.jpg', 'Mobile Accessories', 'F2', 1),
(3, 1, 5, 'MonoPod Selfie Stick ', 'f21', '&#9679;	Extra Firm Grip\r\n&#9679;	Extra Long Length\r\n&#9679;	For Long Lasting Use\r\n&#9679;	Super Stylish\r\n&#9679;	Super Sturdy\r\n', '125', '2', '1', '%', '10', '0', '2017-09-30 19:34:49', '1_2019BLK-B_1024x1024.jpg', 'Mobile Accessories', 'F2', 1),
(4, 2, 5, 'MonoPod Selfie Stick ', 'f21', '&#9679;	Extra Firm Grip\r\n&#9679;	Extra Long Length\r\n&#9679;	For Long Lasting Use\r\n&#9679;	Super Stylish\r\n&#9679;	Super Sturdy\r\n', '125', '2', '1', '%', '10', '0', '2017-09-30 16:04:44', '1_2019BLK-B_1024x1024.jpg', 'Mobile Accessories', 'F2', 1),
(5, 3, 1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', '4', '5', '%', '10', '0', '2017-09-30 16:12:29', '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping', 2),
(8, 9, 1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', '47', '5', '%', '10.0', '0', NULL, '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping', 4),
(9, 9, 2, '3D Mobile Screen Magnifier', 'Es-123', '&#9679;	Compatible with all iphones, Samsung phones, HTC phones, LG, Nokia phones, Blackberry phones, Sony phones, phone, ZTE phones, Motorola phones, Google phones and other brands of mobile phones.\r\n&#9679;	Magnification, it make each mobile phone has large screen clear movies like Reduce eye fatigue of movies and television brought on mobile phone, and stinging eyes and tears phenomenon.\r\n&#9679;	This is the New Mobile Phone Screen Magnifier Bracket and Enlarge Stand, which can 3x~5x magnify your phone screen so as to reduce your eyes'' fatigue during watching videos on the phone.\r\n&#9679;	Mobile phone adapter use skid material, prevent slip, fall. Folding design, small volume, convenient carrying on business travel.\r\n&#9679;	How to use it: Please avoid using in direct sunlight or strong light, avoid light cause is not clear, it will display better in the dark light environment.', '150', '47', '7', '%', '10.0', '0', NULL, '1_Foldable-3-Times-font-b-Mobile-b-font-font-b-Phone-b-font-font-b-Screen.jpg', 'Mobile Accessories', 'Enlarge Screen', 2),
(10, 10, 5, 'MonoPod Selfie Stick ', 'f21', '&#9679;	Extra Firm Grip\r\n&#9679;	Extra Long Length\r\n&#9679;	For Long Lasting Use\r\n&#9679;	Super Stylish\r\n&#9679;	Super Sturdy\r\n', '125', '48', '1', '%', '10.0', '0', NULL, '1_2019BLK-B_1024x1024.jpg', 'Mobile Accessories', 'F2', 2),
(11, 10, 3, 'Virtual Reality Box', 'vr-s12', '&#9679;	Large FOV: You can get a viewing angle of between 95 to 100 degrees, larger than many other VR glasses. Offer you a 1000 inches big screen at the distance of 3 meters. Offers you super 3D picture effect and wonderful feeling. You will not feel visual fatigue and dizzy even you use long time with the resin lens. The 3D glasses are made of ABS plastic and spherical resin lens materials.\r\n&#9679;	Experience Virtual Reality: This 3D VR Box 3.0 headsets will bring you to an immersive, fabulous virtual world while playing games, watching 3D videos & movies with this 3D headset. With this VR device you will find the VR world super amazing. If you want your child to have an unparalleled childhood or make you look different, then this 3D glasses will be your best choice.\r\n&#9679;	Adjustable spherical lens: This 3D gear come with two adjustable lens, allowing you to adjust the focus through moving the button on the top of the VR headset, so you can free up your myopia glasses (under 600 degree) when you enj', '350', '48', '6', '%', '10.0', '0', NULL, '1_71E-XFbPdqL._SL1280_.jpg', 'Mobile Accessories', 'ShineCom ', 1),
(12, 10, 1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', '48', '5', '%', '10.0', '0', NULL, '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping', 2),
(13, 11, 1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', '49', '5', '%', '10.0', '0', NULL, '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping', 1),
(14, 12, 1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', '50', '5', '%', '10.0', '0', NULL, '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping', 1),
(15, 13, 1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', '47', '5', '%', '10.0', '0', NULL, '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping', 4),
(16, 13, 2, '3D Mobile Screen Magnifier', 'Es-123', '&#9679;	Compatible with all iphones, Samsung phones, HTC phones, LG, Nokia phones, Blackberry phones, Sony phones, phone, ZTE phones, Motorola phones, Google phones and other brands of mobile phones.\r\n&#9679;	Magnification, it make each mobile phone has large screen clear movies like Reduce eye fatigue of movies and television brought on mobile phone, and stinging eyes and tears phenomenon.\r\n&#9679;	This is the New Mobile Phone Screen Magnifier Bracket and Enlarge Stand, which can 3x~5x magnify your phone screen so as to reduce your eyes'' fatigue during watching videos on the phone.\r\n&#9679;	Mobile phone adapter use skid material, prevent slip, fall. Folding design, small volume, convenient carrying on business travel.\r\n&#9679;	How to use it: Please avoid using in direct sunlight or strong light, avoid light cause is not clear, it will display better in the dark light environment.', '150', '47', '7', '%', '10.0', '0', NULL, '1_Foldable-3-Times-font-b-Mobile-b-font-font-b-Phone-b-font-font-b-Screen.jpg', 'Mobile Accessories', 'Enlarge Screen', 2);

-- --------------------------------------------------------

--
-- Table structure for table `invoice_master`
--

CREATE TABLE `invoice_master` (
  `invoice_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `order_type` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  `shiping_address1` varchar(255) DEFAULT NULL,
  `shiping_address2` varchar(255) DEFAULT NULL,
  `shiping_state` varchar(255) DEFAULT NULL,
  `shiping_city` varchar(255) DEFAULT NULL,
  `shiping_postcode` varchar(255) DEFAULT NULL,
  `shiping_additiondetails` varchar(255) DEFAULT NULL,
  `billing_address1` varchar(255) DEFAULT NULL,
  `billing_address2` varchar(255) DEFAULT NULL,
  `billing_state` varchar(255) DEFAULT NULL,
  `billing_city` varchar(255) DEFAULT NULL,
  `billing_postcode` varchar(255) DEFAULT NULL,
  `billing_addtionaldeatails` varchar(255) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `shiping_fullname` varchar(255) DEFAULT NULL,
  `shiping_email` varchar(255) DEFAULT NULL,
  `billing_fullname` varchar(255) DEFAULT NULL,
  `billing_email` varchar(255) DEFAULT NULL,
  `total_amount` varchar(255) DEFAULT NULL,
  `grand_total` varchar(255) DEFAULT NULL,
  `shipping_charge` varchar(255) DEFAULT NULL,
  `order_no` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `cart_id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `updated_sate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `shipping_contact_no` varchar(255) NOT NULL,
  `billing_contact_no` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `invoice_master`
--

INSERT INTO `invoice_master` (`invoice_id`, `user_id`, `order_type`, `user_type`, `shiping_address1`, `shiping_address2`, `shiping_state`, `shiping_city`, `shiping_postcode`, `shiping_additiondetails`, `billing_address1`, `billing_address2`, `billing_state`, `billing_city`, `billing_postcode`, `billing_addtionaldeatails`, `create_date`, `shiping_fullname`, `shiping_email`, `billing_fullname`, `billing_email`, `total_amount`, `grand_total`, `shipping_charge`, `order_no`, `ip_address`, `order_status`, `cart_id`, `token`, `salt`, `updated_sate`, `shipping_contact_no`, `billing_contact_no`) VALUES
(1, 0, 'COD', 'GuestUser', 'ohoi', 'hio', 'jfjhf', 'jfjhf', '123123', NULL, 'ohoi', 'hio', 'jfjhf', 'hioh', '123123', 'f', '2017-10-01 01:04:45', 'no', 'pinank1510@gmail.com', 'no', 'pinank1510@gmail.com', '1315.0', '1315.0', '0', 'ORD000001', '0:0:0:0:0:0:0:1', 'Pending', 0, '', '', '2017-10-12 11:24:28', '', '0'),
(2, 0, 'COD', 'GuestUser', 'address, address21, address21, address21, asdf, asdf, asdf', 'asdf', 'state2', 'state2', '939393', NULL, 'address, address21, address21, address21, asdf, asdf, asdf', 'asdf', 'state2', 'city', '939393', 'asd', '2017-09-30 21:34:44', 'asdf', 'gandhi.mohit001@gmail.com', 'asdf', 'gandhi.mohit001@gmail.com', '125.0', '125.0', '0', 'ORD000001', '103.251.213.14', 'Pending', 0, '', '', '2017-10-12 11:24:28', '', '0'),
(3, 7, 'COD', 'RegisterUser', 'address, address21, address21, address21, address21', 'address21', 'state2', 'state2', '939393', NULL, 'address, address21, address21, address21, address21', 'address21', 'state2', 'city', '939393', 'asdf', '2017-09-30 21:42:29', 'full name', 'gandhi.mohit001@gmail.com', 'full name', 'gandhi.mohit001@gmail.com', '498.0', '498.0', '0', 'ORD000002', '103.251.213.14', 'Shipping', 0, '', '', '2017-10-12 11:24:28', '', '0'),
(9, 49, 'COD', 'RegisterUser', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', '2017-10-09 12:12:12', 'jkhas', 'asd@sad.com', 'jkhas', 'asd@sad.com', '1296.0', '1476.0', '180.0', NULL, NULL, 'Pending', 47, '175ef2dfb973a204c2f21529529f148c414279fe7d7f8c66e15eb20d24cd4c39f032a7293e7760e6ed17d61235334aa5d70dc13d89cea8adf28322eec5355437', '@klavya!nfotec#_20171011133132', '2017-10-12 11:48:09', '', '0'),
(10, 49, 'COD', 'RegisterUser', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', '2017-10-12 05:00:29', 'jkhas', 'asd@sad.com', 'jkhas', 'asd@sad.com', '1098.0', '1248.0', '150.0', NULL, NULL, 'Pending', 48, 'fce55ba3a127ad297bf1fcf7142dd5e4b3047642c99e55912071395b3a1e4684405bc62e471355ca23b86c7a8b63b0c6c8d69dcdf2a67bf1ccbb4c2257b5b42f', '@klavya!nfotec#_20171011184158', '2017-10-12 11:30:29', '', '0'),
(11, 49, 'COD', 'RegisterUser', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', '2017-10-12 05:19:10', 'jkhas', 'asd@sad.com', 'jkhas', 'asd@sad.com', '249.0', '279.0', '30.0', NULL, NULL, 'Pending', 49, '8b4652f2fab44a02a0b73a26bd7e9dcd36ab7541d082972d0e741ffb2f54eb58ec2ea858ebbfbae329b0c7df1e5f581b126d7eba0f7cf1ecdb7f16969a4c608f', '@klavya!nfotec#_20171012171901', '2017-10-12 11:49:10', '', '0'),
(12, 49, 'COD', 'RegisterUser', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', '2017-10-12 05:19:37', 'jkhas', 'asd@sad.com', 'jkhas', 'asd@sad.com', '249.0', '279.0', '30.0', NULL, NULL, 'Pending', 50, '3bba9720e00d99a02db251d1d3648b963a084377ad19f10407b7297165e5a7c900d97d09b080115c223e60fa9af3c475b251071d153627733e765c27240d0484', '@klavya!nfotec#_20171012171931', '2017-10-12 11:49:37', '', '0'),
(13, 49, 'COD', 'RegisterUser', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', '2017-10-25 10:58:13', 'jkhas', 'asd@sad.com', 'jkhas', 'asd@sad.com', '1296.0', '1476.0', '180.0', NULL, NULL, 'Pending', 47, '175ef2dfb973a204c2f21529529f148c414279fe7d7f8c66e15eb20d24cd4c39f032a7293e7760e6ed17d61235334aa5d70dc13d89cea8adf28322eec5355437', '@klavya!nfotec#_20171011133132', '2017-10-25 05:28:12', '23123', '23123');

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE `location` (
  `location_id` int(11) NOT NULL,
  `location_name` varchar(255) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `is_enable` tinyint(3) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `location_image` varchar(500) DEFAULT NULL,
  `location_description` varchar(1000) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `user_id` int(11) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(500) NOT NULL DEFAULT '',
  `is_enable` tinyint(3) DEFAULT '0',
  `role` tinyint(4) DEFAULT '0',
  `reffrence_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`user_id`, `email`, `password`, `is_enable`, `role`, `reffrence_id`) VALUES
(1, 'admin@admin.com', 'admin123', 1, 1, NULL),
(48, 'pinank1510@gmail.com', 'pinank', 1, 3, 1),
(49, 'mohit@mohit.com', '123123', 1, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `offers`
--

CREATE TABLE `offers` (
  `offer_id` int(11) NOT NULL,
  `offer_name` varchar(255) DEFAULT NULL,
  `offer_discount` int(11) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `user_id` int(255) DEFAULT NULL,
  `offer_description` varchar(1000) DEFAULT NULL,
  `offer_image` varchar(255) DEFAULT NULL,
  `discount_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `offer_mapping`
--

CREATE TABLE `offer_mapping` (
  `offer_map_id` int(11) NOT NULL,
  `offer_id` int(11) DEFAULT NULL,
  `cat_id` int(11) DEFAULT NULL,
  `pro_id` int(11) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `policy`
--

CREATE TABLE `policy` (
  `policy_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(3) DEFAULT '1',
  `page_contain` blob,
  `description` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `product_image`
--

CREATE TABLE `product_image` (
  `imge_id` int(11) NOT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `default` tinyint(1) DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `product_master`
--

CREATE TABLE `product_master` (
  `pro_mst_id` int(11) NOT NULL,
  `pro_name` varchar(255) DEFAULT NULL,
  `pro_code` varchar(255) DEFAULT NULL,
  `pro_description` varchar(2500) DEFAULT NULL,
  `pro_price` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `cat_id` int(11) DEFAULT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `gst_type` varchar(255) DEFAULT NULL,
  `gst` int(11) DEFAULT NULL,
  `discount_price` int(11) DEFAULT NULL,
  `pro_image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `product_master`
--

INSERT INTO `product_master` (`pro_mst_id`, `pro_name`, `pro_code`, `pro_description`, `pro_price`, `is_enable`, `create_date`, `cat_id`, `brand_id`, `user_id`, `gst_type`, `gst`, `discount_price`, `pro_image`) VALUES
(1, 'Lamp With Torch', 'lamp-123', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', '249', 1, '2017-09-25 08:00:05', 4, 5, 1, '%', 10, 0, '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg'),
(2, '3D Mobile Screen Magnifier', 'Es-123', '&#9679;	Compatible with all iphones, Samsung phones, HTC phones, LG, Nokia phones, Blackberry phones, Sony phones, phone, ZTE phones, Motorola phones, Google phones and other brands of mobile phones.\r\n&#9679;	Magnification, it make each mobile phone has large screen clear movies like Reduce eye fatigue of movies and television brought on mobile phone, and stinging eyes and tears phenomenon.\r\n&#9679;	This is the New Mobile Phone Screen Magnifier Bracket and Enlarge Stand, which can 3x~5x magnify your phone screen so as to reduce your eyes'' fatigue during watching videos on the phone.\r\n&#9679;	Mobile phone adapter use skid material, prevent slip, fall. Folding design, small volume, convenient carrying on business travel.\r\n&#9679;	How to use it: Please avoid using in direct sunlight or strong light, avoid light cause is not clear, it will display better in the dark light environment.', '150', 1, '2017-09-25 08:02:31', 2, 7, 1, '%', 10, 0, '1_Foldable-3-Times-font-b-Mobile-b-font-font-b-Phone-b-font-font-b-Screen.jpg'),
(3, 'Virtual Reality Box', 'vr-s12', '&#9679;	Large FOV: You can get a viewing angle of between 95 to 100 degrees, larger than many other VR glasses. Offer you a 1000 inches big screen at the distance of 3 meters. Offers you super 3D picture effect and wonderful feeling. You will not feel visual fatigue and dizzy even you use long time with the resin lens. The 3D glasses are made of ABS plastic and spherical resin lens materials.\r\n&#9679;	Experience Virtual Reality: This 3D VR Box 3.0 headsets will bring you to an immersive, fabulous virtual world while playing games, watching 3D videos & movies with this 3D headset. With this VR device you will find the VR world super amazing. If you want your child to have an unparalleled childhood or make you look different, then this 3D glasses will be your best choice.\r\n&#9679;	Adjustable spherical lens: This 3D gear come with two adjustable lens, allowing you to adjust the focus through moving the button on the top of the VR headset, so you can free up your myopia glasses (under 600 degree) when you enj', '350', 1, '2017-09-25 08:06:24', 2, 6, 1, '%', 10, 0, '1_71E-XFbPdqL._SL1280_.jpg'),
(4, 'Black Selfie Stick', 'b-12', '&#9679;	Enjoy multi-angle/group photo-taking with this portable selfie stick\r\n&#9679;	UNIVERSAL COMPATIBILITY: Compatible with almost all smartphones\r\n&#9679;	EASY TO USE: 1. Plug in the cord to your phone, 2. press the button to take photos.\r\n&#9679;	PREMIUM ALUMINIUM DESIGN : it comes with premium Silicon handel bar, Portable, Flexible, Easy to use\r\n&#9679;	Super Expendable: Extendable stick as long as 90cm. Adjustable ball head and thumb screw of monopod locks for multiple angle shooting with 180 degree position\r\n', '140', 1, '2017-09-25 08:08:08', 2, 1, 1, '%', 10, 0, '1_1295646_AB_01_FB.EPS_1000.jpg'),
(5, 'MonoPod Selfie Stick ', 'f21', '&#9679;	Extra Firm Grip\r\n&#9679;	Extra Long Length\r\n&#9679;	For Long Lasting Use\r\n&#9679;	Super Stylish\r\n&#9679;	Super Sturdy\r\n', '125', 1, '2017-09-25 08:10:21', 2, 1, 1, '%', 10, 0, '1_2019BLK-B_1024x1024.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `product_package`
--

CREATE TABLE `product_package` (
  `pro_pkg_id` int(11) NOT NULL,
  `pro_mst_id` int(11) DEFAULT NULL,
  `volume_id` varchar(10) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `volume` int(255) DEFAULT NULL,
  `pro_pkg_description` varchar(255) DEFAULT NULL,
  `pro_pkg_img` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `product_type`
--

CREATE TABLE `product_type` (
  `pro_type_id` int(11) NOT NULL,
  `type_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE `profile` (
  `profie_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `account_type` tinyint(4) DEFAULT NULL,
  `compnay_name` varchar(45) DEFAULT NULL,
  `fname` varchar(45) DEFAULT NULL,
  `lname` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `street_address` varchar(255) DEFAULT NULL,
  `alternet_mobile` varchar(10) DEFAULT NULL,
  `mobile` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`profie_id`, `user_id`, `account_type`, `compnay_name`, `fname`, `lname`, `state`, `country`, `city`, `street_address`, `alternet_mobile`, `mobile`) VALUES
(1, 1, 1, 'ORBS', 'admin', 'admin', 'nlknkl', NULL, '23123', 'adkanl', '131313131', '13131'),
(48, 48, 1, 'Pinank', 'Pinank', 'Soni', 'Gujrat', 'India', 'Ankleshwar', 'navidivi', '0264652535', '8000059755'),
(49, 49, 1, 'Aklavya', 'Mohit', 'Gandhi', 'Gujrat', 'India', 'Vadodara', '', '', '1231231123');

-- --------------------------------------------------------

--
-- Table structure for table `review_rating`
--

CREATE TABLE `review_rating` (
  `rating_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `ipaddress` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sliders`
--

CREATE TABLE `sliders` (
  `silder_id` int(11) NOT NULL,
  `slider_name` varchar(255) DEFAULT NULL,
  `slider_description` varchar(1000) DEFAULT NULL,
  `slider_path` varchar(255) DEFAULT NULL,
  `cat_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `type_master`
--

CREATE TABLE `type_master` (
  `type_id` int(11) NOT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  `type_description` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `fname`, `lname`, `password`, `create_date`, `is_enable`, `email`, `phone`) VALUES
(1, 'Pinank', 'Soni', 'pinank', '2017-09-27 07:10:58', 1, 'pinank151dsdsd0@gmail.com', '8000059755'),
(2, 'Pappu', 'Soni', 'pinank', '2017-09-28 09:36:17', 1, 'pinanksoni@ymail.com', '8000059755'),
(3, 'Pappu', 'Soni', 'pinank', '2017-09-28 09:42:15', 1, 'pinanksoni@ymail.com', '8000059755'),
(4, 'Pappu', 'Soni', 'pinank', '2017-09-28 09:42:30', 1, 'pinanksoni@ymail.com', '8000059755'),
(5, 'Pinank ', 'Soni', 'pinank', '2017-09-28 10:28:55', 1, 'pinank1510@gmail.com', '8000059755'),
(6, 'pinank', 'soni', 'pinank', '2017-09-29 07:52:22', 1, 'pinanksoni.dv@gmail.com', '8000059755'),
(7, 'lkjsad', 'lkjsdf', '123123', '2017-09-30 16:06:16', 1, 'gandhi.mohit001@gmail.com', '2093849023'),
(8, 'jdjdj', 'jdjdj', 'admin123', '2017-09-30 16:11:24', 1, 'jdjd@jdjd.com', '213123123'),
(9, 'asdf', 'sadf', '123123', '2017-09-30 16:15:37', 1, 'asdf@asdf.com', '123123');

-- --------------------------------------------------------

--
-- Table structure for table `useradmin`
--

CREATE TABLE `useradmin` (
  `useradmin_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `updatedate` timestamp NULL DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `is_enable` tinyint(3) DEFAULT '0',
  `start_date` date DEFAULT NULL,
  `file` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `useradmin`
--

INSERT INTO `useradmin` (`useradmin_id`, `user_id`, `create_date`, `updatedate`, `expire_date`, `is_enable`, `start_date`, `file`) VALUES
(1, 1, '2017-04-22', '2017-07-07 04:39:46', '2017-11-01', 1, '2017-01-01', '1_TC_Logo_2016.png'),
(34, 48, '2017-08-16', '2017-08-16 04:04:33', '2017-09-01', 1, '2017-08-01', '48_tc-stamp.png');

-- --------------------------------------------------------

--
-- Table structure for table `user_address`
--

CREATE TABLE `user_address` (
  `address_id` int(11) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `postcode` varchar(255) DEFAULT NULL,
  `addition_detail` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '1',
  `default_value` tinyint(1) DEFAULT '0',
  `create_date` timestamp NULL DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `contact_number` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `country_name` varchar(255) NOT NULL,
  `state_name` varchar(255) NOT NULL,
  `city_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_address`
--

INSERT INTO `user_address` (`address_id`, `address1`, `address2`, `state`, `city`, `postcode`, `addition_detail`, `user_id`, `is_enable`, `default_value`, `create_date`, `full_name`, `email`, `contact_number`, `country`, `country_name`, `state_name`, `city_name`) VALUES
(1, 'Address1', 'address2', 'Satatu', 'City', '393300', 'addition al', '1', 1, 1, '2017-09-27 07:11:31', 'Pinank Soni', 'pinank1510@gmail.com', '', '', '', '', ''),
(2, 'pinan', 'pinank', 'nsdlvn', 'nlkn', '546556', NULL, '5', 1, 1, '2017-09-28 10:29:24', 'PInank Soni', NULL, '', '', '', '', ''),
(3, 'njn', 'nj', 'njkn', 'njn', '123132', NULL, '6', 1, 1, '2017-09-29 07:53:03', 'pinank sdv', NULL, '', '', '', '', ''),
(6, 'kjhkhsadf', 'kljhasdkjhf klajsdhf', 'akjshsdf', 'jdjdjd', '123123', 'asdfsdf', '49', 1, 0, NULL, 'jkhas', 'asd@sad.com', '23123', '', '', '', ''),
(7, 'lksajdf', 'kjsdhf', 'kjashdf', 'kjshwe lsadf', '239292', '8282828 klasd', '49', 1, 0, NULL, 'mohit', 'mohit@mohit.com', '23123', '', '', '', ''),
(8, 'asdfasdf', 'asdfsad', '1', '1', '223423', 'asdfasdf asdfasdf', '49', 1, 0, NULL, 'asdfs sdfsdf sf ', 'djd@jdjd.com', 'djd@jdjd.com', '1', 'countryname', 'statename', 'citykajshd');

-- --------------------------------------------------------

--
-- Table structure for table `user_cart`
--

CREATE TABLE `user_cart` (
  `cart_id` int(11) NOT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `user_id` int(255) DEFAULT NULL,
  `cart_status` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `shipping_address_id` int(11) UNSIGNED ZEROFILL DEFAULT '00000000000',
  `billing_address_id` int(11) UNSIGNED ZEROFILL DEFAULT '00000000000',
  `payment_type_id` int(2) UNSIGNED ZEROFILL DEFAULT '00' COMMENT '0 = COD, 1=online payment',
  `payment_type_code` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `shipping_charge` varchar(255) NOT NULL DEFAULT '0',
  `unique_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_cart`
--

INSERT INTO `user_cart` (`cart_id`, `create_date`, `ip_address`, `user_id`, `cart_status`, `token`, `shipping_address_id`, `billing_address_id`, `payment_type_id`, `payment_type_code`, `salt`, `shipping_charge`, `unique_id`) VALUES
(47, NULL, NULL, 49, 'placed', '175ef2dfb973a204c2f21529529f148c414279fe7d7f8c66e15eb20d24cd4c39f032a7293e7760e6ed17d61235334aa5d70dc13d89cea8adf28322eec5355437', 00000000006, 00000000006, 01, 'COD', '@klavya!nfotec#_20171011133132', '0', 'hfhfssisd'),
(48, NULL, NULL, 49, 'placed', 'fce55ba3a127ad297bf1fcf7142dd5e4b3047642c99e55912071395b3a1e4684405bc62e471355ca23b86c7a8b63b0c6c8d69dcdf2a67bf1ccbb4c2257b5b42f', 00000000006, 00000000006, 01, 'COD', '@klavya!nfotec#_20171011184158', '0', 'odmduebl83jdew93b'),
(49, NULL, NULL, 0, 'open', '8b4652f2fab44a02a0b73a26bd7e9dcd36ab7541d082972d0e741ffb2f54eb58ec2ea858ebbfbae329b0c7df1e5f581b126d7eba0f7cf1ecdb7f16969a4c608f', 00000000006, 00000000006, 01, 'COD', '@klavya!nfotec#_20171012171901', '0', 'eLeO6KOuxWU:APA91bGcM_mMXsKkzuMl4br1JgVihvk5Pqfj9He8_I08_mNPWGQqRip7h7nMtsJQGGtEn1IiT4EXQaQrdgZMndfMivrjS9Wxf8crfIs9OsVGQTHpRF27UijVWsczHaKKzFfiW9G9Tr2J'),
(50, NULL, NULL, 49, 'open', '3bba9720e00d99a02db251d1d3648b963a084377ad19f10407b7297165e5a7c900d97d09b080115c223e60fa9af3c475b251071d153627733e765c27240d0484', 00000000006, 00000000006, 01, 'COD', '@klavya!nfotec#_20171012171931', '0', '123123');

-- --------------------------------------------------------

--
-- Table structure for table `user_cart_product`
--

CREATE TABLE `user_cart_product` (
  `user_cart_product_id` int(11) NOT NULL,
  `cart_id` int(11) UNSIGNED ZEROFILL DEFAULT '00000000000',
  `product_id` int(11) UNSIGNED ZEROFILL DEFAULT '00000000000',
  `product_name` varchar(255) DEFAULT NULL,
  `product_qty` int(2) UNSIGNED ZEROFILL DEFAULT '00',
  `product_price` double UNSIGNED DEFAULT '0',
  `product_code` varchar(255) DEFAULT NULL,
  `shipping_charge` int(5) UNSIGNED ZEROFILL DEFAULT '00000',
  `status` varchar(255) DEFAULT NULL,
  `gst_type` varchar(255) DEFAULT NULL,
  `gst` double(10,2) DEFAULT '0.00',
  `subtotal` varchar(255) NOT NULL,
  `description` varchar(2500) NOT NULL,
  `cat_id` int(11) NOT NULL,
  `brand_id` int(11) NOT NULL,
  `discount_price` varchar(255) NOT NULL,
  `image_name` varchar(255) NOT NULL,
  `category_name` varchar(255) NOT NULL,
  `brand_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_cart_product`
--

INSERT INTO `user_cart_product` (`user_cart_product_id`, `cart_id`, `product_id`, `product_name`, `product_qty`, `product_price`, `product_code`, `shipping_charge`, `status`, `gst_type`, `gst`, `subtotal`, `description`, `cat_id`, `brand_id`, `discount_price`, `image_name`, `category_name`, `brand_name`) VALUES
(30, 00000000047, 00000000001, 'Lamp With Torch', 04, 249, 'lamp-123', 00030, 'open', '%', 10.00, '1116.0', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', 4, 5, '0', '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping'),
(31, 00000000047, 00000000002, '3D Mobile Screen Magnifier', 02, 150, 'Es-123', 00030, 'open', '%', 10.00, '360.0', '&#9679;	Compatible with all iphones, Samsung phones, HTC phones, LG, Nokia phones, Blackberry phones, Sony phones, phone, ZTE phones, Motorola phones, Google phones and other brands of mobile phones.\r\n&#9679;	Magnification, it make each mobile phone has large screen clear movies like Reduce eye fatigue of movies and television brought on mobile phone, and stinging eyes and tears phenomenon.\r\n&#9679;	This is the New Mobile Phone Screen Magnifier Bracket and Enlarge Stand, which can 3x~5x magnify your phone screen so as to reduce your eyes'' fatigue during watching videos on the phone.\r\n&#9679;	Mobile phone adapter use skid material, prevent slip, fall. Folding design, small volume, convenient carrying on business travel.\r\n&#9679;	How to use it: Please avoid using in direct sunlight or strong light, avoid light cause is not clear, it will display better in the dark light environment.', 2, 7, '0', '1_Foldable-3-Times-font-b-Mobile-b-font-font-b-Phone-b-font-font-b-Screen.jpg', 'Mobile Accessories', 'Enlarge Screen'),
(32, 00000000048, 00000000005, 'MonoPod Selfie Stick ', 02, 125, 'f21', 00030, 'open', '%', 10.00, '310.0', '&#9679;	Extra Firm Grip\r\n&#9679;	Extra Long Length\r\n&#9679;	For Long Lasting Use\r\n&#9679;	Super Stylish\r\n&#9679;	Super Sturdy\r\n', 2, 1, '0', '1_2019BLK-B_1024x1024.jpg', 'Mobile Accessories', 'F2'),
(33, 00000000048, 00000000003, 'Virtual Reality Box', 01, 350, 'vr-s12', 00030, 'open', '%', 10.00, '380.0', '&#9679;	Large FOV: You can get a viewing angle of between 95 to 100 degrees, larger than many other VR glasses. Offer you a 1000 inches big screen at the distance of 3 meters. Offers you super 3D picture effect and wonderful feeling. You will not feel visual fatigue and dizzy even you use long time with the resin lens. The 3D glasses are made of ABS plastic and spherical resin lens materials.\r\n&#9679;	Experience Virtual Reality: This 3D VR Box 3.0 headsets will bring you to an immersive, fabulous virtual world while playing games, watching 3D videos & movies with this 3D headset. With this VR device you will find the VR world super amazing. If you want your child to have an unparalleled childhood or make you look different, then this 3D glasses will be your best choice.\r\n&#9679;	Adjustable spherical lens: This 3D gear come with two adjustable lens, allowing you to adjust the focus through moving the button on the top of the VR headset, so you can free up your myopia glasses (under 600 degree) when you enj', 2, 6, '0', '1_71E-XFbPdqL._SL1280_.jpg', 'Mobile Accessories', 'ShineCom '),
(34, 00000000048, 00000000001, 'Lamp With Torch', 02, 249, 'lamp-123', 00030, 'open', '%', 10.00, '558.0', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', 4, 5, '0', '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping'),
(36, 00000000050, 00000000001, 'Lamp With Torch', 01, 249, 'lamp-123', 00030, 'open', '%', 10.00, '279.0', '&#9679;	LED is in advantages of energy-saving,high light,long life and high power. Both Highlight and dimlight for choices.,Great product for emergency purpose\r\n&#9679;	Superior design and construction allows lantern to be SUPER lightweight and compact. Lantern is EASILY collapsible with a simple push.\r\n&#9679;	Built with individual premium LEDs, lantern is built for maximum brightness whilst maintaining a super-long battery life.\r\n&#9679;	Can be used as USB Mobile Charger - only for emergency purpose upto 500mAH\r\n&#9679;	COLOR WILL BE SENT AS PER STOCK AVAILABILITY\r\n', 4, 5, '0', '1_Camping-Lantern-LED-Solar-Rechargeable-Camp-Torch-Light-Flashlights-Emergency-Lamp-Power-Bank-for-Android-Cell.jpg', 'Torch', 'Solar Camping');

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `role_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`role_id`, `name`) VALUES
(2, 'Admin'),
(3, 'User');

-- --------------------------------------------------------

--
-- Table structure for table `volume_master`
--

CREATE TABLE `volume_master` (
  `volume_id` int(11) NOT NULL,
  `volume_type` varchar(255) DEFAULT NULL,
  `is_enable` tinyint(3) DEFAULT '1',
  `create_date` timestamp NULL DEFAULT NULL,
  `user_id` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Wishlist`
--

CREATE TABLE `Wishlist` (
  `wishlist_id` int(11) NOT NULL,
  `user_id` int(11) UNSIGNED ZEROFILL DEFAULT '00000000000',
  `product_id` int(11) UNSIGNED ZEROFILL DEFAULT '00000000000'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `brand_master`
--
ALTER TABLE `brand_master`
  ADD PRIMARY KEY (`brand_id`);

--
-- Indexes for table `category_master`
--
ALTER TABLE `category_master`
  ADD PRIMARY KEY (`cat_id`);

--
-- Indexes for table `contact_master`
--
ALTER TABLE `contact_master`
  ADD PRIMARY KEY (`contact_id`);

--
-- Indexes for table `gst_master`
--
ALTER TABLE `gst_master`
  ADD PRIMARY KEY (`gst_id`);

--
-- Indexes for table `invoice_details`
--
ALTER TABLE `invoice_details`
  ADD PRIMARY KEY (`invoice_details_id`);

--
-- Indexes for table `invoice_master`
--
ALTER TABLE `invoice_master`
  ADD PRIMARY KEY (`invoice_id`);

--
-- Indexes for table `location`
--
ALTER TABLE `location`
  ADD PRIMARY KEY (`location_id`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `offers`
--
ALTER TABLE `offers`
  ADD PRIMARY KEY (`offer_id`);

--
-- Indexes for table `offer_mapping`
--
ALTER TABLE `offer_mapping`
  ADD PRIMARY KEY (`offer_map_id`);

--
-- Indexes for table `policy`
--
ALTER TABLE `policy`
  ADD PRIMARY KEY (`policy_id`);

--
-- Indexes for table `product_image`
--
ALTER TABLE `product_image`
  ADD PRIMARY KEY (`imge_id`);

--
-- Indexes for table `product_master`
--
ALTER TABLE `product_master`
  ADD PRIMARY KEY (`pro_mst_id`);

--
-- Indexes for table `product_package`
--
ALTER TABLE `product_package`
  ADD PRIMARY KEY (`pro_pkg_id`);

--
-- Indexes for table `product_type`
--
ALTER TABLE `product_type`
  ADD PRIMARY KEY (`pro_type_id`);

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`profie_id`);

--
-- Indexes for table `review_rating`
--
ALTER TABLE `review_rating`
  ADD PRIMARY KEY (`rating_id`);

--
-- Indexes for table `sliders`
--
ALTER TABLE `sliders`
  ADD PRIMARY KEY (`silder_id`);

--
-- Indexes for table `type_master`
--
ALTER TABLE `type_master`
  ADD PRIMARY KEY (`type_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `useradmin`
--
ALTER TABLE `useradmin`
  ADD PRIMARY KEY (`useradmin_id`);

--
-- Indexes for table `user_address`
--
ALTER TABLE `user_address`
  ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `user_cart`
--
ALTER TABLE `user_cart`
  ADD PRIMARY KEY (`cart_id`);

--
-- Indexes for table `user_cart_product`
--
ALTER TABLE `user_cart_product`
  ADD PRIMARY KEY (`user_cart_product_id`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `volume_master`
--
ALTER TABLE `volume_master`
  ADD PRIMARY KEY (`volume_id`);

--
-- Indexes for table `Wishlist`
--
ALTER TABLE `Wishlist`
  ADD PRIMARY KEY (`wishlist_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `brand_master`
--
ALTER TABLE `brand_master`
  MODIFY `brand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `category_master`
--
ALTER TABLE `category_master`
  MODIFY `cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `contact_master`
--
ALTER TABLE `contact_master`
  MODIFY `contact_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `gst_master`
--
ALTER TABLE `gst_master`
  MODIFY `gst_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `invoice_details`
--
ALTER TABLE `invoice_details`
  MODIFY `invoice_details_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `invoice_master`
--
ALTER TABLE `invoice_master`
  MODIFY `invoice_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `location`
--
ALTER TABLE `location`
  MODIFY `location_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;
--
-- AUTO_INCREMENT for table `offers`
--
ALTER TABLE `offers`
  MODIFY `offer_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `offer_mapping`
--
ALTER TABLE `offer_mapping`
  MODIFY `offer_map_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `policy`
--
ALTER TABLE `policy`
  MODIFY `policy_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `product_image`
--
ALTER TABLE `product_image`
  MODIFY `imge_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `product_master`
--
ALTER TABLE `product_master`
  MODIFY `pro_mst_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `product_package`
--
ALTER TABLE `product_package`
  MODIFY `pro_pkg_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `product_type`
--
ALTER TABLE `product_type`
  MODIFY `pro_type_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `profile`
--
ALTER TABLE `profile`
  MODIFY `profie_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;
--
-- AUTO_INCREMENT for table `review_rating`
--
ALTER TABLE `review_rating`
  MODIFY `rating_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sliders`
--
ALTER TABLE `sliders`
  MODIFY `silder_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `type_master`
--
ALTER TABLE `type_master`
  MODIFY `type_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `useradmin`
--
ALTER TABLE `useradmin`
  MODIFY `useradmin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT for table `user_address`
--
ALTER TABLE `user_address`
  MODIFY `address_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `user_cart`
--
ALTER TABLE `user_cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;
--
-- AUTO_INCREMENT for table `user_cart_product`
--
ALTER TABLE `user_cart_product`
  MODIFY `user_cart_product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `user_role`
--
ALTER TABLE `user_role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `volume_master`
--
ALTER TABLE `volume_master`
  MODIFY `volume_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Wishlist`
--
ALTER TABLE `Wishlist`
  MODIFY `wishlist_id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
