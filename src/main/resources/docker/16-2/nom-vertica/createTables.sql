CREATE TABLE public.proxy_engage (
time timestamp, 
view varchar(64), 
host varchar(512), 
path varchar(10000), 
list varchar(255), 
status_code int, 
beacon_url varchar(128)
);


create table public.dns_engage (
  time timestamp,
  view varchar(64),
  device_id varchar(255),
  name varchar(255),
  threat_id integer
);
