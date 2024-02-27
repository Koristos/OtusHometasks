create table users
(
    id         bigserial primary key,
    username   varchar(30) not null,
    password   varchar(80) not null,
    email      varchar(50) unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE users_roles
(
    user_id bigint not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into users (username, password, email)
values ('user', '$2a$10$fWz4fQ4nBSgfXyXWgQWoKuH7x1C2/DbxWkqKEP507leEFE1hDX7Pa', 'bob_johnson@gmail.com'),
       ('admin', '$2a$10$fWz4fQ4nBSgfXyXWgQWoKuH7x1C2/DbxWkqKEP507leEFE1hDX7Pa', 'john_johnson@gmail.com');

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2);

create table categories
(
    id         bigserial primary key,
    title      varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into categories (title)
values ('Food'), ('Electronics'), ('Furniture');

create table products
(
    id          bigserial primary key,
    title       varchar(255),
    price       numeric(8, 2) not null,
    category_id bigint references categories (id),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

insert into products (title, price, category_id)
values ('Milk', 95, 1),
       ('Bread', 28, 1),
       ('Cheese', 300, 1),
       ('Coffee', 200, 1),
       ('Phone', 120000, 2),
       ('Camera', 5000, 2),
       ('Note Book', 550000, 2),
       ('Chair', 6000, 3),
       ('Table', 10000, 3),
       ('Pillow', 2000, 3);

create table orders
(
    id         bigserial primary key,
    price      numeric(8, 2) not null,
    user_id    bigint references users (id),
    address    varchar(255),
    phone      varchar(32),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table order_items
(
    id                bigserial primary key,
    price             numeric(8, 2) not null,
    price_per_product numeric(8, 2) not null,
    product_id        bigint references products (id),
    order_id          bigint references products (id),
    quantity          int,
    created_at        timestamp default current_timestamp,
    updated_at        timestamp default current_timestamp
);