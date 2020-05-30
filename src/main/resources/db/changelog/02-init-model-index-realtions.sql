-- create foreign key index to speed up join operations
create index if not exists idx_contact_fk_client_id on contact (user_id);