##### Open Cassandra Console

`docker exec -it cassandra cqlsh`

##### Create Keyspace

```angular2html
create keyspace mobile_banking_ks
with replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};
```

##### Create Table

```angular2html
use mobile_banking_ks;

create table transaction_status (
  id   uuid primary key,
  from_account_id       bigint,
  from_account_number   text,
  to_account_id  bigint,
  to_account_number text,
  amount           decimal,
  transaction_time  timestamp,
  type          text,      -- P2P / P2M
  status            text,      -- SUCCEEDED / FAILED
  failure_reason    text,
  created_at        timestamp
);
```

```angular2html
create table transaction_ledger (
account_id        bigint,
account_number    text,
transaction_date          text,       -- YYYYMM (partition bucket)
transaction_time  timestamp,
transaction_id    uuid,
counterparty_id   bigint,
counterparty_account_number text,
amount            decimal,
direction         text,   -- DEBIT / CREDIT
transaction_type          text,   -- P2P / P2M
created_at  timestamp,
primary key ((account_id, transaction_date), transaction_time, transaction_id)
) with clustering order by (transaction_time desc);

```