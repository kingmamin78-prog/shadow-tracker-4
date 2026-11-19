-- Example contract only; review and test security before production use.
create table if not exists public.encrypted_vault_records (
 id uuid primary key,
 owner_id uuid not null references auth.users(id) on delete cascade,
 record_version bigint not null default 1,
 nonce_b64 text not null,
 ciphertext_b64 text not null,
 updated_at timestamptz not null default now()
);
alter table public.encrypted_vault_records enable row level security;
create policy "read own encrypted rows" on public.encrypted_vault_records for select using (auth.uid() = owner_id);
create policy "insert own encrypted rows" on public.encrypted_vault_records for insert with check (auth.uid() = owner_id);
create policy "update own encrypted rows" on public.encrypted_vault_records for update using (auth.uid() = owner_id) with check (auth.uid() = owner_id);
