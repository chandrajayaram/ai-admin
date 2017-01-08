CREATE KEYSPACE message_store_ks
WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE message_store_ks.message (
    service_name text,
    create_ts timestamp,
    payload_id text,
    soap_action text,
    mime_headers map<text,text>,
    request_xml blob,
    PRIMARY KEY (service_name, create_ts)
)WITH CLUSTERING ORDER BY(create_ts desc);

CREATE TABLE message_store_ks.message_by_created_ts (
    service_name text,
    create_ts timestamp,
	payload_id text,
    PRIMARY KEY (service_name, create_ts,payload_id)
)WITH CLUSTERING ORDER BY(create_ts desc);

CREATE TYPE mime_header (
  name text,
  value text
);

CREATE TABLE message_store_ks.message_by_payload_id (
    payload_id text,
    status text,
    mime_headers list<frozen <mime_header>>,
	payload_xml blob,       
    PRIMARY KEY (payload_id)
);

