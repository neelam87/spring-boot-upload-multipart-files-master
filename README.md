# 

Its REST interface to consume data-snapshots from one client, validate and
persist data in storage, distribute persisted data to other clients via REST interface.

REQ-01: As client I want to upload plain text file with comma-separated data via HTTP
request
a. First line of file will contain
i. header: PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP
b. Last line of file always to be empty
c. All other lines will contain four values what represents single record to be persisted
d. Primary key must be non-blank string
• REQ-02: As client I want access data persisted via HTTP request. Values of single record to be
provided for PRIMARY_KEY supplied via request URL
• REQ-03: As service owner I want to remove record from storage via HTTP request by single
PRIMARY_KEY for reconciliation purpose
• REQ-04: As service owner I want no corrupted records client-file (invalid format or anything)
to be saved

