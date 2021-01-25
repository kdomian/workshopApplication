--liquibase formatted sql
--changeset kdomian:10
CREATE TABLE EVENT(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL,
    START_DATE TIMESTAMP NOT NULL,
    END_DATE TIMESTAMP NOT NULL,
    IS_ACTIVE BOOLEAN NOT NULL DEFAULT FALSE
);

--changeset kdomian:11
INSERT INTO EVENT VALUES ( NULL, 'Kizz me more', '2023-01-10 12:00:00', '2023-01-10 16:00:00', false);
INSERT INTO EVENT VALUES ( NULL, 'Silesian Zouk Festival', '2021-04-12 12:00:00','2021-04-12 16:00:00', true );
INSERT INTO EVENT VALUES ( NULL, 'ElSol Warsaw', '2022-02-13 12:00:00','2022-02-13 17:00:00',  true );
