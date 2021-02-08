--liquibase formatted sql
--changeset kdomian:31
CREATE TABLE TICKET(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL,
    GENDER INT,
    TICKET_TYPE INT NOT NULL,
    IS_BALANCED BOOLEAN DEFAULT FALSE
);

--changeset kdomian:32
INSERT INTO TICKET VALUES ( NULL, "Man pass", 0, 0, false );
INSERT INTO TICKET VALUES ( NULL, "Woman pass", 1, 0, false );
INSERT INTO TICKET VALUES ( NULL, "Couple pass", null , 1, true );
