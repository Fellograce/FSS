USE sql8623984;

CREATE TABLE IF NOT EXISTS department
(
    departmentID int auto_increment primary key,
    name         varchar(30) not null
);

CREATE TABLE IF NOT EXISTS file
(
    name         varchar(255) primary key,
    type         varchar(10)  not null,
    path         varchar(255) not null,
    size         int          not null,
    date         date         not null,
    departmentID int          not null,
    foreign key (departmentID) references department (departmentID)
);

CREATE TABLE IF NOT EXISTS angestellte
(
    id           int auto_increment primary key,
    username     varchar(255) not null,
    password     varchar(255) not null,
    authority    boolean      not null,
    departmentID int          not null,
    foreign key (departmentID) references department (departmentID)
);