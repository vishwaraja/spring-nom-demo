CREATE SCHEMA IF NOT EXISTS nom;
CREATE FLEX TABLE IF NOT EXISTS nom.telemetry (     
 creator varchar(64) DEFAULT creator::varchar(40),     
 type varchar(64) DEFAULT type::varchar(40),     
 node_id varchar(40) DEFAULT "node-id"::varchar(40),     
 sample_time TIMESTAMPTZ NOT NULL DEFAULT ('1970-01-01 00:00:00+00'::timestamptz + ((MapLookup(__raw__, 'current-time'))::float * '00:00:01'::interval))
)
PARTITION BY (     
 EXTRACT(YEAR   FROM sample_time AT TIME ZONE 'UTC') * 10000000000 +     
 EXTRACT(MONTH  FROM sample_time AT TIME ZONE 'UTC') * 100000000   +     
 EXTRACT(DAY    FROM sample_time AT TIME ZONE 'UTC') * 1000000     +     
 EXTRACT(HOUR   FROM sample_time AT TIME ZONE 'UTC') * 10000 
)::INT 
;
