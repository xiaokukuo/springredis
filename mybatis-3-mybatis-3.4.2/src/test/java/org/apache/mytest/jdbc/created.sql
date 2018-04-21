DROP TABLE test_blob;

CREATE TABLE `test_blob` (
  `id` INT(11) PRIMARY KEY,
  `content` BLOB
)ENGINE=InnoDB DEFAULT CHARSET=utf8;