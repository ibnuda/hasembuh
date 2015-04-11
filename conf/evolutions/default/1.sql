# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "barang" ("idbarang" SERIAL NOT NULL PRIMARY KEY,"nabarang" VARCHAR(254) NOT NULL,"habarang" INTEGER NOT NULL);
create table "itemset_dua" ("id" SERIAL NOT NULL PRIMARY KEY,"idbarangsatu" INTEGER NOT NULL,"idbarangdua" INTEGER NOT NULL,"support" INTEGER NOT NULL);
create table "itemset_empat" ("id" SERIAL NOT NULL PRIMARY KEY,"idbarangsatu" INTEGER NOT NULL,"idbarangdua" INTEGER NOT NULL,"idbarangtiga" INTEGER NOT NULL,"idbarangempat" INTEGER NOT NULL,"support" INTEGER NOT NULL);
create table "itemset_satu" ("id" SERIAL NOT NULL PRIMARY KEY,"idbarangsatu" INTEGER NOT NULL,"support" INTEGER NOT NULL);
create table "itemset_tiga" ("id" SERIAL NOT NULL PRIMARY KEY,"idbarangsatu" INTEGER NOT NULL,"idbarangdua" INTEGER NOT NULL,"idbarangtiga" INTEGER NOT NULL,"support" INTEGER NOT NULL);
create table "logininfo" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"providerID" VARCHAR(254) NOT NULL,"providerKey" VARCHAR(254) NOT NULL);
create table "oauth1info" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"token" VARCHAR(254) NOT NULL,"secret" VARCHAR(254) NOT NULL,"loginInfoId" BIGINT NOT NULL);
create table "oauth2info" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"accesstoken" VARCHAR(254) NOT NULL,"tokentype" VARCHAR(254),"expiresin" INTEGER,"refreshtoken" VARCHAR(254),"logininfoid" BIGINT NOT NULL);
create table "passwordinfo" ("hasher" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"salt" VARCHAR(254),"loginInfoId" BIGINT NOT NULL);
create table "transaksi" ("no" SERIAL NOT NULL PRIMARY KEY,"idtrans" INTEGER NOT NULL,"idbarang" INTEGER NOT NULL);
create table "userlogininfo" ("userID" VARCHAR(254) NOT NULL,"loginInfoId" BIGINT NOT NULL);
create table "useruser" ("userID" VARCHAR(254) NOT NULL PRIMARY KEY,"username" VARCHAR(254) NOT NULL);
alter table "transaksi" add constraint "idbarang" foreign key("idbarang") references "barang"("idbarang") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "transaksi" drop constraint "idbarang";
drop table "useruser";
drop table "userlogininfo";
drop table "transaksi";
drop table "passwordinfo";
drop table "oauth2info";
drop table "oauth1info";
drop table "logininfo";
drop table "itemset_tiga";
drop table "itemset_satu";
drop table "itemset_empat";
drop table "itemset_dua";
drop table "barang";

