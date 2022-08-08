# 第一次执行
create database my_db;

use my_db;

# 用户表
create table user
(
    id           bigint auto_increment comment 'id' primary key,
    username     varchar(256)       null comment '用户昵称',
    userAccount  varchar(256)       null comment '账号',
    avatarUrl    varchar(1024)      null comment '用户头像',
    gender       tinyint            null comment '性别',
    userPassword varchar(512)       not null comment '密码',
    phone        varchar(128)       null comment '电话',
    email        varchar(512)       null comment '邮箱',
    userStatus   int      default 0 not null comment '状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0 not null comment '是否删除',
    userRole     int      default 0 not null comment '用户角色 0 - 普通用户 1 - 管理员'
) comment '用户';

# 任务
create table work
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)       null comment '任务名称',
    description text               null comment '描述',
    duration    int                null comment '时长（秒）',
    tags        varchar(512)       null comment '标签列表json',
    planTime    datetime comment '计划时间',
    createTime  datetime default CURRENT_TIMESTAMP comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0 not null comment '是否删除'
) comment '任务' ENGINE=InnoDB DEFAULT CHARSET=utf8;


