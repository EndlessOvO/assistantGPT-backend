--- 建表语句示例 后续从navicat导出sql文件放在此处
--- TODO 正式开发删除本文件
CREATE TABLE `example` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--- 插入数据示例
INSERT INTO `example` (`id`, `name`) VALUES (1, '张三');
INSERT INTO `example` (`id`, `name`) VALUES (2, '李四');