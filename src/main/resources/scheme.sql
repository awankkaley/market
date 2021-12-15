create table coinsbit_data
(
    id            bigint not null auto_increment,
    created_date  timestamp,
    modified_date timestamp,
    amount        varchar(255),
    deal_fee      varchar(255),
    deal_money    varchar(255),
    deal_stock    varchar(255),
    left_data     varchar(255),
    maker_fee     varchar(255),
    market        varchar(255),
    order_id      bigint,
    price         varchar(255),
    side          varchar(255),
    taker_fee     varchar(255),
    timestamp     double,
    type          varchar(255),
    primary key (id)
);

create table hotbit_data
(
    id            bigint not null auto_increment,
    created_date  timestamp,
    modified_date timestamp,
    alt_fee       varchar(255),
    amount        varchar(255),
    ctime         double,
    deal_fee      varchar(255),
    deal_fee_alt  varchar(255),
    deal_money    varchar(255),
    deal_stock    varchar(255),
    fee_stock     varchar(255),
    freeze        varchar(255),
    left_data     varchar(255),
    maker_fee     varchar(255),
    market        varchar(255),
    mtime         double,
    order_id      bigint,
    price         varchar(255),
    side          integer,
    source        varchar(255),
    status        integer,
    taker_fee     varchar(255),
    type          integer,
    user          bigint,
    primary key (id)
);

create table order_data
(
    id                bigint  not null auto_increment,
    created_date      timestamp,
    modified_date     timestamp,
    amount            double,
    current_price     double,
    exchange_code     varchar(255),
    exchange_order_id bigint,
    info              varchar(255),
    is_valid          boolean not null,
    price             double,
    side              varchar(255),
    status            integer,
    primary key (id)
)

create table market_setup_entity
(
    id             bigint not null auto_increment,
    created_date   datetime(6),
    modified_date  datetime(6),
    buy_percent    integer,
    profit_percent integer,
    primary key (id)
) engine=InnoDB




