--liquibase formatted sql
--changeset kdomian:21
CREATE TABLE PERIOD (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL,
    START_DATE TIMESTAMP NOT NULL,
    END_DATE TIMESTAMP NOT NULL,
    EVENT_ID BIGINT NOT NULL
);

--changeset kdomian:22
ALTER TABLE PERIOD
    ADD CONSTRAINT PERIOD_EVENT_ID
    FOREIGN KEY (EVENT_ID) REFERENCES EVENT(ID);

--changeset kdomian:23
INSERT INTO PERIOD VALUES ( NULL, 'Early birds', '2022-01-10 00:00:00', '2022-02-10 23:59:59', 1 );
INSERT INTO PERIOD VALUES ( NULL, '1st period', '2022-02-11 00:00:00', '2022-05-10 23:59:59', 1 );
INSERT INTO PERIOD VALUES ( NULL, '2st period', '2022-05-11 00:00:00', '2022-10-10 23:59:59', 1 );
INSERT INTO PERIOD VALUES ( NULL, 'Early birds', '2022-01-10 00:00:00', '2022-02-10 23:59:59', 2 );
INSERT INTO PERIOD VALUES ( NULL, '1st period', '2022-02-11 00:00:00', '2022-05-10 23:59:59', 3 );
INSERT INTO PERIOD VALUES ( NULL, '2st period', '2022-05-11 00:00:00', '2022-10-10 23:59:59', 3 );
